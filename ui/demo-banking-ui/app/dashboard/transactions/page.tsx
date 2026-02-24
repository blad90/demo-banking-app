
import TransactionsTable from "@/app/ui/transactions/transactions-table";
import { Suspense } from "react";
import TableRowSkeleton from "@/app/ui/skeletons";
import {getTransactionsPages} from "@/app/lib/data";
import Pagination from "@/app/ui/transactions/pagination";
import Search from "@/app/ui/search";
import { Transfer } from "@/app/ui/accounts/buttons";
  
export default async function Page(props: {
  searchParams?: Promise<{
    description?: string;
    page?: string;
  }>;
}){
    const searchParams = await props.searchParams;
        const query = searchParams?.description || '';
        const currentPage = Number(searchParams?.page) || 1;
        //const totalPages = await getAccountsPages(query);
        const totalPages = await getTransactionsPages(currentPage, 10, '');

    return <main>
        <div className="mt-4 flex items-center justify-between gap-2 md:mt-8">
            <Search placeholder="Search accounts..." />
            <Transfer/>
        </div>
        <h1 className="text-3xl font-bold">Transactions</h1>
        <Suspense key={query + currentPage} fallback={<TableRowSkeleton />}>
          <TransactionsTable description={query} currentPage={currentPage}/>
        </Suspense>
        <div className="mt-5 flex w-full justify-center">
                <Pagination totalPages={totalPages} />
              </div>
    </main>
}