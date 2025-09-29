"use client";

import { Menu } from "@/types/menu";

import ToggleSwitch from "@/components/ui/ToggleSwitch";

interface AdminMenuCardProps {
    menu: Menu;
    onToggleSoldOut: (menuId: number, current: boolean) => void;
    onEdit: (menu: Menu) => void;
    onDelete: (menuId: number) => void;
}

export default function AdminMenuCard({
    menu,
    onToggleSoldOut,
    onEdit,
    onDelete,
}: AdminMenuCardProps) {
    return (
        <div className="flex items-center border rounded-lg shadow-sm p-4">
            <img
                src={menu.imageUrl}
                alt={menu.name}
                className="w-24 h-24 object-cover rounded"
            />
            <div className="ml-4 flex-1">
                <h3 className="text-lg font-semibold text-black">{menu.name}</h3>
                <p className="text-sm text-gray-600 line-clamp-2">{menu.description}</p>
                <p className="text-md font-bold mt-1 text-black">
                    {menu.price.toLocaleString()}원
                </p>
            </div>

            <div className="flex gap-4 items-center">
                {/* 품절 토글 */}
                <ToggleSwitch
                    checked={!menu.isSoldOut} // 판매중일 때 ON
                    onChange={() => onToggleSoldOut(menu.menuId!, menu.isSoldOut)}
                    onLabel="판매중"
                    offLabel="품절"
                    onColor="bg-green-500"
                    offColor="bg-gray-300"
                />


                {/* 수정 버튼 */}
                <button
                    onClick={() => onEdit(menu)}
                    className="px-3 py-1 border rounded text-black hover:bg-gray-50 transition-colors"
                >
                    수정
                </button>

                {/* 삭제 버튼 */}
                <button
                    onClick={() => onDelete(menu.menuId!)}
                    className="px-3 py-1 border rounded text-red-500 hover:bg-red-50 transition-colors"
                >
                    삭제
                </button>
            </div>
        </div>
    );
}
