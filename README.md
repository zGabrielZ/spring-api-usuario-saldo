# API Gestão Saldos de Usuários

# Tecnologias
- Java (Versão 11)
- Lombok
- Spring Boot REST
- Spring Data JPA
- Spring Security
- JUnit 
- Banco de dados H2 (Memória)
- Banco de dados PostgreSQL
- Swagger (Documentação)
- Docker

# Endpoints do usuário

### POST/usuarios
Esse endpoint é responsável realizar uma inserção de um usuário.

#### Parâmetros 

cpf : CPF do usuário

dataNascimento : Data nascimento do usuário

email : E-mail do usuário

nome : Nome completo do usuário

senha : Senha do usuário

#### Exemplo 

```
{
  "cpf": "84269756071",
  "dataNascimento": "26/12/1997",
  "email": "ferreiragabriel2612@gmail.com",
  "nome": "Gabriel Ferreira",
  "senha": "123"
}
```

##### Respostas
Created ! 201.

Bad Request ! 400.

### GET/usuarios
Esse endpoint é responsável retornar lista de usuários filtrados.

#### Exemplo de retorno

```
{
  "content": [
    {
      "cpf": "84269756071",
      "dataNascimento": "26/12/1997",
      "email": "ferreiragabriel2612@gmail.com",
      "id": 1,
      "nome": "Gabriel Ferreira"
    }
  ],
  "empty": true,
  "first": true,
  "last": true,
  "number": 0,
  "numberOfElements": 0,
  "pageable": {
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 0,
    "paged": true,
    "sort": {
      "empty": true,
      "sorted": true,
      "unsorted": true
    },
    "unpaged": true
  },
  "size": 0,
  "sort": {
    "empty": true,
    "sorted": true,
    "unsorted": true
  },
  "totalElements": 0,
  "totalPages": 0
}
```

##### Respostas
OK ! 200.

Bad Request ! 400.

### PUT/usuarios/{id}
Esse endpoint é responsável realizar uma atualização de um usuário.

#### Parâmetros 

cpf : CPF do usuário

dataNascimento : Data nascimento do usuário

email : E-mail do usuário

nome : Nome completo do usuário

senha : Senha do usuário

#### Exemplo 

```
{
  "cpf": "84269756071",
  "dataNascimento": "31/12/1990",
  "email": "jose@gmail.com",
  "nome": "José Fernandes",
  "senha": "123"
}
```

##### Respostas
Ok ! 200.

Bad Request ! 400.

Not Found ! 404.

### DELETE/usuarios/{id}
Esse endpoint é responsável realizar uma deleção de um usuário.

##### Respostas
No Content ! 204.

Not Found ! 404.

### GET/usuarios/{id}
Esse endpoint é responsável realizar uma consulta de um usuário.

#### Exemplo de retorno

```
{
  "cpf": "84269756071",
  "dataNascimento": "26/12/1997",
  "email": "ferreiragabriel2612@gmail.com",
  "id": 1,
  "nome": "Gabriel Ferreira"
}
```

##### Respostas
Ok ! 200.

Not Found ! 404.

### POST/usuarios/sacar
Esse endpoint é responsável realizar um saque de um usuário.

#### Parâmetros 

idUsuario : ID do usuário

quantidade : Quantidade do valor que o usuário quer sacar

#### Exemplo 

```
{
  "idUsuario": 1,
  "quantidade": 400
}
```

##### Respostas
Created ! 201.

Bad Request ! 400.

Not Found ! 404.

### GET/usuarios/saques/{id}
Esse endpoint é responsável retornar uma lista de saques de um usuário.

#### Exemplo de retorno

```
{
  "content": [
    {
      "dataSaque": "26/06/2022 12:00:00",
      "valor": 500
    }
  ],
  "empty": true,
  "first": true,
  "last": true,
  "number": 0,
  "numberOfElements": 0,
  "pageable": {
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 0,
    "paged": true,
    "sort": {
      "empty": true,
      "sorted": true,
      "unsorted": true
    },
    "unpaged": true
  },
  "size": 0,
  "sort": {
    "empty": true,
    "sorted": true,
    "unsorted": true
  },
  "totalElements": 0,
  "totalPages": 0
}
```

##### Respostas
OK ! 200.

Bad Request ! 400.

### GET/usuarios/saldos/{id}
Esse endpoint é responsável retornar uma lista de saldos de um usuário.

#### Exemplo de retorno

```
{
  "content": [
    {
      "dataDeposito": "26/06/2022 12:00:00",
      "deposito": 500,
      "id": 1
    }
  ],
  "empty": true,
  "first": true,
  "last": true,
  "number": 0,
  "numberOfElements": 0,
  "pageable": {
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 0,
    "paged": true,
    "sort": {
      "empty": true,
      "sorted": true,
      "unsorted": true
    },
    "unpaged": true
  },
  "size": 0,
  "sort": {
    "empty": true,
    "sorted": true,
    "unsorted": true
  },
  "totalElements": 0,
  "totalPages": 0
}
```

##### Respostas
OK ! 200.

Bad Request ! 400.

### GET/usuarios/saldo-total/{id}
Esse endpoint é responsável retornar saldo total de um usuário.

#### Exemplo de retorno

```
{
  "saldoTotal": 500
}
```

##### Respostas
OK ! 200.

Not Found ! 404.

# Endpoints do saldo

### POST/saldos/depositar
Esse endpoint é responsável realizar um depósito de um usuário.

#### Parâmetros 

dataDeposito : Data depósito de um usuário

deposito : Valor do depósito de um usuário

idUsuario : ID de um usuário

#### Exemplo 

```
{
  "dataDeposito": "26/06/2022 12:00:00",
  "deposito": 500,
  "idUsuario": 1
}
```

##### Respostas
Created ! 201.

Bad Request ! 400.


Not Found ! 404.

