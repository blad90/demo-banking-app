## Demo Bank
#### by BLAD

---
## Entities
- **User**
  - id
  - firstName
  - lastName
  - alias
  - address
  - phoneNumber
  - emailAddress
  - userState (USER_CREATED, USER_ACTIVE, USER_SUSPENDED, USER_INACTIVE)
- **Account**
  - id
  - accountNumber
  - owner: User
  - balance
  - accountType
  - accountCreationDate
  - accountLastUpdated
  - accountState (ACCOUNT_CREATED, ACCOUNT_ACTIVE, ACCOUNT_FROZEN, ACCOUNT_INACTIVE)
- **Transaction**
  - id
  - description
  - transactionAmount
  - transactionDate
  - type (DEBIT, CREDIT)
  - transactionState (TRAN_INITIATED, TRAN_PROCESSING, TRAN_COMPLETED, TRAN_REJECTED, TRAN_CANCELLED)
---
## Services
- **User service**
  - Register user
  - Update user
  - Disable user
  - Retrieve user by id
  - Retrieve all users
- **Account service**
  - Open account
  - Update account
  - Disable account
  - Retrieve account by account number
  - Retrieve all accounts
- **Transaction service**
  - create transaction
  - retrieve transaction by id
  - retrieve all transactions
---
## Flows
### Open Account Flow
1. The `Account Service` receives `POST /accounts` request and creates the `Open Account` saga orchestrator.
2. The saga orchestrator creates an `Account` in the `ACCOUNT_CREATED` state.
3. It then sends a `Validate User` command to the `User Service`.
4. The `User Service` tries to validate the user.
5. It then back a reply message indicating the outcome.
6. The saga orchestrator either approves or reject the `Account`.

### Create Transaction Flow

1. The `Transaction Service` receives `POST /transactions` request and creates the `Create Transaction` saga orchestrator.
2. The saga orchestrator creates a `Transaction` in the `TRAN_INITIATED` state.
3. It then sends a `Reserve Credit` command to the `Account Service`.
4. The `Account Service` tries to reserve credit.
5. It then back a reply message indicating the outcome.
6. The saga orchestrator either approves or reject the `Transaction`.