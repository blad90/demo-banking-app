'use client';

import {
  UserGroupIcon,
  HomeIcon,
  DocumentDuplicateIcon,
  CurrencyDollarIcon,
} from '@heroicons/react/24/outline';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import clsx from 'clsx';

// Map of links to display in the side navigation.
// Depending on the size of the application, this would be stored in a database.
const links = [
  { name: 'Home', href: '/dashboard', icon: HomeIcon },
  {
    name: 'Accounts',
    href: '/dashboard/accounts',
    icon: DocumentDuplicateIcon,
  },
  // { name: 'Customers', href: '/dashboard/customers', icon: UserGroupIcon },
  { name: 'Transactions', href: '/dashboard/transactions', icon: CurrencyDollarIcon },
];

export default function NavLinks() {
  const pathname = usePathname();
  return (
    <div>
    <nav className="flex flex-row items-center justify-start gap-4 p-4">
      {links.map((link) => {
        const LinkIcon = link.icon;
        return (
          <Link
            key={link.name}
            href={link.href}
            className={clsx(
              'flex flex-row items-center justify-start gap-4 p-4 text-sm font-medium hover:bg-sky-100 hover:text-[#006D77]',
              {
                'bg-sky-100 text-[#006D77]': pathname === link.href,
              },
            )}
          >
            <LinkIcon className="w-6" />
            <p className="hidden md:block">{link.name}</p>
          </Link>
        );
      })}
    </nav>
    </div>
  );
}
