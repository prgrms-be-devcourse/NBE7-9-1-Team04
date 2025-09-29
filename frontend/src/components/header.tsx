// src/components/header.tsx
"use client";

import { useAuth } from "@/context/AuthContext";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { fetchApi } from "@/lib/client";
import { useEffect, useState } from 'react';

export function Header() {
  const { user, cartCount, isLoading, refetch } = useAuth();
  const router = useRouter();
  

  const handleLogout = async () => {
    try {
      await fetchApi("/api/users/logout", { method: "GET" });
      alert("로그아웃 되었습니다.");
      await refetch();
      router.push("/user");
    } catch (error) {
      console.error("로그아웃 실패:", error);
      alert("로그아웃에 실패했습니다.");
    }
  };

  return (
    <header className="flex items-center justify-between h-16 px-4 border-b bg-background text-foreground">
      {/* 로고는 왼쪽에 고정 */}
      <Link href="/menu" className="flex items-center gap-2 text-lg font-semibold">
        ☕ Grids & Circles
      </Link>
      {/* 오른쪽 영역: 네비게이션 + 사용자 정보 + 장바구니 */}
      <div className="flex items-center gap-6">
        {/* 네비게이션 메뉴 */}
        <nav className="hidden md:flex items-center gap-4 text-sm font-medium">
          {user && (
            <Link
              href={user.level === 0 ? "/orders/admin" : "/orders"}
              className="hover:text-primary"
            >
              주문내역
            </Link>
          )}
          {user?.level === 0 && (
            <Link href="/admin" className="hover:text-primary">
              관리자 대시보드
            </Link>
          )}
        </nav>

        {/* 구분선 추가 */}
        {user && <div className="hidden md:block h-4 w-px bg-gray-300" />}

        {/* 사용자 정보 영역 */}
        <div className="flex items-center gap-4">
          {isLoading ? (
            <div className="h-5 w-24 bg-gray-200 rounded animate-pulse" />
          ) : user ? (
            <>
              <Link href="/user/my" className="text-sm hover:text-primary">
                {user?.userEmail?.split('@')[0] || '사용자'}님
              </Link>
              <button onClick={handleLogout} className="text-sm hover:text-primary">
                로그아웃
              </button>
            </>
          ) : (
            <Link href="/user" className="text-sm hover:text-primary">로그인</Link>
          )}

          {/* 장바구니 */}
          <Link href="/cart" className="relative">
            <span className="text-2xl">🛒</span>
            {user && cartCount > 0 && (
              <span className="absolute -top-2 -right-2 flex h-5 w-5 items-center justify-center rounded-full bg-red-500 text-xs font-bold text-white">
                {cartCount}
              </span>
            )}
          </Link>
        </div>
      </div>
    </header>
  );
}