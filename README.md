# ARGOS Web CRUD

Aplicação web para gerenciamento de informações da atividade pecuária: administradores, usuários, propriedades rurais, rebanhos, lotes e animais.

Desenvolvida em **Java, Servlets, JSP e JDBC (POO)** como parte do projeto interdisciplinar ARGOS, no 1º ano do Ensino Médio Técnico em Desenvolvimento de Sistemas.

> Este repositório contém a aplicação web. Os demais componentes do projeto ficam em repositórios próprios, listados em [Repositórios do projeto](#repositórios-do-projeto).

---

## Funcionalidades

| Módulo | Rota | O que faz |
| --- | --- | --- |
| Autenticação | `/login`, `/logout` | Login de administradores com senha protegida por hash BCrypt |
| Painel inicial | `/home`, `/welcome` | Resumo do sistema e registros atualizados recentemente |
| Administradores | `/admins` | Cadastro, consulta, edição e exclusão |
| Usuários | `/users` | Cadastro, consulta, edição e exclusão |
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
| Segurança | jBCrypt 0.4 |
| Build | Maven (com Maven Wrapper), empacotamento WAR |
| Servidor | Apache Tomcat compatível com Jakarta Servlet 6.x |
| Interface | HTML, CSS e JavaScript |
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
├── interfaces/    contratos dos DAOs
├── model/         entidades
├── service/       regras de negócio e validações
└── util/          validação, normalização e formatação

src/main/webapp/
├── assets/        CSS, JavaScript e imagens
└── WEB-INF/views/ páginas JSP
```

---

## Modelo de dados

O banco do ARGOS é mantido em um repositório próprio, o [argos-bd](https://github.com/projeto-argos/argos-bd), que reúne o script de criação e a modelagem completa. Ele inclui tabelas de outros módulos do ecossistema (como medicamentos e carência), que esta aplicação não utiliza.

Tabelas usadas por esta aplicação:

| Tabela | Finalidade |
| --- | --- |
| `admin` | Administradores do sistema |
| `USER` | Usuários vinculados às propriedades |
| `address` | Endereços |
| `property` | Propriedades rurais (um endereço por propriedade) |
| `user_property` | Vínculo muitos-para-muitos entre usuários e propriedades |
| `herd` | Rebanhos de uma propriedade |
| `lot` | Lotes de um rebanho (categorias: engorda, reprodução e recria) |
| `animal` | Animais de um lote, identificados por brinco |

---

## Como executar

### Pré-requisitos

- JDK 21
- PostgreSQL
- Apache Tomcat compatível com Jakarta Servlet 6.x
- Maven (opcional: o projeto inclui o Maven Wrapper)

### Passo a passo

1. Clone o repositório:

   ```bash
   git clone https://github.com/projeto-argos/argos-web-crud.git
   cd argos-web-crud
   ```

2. Crie um banco no PostgreSQL e execute o script [`sql/schema.sql`](https://github.com/projeto-argos/argos-bd/blob/main/sql/schema.sql) do repositório argos-bd.

3. Crie o arquivo `src/main/resources/.env` (veja [Variáveis de ambiente](#variáveis-de-ambiente)).

4. Gere o pacote da aplicação:

   ```bash
   ./mvnw clean package
   ```

5. Publique o arquivo `.war` gerado em `target/` no Tomcat (ou execute pela IDE) e acesse `/login`.

### Primeiro acesso

O painel só aceita administradores, então o primeiro precisa ser criado direto no banco. O exemplo abaixo gera o hash BCrypt com a extensão `pgcrypto` do PostgreSQL. Troque os dados antes de usar:

```sql
CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO admin (full_name, cpf, email, password)
VALUES ('Administrador', '00000000000', 'admin@exemplo.com',
        crypt('troque-esta-senha', gen_salt('bf', 10)));
```

Depois do primeiro login, os demais administradores podem ser cadastrados pela própria tela `/admins`.

---

## Variáveis de ambiente

O projeto lê as configurações do banco de um arquivo `.env` em `src/main/resources/`.

⚠️ **Nunca versionar o `.env`.** Ele já está no `.gitignore`.

```
# BANCO DE DADOS
DB_URL=jdbc:postgresql://localhost:5432/nome_do_banco
DB_USER=
DB_PASSWORD=
```

---

## Repositórios do projeto

| Repositório | Conteúdo |
| --- | --- |
| [argos-web-crud](https://github.com/projeto-argos/argos-web-crud) | Aplicação web (este repositório) |
| [argos-bd](https://github.com/projeto-argos/argos-bd) | Modelagem e scripts do banco de dados |
| [argos-landingpage](https://github.com/projeto-argos/argos-landingpage) | Página pública de apresentação |
| [argos-ai](https://github.com/projeto-argos/argos-ai) | Memorial, prompts e evidências do uso de IA |
| [argos-docs](https://github.com/projeto-argos/argos-docs) | UX, metodologia e documentação |
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

Exemplos: `feat(property): vincular usuários à propriedade`, `fix(controller): tratar erro de validação`, `docs: atualizar README`.

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
