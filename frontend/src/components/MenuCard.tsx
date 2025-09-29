// src/components/MenuCard.tsx
"use client";
import { Menu } from "@/types/menu";

export function MenuCard({
  menu,
  onClick,
  disabled,
  buttonLabel,
}: {
  menu: Menu;
  onClick?: () => void;
  disabled?: boolean;
  buttonLabel?: string;
}) {
  return (
    <div className="rounded-lg border shadow-sm overflow-hidden flex flex-col">
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
      {onClick && (
        <button
          disabled={disabled}
          onClick={onClick}
          className={`w-full py-3 font-medium ${
            disabled
              ? "bg-gray-300 text-gray-600 cursor-not-allowed"
              : "bg-black text-white hover:bg-gray-800"
          }`}
        >
          {buttonLabel || "확인"}
        </button>
      )}
    </div>
  );
}
