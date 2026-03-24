import Link from "next/link";
import { auth } from "../lib/auth"
import OBLogo from "../ui/ob-logo";
import NavLinks from "./nav-links";
import LogoutButton from "../logout/logout-button";

export default async function NavBar(){
    const session = await auth();

    return(
<nav className="fixed top-0 left-0 w-full bg-white shadow-xs z-50">
  <div className="max-w-7xl mx-auto px-4 py-3 flex justify-between items-center">

<Link className="mb-2 flex h-20 items-end justify-start rounded-md p-4 md:h-40" href="/">
            <div className=" text-white md">
                <OBLogo />
            </div>
        </Link>

    

    <div className="space-x-16">
        <NavLinks/>
        <div className="hidden h-auto w-full grow rounded-md bg-gray-50 md:block">
                  {session ? (
                  <form>
                    <strong>Logged in as:</strong> {session.user?.name}<LogoutButton />
                  </form>
                ) : (
                  <a href="/login">Sign In</a>
                )}
                </div>
      {/* <a href="#" className="text-gray-600 hover:text-blue-500">Home</a>
      <a href="#" className="text-gray-600 hover:text-blue-500">About</a>
      <a href="#" className="text-gray-600 hover:text-blue-500">Contact</a> */}
    </div>
  </div>
</nav>
    );
}


