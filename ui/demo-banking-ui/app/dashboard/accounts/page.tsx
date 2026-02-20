import AccountsTable from "@/app/ui/accounts/accounts-table";
import TableRowSkeleton from "@/app/ui/skeletons";
import { Suspense } from "react";
import {getAccounts} from "@/app/lib/data";

export default async function Page(){
    const accounts = await getAccounts();

    return <main>
        <h1 className="text-3xl font-bold">Accounts</h1>
        <Suspense fallback={<TableRowSkeleton />}>
            <AccountsTable accounts={accounts}/>
        </Suspense>
    </main>
}