export type User = {
  id: number;
  firstName: string;
  lastName: string;
  alias: string;
  nationalId: string;
  address: string;
  phoneNumber: string;
  emailAddress: string;
  userState: UserState;
  userSessionId: string;
};

export enum UserState {
  USER_CREATED,
  USER_ACTIVE,
  USER_SUSPENDED,
  USER_INACTIVE
};