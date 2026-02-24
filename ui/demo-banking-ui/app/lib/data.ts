import { redirect } from "next/navigation";
import { auth } from "./auth";

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

  export async function getFilteredAccounts(query: string, currentPage: number){
    const offset = (currentPage - 1) * 10; // accounts per page
    
    const res = await fetch(`http://localhost:8082/accounts/page/all/search?query=${query}&page=${currentPage}`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch accounts');
    }
    return res.json();
}

export async function getAccountsPages(page: Number, size: Number, sort: string){  
    const res = await fetch(`http://localhost:8082/accounts/page/all`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch accounts');
    }
    const data = res.json();
    //const totalPages = Math.ceil(Number(data[0].count) / 10); // /10 items per page
    return 5;
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

export async function getAccountsByPage(page: number, size: number, sort: string){  
    const res = await fetch(`http://localhost:8082/accounts/page/all?page=${page}&size=${size}`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch accounts');
    }
    return res.json();
}

export async function getTransactionsByPage(page: number, size: number, sort: string){  
    const res = await fetch(`http://localhost:8083/transactions/page/all?page=${page}&size=${size}`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch transactions');
    }
    return res.json();
}