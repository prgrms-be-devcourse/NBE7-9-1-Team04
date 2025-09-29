// src/components/auth/AuthGuard.tsx
"use client";

import { useEffect, useRef } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";

interface AuthGuardProps {
  children: React.ReactNode;
  redirectTo?: string;
}

export default function AuthGuard({ 
  children, 
  redirectTo = "/user" 
}: AuthGuardProps) {
  const { user, isLoading } = useAuth();
  const router = useRouter();
  const hasRedirectedRef = useRef(false);

  useEffect(() => {
    // 로딩 중이거나 이미 리다이렉트했으면 스킵
    if (isLoading || hasRedirectedRef.current) {
      return;
    }

    // 인증되지 않은 경우에만 리다이렉트
    if (!user) {
      console.log('[AuthGuard] 미인증 사용자 - 리다이렉트:', redirectTo);
      hasRedirectedRef.current = true;
      router.push(redirectTo);
    }
  }, [user, isLoading, router, redirectTo]);

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto mb-4"></div>
          <p className="text-gray-600">인증 확인 중...</p>
        </div>
      </div>
    );
  }

  if (!user) {
    return null;
  }

  return <>{children}</>;
}