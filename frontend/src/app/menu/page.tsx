"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetchApi } from "@/lib/client";

interface Menu {
  menuId: number;
  name: string;
  price: number;
  isSoldOut: boolean;
  description: string;
  imageUrl: string;
}

export default function Home() {
  const [menus, setMenus] = useState<Menu[]>([]);
  const [showToast, setShowToast] = useState(false);
  const router = useRouter();

  useEffect(() => {
    fetchApi("/api/menus")
      .then((data) => {
        if (data?.data) setMenus(data.data);
      })
      .catch((err) => console.error("메뉴 불러오기 실패:", err));
  }, []);

  const handleAddToCart = async (menuId: number) => {
    try {
      await fetchApi("/api/carts/items", {
        method: "POST",
        body: JSON.stringify({ menuId, quantity: 1 }),
      });

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
          <div
            key={menu.menuId}
            className="rounded-lg border shadow-sm overflow-hidden flex flex-col"
          >
            {/* 이미지 */}
            <div className="relative">
              <img
                src={menu.imageUrl}
                alt={menu.name}
                className={`w-full h-56 object-cover ${
                  menu.isSoldOut ? "opacity-50" : ""
                }`}
              />
              {menu.isSoldOut && (
                <span className="absolute top-2 right-2 bg-red-500 text-white text-xs px-2 py-1 rounded">
                  품절
                </span>
              )}
            </div>

            {/* 내용 */}
            <div className="p-4 flex flex-col gap-2 flex-grow">
              <h3 className="text-black font-semibold">{menu.name}</h3>
              <p className="text-sm text-gray-400 truncate" title={menu.description}>
                {menu.description}
              </p>
              <p className="text-black font-bold mt-auto">
                {menu.price.toLocaleString()}원
              </p>
            </div>

            {/* 버튼 */}
            <button
              disabled={menu.isSoldOut}
              onClick={() => handleAddToCart(menu.menuId)}
              className={`w-full py-3 font-medium ${
                menu.isSoldOut
                  ? "bg-gray-300 text-gray-600 cursor-not-allowed"
                  : "bg-black text-white hover:bg-gray-800"
              }`}
            >
              {menu.isSoldOut ? "품절" : "장바구니 담기"}
            </button>
          </div>
        ))}
      </div>

      {/* 토스트 (하단 고정) */}
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
