# ARGOS Web CRUD

Aplicação web para gerenciamento de informações da atividade pecuária: administradores, usuários, propriedades rurais, rebanhos, lotes e animais.

Desenvolvida em **Java, Servlets, JSP e JDBC (POO)** como parte do projeto interdisciplinar ARGOS, no 1º ano do Ensino Médio Técnico em Desenvolvimento de Sistemas.

**Sistema publicado:** [argos-web-crud.onrender.com](https://argos-web-crud.onrender.com) · **Landing page:** [argos-landingpage.vercel.app](https://argos-landingpage.vercel.app/)

> Este repositório contém a aplicação web. Os demais componentes do projeto ficam em repositórios próprios, listados em [Repositórios do projeto](#repositórios-do-projeto).

---

## Funcionalidades

| Módulo | Rota | O que faz |
| --- | --- | --- |
| Autenticação | `/login`, `/logout` | Login de administradores com senha protegida por hash BCrypt |
| Painel inicial | `/home`, `/welcome` | Resumo do sistema e registros atualizados recentemente |
| Administradores | `/admins` | Cadastro, consulta, edição e exclusão |
| Usuários | `/users` | Cadastro, consulta, edição, exclusão e reativação pelo CPF (`/users/reactivate`) |
| Propriedades | `/properties` | CRUD com endereço e vínculo de vários usuários por propriedade |
| Rebanhos | `/herds` | Cadastro, consulta, edição e exclusão |
| Lotes | `/lots` | Cadastro, consulta, edição e exclusão |
| Animais | `/animals` | Cadastro, consulta, edição e exclusão |

Cada módulo segue o mesmo padrão de rotas: `GET /modulo` lista os registros e `POST /modulo/register`, `/modulo/edit` e `/modulo/delete` executam as operações.

### Decisões técnicas

- **Exclusão lógica:** excluir um registro o marca como inativo (`active = false`) e preserva o histórico.
- **Controle de acesso:** o filtro `AuthFilter` protege todas as telas do painel e só libera sessões de administrador.
- **Senhas:** armazenadas apenas como hash BCrypt.
- **Identificadores UUID** e campo `updated_at` em todas as tabelas.
- **SQL parametrizado** com `PreparedStatement` e **pool de conexões** HikariCP.
- **Transações** ao criar e editar propriedades, mantendo o vínculo com usuários consistente.
- **Validações** de CPF, CNPJ, e-mail, telefone e senha na camada de serviço, com exceções próprias (`ValidationException`, `RequiredFieldException`, `DataAccessException`).

---

## Tecnologias

| Camada | Tecnologia |
| --- | --- |
| Linguagem | Java 21 |
| Web | Jakarta Servlet 6.1, JSP e JSTL 3.0 |
| Banco de dados | PostgreSQL, JDBC (driver 42.7.14) e HikariCP 7.1.0 |
| Segurança | jBCrypt 0.4 e Cloudflare Turnstile no login |
| Build | Maven (com Maven Wrapper), empacotamento WAR |
| Servidor | Apache Tomcat 11 |
| Interface | HTML, CSS e JavaScript |
| Testes | JUnit 5 |
| Ferramentas | Git, GitHub e IntelliJ IDEA |

---

## Arquitetura

A aplicação separa responsabilidades em camadas, seguindo a ideia do padrão MVC:

```
Navegador → Servlet → Service → DAO → PostgreSQL
              ↓
             JSP (resposta ao usuário)
```

| Camada | Responsabilidade |
| --- | --- |
| Model | Entidades que representam as tabelas do banco |
| Controller (Servlets) | Recebem as requisições HTTP e coordenam as operações |
| Service | Regras de negócio e validações |
| DAO | Acesso ao banco com JDBC |
| View (JSP) | Páginas exibidas ao usuário |
| ConnectionFactory | Pool de conexões com o banco |

---

## Estrutura de pacotes

```
src/main/java/br/com/argos/
├── connection/    pool de conexões e ciclo de vida da aplicação
├── controller/    servlets organizados por módulo
├── dao/           acesso a dados (CRUD com JDBC)
├── exceptions/    exceções personalizadas
├── filter/        cabeçalhos de segurança das respostas
├── interfaces/    contratos dos DAOs
├── model/         entidades
├── service/       regras de negócio e validações
└── util/          validação, normalização e formatação

src/main/webapp/
├── assets/        CSS, JavaScript e imagens
└── WEB-INF/views/ páginas JSP

src/test/java/br/com/argos/util/   testes de validação e normalização
```

---

## Modelo de dados

O banco do ARGOS é mantido em um repositório próprio, o [argos-bd](https://github.com/projeto-argos/argos-bd), que reúne os scripts, a modelagem e a convenção de nomenclatura. Ele inclui tabelas de outros módulos do ecossistema (como medicamentos e carência), que esta aplicação não utiliza.

Tabelas usadas por esta aplicação:

| Tabela | Finalidade |
| --- | --- |
| `admin` | Administradores do sistema |
| `USER` | Usuários vinculados às propriedades |
| `address` | Endereços |
| `property` | Propriedades rurais (um endereço por propriedade) |
| `user_property` | Vínculo muitos-para-muitos entre usuários e propriedades |
| `herd` | Rebanhos de uma propriedade (finalidade `CORTE`) |
| `lot` | Lotes de um rebanho (categorias: `ENGORDA`, `CRIA` e `RECRIA`) |
| `animal` | Animais de um lote, identificados por brinco |

---

## Como executar

### Pré-requisitos

- JDK 21
- PostgreSQL
- Apache Tomcat 11
- Maven (opcional: o projeto inclui o Maven Wrapper)

### Passo a passo

1. Clone o repositório:

   ```bash
   git clone https://github.com/projeto-argos/argos-web-crud.git
   cd argos-web-crud
   ```

2. Crie um banco no PostgreSQL e execute, nesta ordem, os scripts em inglês da pasta [`sql/`](https://github.com/projeto-argos/argos-bd/tree/main/sql) do argos-bd:
   1. `scriptDDL+ConstraintsIngles.sql` (tabelas e restrições)
   2. `scriptDataLoadIngles.sql` (dados iniciais, incluindo os administradores da equipe)

3. Crie o arquivo `src/main/resources/.env` (veja [Variáveis de ambiente](#variáveis-de-ambiente)).

4. Gere o pacote da aplicação:

   ```bash
   ./mvnw clean package
   ```

5. Publique o arquivo `.war` gerado em `target/` no Tomcat (ou execute pela IDE) e acesse `/login`.

### Primeiro acesso

O painel só aceita administradores. O DataLoad do argos-bd já cria os administradores da equipe. Em um banco vazio, crie o primeiro direto no banco. O exemplo abaixo gera o hash BCrypt com a extensão `pgcrypto` do PostgreSQL. Troque os dados antes de usar:

```sql
CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO admin (full_name, cpf, email, password)
VALUES ('Administrador', '00000000000', 'admin@exemplo.com',
        crypt('troque-esta-senha', gen_salt('bf', 10)));
```

Depois do primeiro login, os demais administradores podem ser cadastrados pela própria tela `/admins`.

### Testes

```bash
./mvnw test
```

Os testes `ValidatorTest` e `NormalizerTest` cobrem a validação (CPF, CNPJ, e-mail, telefone e senha) e a normalização dos dados de entrada.

---

## Variáveis de ambiente

O projeto lê as configurações de um arquivo `.env` em `src/main/resources/` (ou de variáveis de ambiente com os mesmos nomes, que têm prioridade). Use o [`.env.example`](.env.example) da raiz como modelo.

⚠️ **Nunca versionar o `.env`.** Ele já está no `.gitignore`.

| Variável | Obrigatória | Para que serve |
| --- | --- | --- |
| `DB_URL` | sim | Endereço JDBC do banco (`jdbc:postgresql://HOST:PORTA/BANCO?sslmode=require`) |
| `DB_USER` | sim | Usuário do banco |
| `DB_PASSWORD` | sim | Senha do banco |
| `TURNSTILE_SECRET` | sim | Chave secreta do Cloudflare Turnstile, usada no login |
| `COOKIE_SECURE` | na hospedagem | `true` marca o cookie de sessão como `Secure` (só com HTTPS) |
| `CSP_REPORT_ONLY` | não | `true` testa a política de segurança (CSP) sem bloquear nada |
| `DB_POOL_MAX` | não | Máximo de conexões do pool, de 1 a 8 (padrão 3). O Aiven aceita 20 no total |

---

## Publicação

O sistema está publicado no Render a partir do `Dockerfile` da raiz: a primeira etapa compila o WAR com Maven, e a segunda o publica como aplicação raiz em um Tomcat 11 com Java 21. As variáveis de ambiente são configuradas no próprio Render, com `COOKIE_SECURE=true`.

---

## Repositórios do projeto

| Repositório | Conteúdo |
| --- | --- |
| [argos-web-crud](https://github.com/projeto-argos/argos-web-crud) | Aplicação web (este repositório) |
| [argos-bd](https://github.com/projeto-argos/argos-bd) | Modelagem, scripts SQL e convenção de nomenclatura |
| [argos-landingpage](https://github.com/projeto-argos/argos-landingpage) | Landing page e telas estáticas do sistema |
| [argos-ai](https://github.com/projeto-argos/argos-ai) | Agente de IA (Gem) e memorial descritivo |
| [argos-docs](https://github.com/projeto-argos/argos-docs) | Artefatos de UX |
| [.github](https://github.com/projeto-argos/.github) | Perfil da organização e padrões de colaboração |

---

## Colaboração

Este projeto adota o padrão [Conventional Commits](https://www.conventionalcommits.org/pt-br/v1.0.0/), com mensagens no formato `tipo(escopo): descrição`.

| Tipo | Uso |
| --- | --- |
| `feat` | Nova funcionalidade |
| `fix` | Correção de bug |
| `docs` | Documentação |
| `style` | Formatação, sem mudar o comportamento |
| `refactor` | Refatoração, sem mudar a funcionalidade |
| `test` | Testes |
| `chore` | Build, dependências e configurações |

Exemplos: `feat(login): botão para voltar à landing page`, `fix(userUpdateServlet.java): trata id ausente na mensagem de erro`, `docs(env.example): documenta DB_POOL_MAX`.

As regras de branches e Pull Requests estão em [CONTRIBUTING.md](https://github.com/projeto-argos/.github/blob/main/CONTRIBUTING.md).

---

## Equipe

- Eduardo Toledo Alves de Almeida
- Letícia Silva Ribeiro
- Lucca Cansani
- Murilo Gonçalves Paiva
- Pietra de Oliveira Silva Gomes

---

## Licença

Distribuído sob a licença MIT. Consulte o arquivo [LICENSE](LICENSE).
