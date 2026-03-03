import { getCustomers } from "@/app/services/user-service";

export default async function Page(){
    const customers = await getCustomers();

    return <main>
        <h1>Customers</h1>
        <ul>
            {customers.map((customer: any) => (
                <li key={customer.id}>{customer.firstName} - {customer.lastName} - {customer.alias} -  {customer.nationalId}</li>
            ))}
        </ul>
    </main>
}