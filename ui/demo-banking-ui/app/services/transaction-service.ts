import { redirect } from "next/navigation";
import { auth } from "../lib/auth";
import { Transaction } from "@/types/transaction";
import { Account } from "@/types/account";
import { fetchAccountsByAccountNumbers } from "./account-service";
import { fetchUsersByIds, getCustomerById, getCustomerByUserSessionId } from "./user-service";
import { User } from "@/types/user";

const session = await auth();

export async function getTransactions() {
  

  if (!session) {
    redirect("/login")
  }
    //await new Promise((resolve) => setTimeout(resolve, 5000));
    
    const res = await fetch(
      "http://localhost:8083/transactions/all",
      {
        cache: "no-store",
        headers: {
          Authorization: `Bearer ${session?.accessToken}`,
        },
      }
    )

    if (!res.ok) {
      throw new Error("Failed to fetch transactions")
    }

    return res.json()
  }

  export async function getFilteredTransactions(query: string, currentPage: number){
    const offset = (currentPage - 1) * 10; // transactions per page
    
    const res = await fetch(`http://localhost:8083/transactions/page/all/search?description=${query}&page=${currentPage}`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch transactions');
    }
    return res.json();
}



export async function getTransactionsPages(page: Number, size: Number, sort: string){  
  const userBySession = await getCustomerByUserSessionId(session?.userId!);  
  const res = await fetch(`http://localhost:8083/transactions/page/all/${userBySession.id}?page=${page}&size=${size}`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch transactions');
    }
    const data = await res.json();
    return data.totalPages;
}

export async function getTransactionsByCustomerIdByPage(page: number, size: number, sort: string){
  const userBySession = await getCustomerByUserSessionId(session?.userId!);

  const res = await fetch(`http://localhost:8083/transactions/page/all/${userBySession.id}?page=${page}&size=${size}`, { cache: 'no-cache'});
  if(!res.ok){
        throw new Error('Failed to fetch transactions by customer ID #: ' + userBySession.id);
    }
    const data = await res.json();
  const transactions: Transaction[] = data.content ?? data;

  // customer field is the userId
  const user: User = await getCustomerById(userBySession.id);

  return {
    ...data,
    content: transactions.map(transaction => ({
      ...transaction,
      customer: user ?? null,
    })),
  };
}

export async function getTransactionsByPage(page: number, size: number, sort: string){  
    const res = await fetch(`http://localhost:8083/transactions/page/all?page=${page}&size=${size}`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch transactions');
    }
    const data = await res.json();
    const transactions: Transaction[] = data.content ?? data;

    const accountNumbers = [...new Set<string>(transactions.map(t => t.destinationAccount))];
    const accounts: Account[] = await fetchAccountsByAccountNumbers(accountNumbers);
    const accountMap = Object.fromEntries(accounts.map(a => [a.accountNumber, a]));

    const customerIds = [...new Set<number>(accounts.map(a => a.customerId))];
    const users: User[] = await fetchUsersByIds(customerIds);
    const userMap = Object.fromEntries(users.map(u => [u.id, u]));

    return {
      ...data,
      content: transactions.map(transaction => ({
      ...transaction,
      account: {
        ...accountMap[transaction.destinationAccount],
        customer: userMap[accountMap[transaction.destinationAccount]?.customerId] ?? null,
      },
    })),
    }
}

