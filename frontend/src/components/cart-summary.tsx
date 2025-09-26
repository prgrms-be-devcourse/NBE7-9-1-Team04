// src/components/cart-summary.tsx
"use client";

import Link from "next/link";

type CartSummaryProps = {
  totalAmount: number;
  itemCount: number;
};

export default function CartSummary({ totalAmount, itemCount }: CartSummaryProps) {
  const shippingFee = totalAmount >= 50000 ? 0 : 3000;
  const finalAmount = totalAmount + shippingFee;

  return (
    <div className="bg-white border rounded-lg p-6 sticky top-24">
      <h2 className="text-lg font-semibold mb-4">주문 요약</h2>
      <div className="space-y-2 text-sm">
        <div className="flex justify-between">
          <span className="text-gray-600">상품 금액</span>
          <span>{totalAmount.toLocaleString()}원</span>
        </div>
        <div className="flex justify-between">
          <span className="text-gray-600">배송비</span>
          <span>{shippingFee === 0 ? "무료" : `${shippingFee.toLocaleString()}원`}</span>
        </div>
      </div>
      <div className="border-t my-4"></div>
      <div className="flex justify-between font-bold">
        <span>총 결제 금액</span>
        <span>{finalAmount.toLocaleString()}원</span>
      </div>

      {/* 2. Link 컴포넌트로 버튼을 감싸줍니다. */}
      <Link href="/payment" className={itemCount === 0 ? "pointer-events-none" : ""}>
        <button 
          className="w-full bg-black text-white rounded py-3 mt-6 hover:bg-gray-800 disabled:bg-gray-300"
          // 3. 장바구니에 상품이 없으면 버튼을 비활성화합니다.
          disabled={itemCount === 0}
        >
          결제하기 ({itemCount}개 상품)
        </button>
      </Link>
    </div>
  );
}