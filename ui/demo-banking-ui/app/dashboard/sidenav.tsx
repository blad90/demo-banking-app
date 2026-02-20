import Link from 'next/link';
import NavLinks from '@/app/dashboard/nav-links';
import OBLogo from '@/app/ui/ob-logo';
import LogoutButton from '../logout/logout-button';
import { auth } from '../lib/auth';


export default async function SideNav() {
  const session = await auth();
  return (
    <div className="flex h-full flex-col px-3 py-4 md:px-2">
      <Link
        className="mb-2 flex h-20 items-end justify-start rounded-md bg-[#006D77] p-4 md:h-40"
        href="/"
      >
        <div className="w-32 text-white md:w-40">
          <OBLogo />
        </div>
      </Link>
      <div className="flex grow flex-row justify-between space-x-2 md:flex-col md:space-x-0 md:space-y-2">
        <NavLinks />
        <div className="hidden h-auto w-full grow rounded-md bg-gray-50 md:block">
          {session ? (
          <form>
            <strong>Logged in as:</strong> {session.user?.name}<LogoutButton />
          </form>
        ) : (
          <a href="/login">Sign In</a>
        )}
        </div>
        
      </div>
    </div>
  );
}
