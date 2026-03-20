import SideNav from '@/app/dashboard/sidenav';
import { SessionProvider } from 'next-auth/react';
import NavBar from './navbar';
import Footer from '../ui/footer';
 
export default function Layout({ children }: { children: React.ReactNode }) {
  return (
<div>
    <div className="flex min-h-screen flex-col">
      <div className="w-full flex-none">
        {/* <SideNav /> */}
        <NavBar/>
      </div>
      <SessionProvider>
        <div className="grow p-6 md:p-12">{children}</div>
      </SessionProvider>
    </div>
    <Footer/>
    </div>
  );
}