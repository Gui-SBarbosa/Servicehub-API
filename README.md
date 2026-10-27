# ServiceHub API

API REST que conecta empresas que precisam contratar serviços a prestadores. Empresas se cadastram como `CLIENT`, prestadores publicam serviços como `PROVIDER` e usuários `ADMIN` administram a plataforma.

## Funcionalidades

- Autenticação JWT com senhas criptografadas via BCrypt
- Controle de acesso baseado em papéis (RBAC): `CLIENT`, `PROVIDER` e `ADMIN`
- Publicação, atualização e desativação lógica de serviços
- Solicitações de serviço com transições controladas de status
- Uma avaliação por solicitação de serviço concluída
- Bean Validation, normalização de entradas e erros padronizados da API
- Schema PostgreSQL versionado com Flyway
- Perfil de testes H2, testes unitários com Mockito e teste de contexto integrado do Spring
- Docker Compose com armazenamento persistente PostgreSQL e health checks
- Endpoints de saúde: `/health` e `/actuator/health`

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Web MVC, Spring Data JPA, Spring Security, Validation e Actuator
- PostgreSQL e Flyway
- H2 para testes
- JJWT
- Lombok
- Maven

## Modelo de domínio

| Entidade | Descrição |
| --- | --- |
| `users` | Usuários da plataforma. Cada usuário possui um papel: `CLIENT`, `PROVIDER` ou `ADMIN`. |
| `services` | Serviços oferecidos pelos prestadores. |
| `service_requests` | Solicitação criada quando um cliente contrata um serviço. O preço é copiado para `total_price`. |
| `reviews` | Avaliação do cliente para uma solicitação concluída; só é permitida uma avaliação por solicitação. |

### Papéis

| Papel | Permissões |
| --- | --- |
| `CLIENT` | Cadastrar-se, autenticar-se, criar solicitações, cancelar solicitações próprias elegíveis e avaliar solicitações concluídas. |
| `PROVIDER` | Cadastrar-se, autenticar-se, criar e gerenciar seus serviços e gerenciar solicitações de seus serviços. |
| `ADMIN` | Papel administrativo reservado. O cadastro público não permite criar contas administrativas. |

### Fluxo de status da solicitação

```text
PENDING -> ACCEPTED -> IN_PROGRESS -> COMPLETED
    |          |
    +--------> CANCELLED
```

Clientes só podem cancelar suas próprias solicitações. Prestadores só podem agir sobre solicitações de seus próprios serviços. Solicitações concluídas e canceladas são estados finais.

## Pré-requisitos

- JDK 21
- Docker Desktop e Docker Compose, ou PostgreSQL 16+

O Maven Wrapper já está incluído; não é necessário instalar o Maven localmente.

## Configuração

Copie o arquivo de exemplo de variáveis de ambiente:

```powershell
Copy-Item .env.example .env
```

Defina os valores no `.env`:

```properties
DB_URL=jdbc:postgresql://localhost:5432/servicehub
DB_USERNAME=postgres
DB_PASSWORD=your_password
DB_DRIVER=org.postgresql.Driver
DB_DDL_AUTO=validate
DB_SHOW_SQL=true
FLYWAY_ENABLED=true
JWT_SECRET=use-a-secret-with-at-least-32-characters
JWT_EXPIRATION_MINUTES=60
```

`JWT_SECRET` deve conter pelo menos 32 caracteres. Não versione o arquivo `.env`, pois ele pode conter credenciais.

## Executar localmente

Inicie o PostgreSQL, configure o `.env` e execute:

```powershell
.\mvnw.cmd spring-boot:run
```

A API inicia em `http://localhost:8080`.

## Executar com Docker

Compile e inicie a API e o PostgreSQL:

```powershell
docker compose up --build
```

Os dados do PostgreSQL são persistidos no volume nomeado `postgres_data`. Para interromper a stack:

```powershell
docker compose down
```

## Migrations do banco

O Flyway executa migrations automaticamente na inicialização. O Hibernate está configurado com `ddl-auto=validate`, portanto valida o schema mapeado em vez de alterá-lo.

| Migration | Finalidade |
| --- | --- |
| `V1__create_providers_table.sql` | Tabela legada original de prestadores. |
| `V2__create_servicehub_core_schema.sql` | Cria usuários, serviços, solicitações, avaliações, constraints e índices. |
| `V3__remove_legacy_providers.sql` | Remove a tabela obsoleta `providers` e seus dados legados. |

Nunca altere uma migration já aplicada. Crie uma nova migration versionada para cada alteração no schema.

## Autenticação

### Cadastrar um cliente

```http
POST /api/auth/register
Content-Type: application/json

{
  "fullName": "Acme Company",
  "email": "contact@acme.com",
  "password": "secure-password",
  "role": "CLIENT",
  "phone": "+55 62 99999-9999"
}
```

Para cadastrar um prestador, defina `"role": "PROVIDER"`. O cadastro público rejeita `ADMIN`.

### Autenticar

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "contact@acme.com",
  "password": "secure-password"
}
```

Use o token retornado nas requisições protegidas:

```http
Authorization: Bearer <accessToken>
```

## Endpoints da API

### Públicos

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/api/auth/register` | Cria uma conta de cliente ou prestador. |
| `POST` | `/api/auth/login` | Retorna um token de acesso JWT. |
| `GET` | `/api/services` | Lista serviços ativos. |
| `GET` | `/api/services/{id}` | Busca um serviço. |
| `GET` | `/health` | Health check da aplicação. |
| `GET` | `/actuator/health` | Health check do Spring Boot. |

### Autenticados

| Método | Endpoint | Papel necessário | Descrição |
| --- | --- | --- | --- |
| `GET` | `/api/users/me` | Qualquer | Retorna o usuário atual. |
| `POST` | `/api/services` | `PROVIDER` | Publica um serviço. |
| `GET` | `/api/services/mine` | `PROVIDER` | Lista os serviços do prestador. |
| `PUT` | `/api/services/{id}` | Prestador dono ou `ADMIN` | Atualiza um serviço. |
| `DELETE` | `/api/services/{id}` | Prestador dono ou `ADMIN` | Desativa logicamente um serviço. |
| `POST` | `/api/service-requests` | `CLIENT` | Contrata um serviço. |
| `GET` | `/api/service-requests/mine` | `CLIENT` | Lista as solicitações do cliente. |
| `GET` | `/api/service-requests/provider` | `PROVIDER` | Lista solicitações dos serviços do prestador. |
| `PATCH` | `/api/service-requests/{id}/status` | Cliente/prestador envolvido ou `ADMIN` | Altera o status conforme a máquina de estados. |
| `POST` | `/api/reviews` | Cliente da solicitação | Cria uma avaliação para solicitação concluída. |

A documentação interativa OpenAPI está disponível em `/swagger-ui/index.html` enquanto a aplicação estiver em execução.

## Exemplo de criação de serviço

```http
POST /api/services
Authorization: Bearer <provider-token>
Content-Type: application/json

{
  "title": "Network infrastructure assessment",
  "description": "Assessment and improvement plan for small business networks.",
  "price": 450.00,
  "category": "REDES_E_INFRAESTRUTURA"
}
```

## Testes

Execute a suíte de testes:

```powershell
.\mvnw.cmd test
```

Os testes usam o perfil `test` com banco H2 em memória. A suíte inclui:

- Testes unitários Mockito para normalização da autenticação, hash de senha e rejeição de cadastro administrativo
- Teste de integração do contexto Spring usando H2

## Estrutura do projeto

```text
src/main/java/com/servicehub/servicehub_api
├── config          # Configurações de segurança e OpenAPI
├── controller      # Endpoints HTTP
├── dto             # Contratos de requisição e resposta
├── exception       # Tratamento de exceções da API
├── model           # Entidades JPA e enums
├── repository      # Repositórios Spring Data
├── security        # Geração JWT e filtro de autenticação
├── service         # Regras de negócio
└── util            # Normalização de entradas

src/main/resources
├── db/migration    # Migrations Flyway
├── application.yml
└── application-test.yml
```

## Notas de segurança

- Senhas são armazenadas apenas como hashes BCrypt.
- Hashes de senha nunca aparecem nas respostas da API.
- JWTs têm tempo de expiração configurado por `JWT_EXPIRATION_MINUTES`.
- Contas desativadas não conseguem autenticar e são rejeitadas mesmo que possuam token ainda não expirado.
- As entradas são validadas com Jakarta Bean Validation e normalizadas antes da persistência.
- As consultas usam binding de parâmetros do Spring Data JPA; nenhuma entrada do usuário é concatenada em SQL.
