import { auth } from "../lib/auth";

const session = await auth();

export async function getCustomers(){
    const res = await fetch(`${process.env.USER_SERVICE_API_URL}/all`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch customers');
    }
    return res.json();
}

export async function getCustomerById(id: number){
    const res = await fetch(`${process.env.USER_SERVICE_API_URL}/${id}`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error(`Failed to fetch customer with ID: ${id}`);
    }
    return res.json();
}

export async function getCustomerByUserSessionId(id: string){
    const res = await fetch(`${process.env.USER_SERVICE_API_URL}/userSessionId/${id}`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error(`Failed to fetch customer with session ID: ${id}`);
    }
    return res.json();
}

export async function getLoggedCustomer(){
    const userSessionId = session?.userId;
    const res = await fetch(`${process.env.USER_SERVICE_API_URL}/userSessionId/${userSessionId}`, { cache: 'no-cache'});
    if(!res.ok){
        throw new Error(`Failed to fetch customer with session ID: ${userSessionId}`);
    }
    return res.json();
}

export async function fetchUsersByIds(ids: number[]) {
  const res = await fetch(
    `${process.env.USER_SERVICE_API_URL}?ids=${ids.join(',')}`,
    { cache: 'no-cache' }
  );
  if (!res.ok) throw new Error('Failed to fetch users');
  return res.json();
}