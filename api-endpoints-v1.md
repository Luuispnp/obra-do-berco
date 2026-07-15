# Especificação de API — Sistema de Gestão de Enxovais (v1)

> Base URL: `https://api.seudominio.org/api/v1`
> Formato: JSON | Auth: Bearer JWT

Esta versão foi reformulada pensando no consumo por um front-end React: nomes de rotas previsíveis, filtros consistentes via query string e formato de erro padronizado (para facilitar hooks genéricos de fetch, ex. `useApi('/gestantes', { filtros })`).

---

## Arquitetura por trás (Spring Cloud Gateway + Eureka)

O back-end é dividido em microsserviços (Auth, Voluntários, Gestantes, Solicitações, Eventos, Documentos), cada um registrado no **Eureka Server** e roteado pelo **Spring Cloud Gateway**. Isso é transparente para o React: **o front nunca aponta para os serviços individuais**, apenas para o Gateway.

```
React  ──────►  Gateway (porta única, ex. 8080)  ──────►  Eureka descobre a instância
                        │                                   e faz o roteamento (lb://)
                        ├── /api/v1/auth/**          → auth-service
                        ├── /api/v1/voluntarios/**   → voluntarios-service
                        ├── /api/v1/gestantes/**     → gestantes-service
                        ├── /api/v1/solicitacoes/**  → solicitacoes-service
                        ├── /api/v1/eventos/**       → eventos-service
                        └── /api/v1/documentos/**    → documentos-service
```

Consequências práticas para o front:

- **Uma única `baseURL`** no client Axios/fetch — não existe "URL do serviço de gestantes" separada da "URL do serviço de eventos". Tudo é `https://api.seudominio.org/api/v1/...`.
- **Autenticação validada no Gateway** (via filtro JWT): se o token for inválido/expirado, o front recebe `401` do próprio Gateway, antes mesmo de chegar ao serviço de destino — o tratamento de erro no front pode ser único e centralizado, sem se preocupar com qual serviço gerou o erro.
- **Um serviço fora do ar não derruba os outros.** Se `gestantes-service` cair, chamadas a `/voluntarios` ou `/auth` continuam normais; só as rotas de `/gestantes/**` (e o que depender delas) falham. Vale o front tratar erro de rede por endpoint, não globalmente como "sistema fora do ar".
- **Endpoints agregados dependem de chamada serviço-a-serviço internamente.** Alguns dados que o front recebe "prontos" na verdade exigem que um serviço chame outro por trás do Gateway (via Eureka/`RestTemplate`/`WebClient`/OpenFeign) antes de responder:
  - `GET /eventos/{eventoId}/participantes` → o campo `nomeGestante` embutido exige que `eventos-service` busque dados em `gestantes-service` (direto ou via `solicitacoes-service`).
  - `POST /solicitacoes` → `solicitacoes-service` precisa validar `gestanteId` chamando `gestantes-service`.
  - `POST /eventos/{id}/participantes` → `eventos-service` precisa validar que a `solicitacaoId` existe e está com `status = APROVADA`, chamando `solicitacoes-service`.
  - `PATCH /eventos/{id}/finalizar` → `eventos-service` chama `solicitacoes-service` para atualizar o status (`ENTREGUE`/`NAO_COMPARECEU`) de cada solicitação vinculada.
  - `GET /documentos/eventos/{eventoId}/fichas` → `documentos-service` cruza dados de `eventos-service`, `solicitacoes-service` e `gestantes-service` para montar o PDF.

  Essas chamadas internas são **síncronas** (o front espera a resposta consolidada numa única requisição), então o tempo de resposta desses endpoints tende a ser maior — vale já prever *loading states* mais generosos no front para eles, em vez de assumir latência uniforme entre todos os endpoints.

- **Timeouts em cascata:** se `gestantes-service` demorar para responder a uma chamada interna do `eventos-service`, o Gateway pode devolver `504 Gateway Timeout` para o front. Vale tratar `504` como "tente novamente" na UI, não como erro definitivo.

### Formato de resposta em listagens
Listagens retornam um array simples, sem envelope:
```json
[
  { "id": "a1b2c3d4-...", "nome": "..." },
  { "id": "b2c3d4e5-...", "nome": "..." }
]
```

### Formato de erro
```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "CPF inválido",
    "details": [{ "field": "cpf", "message": "Formato incorreto" }]
  }
}
```
Isso permite no front tratar erros de forma genérica (ex. um `try/catch` central no Axios/interceptor) e mapear `details` para mensagens de campo em formulários.

### Busca e filtros
Em vez de endpoints `/search` separados, os filtros vão como query params na própria listagem:
```
GET /gestantes?nome=Maria&cpf=123
GET /solicitacoes?status=aprovada&gestanteId=5&dataInicio=2026-01-01&dataFim=2026-03-01
```

### Soft delete
`voluntarios` e `gestantes` não são apagados fisicamente — usam `PATCH .../inativar` e `PATCH .../reativar`, preservando histórico. `DELETE` continua existindo apenas para `eventos` e `solicitacoes` em rascunho (dado que não geram histórico crítico).

### Headers padrão
```
Authorization: Bearer <token>
Content-Type: application/json
```

---

## 1. Auth Service — `/api/v1/auth`

| Método | Rota | Objetivo |
|---|---|---|
| POST | `/auth/login` | Autentica e-mail/senha, retorna `accessToken` + `refreshToken` |
| POST | `/auth/refresh` | Gera novo `accessToken` a partir do `refreshToken` |
| POST | `/auth/logout` | Invalida o `refreshToken` atual |
| GET | `/auth/me` | Retorna dados do usuário autenticado (nome, e-mail, papel/permissões) |

**Resposta de `/auth/login`:**
```json
{
  "accessToken": "eyJ...",
  "refreshToken": "eyJ...",
  "expiresIn": 3600,
  "user": { "id": "a1b2c3d4-...", "nome": "Ana", "email": "ana@ong.org", "role": "admin" }
}
```
> No front, isso mapeia direto para um `AuthContext`/store (Zustand, Redux ou Context API) guardando `user` e agendando o refresh automático antes do `expiresIn`.

---

## 2. Voluntários Service — `/api/v1/voluntarios`

| Método | Rota | Objetivo |
|---|---|---|
| POST | `/voluntarios` | Cadastra um novo voluntário |
| GET | `/voluntarios` | Lista voluntários (filtros: `nome`, `email`, `ativo`) |
| GET | `/voluntarios/{id}` | Retorna um voluntário específico |
| PUT | `/voluntarios/{id}` | Atualiza os dados de um voluntário |
| PATCH | `/voluntarios/{id}/inativar` | Inativa (soft delete) |
| PATCH | `/voluntarios/{id}/reativar` | Reativa um voluntário inativo |

---

## 3. Gestantes Service — `/api/v1/gestantes`

| Método | Rota | Objetivo |
|---|---|---|
| POST | `/gestantes` | Cadastra uma nova gestante |
| GET | `/gestantes` | Lista gestantes (filtros: `nome`, `cpf`, `telefone`) |
| GET | `/gestantes/{id}` | Retorna dados completos de uma gestante |
| PUT | `/gestantes/{id}` | Atualiza dados cadastrais |
| PATCH | `/gestantes/{id}/inativar` | Inativa (soft delete, preserva histórico) |
| PATCH | `/gestantes/{id}/reativar` | Reativa |

**Nota de privacidade (LGPD):** na listagem (`GET /gestantes`), o back-end deve mascarar CPF (ex. `123.***.**-00`); o CPF completo só aparece em `GET /gestantes/{id}`, e idealmente restrito por `role` no token JWT. O front deve tratar esses dois formatos de retorno de forma diferente (não assumir que o campo `cpf` sempre vem completo).

---

## 4. Solicitações Service — `/api/v1/solicitacoes`

| Método | Rota | Objetivo |
|---|---|---|
| POST | `/solicitacoes` | Cadastra uma nova solicitação para uma gestante |
| GET | `/solicitacoes` | Lista solicitações (filtros: `status`, `gestanteId`, `dataInicio`, `dataFim`) |
| GET | `/solicitacoes/{id}` | Retorna detalhes de uma solicitação |
| PUT | `/solicitacoes/{id}` | Atualiza dados de uma solicitação (apenas se `status = EM_ANALISE`) |
| PATCH | `/solicitacoes/{id}/aprovar` | Aprova a solicitação |
| PATCH | `/solicitacoes/{id}/recusar` | Recusa a solicitação (body: `{ "motivo": "..." }`) |
| PATCH | `/solicitacoes/status-lote` | Atualiza o status de várias solicitações de uma vez (uso interno, chamado pelo `eventos-service` na finalização de evento — body: `{ "atualizacoes": [{ "solicitacaoId": "...", "status": "ENTREGUE" }, { "solicitacaoId": "...", "status": "NAO_COMPARECEU" }] }`, preenchendo `dataEncerramento` automaticamente para cada item) |
| DELETE | `/solicitacoes/{id}` | Remove solicitação (apenas se `status = EM_ANALISE`) |

**Valores possíveis de `status`:** `EM_ANALISE` (inicial), `APROVADA`, `RECUSADA`, `ENTREGUE`, `NAO_COMPARECEU`.

- `EM_ANALISE` → status padrão no cadastro, ainda sem decisão do voluntário.
- `APROVADA` / `RECUSADA` → definidos manualmente via `/aprovar` e `/recusar`.
- `ENTREGUE` / `NAO_COMPARECEU` → **não são setados diretamente pelo front nesse serviço**. Eles são definidos automaticamente pelo `eventos-service` (via chamada interna Feign ao `/status-lote`, ou N chamadas individuais a `/aprovar`-like, dependendo da implementação escolhida) no momento em que um evento é finalizado — `ENTREGUE` para quem estava com presença marcada, `NAO_COMPARECEU` para quem não compareceu. O front nunca deve tentar setar esses dois valores diretamente aqui, só através da finalização do evento (seção 5).

**Elegibilidade para eventos:** apenas solicitações com `status = APROVADA` podem ser adicionadas a um evento (ver `GET /eventos/gestantes-disponiveis` e `POST /eventos/{id}/participantes` na seção 5). `EM_ANALISE`, `RECUSADA`, `ENTREGUE` e `NAO_COMPARECEU` não aparecem como opção disponível para vincular a um evento.

A antiga rota `/solicitacoes/aprovadas` some — o front passa a usar `GET /solicitacoes?status=APROVADA`.

---

## 5. Eventos Service — `/api/v1/eventos`

| Método | Rota | Objetivo |
|---|---|---|
| POST | `/eventos` | Cria um novo evento (body: `{ "titulo": "...", "dataEvento": "..." }`) |
| GET | `/eventos` | Lista eventos (filtros: `dataInicio`, `dataFim`, `status`) |
| GET | `/eventos/{id}` | Retorna detalhes de um evento |
| PUT | `/eventos/{id}` | Atualiza `titulo`/`dataEvento` de um evento (apenas se `status = CRIADO`) |
| DELETE | `/eventos/{id}` | Remove um evento (apenas se `status = CRIADO`) |
| PATCH | `/eventos/{id}/finalizar` | Finaliza o evento — dispara a atualização de status em cada solicitação vinculada (ver regra abaixo) |
| GET | `/eventos/gestantes-disponiveis` | Lista solicitações aprovadas que ainda não estão vinculadas a nenhum evento em aberto (ver regra de exclusividade abaixo) |
| GET | `/eventos/{eventoId}/participantes` | Lista participantes do evento (checklist de presença) |
| POST | `/eventos/{eventoId}/participantes` | Adiciona uma solicitação ao evento (body: `{ "solicitacaoId": "..." }`) |
| POST | `/eventos/{eventoId}/participantes/lote` | Adiciona várias solicitações de uma vez (body: `{ "solicitacaoIds": ["...", "..."] }`) |
| DELETE | `/eventos/{eventoId}/participantes/{participanteId}` | Remove um participante do evento |
| PATCH | `/eventos/{eventoId}/participantes/{participanteId}/presenca` | Marca/desmarca presença (body: `{ "presente": true }`) — é o "check" do checklist |

**Valores possíveis de `status` (Evento):** `CRIADO` (inicial), `FINALIZADO`.

**Sobre os filtros de data:** `dataInicio` e `dataFim` formam um intervalo aplicado sobre `dataEvento` (ex. "eventos entre 01/01/2026 e 30/07/2026"). Os dois podem ser usados juntos (intervalo fechado) ou isolados (`dataInicio` sozinho = "a partir de", `dataFim` sozinho = "até"). Como `dataEvento` é `LocalDate`, a comparação é direta, sem conversão de hora. `dataCriacao`/`dataFinalizacao` não são filtros — servem só como dados de auditoria exibidos no detalhe do evento (`GET /eventos/{id}`).

**Regra de finalização (`PATCH /eventos/{id}/finalizar`):** ao finalizar, o `eventos-service` percorre todos os `participantes` do evento e, via chamada Feign ao `solicitacoes-service`, atualiza cada `Solicitacao` vinculada:
- Participante com `presente = true` → `Solicitacao.status = ENTREGUE`
- Participante com `presente = false` → `Solicitacao.status = NAO_COMPARECEU`
- Em ambos os casos, `Solicitacao.dataEncerramento` é preenchida com a data/hora atual (`LocalDateTime.now()`) no momento da finalização.

Para o front, isso significa que o botão "Finalizar evento" é uma ação **irreversível e de consequência ampla** — vale um passo de confirmação explícita antes de disparar o PATCH, já que ele altera o status de todas as solicitações do checklist de uma vez, não só do evento.

**Regras de vinculação de participantes (`POST /participantes` e `/participantes/lote`):** antes de criar o vínculo, o `eventos-service` valida, nessa ordem:
1. O evento existe e está com `status = CRIADO` — em evento `FINALIZADO` nenhuma escrita no checklist é permitida (`POST`, `DELETE` e o `PATCH` de presença retornam `400`).
2. A `solicitacaoId` existe (chamada Feign a `solicitacoes-service`) — `404` caso contrário.
3. A solicitação está com `status = APROVADA` — `400` caso contrário.
4. A solicitação ainda não está vinculada a nenhum outro evento em aberto (mesma regra de exclusividade do `gestantes-disponiveis`) — `400` caso contrário.

No `/lote`, cada `solicitacaoId` da lista passa por essas 4 validações individualmente; se uma delas falhar no meio da lista, a requisição inteira é abortada (nenhum participante é adicionado).

> **Nota de nomenclatura:** `participanteId` é o ID do vínculo evento↔solicitação (uma entidade própria, tipo tabela associativa), **não** o mesmo que `solicitacaoId`. Isso importa para o front: ao listar `/participantes`, cada item deve trazer os dois IDs (`participanteId` e `solicitacaoId`) explicitamente, porque as ações de presença usam um e o de exibição de dados usa outro.

**Exemplo de retorno de `GET /eventos/{eventoId}/participantes`:**
```json
[
  {
    "participanteId": "b2c3d4e5-...",
    "solicitacaoId": "a1b2c3d4-...",
    "gestanteId": "f6e5d4c3-...",
    "nomeGestante": "Joana Silva",
    "presente": false
  }
]
```
> `nomeGestante` **não é persistido** no `eventos-service` — é resolvido em tempo real a cada resposta (chamada Feign a `gestantes-service` por participante, tanto no `GET /participantes` quanto no retorno de `POST /participantes` e `/lote`). O front só precisa exibir o checklist com nome + checkbox, sem se preocupar em buscar isso de outro lugar; só vale lembrar que essas respostas têm uma chamada síncrona a mais por participante embutida (ver nota de timeout no início do documento).

---

## 6. Document Service — `/api/v1/documentos`

| Método | Rota | Objetivo |
|---|---|---|
| GET | `/documentos/ficha/{solicitacaoId}` | Gera a ficha individual de uma solicitação (PDF) |
| GET | `/documentos/eventos/{eventoId}/lista` | Gera a lista de participantes do evento (PDF) |
| GET | `/documentos/eventos/{eventoId}/fichas` | Gera todas as fichas das participantes de um evento (PDF, um arquivo consolidado) |

**Resposta:** `Content-Type: application/pdf`, header `Content-Disposition: attachment; filename="ficha-123.pdf"`.
No React, o consumo típico é via `fetch` com `responseType: 'blob'` e `window.open(URL.createObjectURL(blob))` ou disparando o download direto, em vez de tratar como JSON.

**Campo `EVENTO` na ficha:** só é preenchido (título + data) quando a ficha é gerada via `/eventos/{eventoId}/fichas` — nesse caso o `document-service` já sabe qual evento gerou a chamada. Na ficha avulsa (`/ficha/{solicitacaoId}`), o campo fica em branco: essa rota é normalmente usada logo no cadastro da gestante, antes de ela estar vinculada a qualquer evento, e o endpoint não faz uma busca reversa solicitação→evento para preenchê-lo.

---

## Tabela-resumo (para gerar clientes de API / hooks no front)

| Serviço | Prefixo | Auth exigida |
|---|---|---|
| Auth | `/auth` | Parcial (`/me` exige token) |
| Voluntários | `/voluntarios` | Sim |
| Gestantes | `/gestantes` | Sim |
| Solicitações | `/solicitacoes` | Sim |
| Eventos | `/eventos` | Sim |
| Documentos | `/documentos` | Sim (retorno binário) |

## Sugestão de organização no front-end

```
src/
  api/
    client.js          // instância Axios/fetch com interceptor de auth + refresh
    auth.js
    voluntarios.js
    gestantes.js
    solicitacoes.js
    eventos.js
    documentos.js
  hooks/
    useAuth.js
    useVoluntarios.js
    useGestantes.js
    useSolicitacoes.js
    useEventos.js
```

Cada arquivo em `api/` expõe funções puras (`getGestantes(filtros)`, `criarGestante(dados)` etc.) que batem exatamente com essas rotas, e os hooks encapsulam estado de loading/erro por cima — assim o mapeamento acima serve como contrato direto entre back e front.
