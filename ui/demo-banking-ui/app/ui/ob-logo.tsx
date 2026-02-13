import { GlobeAltIcon } from '@heroicons/react/24/outline';
import { lusitana } from '@/app/ui/fonts';
import Image from 'next/image';

export default function OBLogo() {
  return (
    <div
      className={`${lusitana.className} flex flex-row items-center leading-none text-white`}
    >
      {/* <GlobeAltIcon className="h-12 w-12 rotate-[15deg]" /> */}
      {/* <Image
                  src="/OB.png"
                  width={120}
                  height={100}
                  className='hidden md:block'
                  alt='OB DemoBank project...'/> */}
      <p className="text-[44px]">OB DemoBank</p>
    </div>
  );
}
