# Obra do Berço

Sistema de gestão para a Obra do Berço, obra social que atende gestantes em
situação de vulnerabilidade: cadastro de gestantes, solicitações de enxoval,
eventos de entrega e gestão de voluntários.

Arquitetura de microsserviços (Spring Boot) com API Gateway + service
discovery, e um front-end React/Vite consumindo tudo através do gateway.

## Serviços

| Serviço | Porta | Descrição |
|---|---|---|
| `obra-do-berco-eureka-server` | 8761 | Service registry (Eureka) |
| `obra-do-berco-api-gateway` | 8765 | Gateway único que o front-end consome |
| `obra-do-berco-auth` | 8085 | Login, refresh, JWT |
| `obra-do-berco-gestante` | 8080 | Cadastro de gestantes |
| `obra-do-berco-solicitacoes` | 8081 | Solicitações de enxoval |
| `obra-do-berco-document` | 8082 | Geração de PDFs (fichas, listas de presença) |
| `obra-do-berco-voluntarios` | 8083 | Voluntários e admin |
| `obra-do-berco-eventos` | 8084 | Eventos de entrega e participantes |
| `obra-do-berco-frontend` | 5173 (dev) | SPA React + Vite |

Detalhes de cada rota estão em [api-endpoints-v1.md](api-endpoints-v1.md).

## Requisitos

- Java 26
- PostgreSQL rodando localmente (usuário `postgres`, uma database por
  serviço: `obra_do_berco_auth`, `obra_do_berco_gestante`,
  `obra_do_berco_voluntarios`, `obra_do_berco_solicitacoes`,
  `obra_do_berco_eventos`)
- Node 24+ (frontend)

## Configuração local

Cada serviço com dependências externas tem um `.env.example` — copie para
`.env` e ajuste:

```
JWT_SECRET=<mesmo valor em auth, gestante, voluntarios, solicitacoes, eventos, document e api-gateway>
DB_URL / DB_USERNAME / DB_PASSWORD  (opcional — default aponta pro Postgres local)
```

O front-end também tem um `.env.example` (`VITE_API_BASE_URL`, default
`http://localhost:8765`).

## Rodando localmente

Suba o Eureka primeiro, depois os demais serviços (qualquer ordem), depois o
gateway e o front-end:

```bash
# em cada obra-do-berco-<servico>/
./mvnw spring-boot:run       # mvnw.cmd no Windows

# no obra-do-berco-frontend/
npm install
npm run dev
```

## Docker

Cada serviço tem seu próprio `Dockerfile` (build multi-stage, contexto é o
próprio diretório do serviço — não precisa do pom raiz). O front-end também
tem um `Dockerfile` (build Vite + nginx, aceita `VITE_API_BASE_URL` como
build arg). Não há `docker-compose` ainda.

## CI

`.github/workflows/build.yml` builda e testa os 8 serviços de backend
(contra um Postgres de serviço, uma database por job) e o front-end
(lint + build) em todo push/PR para `main`.

## Testes

Cada serviço de backend tem um teste de contexto (`@SpringBootTest`) e, onde
faz sentido, um smoke test de segurança (endpoint principal exige
autenticação). Cobertura ainda é básica — não há testes unitários de
regras de negócio.

## Pendências conhecidas

- `spring.jpa.hibernate.ddl-auto=update`: Hibernate ainda gerencia o schema
  automaticamente. Migrar para Flyway/Liquibase antes de qualquer deploy
  real.
- Sem pipeline de deploy (CI só builda/testa).
- `JWT_SECRET` e credenciais de banco continuam vindo de `.env` local — em
  produção precisam vir de um secrets manager.
