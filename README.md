# 💆 Hair Removal Appointment API

API REST para gerenciamento de agendamentos de depilação, desenvolvida com Spring Boot. Permite cadastro de usuários, autenticação com JWT e controle de agendamentos com níveis de acesso (Admin e User).

---

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3**
- **Spring Security + JWT**
- **Spring Data JPA + Hibernate**
- **MySQL**
- **MapStruct**
- **Lombok**

---

## ⚙️ Como rodar localmente

### Pré-requisitos
- Java 17+
- MySQL rodando localmente
- Gradle

### 1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/cidasdepilacao-api.git
cd cidasdepilacao-api
```

### 2. Configure as variáveis de ambiente
Crie um arquivo `.env` na raiz do projeto baseado no `.env.example`:
```env
DB_URL=jdbc:mysql://localhost:3306/cidasdepilacao?createDatabaseIfNotExist=true
DB_USER=root
DB_PASSWORD=suasenha
JWT_SECRET=sua-chave-secreta-minimo-32-caracteres
PORT=8080
```

### 3. Configure o profile
No `application.properties`:
```properties
spring.profiles.active=dev
```

### 4. Rode a aplicação
```bash
./gradlew bootRun
```

A API estará disponível em `http://localhost:8080`

---

## 🔐 Autenticação

A API usa **Bearer Token (JWT)**. Para acessar rotas protegidas:

1. Faça o registro ou login
2. Copie o token retornado
3. Adicione no header das requisições:
```
Authorization: Bearer <seu-token>
```

---

## 📋 Endpoints

### Auth — Público

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/v1/auth/register` | Cadastro de novo usuário |
| `POST` | `/api/v1/auth/login` | Login e geração do token JWT |

#### Registro
```http
POST /api/v1/auth/register
Content-Type: application/json

{
    "name": "João Silva",
    "email": "joao@email.com",
    "password": "senha123",
    "phoneNumber": "+5511999999999",
    "birthday": "1995-05-20"
}
```

#### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
    "email": "joao@email.com",
    "password": "senha123"
}
```

Resposta:
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "areas": "Bearer",
    "email": "joao@email.com",
    "role": "USER"
}
```

---

### Appointment — Autenticado (USER e ADMIN)

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/v1/appointment` | Criar agendamento |
| `GET` | `/api/v1/appointment/my` | Ver meu agendamento |

#### Criar agendamento
```http
POST /api/v1/appointment
Authorization: Bearer <token>
Content-Type: application/json

{
    "horary": "2026-06-15T10:00:00",
    "areas": "LASER",
    "payment": "PIX"
}
```

Resposta:
```json
{
    "id": "uuid",
    "horary": "2026-06-15T10:00:00",
    "areas": "LASER",
    "payment": "PIX",
    "situation": "PROGRESS"
}
```

---

### Appointment — Somente ADMIN

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/v1/appointment` | Listar todos os agendamentos |
| `GET` | `/api/v1/appointment/{id}` | Buscar agendamento por ID |
| `PUT` | `/api/v1/appointment/{id}` | Atualizar agendamento |
| `DELETE` | `/api/v1/appointment/{id}` | Deletar agendamento |

---

### User — Somente ADMIN

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/v1/user` | Listar todos os usuários |
| `GET` | `/api/v1/user/{name}` | Buscar usuário por nome |
| `DELETE` | `/api/v1/user` | Deletar usuário |
| `PATCH` | `/api/v1/user/{id}/promote` | Promover usuário para ADMIN |

---

### User — Autenticado (USER e ADMIN)

| Método | Rota | Descrição |
|--------|------|-----------|
| `PUT` | `/api/v1/user/{id}` | Atualizar próprio perfil |

---

## 🔒 Níveis de Acesso

| Role | Permissões |
|------|------------|
| `USER` | Criar agendamento, ver próprio agendamento, atualizar próprio perfil |
| `ADMIN` | Tudo do USER + gerenciar todos os usuários e agendamentos |

> Por padrão todo usuário cadastrado recebe a role `USER`. Para promover um usuário para `ADMIN`, use o endpoint `PATCH /api/v1/user/{id}/promote` com um token de ADMIN.

---

## ❌ Respostas de Erro

| Status | Descrição |
|--------|-----------|
| `400` | Dados inválidos na requisição |
| `401` | Token ausente ou inválido |
| `403` | Sem permissão para acessar o recurso |
| `404` | Recurso não encontrado |
| `500` | Erro interno do servidor |

Exemplo de erro:
```json
{
    "status": 403,
    "error": "Acesso negado",
    "message": "Você não tem permissão para acessar este recurso",
    "path": "/api/v1/user"
}
```

---

## 🗄️ Variáveis de Ambiente

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `DB_URL` | URL de conexão com o banco | `jdbc:mysql://host:3306/db` |
| `DB_USER` | Usuário do banco | `root` |
| `DB_PASSWORD` | Senha do banco | `suasenha` |
| `JWT_SECRET` | Chave secreta para o JWT | `chave-minimo-32-caracteres` |
| `PORT` | Porta da aplicação | `8080` |

---

## 📁 Estrutura do Projeto

```
src/main/java/br/com/latanks/cidasdepilacao_api/
├── configs/          # Configurações (Security, Payment)
├── controllers/      # Controllers REST
├── dtos/             # DTOs de request e response
│   ├── request/
│   └── response/
├── exceptions/       # Exceções customizadas
├── filters/          # Filtro JWT
├── models/           # Entidades JPA
│   └── enums/
├── repositories/     # Interfaces JPA
├── security/         # JWT Service, UserDetails
├── services/         # Regras de negócio
└── utils/            # Utilitários
```

---

## 👤 Autor

Desenvolvido por **Latanks**
