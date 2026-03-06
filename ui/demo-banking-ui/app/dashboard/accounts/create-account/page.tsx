import { getLoggedCustomer } from "@/app/services/user-service";
import CreateAccount from "@/app/ui/accounts/create-account";

export default async function CreateAccountPage() {
    const user = await getLoggedCustomer();
    return <CreateAccount user={user}/>
}