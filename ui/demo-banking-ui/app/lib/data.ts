import { redirect } from "next/navigation";
import { auth } from "./auth";

export async function getTransactions() {
  const session = await auth();

  if (!session) {
    redirect("/login")
  }
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

  export async function getAccounts(){
    const res = await fetch('http://localhost:8082/accounts/all', { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch accounts');
    }
    return res.json();
}