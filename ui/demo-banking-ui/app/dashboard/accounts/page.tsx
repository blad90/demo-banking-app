import AccountsTable from "@/app/ui/accounts/accounts-table";
import TableRowSkeleton from "@/app/ui/skeletons";
import { Suspense } from "react";
import {getAccountsPages} from "@/app/lib/data";
import Search from "@/app/ui/search";
import { CreateAccount } from "@/app/ui/accounts/buttons";
import Pagination from "@/app/ui/accounts/pagination";

export default async function Page(props: {
  searchParams?: Promise<{
    query?: string;
    page?: string;
  }>;
}){
    const searchParams = await props.searchParams;
    const query = searchParams?.query || '';
    const currentPage = Number(searchParams?.page) || 1;
    //const totalPages = await getAccountsPages(query);
    const totalPages = await getAccountsPages(currentPage, 10, '');

    return <main>
        <div className="mt-4 flex items-center justify-between gap-2 md:mt-8">
        <Search placeholder="Search accounts..." />
        <CreateAccount />
      </div>
        <h1 className="text-3xl font-bold">Accounts</h1>
        <Suspense key={query + currentPage} fallback={<TableRowSkeleton />}>
            <AccountsTable query={query} currentPage={currentPage}/>
        </Suspense>
        <div className="mt-5 flex w-full justify-center">
        <Pagination totalPages={totalPages} />
      </div>
    </main>
}