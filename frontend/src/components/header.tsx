// // src/components/header.tsx
// "use client";

// import Link from "next/link";

// export function Header() {
//   const cartItemCount = 2; // 예시로 2개 표시

//   return (
//     <header className="flex items-center justify-between h-16 px-4 border-b bg-background text-foreground">
//       <Link href="/" className="flex items-center gap-2 text-lg font-semibold">
//         <span className="sr-only">Home</span>
//         카페 원두
//       </Link>
//       <nav className="hidden md:flex items-center gap-4 text-sm font-medium">
//         <Link href="/" className="hover:text-primary">홈</Link>
//         <Link href="/orders" className="hover:text-primary">주문내역</Link>
//       </nav>
//       <div className="flex items-center gap-4">
//         <span className="text-sm">사용자님</span>
//         <Link href="/login" className="text-sm hover:text-primary">로그아웃</Link>
//         <Link href="/cart" className="relative">
//           <span className="text-2xl">🛒</span> {/* 장바구니 이모지 사용 */}
//           {cartItemCount > 0 && (
//             <span className="absolute -top-2 -right-2 flex h-5 w-5 items-center justify-center rounded-full bg-red-500 text-xs font-bold text-white">
//               {cartItemCount}
//             </span>
//           )}
//         </Link>
//       </div>
//     </header>
//   );
// }