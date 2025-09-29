"use client";

import { useState } from "react";
import { Menu } from "@/types/menu";

import ToggleSwitch from "@/components/ui/ToggleSwitch";

export default function CreateMenuForm({
  onCancel,
  onSave,
}: {
  onCancel: () => void;
  onSave: (menu: Menu) => void;
}) {
  const [form, setForm] = useState<Menu>({
    name: "",
    price: 0,
    isSoldOut: false,
    description: "",
    imageUrl: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: name === "price" ? +value : value,
    }));
  };

  return (
    <div className="border rounded-lg shadow-md p-6 mb-6 bg-white">
      <h2 className="text-xl font-semibold mb-4 text-black">새 메뉴 추가</h2>

      <div className="space-y-4">
        <input
          type="text"
          name="name"
          value={form.name}
          onChange={handleChange}
          placeholder="메뉴 이름"
          className="w-full border border-gray-300 p-3 rounded text-black"
        />
        <input
          type="number"
          name="price"
          value={form.price}
          onChange={handleChange}
          placeholder="가격"
          className="w-full border border-gray-300 p-3 rounded text-black"
        />
        <input
          type="text"
          name="description"
          value={form.description}
          onChange={handleChange}
          placeholder="설명"
          className="w-full border border-gray-300 p-3 rounded text-black"
        />
        <input
          type="text"
          name="imageUrl"
          value={form.imageUrl}
          onChange={handleChange}
          placeholder="이미지 URL"
          className="w-full border border-gray-300 p-3 rounded text-black"
        />

        {/* 품절 여부 토글 */}
        <ToggleSwitch
          checked={form.isSoldOut}
          onChange={() => setForm((prev) => ({ ...prev, isSoldOut: !prev.isSoldOut }))}
          onLabel="품절"
          offLabel="판매중"
        />
      </div>

      <div className="flex justify-end gap-3 mt-6">
        <button
          onClick={onCancel}
          className="px-4 py-2 border border-gray-300 rounded text-gray-600 hover:bg-gray-50"
        >
          취소
        </button>
        <button
          onClick={() => onSave(form)}
          className="px-4 py-2 bg-black text-white rounded hover:bg-gray-800"
        >
          저장
        </button>
      </div>
    </div>
  );
}
