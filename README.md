# AgroMach Backend

Backend completo da plataforma AgroMach em Spring Boot + Java 21 + Maven, com interface web local em Thymeleaf, API REST, autenticacao JWT, Swagger/OpenAPI e CRUD para todos os modulos.

## Tecnologias

- Java 21
- Spring Boot 3.4.x
- Spring Security + JWT
- Spring Data JPA
- H2 local (padrao, sem Docker)
- PostgreSQL (perfil opcional `postgres`)
- Maven
- Swagger/OpenAPI (springdoc)

## Estrutura do Projeto

```text
src/main/java/com/agromach
  config
  controller
  dto
  entity
  exception
  repository
  security
  service
  util
```

## Arquitetura (Spring Boot)

- `controller`: camada HTTP (endpoints REST, validacao de entrada, status code)
- `service`: regras de negocio e orquestracao entre componentes
- `repository`: acesso a dados via Spring Data JPA
- `entity`: mapeamento JPA das tabelas do banco
- `dto`: contratos de entrada/saida da API
- `security`: JWT, filtro de autenticacao e regras de autorizacao
- `config`: configuracoes transversais (CORS, OpenAPI, bootstrap de dados)
- `exception`: padronizacao de erros da API

## Codigo Comentado

- Todas as classes em `src/main/java` possuem JavaDoc de responsabilidade.
- Metodos principais tambem estao comentados para facilitar entendimento do fluxo.
- `application.yml` esta comentado com o significado de cada configuracao Spring.
- `docker-compose.yml` esta comentado com o papel de cada bloco de infraestrutura.

## Como configurar o banco

### Opcao 1: H2 local (padrao, recomendado para rodar na maquina)

Nao precisa subir Docker nem instalar PostgreSQL. Ao iniciar a aplicacao, o Spring Boot cria/atualiza um banco H2 persistido em:

```text
./data/agromach-local.mv.db
```

Console H2 local:

- URL: `http://localhost:8081/h2-console`
- JDBC URL: `jdbc:h2:file:./data/agromach-local;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH`
- User: `sa`
- Password: deixe em branco

### Opcao 2: PostgreSQL com Docker

```bash
docker compose up -d
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

Banco criado pelo Docker Compose:

- Database: `agromach_db`
- User: `postgres`
- Password: `postgres`
- Port: `15432`

## Como rodar o backend

### 1. Compilar

```bash
mvn clean install
```

### 2. Executar

```bash
mvn spring-boot:run
```

Se o comando `mvn` nao estiver no PATH do Windows, use diretamente:

```powershell
& "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2\plugins\maven\lib\maven3\bin\mvn.cmd" spring-boot:run
```

Para tornar `mvn` definitivo no PowerShell:

```powershell
if (!(Test-Path $PROFILE)) { New-Item -ItemType File -Path $PROFILE -Force }
Add-Content $PROFILE 'Set-Alias mvn "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2\plugins\maven\lib\maven3\bin\mvn.cmd"'
```

Aplicacao sobe em:

- Interface Thymeleaf: `http://localhost:8081`
- Login web: `http://localhost:8081/login`
- API: `http://localhost:8081/api`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`
- H2 Console local: `http://localhost:8081/h2-console`

## Seguranca e autenticacao

- Senhas criptografadas com BCrypt
- JWT Bearer Token
- Rotas publicas:
  - `POST /auth/register` e `POST /api/auth/register`
  - `POST /auth/login` e `POST /api/auth/login`
  - `GET /api/**` (somente leitura para facilitar testes de frontend)
  - Swagger/OpenAPI
- Demais rotas exigem token

Usuario admin padrao criado automaticamente no startup (se nao existir):

- Email: `admin@agromach.com`
- Senha: `admin123`

### Roles

- `CLIENTE` e `PRESTADOR` recebem permissao `ROLE_USER`
- `ADMIN` recebe `ROLE_ADMIN` e `ROLE_USER`
- Endpoints de escrita (`POST/PUT/DELETE`) exigem autenticacao por role

## Principais endpoints

### Autenticacao

- `POST /auth/register`
- `POST /auth/login`
- Alias suportados: `POST /api/auth/register`, `POST /api/auth/login`, `POST /auth/signup`, `POST /auth/signin`
- Campos aceitos no JSON:
  - Cadastro: `nome`/`name`, `email`/`username`, `senha`/`password`, `telefone`/`phone`, `documento`/`document`, `role`
  - Login: `email`/`username`, `senha`/`password`
- Campos de resposta:
  - Token: `token` e `accessToken`
  - Usuario: `usuario` e `user`

### CRUDs

- `GET/POST/PUT/DELETE /api/usuarios`
- `GET/POST/PUT/DELETE /api/fazendas`
- `GET/POST/PUT/DELETE /api/maquinas`
- `GET/POST/PUT/DELETE /api/funcionarios`
- `GET/POST/PUT/DELETE /api/produtos`
- `GET/POST/PUT/DELETE /api/pedidos`
- `GET/POST/PUT/DELETE /api/postagens`

## Como testar as rotas

1. Cadastre usuario em `POST /auth/register` (pelo Swagger ou Postman).
2. Faca login em `POST /auth/login` e copie o token.
3. No Swagger, clique em Authorize e informe:
   ```
   Bearer SEU_TOKEN
   ```
4. Teste os endpoints protegidos.

## Geracao de schema

O schema e gerado automaticamente pelo Hibernate com:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

## Interface web local (Thymeleaf)

A aplicacao tambem funciona como sistema web, nao apenas como API. Depois de rodar `mvn spring-boot:run`, acesse `http://localhost:8081/login` e entre com o admin padrao:

- Email: `admin@agromach.com`
- Senha: `admin123`

Telas disponíveis:

- Dashboard: `/`
- Fazendas: `/fazendas`
- Maquinas: `/maquinas`
- Funcionarios: `/funcionarios`
- Produtos: `/produtos`
- Pedidos: `/pedidos`
- Postagens: `/postagens`

## Observacoes

- Projeto sem dados mockados, exceto o admin padrao de desenvolvimento.
- Estrutura preparada para evolucao com testes, migrations (Flyway/Liquibase) e CI/CD.
- Banco Docker persistente em volume `agromach_postgres_data`.
- Admin padrao de desenvolvimento: `admin@agromach.com` / `admin123`.
