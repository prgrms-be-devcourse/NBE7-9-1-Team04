"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";

interface AdminGuardProps {
  children: React.ReactNode;
  redirectTo?: string;
}

export default function AdminGuard({
  children,
  redirectTo = "/",
}: AdminGuardProps) {
  const { user, isLoading } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!isLoading) {
      if (!user) {
        router.push("/login");
      } else if (user.level !== 0) {
        router.push(redirectTo); // 권한 없는 경우 리다이렉트
      }
    }
  }, [user, isLoading, router, redirectTo]);

  if (isLoading) {
    return <div className="p-10">관리자 인증 확인 중...</div>;
  }

  if (!user || user.level !== 0) {
    return null;
  }

  return <>{children}</>;
}