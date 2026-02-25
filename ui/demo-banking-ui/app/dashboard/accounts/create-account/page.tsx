'use client';

import { useRouter } from "next/navigation";
import { useState, FormEvent } from "react";



export default function CreateAccountPage(){
    const router = useRouter();
    
    const [userId, setUserId] = useState(0);
    const [accountType, setAccountType] = useState('');
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');
    const [status, setStatus] = useState('');
    const [sagaId, setSagaId] = useState('');

    async function handleSubmit(e: FormEvent){
        e.preventDefault();
        setLoading(true);
        setMessage('');

        try{
            const res = await fetch('/api/accounts', {
                method: 'POST',
                headers: { 'Content-Type' : 'application/json' },
                body: JSON.stringify({ userId, accountType})
            });

            const data = await res.json();
            
            setSagaId(data.sagaId);
            setStatus("STARTED");

            pollStatus(data.sagaId);

            if(!res.ok) throw new Error(data.error || 'Failed');
            setUserId(0);
            setAccountType(''); 
        } catch(err: any){
            setMessage(err.message || 'There was an error');
        } finally {
            setLoading(false);
        }
    }

    async function pollStatus(id: string) {

    try {
    const response = await fetch(
      `http://localhost:8087/orchestrator/saga/${id}`
    );

    if (response.status === 202) {
      setStatus("PROCESSING");
      setTimeout(() => pollStatus(id), 2000);
      return;
    }

    if (response.status === 200) {
      setStatus("ACCOUNT_CREATED");
      setMessage("Account created successfully!");
      setTimeout(() => {
      router.push("/dashboard/accounts");
    }, 2000);
      return;
    }

    if (response.status === 404) {
      setStatus("FAILED");
      setMessage("Account creation failed. Error while validating the user.");
      return;
    }

  } catch (error) {
    setStatus("FAILED");
    setMessage("Error while validating the user.");
  }
  }
    return(
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
                                    onChange={e => setUserId(Number(e.target.value))}
                                    required
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
                    <button type="submit" disabled={loading} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                        {loading ? 'Processing...': 'Create Account'}
                    </button>
                    </div>
                </form>
                {message && <p style={{color: message.includes('Error') ? 'red' : 'green'}}> {message} </p>}
                </div>
            </div>
        );
 

}