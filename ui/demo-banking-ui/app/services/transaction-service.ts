import { redirect } from "next/navigation";
import { auth } from "../lib/auth";
import { Transaction } from "@/types/transaction";
import { Account } from "@/types/account";
import { fetchAccountsByAccountNumbers } from "./account-service";
import { fetchUsersByIds } from "./user-service";
import { User } from "@/types/user";

export async function getTransactions() {
  const session = await auth();

  if (!session) {
    redirect("/login")
  }
    console.log('Fetching transactions data...');
    await new Promise((resolve) => setTimeout(resolve, 5000));
    
    const res = await fetch(
      "http://localhost:8083/transactions/all",
      {
        cache: "no-store",
        headers: {
          Authorization: `Bearer ${session?.accessToken}`,
        },
      }
    )
    console.log('Data fetch completed after 5 seconds.');

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
    const res = await fetch(`http://localhost:8083/transactions/page/all`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch transactions');
    }
    const data = res.json();
    //const totalPages = Math.ceil(Number(data[0].count) / 10); // /10 items per page
    return 5;
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

    const customerIds = [...new Set<number>(accounts.map(a => a.customer))];
    const users: User[] = await fetchUsersByIds(customerIds);
    const userMap = Object.fromEntries(users.map(u => [u.id, u]));

    return {
      ...data,
      content: transactions.map(transaction => ({
      ...transaction,
      account: {
        ...accountMap[transaction.destinationAccount],
        customer: userMap[accountMap[transaction.destinationAccount]?.customer] ?? null,
      },
    })),
    }
}

