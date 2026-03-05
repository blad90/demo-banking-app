export type Account = {
  id: number;
  accountNumber: string;
  customerId: number;
  balance: number;
  accountType: string;
  accountCreationDate: Date;
  accountLastUpdated: Date;
  accountState: AccountState;
};

export enum AccountState {
    ACCOUNT_CREATED,
    ACCOUNT_ACTIVE,
    ACCOUNT_FROZEN,
    ACCOUNT_INACTIVE
}