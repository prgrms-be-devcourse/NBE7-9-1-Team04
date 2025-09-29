// src/app/admin/page.tsx
"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetchApi } from "@/lib/client";
import { useAuth } from "@/context/AuthContext";
import { Menu } from "@/types/menu";

import CreateMenuForm from "@/components/admin/CreateMenuForm";
import EditMenuForm from "@/components/admin/EditMenuForm";
import AdminMenuCard from "@/components/admin/AdminMenuCard";
import AdminGuard from "@/components/auth/AdminGuard";

function AdminDashboardContent() {
  const { user } = useAuth();
  const [menus, setMenus] = useState<Menu[]>([]);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingMenu, setEditingMenu] = useState<Menu | null>(null);

  // 메뉴 불러오기
  useEffect(() => {
    if (!user || user.level !== 0) return;
    fetchApi("/api/admin/menus")
      .then((data) => {
        if (Array.isArray(data?.data)) setMenus(data.data);
      })
      .catch((err) => console.error("메뉴 불러오기 실패:", err));
  }, [user]);

  // 메뉴 추가
  const handleCreate = async (menu: Menu) => {
    try {
      const body = {
        name: menu.name,
        price: menu.price,
        isSoldOut: menu.isSoldOut ?? false,
        description: menu.description,
        imageUrl: menu.imageUrl,
      };

      const newMenu = await fetchApi("/api/admin/menus", {
        method: "POST",
        body: JSON.stringify(body),
      });

      setMenus((prev) => [...prev, newMenu.data as Menu]);
      setShowCreateForm(false);
    } catch (err: any) {
      console.error("생성 실패:", err);
      alert(err?.message ?? "메뉴 생성 중 오류가 발생했습니다.");
    }
  };

  // 메뉴 수정
  const handleUpdate = async (menu: Menu) => {
    try {
      if (!menu.menuId) return;

      const body = {
        name: menu.name,
        price: menu.price,
        isSoldOut: menu.isSoldOut ?? false,
        description: menu.description,
        imageUrl: menu.imageUrl,
      };

      await fetchApi(`/api/admin/menus/${menu.menuId}`, {
        method: "PUT",
        body: JSON.stringify(body),
      });

      setMenus((prev) =>
        prev.map((m) => (m.menuId === menu.menuId ? { ...m, ...body } : m))
      );
      setEditingMenu(null);
    } catch (err: any) {
      console.error("수정 실패:", err);
      alert(err.message);
    }
  };

  // 품절 토글
  const handleToggleSoldOut = async (menuId: number, current: boolean) => {
    try {
      await fetchApi(`/api/admin/menus/${menuId}/isSoldOut`, {
        method: "PATCH",
        body: JSON.stringify({ isSoldOut: !current }),
      });
      setMenus((prev) =>
        prev.map((m) =>
          m.menuId === menuId ? { ...m, isSoldOut: !current } : m
        )
      );
    } catch (err) {
      console.error("품절 상태 변경 실패:", err);
    }
  };

  // 메뉴 삭제
  const handleDelete = async (menuId: number) => {
    if (!confirm("정말 삭제하시겠습니까?")) return;
    try {
      await fetchApi(`/api/admin/menus/${menuId}`, { method: "DELETE" });
      setMenus((prev) => prev.filter((m) => m.menuId !== menuId));
    } catch (err) {
      console.error("삭제 실패:", err);
    }
  };

  return (
    <div className="p-6 bg-white min-h-screen">
      <h1 className="text-2xl font-bold text-black mb-6">관리자 대시보드</h1>

      {/* 메뉴 관리 */}
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-xl font-semibold text-black">원두 패키지 관리</h2>
        <button
          onClick={() => setShowCreateForm(true)}
          className="bg-black text-white px-4 py-2 rounded hover:bg-gray-800 transition-colors"
        >
          + 새 원두 추가
        </button>
      </div>

      {/* 생성 폼 */}
      {showCreateForm && (
        <CreateMenuForm
          onCancel={() => setShowCreateForm(false)}
          onSave={handleCreate}
        />
      )}

      {/* 수정 폼 */}
      {editingMenu && (
        <EditMenuForm
          menu={editingMenu}
          onCancel={() => setEditingMenu(null)}
          onSave={handleUpdate}
        />
      )}

      {/* 메뉴 리스트 */}
      <div className="grid gap-6 mt-6">
        {menus.map((menu) => (
          <AdminMenuCard
            key={menu.menuId}
            menu={menu}
            onToggleSoldOut={handleToggleSoldOut}
            onEdit={setEditingMenu}
            onDelete={handleDelete}
          />
        ))}
      </div>
    </div>
  );
}

// 최종 export
export default function AdminPage() {
  return (
    <AdminGuard>
      <AdminDashboardContent />
    </AdminGuard>
  );
}