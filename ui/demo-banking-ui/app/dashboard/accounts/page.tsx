

async function getAccounts(){
    const res = await fetch('http://localhost:8082/accounts/all', { cache: 'force-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch accounts');
    }
    return res.json();
}
export default async function Page(){
    const accounts = await getAccounts();

    return <main>
        <h1>Accounts</h1>
        <ul>
            {accounts.map((account: any) => (
                <li key={account.id}>{account.accountNumber}</li>
            ))}
        </ul>
    </main>
}