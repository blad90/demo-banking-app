
import { auth } from "@/app/lib/auth";
import TransactionsTable from "@/app/ui/transactions/transactions-table";
import { redirect } from "next/navigation";
import { Suspense } from "react";
import TableRowSkeleton from "@/app/ui/skeletons";
import {getTransactions} from "@/app/lib/data";
import TransferPage from "./transfer/page";
  
export default async function Page(){
    const transactions = await getTransactions();

    return <main>
        <h1 className="text-3xl font-bold">Transactions</h1>
        <Suspense fallback={<TableRowSkeleton />}>
          <TransactionsTable transactions={transactions}/>
        </Suspense>
    </main>
}