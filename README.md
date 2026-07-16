# Obra do Berço

Sistema de gestão construído para a **Obra do Berço**, uma obra social (fundada
em 1954, em Belo Horizonte) que atende gestantes em situação de vulnerabilidade
com enxovais, acolhimento e orientação. O sistema digitaliza todo o fluxo que
antes era manual: cadastro de gestantes, análise de solicitações de enxoval,
organização dos eventos de entrega e gestão dos voluntários.

Projeto real, em produção para a instituição — não é um exercício.

## Arquitetura

Backend em microsserviços (Spring Boot), com service discovery e um API
Gateway único como porta de entrada para o front-end:

```
React (SPA)  ──►  API Gateway  ──►  Eureka descobre a instância saudável
                       │
                       ├─► Auth          (login, JWT, refresh)
                       ├─► Gestante      (cadastro de gestantes)
                       ├─► Solicitações  (pedidos de enxoval)
                       ├─► Eventos       (entregas, presença)
                       ├─► Voluntários   (equipe, RBAC admin/voluntário)
                       └─► Document      (geração de PDFs)
```

O front-end nunca fala diretamente com um serviço — tudo passa pelo gateway,
que resolve a instância via Eureka e aplica CORS e validação de JWT antes de
rotear.

## Stack

**Backend:** Java 26 · Spring Boot 4 · Spring Cloud Gateway · Netflix Eureka
· Spring Security (JWT) · Spring Data JPA · PostgreSQL · OpenFeign (chamadas
service-to-service) · MapStruct

**Frontend:** React 19 · TypeScript · Vite · React Router · React Hook Form +
Zod · Tailwind CSS · Axios

**Infra & qualidade:** Docker (multi-stage build por serviço) · GitHub
Actions (build + testes em matriz, um job por serviço) · JUnit 5 · RBAC
(admin vs. voluntário) · configuração via variáveis de ambiente

## Serviços

| Serviço | Porta | Responsabilidade |
|---|---|---|
| `obra-do-berco-eureka-server` | 8761 | Service registry |
| `obra-do-berco-api-gateway` | 8765 | Roteamento, CORS, validação de JWT |
| `obra-do-berco-auth` | 8085 | Login, refresh token, emissão de JWT |
| `obra-do-berco-gestante` | 8080 | Cadastro e histórico de gestantes |
| `obra-do-berco-solicitacoes` | 8081 | Fluxo de análise/aprovação de enxoval |
| `obra-do-berco-document` | 8082 | Geração de PDFs (fichas, listas de presença) |
| `obra-do-berco-voluntarios` | 8083 | Voluntários, papéis e permissões |
| `obra-do-berco-eventos` | 8084 | Eventos de entrega e checklist de presença |
| `obra-do-berco-frontend` | 5173 (dev) | SPA React consumida pela equipe |

A especificação completa de cada rota está em
[api-endpoints-v1.md](api-endpoints-v1.md).

## Rodando localmente

Requisitos: Java 26, Node 24+, PostgreSQL (uma database por serviço com
dependência de banco).

```bash
# cada serviço tem seu próprio .env.example — copie para .env e ajuste
# (JWT_SECRET precisa ser o mesmo valor em todos os serviços)

# suba o Eureka primeiro, depois os demais em qualquer ordem
./mvnw spring-boot:run        # mvnw.cmd no Windows

# front-end
cd obra-do-berco-frontend
npm install && npm run dev
```

Cada serviço também tem um `Dockerfile` próprio (build multi-stage,
independente do pom raiz).

## CI

Todo push/PR para `main` builda e testa os 8 serviços de backend em paralelo
(cada um contra sua própria database, via Postgres de serviço) e o front-end
(lint + build) — ver [.github/workflows/build.yml](.github/workflows/build.yml).
