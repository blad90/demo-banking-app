export async function getCustomers(){
    const res = await fetch('http://localhost:8081/users/all', { cache: 'no-cache'});
    if(!res.ok){
        throw new Error('Failed to fetch customers');
    }
    return res.json();
}

export async function fetchUsersByIds(ids: number[]) {
  const res = await fetch(
    `http://localhost:8081/users?ids=${ids.join(',')}`,
    { cache: 'no-cache' }
  );
  if (!res.ok) throw new Error('Failed to fetch users');
  return res.json();
}