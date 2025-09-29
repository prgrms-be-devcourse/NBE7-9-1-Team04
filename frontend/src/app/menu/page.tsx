"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetchApi } from "@/lib/client";
import { useAuth } from "@/context/AuthContext";
import { Menu } from "@/types/menu";
import { MenuCard } from "@/components/MenuCard";
import AuthGuard from "@/components/auth/AuthGuard";
import { useToast } from "@/context/ToastContext";

export default function MenuPage() {
  return (
    <AuthGuard>
      <MenuPageContent />
    </AuthGuard>
  );
}

function MenuPageContent() {
  const [menus, setMenus] = useState<Menu[]>([]);
  const { refetch } = useAuth();
  const { showToast } = useToast();
  const router = useRouter();

  useEffect(() => {
    fetchApi("/api/menus")
      .then((data) => {
        if (Array.isArray(data?.data)) setMenus(data.data);
        else setMenus([]);
      })
      .catch(() => setMenus([]));
  }, []);

  const handleAddToCart = async (menuId: number) => {
    try {
      await fetchApi("/api/carts/items", {
        method: "POST",
        body: JSON.stringify({ menuId, quantity: 1 }),
      });
      await refetch();

      // 전역 토스트 호출
      showToast({
        message: "장바구니에 담겼습니다 🛒",
        actionLabel: "장바구니로 이동",
        onAction: () => router.push("/cart"),
      });
    } catch {
      showToast({ message: "장바구니 담기 실패 😢" });
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-6 bg-white">
      <h1 className="text-black text-4xl font-bold mb-12">☕ 원두 메뉴</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 max-w-7xl w-full">
        {menus.map((menu) => (
          <MenuCard
            key={menu.menuId}
            menu={menu}
            onClick={() => handleAddToCart(menu.menuId!)}
            buttonLabel="장바구니 담기"
          />
        ))}
      </div>
    </div>
  );
}