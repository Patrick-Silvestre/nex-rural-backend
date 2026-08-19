# AgroMach Backend (API do Nex-Rural)

Backend da plataforma Nex-Rural em Spring Boot + Java 21 + Maven + PostgreSQL: uma API REST pura (sem tela server-rendered) consumida pelo frontend Next.js do repositorio `agromachmonorepo`.

O produto hoje tem duas frentes:

1. **Cockpit operacional** - fazenda, maquinas, funcionarios, pasto/talhao/confinamento (com o que esta ocupando cada area agora), avisos de manejo (fertilizacao, preparo de solo, vacinacao) e previsao do tempo da propriedade.
2. **Rede de contatos** - diretorio de veterinarios, agronomos, fornecedores de insumo/semente e de gado (com ou sem rastreabilidade), sem carrinho ou pedido - so contato direto.

> O antigo marketplace de maquinas/insumos com pedido (`ProdutoMarketplace`/`Pedido`), o feed social (`Postagem`) e a interface administrativa em Thymeleaf (`web/` + `templates/`) foram removidos. Eram escopo do TCC original; o produto foi reduzido de proposito para caber num time pequeno e evitar competir de frente com marketplaces ja estabelecidos (MF Rural, Agrofy, Agrishare). Se precisar recuperar algo, esta no historico do git.

## Tecnologias

- Java 21
- Spring Boot 3.4.x
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Maven
- Swagger/OpenAPI (springdoc)
- Open-Meteo (previsao do tempo, API publica sem chave)

## Estrutura do Projeto

```text
src/main/java/com/agromach
  config       # CORS, OpenAPI, bootstrap do admin padrao
  controller   # Endpoints REST
  dto          # Contratos de entrada/saida (records)
  entity       # Mapeamento JPA
  exception    # Tratamento global de erros
  repository   # Spring Data JPA
  security     # JWT, filtro de autenticacao, checagem de posse (SecurityUtils)
  service      # Regras de negocio
  util         # Constantes de rota (ApiPaths)
```

## Arquitetura (Spring Boot)

- `controller`: camada HTTP (endpoints REST, validacao de entrada, status code)
- `service`: regras de negocio, orquestracao entre componentes e checagem de posse (ex: so o dono da fazenda ou um ADMIN pode editar/apagar seus registros)
- `repository`: acesso a dados via Spring Data JPA (sem SQL nativo em nenhum lugar do projeto)
- `entity`: mapeamento JPA das tabelas do banco
- `dto`: contratos de entrada/saida da API
- `security`: JWT, filtro de autenticacao e `SecurityUtils` (utilitario de ownership usado pelos services)
- `config`: configuracoes transversais (CORS, OpenAPI, bootstrap de dados)
- `exception`: padronizacao de erros da API

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

Crie um banco e aponte as variaveis de ambiente da secao abaixo para ele (nao precisa editar `application.yml` diretamente).

## Como rodar o backend

### 1. Compilar

```bash
mvn clean install
```

### 2. Executar

```bash
mvn spring-boot:run
```

Se o comando `mvn` nao estiver no PATH do Windows, use o Maven que vem junto do IntelliJ:

```powershell
& "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2\plugins\maven\lib\maven3\bin\mvn.cmd" spring-boot:run
```

Aplicacao sobe em:

- API: `http://localhost:8081`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## Variaveis de ambiente (segredos)

Nenhum segredo real deve ficar hardcoded fora do proprio ambiente. `application.yml` ja usa fallback de dev local, mas em qualquer ambiente que nao seja seu localhost defina:

| Variavel | Para que serve | Fallback de dev |
|---|---|---|
| `DB_URL` | URL JDBC do Postgres | `jdbc:postgresql://localhost:15432/agromach_db` |
| `DB_USERNAME` | Usuario do banco | `postgres` |
| `DB_PASSWORD` | Senha do banco | `postgres` |
| `JWT_SECRET` | Chave HS256 que assina os tokens | valor de dev fixo no `application.yml` |
| `JWT_EXPIRATION_MS` | Validade do token (ms) | `86400000` (24h) |
| `APP_BOOTSTRAP_SEED_ADMIN` | Se `true`, cria o admin padrao no startup | `true` |
| `APP_BOOTSTRAP_ADMIN_PASSWORD` | Senha do admin padrao seedado | `admin123` (inseguro - troque sempre que sair do seu localhost) |

Em producao, defina pelo menos `JWT_SECRET`, `DB_PASSWORD` e `APP_BOOTSTRAP_ADMIN_PASSWORD` (ou `APP_BOOTSTRAP_SEED_ADMIN=false`) com valores proprios.

## Seguranca e autenticacao

- Senhas com BCrypt.
- JWT Bearer Token, unica cadeia de filtros stateless (nao ha mais sessao/cookie - o backend e so API).
- Rotas publicas: `POST /auth/register`, `POST /auth/login` (e aliases `/api/auth/**`), Swagger/OpenAPI.
- **Todo o resto exige token**, inclusive `GET /api/**` (antes era publico; foi fechado por ser um risco de exposicao de dado pessoal).
- Cadastro publico (`/auth/register`) so cria `CLIENTE` ou `PRESTADOR`. Qualquer outro valor enviado no campo `role` (inclusive `ADMIN`) e ignorado e cai para `CLIENTE`. Promover alguem a `ADMIN` so e possivel via `PUT /api/usuarios/{id}` por outro `ADMIN`.
- Toda escrita (`create`/`update`/`delete`) em Fazenda, Maquina, Funcionario, Area de Producao, Aviso e Profissional so e permitida para o dono do registro (direto ou via fazenda) ou para um `ADMIN` - ver `security/SecurityUtils.java`.

Usuario admin padrao criado automaticamente no startup (se nao existir e `APP_BOOTSTRAP_SEED_ADMIN=true`):

- Email: `admin@agromach.com`
- Senha: definida por `APP_BOOTSTRAP_ADMIN_PASSWORD` (padrao de dev: `admin123`)

## Principais endpoints

### Autenticacao

- `POST /auth/register` - campos: `nome`/`name`, `email`/`username`, `senha`/`password`, `telefone`/`phone`, `documento`/`document`, `role` (so `CLIENTE` ou `PRESTADOR` tem efeito)
- `POST /auth/login` - campos: `email`/`username`, `senha`/`password`
- Resposta: `token`/`accessToken` + dados do `usuario`/`user`

### Cockpit

- `GET /api/dashboard` - dashboard da primeira fazenda do usuario logado
- `GET /api/dashboard/{fazendaId}` - dashboard de uma fazenda especifica

### CRUDs

- `GET/POST/PUT/DELETE /api/usuarios` (escrita restrita a `ADMIN`)
- `GET/POST/PUT/DELETE /api/fazendas`
- `GET/POST/PUT/DELETE /api/maquinas`
- `GET/POST/PUT/DELETE /api/funcionarios`
- `GET/POST/PUT/DELETE /api/areas-producao` (pasto/talhao/confinamento)
- `GET/POST/PUT/DELETE /api/avisos` (fertilizacao/preparo de solo/vacinacao)
- `GET/POST/PUT/DELETE /api/profissionais` (veterinario/agronomo/fornecedor/trabalhador de campo)

## Como testar as rotas

1. Cadastre usuario em `POST /auth/register` (pelo Swagger ou Postman).
2. Faca login em `POST /auth/login` e copie o token.
3. No Swagger, clique em Authorize e informe:
   ```
   Bearer SEU_TOKEN
   ```
4. Cadastre uma fazenda em `POST /api/fazendas` (com `latitude`/`longitude` se quiser testar o clima).
5. Cadastre uma area de producao em `POST /api/areas-producao` apontando para essa fazenda.
6. Confira tudo junto em `GET /api/dashboard`.

## Geracao de schema

O schema e gerado automaticamente pelo Hibernate com `ddl-auto: update`. Nao ha Flyway/Liquibase ainda (ver Observacoes).

## Observacoes

- Sem testes automatizados ainda (so o teste de contexto padrao do Spring Boot). Maior risco tecnico do projeto hoje.
- Sem migrations (Flyway/Liquibase) - schema depende do `ddl-auto: update`.
- Clima usa a API publica Open-Meteo (`https://open-meteo.com`), sem chave de API; se a fazenda nao tiver `latitude`/`longitude`, o dashboard simplesmente omite o clima.
- Banco Docker persistente em volume `agromach_postgres_data`.
