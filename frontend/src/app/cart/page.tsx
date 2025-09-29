// src/app/cart/page.tsx
"use client";

import { useEffect, useState } from "react";
import { fetchApi } from "@/lib/client";
import { CartItemType, CartListType } from "@/types/cart";
import CartItem from "@/components/cart-item";
import CartSummary from "@/components/cart-summary";
import AuthGuard from "@/components/auth/AuthGuard";

// ✅ 변경점: AuthGuard 사용하여 로그인 상태 처리
export default function CartPage() {
  return (
    <AuthGuard>
      <CartPageContent />
    </AuthGuard>
  );
}

function CartPageContent() {
  const [cart, setCart] = useState<CartListType | null>(null);
  const [loading, setLoading] = useState(true);

  const loadCart = async () => {
    try {
      setLoading(true);
      const res = await fetchApi("/api/carts", { method: "GET" });
      setCart(res.data);
    } catch (error) {
      console.error("장바구니 조회 실패:", error);
      // ✅ 변경점: 에러 발생 시 빈 장바구니 상태로 설정하여 레이아웃 유지
      setCart({ cartItems: [], grandTotal: 0 });
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateQuantity = async (menuId: number, quantity: number) => {
    if (quantity <= 0) {
      if (confirm("상품을 장바구니에서 삭제하시겠습니까?")) {
        handleDeleteItem(menuId);
      }
      return;
    }
    try {
      await fetchApi(`/api/carts/items/${menuId}`, {
        method: "PUT",
        body: JSON.stringify({ quantity }),
      });
      loadCart();
    } catch (error) {
      console.error("수량 변경 실패:", error);
      alert("수량 변경에 실패했습니다.");
    }
  };

  const handleDeleteItem = async (menuId: number) => {
    try {
      await fetchApi(`/api/carts/items/${menuId}`, {
        method: "DELETE",
      });
      alert("상품이 삭제되었습니다.");
      loadCart();
    } catch (error) {
      console.error("상품 삭제 실패:", error);
      alert("상품 삭제에 실패했습니다.");
    }
  };

  const handleClearCart = async () => {
    if (confirm("장바구니를 비우시겠습니까?")) {
      try {
        await fetchApi("/api/carts", { method: "DELETE" });
        alert("장바구니를 비웠습니다.");
        setCart({ cartItems: [], grandTotal: 0 });
      } catch (error) {
        console.error("장바구니 비우기 실패:", error);
        alert("장바구니를 비우는 데 실패했습니다.");
      }
    }
  };

  useEffect(() => {
    loadCart();
  }, []);

  if (loading) {
    return <div className="p-10">장바구니를 불러오는 중...</div>;
  }

  // ✅ 변경점: 빈 장바구니일 때 다른 화면을 리턴하던 코드를 삭제하고,
  // isCartEmpty 변수를 만들어 아래 JSX에서 활용합니다.
  const isCartEmpty = !cart || cart.cartItems.length === 0;

  return (
    <div className="max-w-4xl mx-auto p-4 sm:p-6 lg:p-8">
      <h1 className="text-2xl font-bold mb-6">장바구니</h1>
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* 장바구니 상품 목록 */}
        <div className="lg:col-span-2">
          <div className="flex justify-end mb-4">
            <button
              onClick={handleClearCart}
              // ✅ 변경점: 장바구니가 비어있으면 버튼 비활성화
              disabled={isCartEmpty}
              className="text-sm text-gray-500 hover:text-gray-800 disabled:text-gray-300 disabled:cursor-not-allowed"
            >
              전체 삭제
            </button>
          </div>
          <div className="space-y-4">
            {/* ✅ 변경점: isCartEmpty 값에 따라 상품 목록 또는 메시지를 보여줌 */}
            {isCartEmpty ? (
              <div className="bg-white border rounded-lg p-10 text-center text-gray-500">
                장바구니에 담긴 상품이 없습니다.
              </div>
            ) : (
              cart.cartItems.map((item) => (
                <CartItem
                  key={item.cartId}
                  item={item}
                  onUpdateQuantity={handleUpdateQuantity}
                  onDeleteItem={handleDeleteItem}
                />
              ))
            )}
          </div>
        </div>

        {/* 주문 요약 */}
        <div className="lg:col-span-1">
          <CartSummary
            // ✅ 변경점: 장바구니가 비어있으면 0을 전달
            totalAmount={isCartEmpty ? 0 : cart.grandTotal}
            itemCount={isCartEmpty ? 0 : cart.cartItems.length}
          />
        </div>
      </div>
    </div>
  );
}