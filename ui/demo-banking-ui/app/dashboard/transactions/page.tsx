import { auth } from "@/app/lib/auth"
import { redirect } from "next/navigation"


  async function getTransactions() {
    const session = await auth()

  if (!session) {
    redirect("/login")
  }
    const res = await fetch(
      "http://localhost:8083/transactions/all",
      {
        cache: "no-store",
        headers: {
          Authorization: `Bearer ${session?.accessToken}`,
        },
      }
    )

    if (!res.ok) {
      throw new Error("Failed to fetch transactions")
    }

    return res.json()
  }
  
export default async function Page(){
    const transactions = await getTransactions();

    return <main>
        <h1 className="text-3xl font-bold">Transactions</h1>
        {/* <ul>
            {transactions.map((transaction: any) => (
                <li key={transaction.id}>{transaction.id}</li>
            ))}
        </ul> */}
        <div className="overflow-x-auto shadow-md sm:rounded-lg">
      <table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-100">
          <tr>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Source Account
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Destination Account
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Description
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Transaction Amount
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Transaction Date
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Transaction Type
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Transaction State
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {transactions.map((transaction: any) => (
            <tr key={transaction.id} className="hover:bg-gray-50">
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm font-medium text-gray-900">{transaction.sourceAccount}</div>
                <div className="text-sm text-gray-500">{transaction.sourceAccount}</div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm text-gray-900">{transaction.destinationAccount}</div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm text-gray-900">{transaction.description}</div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm text-gray-900">$ {transaction.transactionAmount}</div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm text-gray-900">{transaction.transactionDate}</div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm text-gray-900">{transaction.type}</div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm text-gray-900">{transaction.transactionState}</div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
    </main>
}