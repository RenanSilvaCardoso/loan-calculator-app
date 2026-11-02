# Calculadora de Empréstimos

Aplicação web que calcula o cronograma de um empréstimo.

O usuário informa as datas, o valor e a taxa de juros, e a tela mostra uma grid com as parcelas, a amortização, o saldo e os juros de cada data.

## Tecnologias

| Camada   | Stack                                        |
|----------|----------------------------------------------|
| Backend  | Java 17, Spring Boot 4, Bean Validation      |
| Frontend | React 19, TypeScript, Vite, Sass             |
| Infra    | Docker, Docker Compose, Nginx                |

## Como rodar

### Com Docker (recomendado)

```bash
docker compose up --build
```

Acesse **http://localhost:3000**.

O Nginx serve o frontend e encaminha as chamadas `/api` para o backend.

### Sem Docker

Pré-requisitos: Java 17+ e Node 22+.

```bash
# Backend (porta 8080)
cd backend
./mvnw spring-boot:run

# Frontend (porta 5173), em outro terminal
cd frontend
npm install
npm run dev
```

Acesse **http://localhost:5173**. O Vite encaminha as chamadas `/api` para o backend.

### Testes

```bash
cd backend
./mvnw test
```

Os testes do cálculo usam o mesmo cenário da planilha e comparam os valores linha a linha.

## Estrutura do projeto

```
├── backend/
│   └── src/main/java/com/loancalculator/backend/
│       ├── controller/   # Endpoint REST
│       ├── dto/          # Request, response e linha do cronograma
│       ├── exception/    # Tratamento dos erros de validação
│       └── service/      # Regras de cálculo e calendário de dias úteis
├── frontend/
│   └── src/
│       ├── components/   # Formulário e tabela
│       ├── services/     # Chamada à API
│       ├── types/        # Tipos TypeScript
│       └── utils/        # Formatação de moeda, número e data
└── docker-compose.yml
```
