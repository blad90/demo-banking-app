import AccountsTable from "@/app/ui/accounts/accounts-table";
import TableRowSkeleton from "@/app/ui/skeletons";
import { Suspense } from "react";
import Search from "@/app/ui/search";
import { CreateAccountButton } from "@/app/ui/accounts/buttons";

export default async function Page(props: {
  searchParams?: Promise<{
    query?: string;
    page?: string;
  }>;
}){
    const searchParams = await props.searchParams;
    const query = searchParams?.query || '';
    const currentPage = Number(searchParams?.page) || 1;

    return <main className="pt-32">
        <div className="mt-4 flex items-center justify-between gap-2 md:mt-8">
        <Search placeholder="Search accounts..." />
        <CreateAccountButton />
      </div>
        <h1 className="text-3xl font-bold">Accounts</h1>
        <Suspense key={query + currentPage} fallback={<TableRowSkeleton />}>
            <AccountsTable query={query} currentPage={currentPage}/>
        </Suspense>
    </main>
}