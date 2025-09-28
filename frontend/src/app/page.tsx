"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetchApi, ApiResponse } from "@/lib/client";

type User = {
  userId: number;
  userEmail: string;
  phoneNumber: string;
  createDate: string;
  modifyDate: string;
  level: number; // 0 = 관리자, 1 = 일반 사용자
  apiKey: string;
};

export default function Home() {
  const router = useRouter();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const res = await fetchApi<User>("/api/users/my", { method: "GET" });
        const user = res.data;

        if (user.level === 0) {
          router.replace("/admin");
        } else if (user.level === 1) {
          router.replace("/menu");
        } else {
          router.replace("/user");
        }
      } catch (error) {
        console.error("사용자 정보 불러오기 실패:", error);
        router.replace("/user");
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, [router]);

  if (loading) return <div>로딩 중...</div>;

  return null;
}
