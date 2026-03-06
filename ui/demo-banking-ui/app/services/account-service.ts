import { Account} from "@/types/account";
import { fetchUsersByIds, getCustomerById, getCustomerByUserSessionId } from "./user-service";
import { User } from "@/types/user";
import { auth } from "../lib/auth";

const session = await auth();

  export async function getFilteredAccounts(query: string, currentPage: number){
    const offset = (currentPage - 1) * 10; // accounts per page
    
    const res = await fetch(`http://localhost:8082/accounts/page/all/search?query=${query}&page=${currentPage}`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch accounts');
    }
    return res.json();
}

export async function getAccountsPages(page: Number, size: Number, sort: string){  
  const user = await getCustomerByUserSessionId(session?.userId!);  
  const res = await fetch(`http://localhost:8082/accounts/page/all/${user.id}?page=${page}&size=${size}`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch accounts');
    }
    const data = await res.json();
    return data.totalPages;
}

export async function getAccountsByCustomerIdByPage(page: number, size: number, sort: string){
  const user = await getCustomerByUserSessionId(session?.userId!);

  const res = await fetch(`http://localhost:8082/accounts/page/all/${user.id}?page=${page}&size=${size}`, { cache: 'no-cache'});
  if(!res.ok){
        throw new Error('Failed to fetch accounts');
    }
    const data = await res.json();
  const accounts: Account[] = data.content ?? data;

  // customer field is the userId
  const customerIds = [...new Set<number>(accounts.map(a => a.customerId))];
  const users: User[] = await fetchUsersByIds(customerIds);
  
  const userMap = Object.fromEntries(users.map(u => [u.id, u]));

  return {
    ...data,
    content: accounts.map(account => ({
      ...account,
      customer: userMap[account.customerId] ?? null,
    })),
  };
}

export async function getAccountsByPage(page: number, size: number, sort: string){  
    const res = await fetch(`http://localhost:8082/accounts/page/all?page=${page}&size=${size}`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch accounts');
    }
    const data = await res.json();
  const accounts: Account[] = data.content ?? data;

  // customer field is the userId
  const customerIds = [...new Set<number>(accounts.map(a => a.customerId))];
  const users: User[] = await fetchUsersByIds(customerIds);
  
  const userMap = Object.fromEntries(users.map(u => [u.id, u]));

  return {
    ...data,
    content: accounts.map(account => ({
      ...account,
      customer: userMap[account.customerId] ?? null,
    })),
  };
}

export async function fetchAccountsByAccountNumbers(accountNumbers: string[]) {
  const res = await fetch(
    `http://localhost:8082/accounts?accountNumbers=${accountNumbers.join(',')}`,
    { cache: 'no-cache' }
  );
  if (!res.ok) throw new Error('Failed to fetch accounts');
  return res.json();
}