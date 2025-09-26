// src/components/cart-item.tsx
"use client";

import { CartItemType } from "@/types/cart";
import Image from "next/image";

type CartItemProps = {
  item: CartItemType;
  onUpdateQuantity: (menuId: number, quantity: number) => void;
  onDeleteItem: (menuId: number) => void;
};

export default function CartItem({
  item,
  onUpdateQuantity,
  onDeleteItem,
}: CartItemProps) {
  return (
    <div className="bg-white border rounded-lg p-4 flex items-center space-x-4">
      {/* 상품 이미지 */}
      <Image
        src={item.imageUrl || "/placeholder.png"}
        alt={item.name}
        width={80}
        height={80}
        className="rounded"
      />

      {/* 상품 정보 */}
      <div className="flex-grow">
        <p className="font-semibold">{item.name}</p>
        <p className="text-sm text-gray-500">{item.price.toLocaleString()}원</p>
        {/* 태그 (예시) */}
        <div className="flex gap-2 mt-1">
          <span className="text-xs px-2 py-0.5 bg-gray-100 rounded">콜롬비아</span>
          <span className="text-xs px-2 py-0.5 bg-gray-100 rounded">미디엄 로스트</span>
        </div>
      </div>

      {/* 수량 조절 및 삭제 */}
      <div className="flex flex-col items-end">
        <button onClick={() => onDeleteItem(item.menuId)} className="text-gray-400 hover:text-red-500 text-lg mb-2">
          🗑️
        </button>
        <div className="flex items-center border rounded">
          <button
            onClick={() => onUpdateQuantity(item.menuId, item.quantity - 1)}
            className="px-3 py-1 text-lg"
          >
            -
          </button>
          <span className="px-4 py-1 text-center w-12">{item.quantity}</span>
          <button
            onClick={() => onUpdateQuantity(item.menuId, item.quantity + 1)}
            className="px-3 py-1 text-lg"
          >
            +
          </button>
        </div>
        <p className="font-bold mt-2 text-right w-full">
          {item.orderAmount.toLocaleString()}원
        </p>
      </div>
    </div>
  );
}