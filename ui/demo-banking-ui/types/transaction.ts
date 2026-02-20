export type Transaction = {
  correlationId: string;
  sourceAccount: string;
  destinationAccount: string;
  transactionAmount: number;
  transactionDate: Date;
  type: TransactionType;
  transactionState: TransactionState;
};

export enum TransactionType {
  DEBIT, CREDIT, TRANSFER
};

export enum TransactionState {
    TRAN_INITIATED,
    TRAN_PROCESSING,
    TRAN_COMPLETED,
    TRAN_REJECTED,
    TRAN_CANCELLED
};