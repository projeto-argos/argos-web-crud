# ARGOS — Aplicação Web CRUD

Aplicação web do projeto interdisciplinar ARGOS, desenvolvida em Java com Maven, JDBC, Servlets e PostgreSQL.

## Tecnologias

- Java
- Maven
- JDBC
- Servlets
- PostgreSQL
- HTML
- CSS
- JavaScript

## Arquitetura

O projeto utiliza separação de responsabilidades:

```text
src/
└── main/
    ├── java/
    │   └── br/com/argos/
    │       ├── model/
    │       ├── dao/
    │       ├── servlet/
    │       └── util/
    ├── resources/
    └── webapp/
        ├── assets/
        │   ├── css/
        │   └── img/
        └── WEB-INF/
```

- **Model:** representa as entidades e os dados do sistema.
- **DAO:** realiza as operações com o banco de dados.
- **Servlet:** recebe as requisições e controla os fluxos da aplicação.
- **Util:** reúne configurações e recursos auxiliares.

## Banco de dados

A modelagem e os scripts oficiais do PostgreSQL estão no repositório [argos-bd](https://github.com/projeto-argos/argos-bd).

## Configuração

Dados de conexão, senhas e outras informações sensíveis não devem ser enviados ao GitHub. O projeto utilizará variáveis de ambiente e um arquivo `.env.example` sem valores secretos.

## Status

Em desenvolvimento.
