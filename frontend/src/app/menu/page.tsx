"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetchApi } from "@/lib/client";
import { useAuth } from "@/context/AuthContext";
import { Menu } from "@/types/menu";

import { MenuCard } from "@/components/MenuCard";
import AuthGuard from "@/components/auth/AuthGuard";

export default function MenuPage() {
  return (
    <AuthGuard>
      <MenuPageContent />
    </AuthGuard>
  );
}

function MenuPageContent() {
  const [menus, setMenus] = useState<Menu[]>([]);
  const [showToast, setShowToast] = useState(false);
  const router = useRouter();
  const { refetch } = useAuth();

  // 메뉴 불러오기
  useEffect(() => {
    fetchApi("/api/menus")
      .then((data) => {
        if (Array.isArray(data?.data)) setMenus(data.data);
        else setMenus([]);
      })
      .catch((err) => {
        console.error("메뉴 불러오기 실패:", err);
        setMenus([]);
      });
  }, []);

  // 장바구니 담기
  const handleAddToCart = async (menuId: number) => {
    try {
      await fetchApi("/api/carts/items", {
        method: "POST",
        body: JSON.stringify({ menuId, quantity: 1 }),
      });
      await refetch(); // 장바구니 카운트 갱신

      // 토스트 보이기
      setShowToast(true);
      setTimeout(() => setShowToast(false), 3000);
    } catch (err) {
      console.error("장바구니 추가 실패:", err);
      alert("장바구니 담기 실패 😢");
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-6 bg-white">
      {/* 제목 */}
      <h1 className="text-black text-4xl font-bold mb-12">☕ 원두 메뉴</h1>

      {/* 카드 리스트 */}
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

      {/* 토스트 */}
      {showToast && (
        <div className="fixed bottom-4 inset-x-0 flex justify-center">
          <div className="bg-black text-white px-4 py-3 rounded-lg shadow-lg flex items-center gap-4">
            <span>장바구니에 담겼습니다 🛒</span>
            <button
              onClick={() => router.push("/cart")}
              className="bg-white text-black px-3 py-1 rounded text-sm"
            >
              장바구니로 이동
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
