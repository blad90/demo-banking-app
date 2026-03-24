'use client'

import { useRouter } from "next/navigation";
import { FormEvent, useState } from "react";

export default function Transfer({user} : any){
    const router = useRouter();

    const [sourceAccountNumber, setSourceAccountNumber] = useState('');
    const [destinationAccountNumber, setDestinationAccountNumber] = useState('');
    const [amount, setAmount] = useState(0.00);
    const [description, setDescription] = useState('');
    const [transactionDate, setTransactionDate] = useState(null);
    const [transactionType, setTransactionType] = useState('');
    const [transactionState, setTransactionState] = useState('');
    const [customerId, setCustomerId] = useState(user.id);

    const [message, setMessage] = useState('');
    const [status, setStatus] = useState('');
    const [sagaId, setSagaId] = useState('');

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setMessage('');

        try {
            const res = await fetch('/api/transactions', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ sourceAccountNumber, destinationAccountNumber, transactionType, amount, description, customerId })
            });

            const data = await res.json();

            if (!res.ok) throw new Error(data.error || 'Failed');
            setSagaId(data.sagaId);
            setStatus("STARTED");
            pollStatus(data.sagaId);
            setSourceAccountNumber('');
            setDestinationAccountNumber('');
            setAmount(0);
            setDescription('');
            setTransactionDate(null);
            setTransactionType('');
            setTransactionState('');
        } catch (err: any) {
            setMessage(err.message || 'There was an error');
        } finally {
        }
    }

    async function pollStatus(id: string) {
        const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));
        let delay = 2000;
        const maxDelay = 15000;
        const maxAttempts = 10;

        for (let attempt = 0; attempt < maxAttempts; attempt++) {
            try {
                const response = await fetch(
                    `http://localhost:8088/orchestrator/saga/${id}`
                );

                if (!response.ok) {
                    setStatus("FAILED");
                    setMessage("Transaction processing failed.");
                    return;
                }

                const data = await response.json();
                const sagaStatus = data.transactionSagaStatus;

                setStatus(sagaStatus);

                if (sagaStatus === "FAILED") {
                    setMessage(
                        "Transaction processing failed. Error while validating the account."
                    );
                    return;
                }

                if (sagaStatus === "COMPLETED") {
                    setMessage("Transaction processed successfully!");
                    setTimeout(() => {
                        router.push("/dashboard/transactions");
                    }, 2000);
                    return;
                }

                await sleep(delay);

                delay = Math.min(delay * 1.5, maxDelay);

            } catch (error) {
                setStatus("FAILED");
                setMessage("Unexpected error while checking transaction status.");
                return;
            }
        }
        setStatus("FAILED");
        setMessage("Transaction timed out. Please check later.");
    }

    return (
        <div className="pt-32 space-y-12">
            <div className="border-b border-gray-900/10 pb-12">
                <form onSubmit={handleSubmit}>
                    <div className="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
                        <div className="sm:col-span-4">

                            <div className="mt-2">
                                <label htmlFor="sourceAccountInput" className="block text-sm/6 font-medium text-gray-900">Source Account</label>
                                <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
                                    <input
                                        id="sourceAccountInput"
                                        className="block min-w-0 grow bg-white py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-300 focus:outline-hidden sm:text-sm/6"
                                        type="text"
                                        value={sourceAccountNumber}
                                        onChange={e => setSourceAccountNumber(e.target.value)}
                                        placeholder="e.g. OB-XXXXXXX"
                                        required
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
                        <div className="sm:col-span-4">
                            <div className="mt-2">
                                <label htmlFor="destinationAccountInput" className="block text-sm/6 font-medium text-gray-900">Destination Account</label>
                                <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
                                    <input
                                        id="destinationAccountInput"
                                        className="block min-w-0 grow bg-white py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-300 focus:outline-hidden sm:text-sm/6"
                                        type="text"
                                        value={destinationAccountNumber}
                                        onChange={e => setDestinationAccountNumber(e.target.value)}
                                        placeholder="e.g. OB-XXXXXXX"
                                        required
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
                        <div className="sm:col-span-4">
                            <div className="mt-2">
                                <label htmlFor="transactionAmountInput" className="block text-sm/6 font-medium text-gray-900">Transaction Amount</label>
                                <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
                                    <input
                                        id="transactionAmountInput"
                                        className="block min-w-0 grow bg-white py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-300 focus:outline-hidden sm:text-sm/6"
                                        type="number"
                                        value={amount}
                                        onChange={e => setAmount(Number(e.target.value))}
                                        required
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
                        <div className="sm:col-span-4">
                            <div className="mt-2">
                                <label htmlFor="transactionDescriptionInput" className="block text-sm/6 font-medium text-gray-900">Transaction Description</label>
                                <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
                                    <input
                                        id="transactionDescriptionInput"
                                        className="block min-w-0 grow bg-white py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-300 focus:outline-hidden sm:text-sm/6"
                                        type="text"
                                        value={description}
                                        onChange={e => setDescription(e.target.value)}
                                        placeholder="Some short description"
                                        required
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
                        <div className="sm:col-span-4">
                            <div className="mt-2">
                                <label htmlFor="transactionTypeInput" className="block text-sm/6 font-medium text-gray-900">Transaction Type</label>
                                <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
                                    <select
                                        id="transactionTypeInput"
                                        value={transactionType}
                                        onChange={(e) => setTransactionType(e.target.value)}
                                        className="block w-full rounded-md bg-white py-2 px-3 text-sm text-gray-900 outline-solid outline-1 outline-gray-300 focus:outline-2 focus:outline-indigo-600">
                                        <option>--</option>
                                        <option value="TRANSFER">TRANSFER</option>
                                        <option value="OTHER">OTHER</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="mt-5">
                        <button
  type="submit"
  disabled={
    status === "STARTED" || status === "PROCESSING"
  }
  className="bg-[#006D77] hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-sm"
>
  {status === "STARTED" || status === "PROCESSING"
    ? "Processing..."
    : "Transfer"}
</button>
                    </div>
                </form>
                {message && (
                    <p
                        className={`mt-4 font-medium ${status === "FAILED"
                                ? "text-red-600"
                                : status === "COMPLETED"
                                    ? "text-green-600"
                                    : "text-gray-600"
                            }`}
                    >
                        {message}
                    </p>
                )}
            </div>
        </div>
    );
}