# AgroMach Backend

Backend completo da plataforma AgroMach em Spring Boot + Java 21 + Maven + PostgreSQL, com arquitetura em camadas, autenticacao JWT, Swagger/OpenAPI e CRUD para todos os modulos.

## Tecnologias

- Java 21
- Spring Boot 3.4.x
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
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

## Como configurar o banco (PostgreSQL local)

### Opcao 1: Docker (recomendado)

```bash
docker compose up -d
```

Banco criado:

- Database: `agromach_db`
- User: `postgres`
- Password: `postgres`
- Port: `15432`

### Opcao 2: PostgreSQL local manual

Crie um banco com as mesmas credenciais do `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/agromach_db
    # Se estiver usando o docker-compose deste projeto:
    # url: jdbc:postgresql://localhost:15432/agromach_db
    username: postgres
    password: postgres
```

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

- API: `http://localhost:8081`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

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

## Observacoes

- Projeto sem dados mockados.
- Estrutura preparada para evolucao com testes, migrations (Flyway/Liquibase) e CI/CD.
- Banco Docker persistente em volume `agromach_postgres_data`.
- Admin padrao de desenvolvimento: `admin@agromach.com` / `admin123`.
