'use client';

import { useState, FormEvent } from "react";

export default function TransferPage(){
    const [sourceAccountNumber, setSourceAccountNumber] = useState('');
    const [destinationAccountNumber, setDestinationAccountNumber] = useState('');
    const [amount, setAmount] = useState(0.00);
    const [description, setDescription] = useState('');
    const [transactionDate, setTransactionDate] = useState(null);
    const [transactionType, setTransactionType] = useState('');
    const [transactionState, setTransactionState] = useState('');
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');

    async function handleSubmit(e: FormEvent){
        e.preventDefault();
        setLoading(true);
        setMessage('');

        try{
            const res = await fetch('/api/transactions', {
                method: 'POST',
                headers: { 'Content-Type' : 'application/json' },
                body: JSON.stringify({ sourceAccountNumber, destinationAccountNumber, transactionType, amount, description})
            });

            const data = await res.json();

            if(!res.ok) throw new Error(data.error || 'Failed');
            setMessage('Transaction processed successfully!');
            setSourceAccountNumber('');
            setDestinationAccountNumber('');
            setAmount(0);
            setDescription('');
            setTransactionDate(null);
            setTransactionType('');
            setTransactionState('');   
        } catch(err: any){
            setMessage(err.message || 'There was an error');
        } finally {
            setLoading(false);
        }
    }
    return(
            <div className="space-y-12">
                <div className="border-b border-gray-900/10 pb-12">
                <form onSubmit={handleSubmit}>
                    <div className="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
                        <div className="sm:col-span-4">
                            <label className="block text-sm/6 font-medium text-gray-900">Source Account</label>
                            <div className="mt-2">
                                <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
                                    <input
                                    className="block min-w-0 grow bg-white py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-300 focus:outline-none sm:text-sm/6" 
                                    type="text"
                                    value={sourceAccountNumber}
                                    onChange={e => setSourceAccountNumber(e.target.value)}
                                    required
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
                        <div className="sm:col-span-4">
                            <label className="block text-sm/6 font-medium text-gray-900">Destination Account</label>
                            <div className="mt-2">
                                <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
                                    <input
                                    className="block min-w-0 grow bg-white py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-300 focus:outline-none sm:text-sm/6" 
                                    type="text"
                                    value={destinationAccountNumber}
                                    onChange={e => setDestinationAccountNumber(e.target.value)}
                                    required
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
                        <div className="sm:col-span-4">
                            <label className="block text-sm/6 font-medium text-gray-900">Transaction Amount</label>
                            <div className="mt-2">
                                <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
                                    <input
                                    className="block min-w-0 grow bg-white py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-300 focus:outline-none sm:text-sm/6" 
                                    type="text"
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
                            <label className="block text-sm/6 font-medium text-gray-900">Transaction Description</label>
                            <div className="mt-2">
                                <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
                                    <input
                                    className="block min-w-0 grow bg-white py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-300 focus:outline-none sm:text-sm/6" 
                                    type="text"
                                    value={description}
                                    onChange={e => setDescription(e.target.value)}
                                    required
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
                        <div className="sm:col-span-4">
                            <label className="block text-sm/6 font-medium text-gray-900">Transaction Type</label>
                            <div className="mt-2">
                                <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
                                    <input
                                    className="block min-w-0 grow bg-white py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-300 focus:outline-none sm:text-sm/6" 
                                    type="text"
                                    value={transactionType}
                                    onChange={e => setTransactionType(e.target.value)}
                                    required
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="mt-5">
                    <button type="submit" disabled={loading} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                        {loading ? 'Processing...': 'Transfer'}
                    </button>
                    </div>
                </form>
                {message && <p style={{color: message.includes('error') ? 'red' : 'green'}}> {message} </p>}
                </div>
            </div>
        );
 

}