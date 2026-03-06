
import { getLoggedCustomer } from "@/app/services/user-service";
import Transfer from "@/app/ui/transactions/transfer";

export default async function TransferPage() {
    const user = await getLoggedCustomer();
    return <Transfer user={user}/>
}