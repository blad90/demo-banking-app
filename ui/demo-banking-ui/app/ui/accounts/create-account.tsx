'use client'

import { useRouter } from "next/navigation";
import { FormEvent, useState } from "react";

export default function CreateAccount({user} : any){

    const [userId] = useState(user.id);
    const [accountType, setAccountType] = useState('');
    const [message, setMessage] = useState('');
    const [status, setStatus] = useState('');
    const [sagaId, setSagaId] = useState('');

    const router = useRouter();

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setMessage('');
        try {
            const res = await fetch('/api/accounts', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ userId, accountType })
            });

            const data = await res.json();

            if (!res.ok) throw new Error(data.error || 'Failed');
            setSagaId(data.sagaId);
            setStatus("STARTED");
            pollStatus(data.sagaId);
            setAccountType('');
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
                    `http://localhost:8087/orchestrator/saga/${id}`
                );

                if (!response.ok) {
                    setStatus("FAILED");
                    setMessage("Account creation failed.");
                    return;
                }

                const data = await response.json();
                const sagaStatus = data.accountSagaStatus;

                setStatus(sagaStatus);

                if (sagaStatus === "FAILED") {
                    setMessage(
                        "Account creation failed. Error while validating the user."
                    );
                    return;
                }

                if (sagaStatus === "COMPLETED") {
                    setMessage("Account creation processed successfully!");
                    setTimeout(() => {
                        router.push("/dashboard/accounts");
                    }, 2000);
                    return;
                }

                await sleep(delay);

                delay = Math.min(delay * 1.5, maxDelay);

            } catch (error) {
                setStatus("FAILED");
                setMessage("Unexpected error while checking account status.");
                return;
            }
        }
        setStatus("FAILED");
        setMessage("Account creation timed out. Please check later.");
    }

    return (
        <div className="space-y-12">
            <div className="border-b border-gray-900/10 pb-12">
                <form onSubmit={handleSubmit}>
                    <div className="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
                        <div className="sm:col-span-4">
                            <label className="block text-sm/6 font-medium text-gray-900">User ID</label>
                            <div className="mt-2">
                                <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
                                    <input
                                        className="block min-w-0 grow bg-white py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-300 focus:outline-none sm:text-sm/6"
                                        type="number"
                                        value={userId}
                                        disabled
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">
                        <div className="sm:col-span-4">
                            <label className="block text-sm/6 font-medium text-gray-900">Account Type</label>
                            <div className="mt-2">
                                <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
                                    <input
                                        className="block min-w-0 grow bg-white py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-300 focus:outline-none sm:text-sm/6"
                                        type="text"
                                        value={accountType}
                                        onChange={e => setAccountType(e.target.value)}
                                        required
                                    />
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
                            className="bg-[#006D77] hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                        >
                            {status === "STARTED" || status === "PROCESSING"
                                ? "Processing..."
                                : "Create Account"}
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