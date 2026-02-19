async function getCustomers(){
    const res = await fetch('http://localhost:8081/users/all', { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch customers');
    }
    return res.json();
}

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