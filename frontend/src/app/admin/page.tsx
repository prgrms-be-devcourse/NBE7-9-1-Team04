"use client";

import { useEffect, useState } from "react";
import { fetchApi } from "@/lib/client";

interface Menu {
  menuId?: number;
  name: string;
  price: number;
  isSoldOut: boolean;
  description: string;
  imageUrl: string;
}

export default function AdminDashboard() {
  const [menus, setMenus] = useState<Menu[]>([]);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingMenu, setEditingMenu] = useState<Menu | null>(null);

  // 메뉴 불러오기(관리자 조회)
  useEffect(() => {
    fetchApi("/api/admin/menus")
      .then((data) => {
        if (data?.data) setMenus(data.data);
      })
      .catch((err) => console.error("메뉴 불러오기 실패:", err));
  }, []);

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

      setMenus((prev) => [...prev, newMenu.data]);
      setShowCreateForm(false);
    } catch (err: any) {
      console.error("생성 실패:", err);
      alert(err.message);
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
          <div
            key={menu.menuId}
            className="flex items-center border rounded-lg shadow-sm p-4"
          >
            <img
              src={menu.imageUrl}
              alt={menu.name}
              className="w-24 h-24 object-cover rounded"
            />
            <div className="ml-4 flex-1">
              <h3 className="text-lg font-semibold text-black">{menu.name}</h3>
              <p className="text-sm text-gray-600 line-clamp-2">
                {menu.description}
              </p>
              <p className="text-md font-bold mt-1 text-black">
                {menu.price.toLocaleString()}원
              </p>
            </div>

            <div className="flex gap-4 items-center">
              {/* 품절 토글 - 개선된 버전 */}
              <div className="flex items-center gap-2">
                <span className="text-sm text-gray-600">
                  {menu.isSoldOut ? "품절" : "재고있음"}
                </span>
                <button
                  onClick={() => handleToggleSoldOut(menu.menuId!, menu.isSoldOut)}
                  className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 ${
                    menu.isSoldOut 
                      ? 'bg-gray-300 focus:ring-gray-500' 
                      : 'bg-green-500 focus:ring-green-500'
                  }`}
                >
                  <span
                    className={`inline-block h-4 w-4 transform rounded-full bg-white transition-transform duration-200 ${
                      menu.isSoldOut ? 'translate-x-1' : 'translate-x-6'
                    }`}
                  />
                </button>
              </div>

              {/* 수정 버튼 */}
              <button
                onClick={() => setEditingMenu(menu)}
                className="px-3 py-1 border rounded text-black hover:bg-gray-50 transition-colors"
              >
                수정
              </button>

              {/* 삭제 버튼 */}
              <button
                onClick={() => handleDelete(menu.menuId!)}
                className="px-3 py-1 border rounded text-red-500 hover:bg-red-50 transition-colors"
              >
                삭제
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

//
// 생성 폼 컴포넌트
//
function CreateMenuForm({
  onCancel,
  onSave,
}: {
  onCancel: () => void;
  onSave: (menu: Menu) => void;
}) {
  const [form, setForm] = useState<Menu>({
    name: "콜롬비아 수프리모", 
    price: 25000, 
    isSoldOut: false, 
    description: "균형잡힌 맛과 부드러운 바디감", 
    imageUrl: "https://hebbkx1anhila5yf.public.blob.vercel-storage.com/attachments/gen-images/public/colombian-coffee-beans-package-avUaPASEOeFsLXIh0tlGy3QLvNNwkP.jpg",
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
          className="w-full border border-gray-300 p-3 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-black"
        />
        <input
          type="number"
          name="price"
          value={form.price}
          onChange={handleChange}
          placeholder="가격"
          className="w-full border border-gray-300 p-3 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-black"
        />
        <input
          type="text"
          name="description"
          value={form.description}
          onChange={handleChange}
          placeholder="설명"
          className="w-full border border-gray-300 p-3 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-black"
        />
        <input
          type="text"
          name="imageUrl"
          value={form.imageUrl}
          onChange={handleChange}
          placeholder="이미지 URL"
          className="w-full border border-gray-300 p-3 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-black"
        />

        {/* 품절 여부 토글 - 개선된 버전 */}
        <div className="flex items-center justify-between py-2">
          <span className="text-sm text-gray-700 font-medium">품절 여부</span>
          <div className="flex items-center gap-3">
            <span className="text-sm text-gray-600">
              {form.isSoldOut ? "품절" : "판매중"}
            </span>
            <button
              type="button"
              onClick={() => setForm(prev => ({ ...prev, isSoldOut: !prev.isSoldOut }))}
              className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 ${
                form.isSoldOut 
                  ? 'bg-red-500 focus:ring-red-500' 
                  : 'bg-green-500 focus:ring-green-500'
              }`}
            >
              <span
                className={`inline-block h-4 w-4 transform rounded-full bg-white transition-transform duration-200 ${
                  form.isSoldOut ? 'translate-x-6' : 'translate-x-1'
                }`}
              />
            </button>
          </div>
        </div>
      </div>

      <div className="flex justify-end gap-3 mt-6">
        <button
          onClick={onCancel}
          className="px-4 py-2 border border-gray-300 rounded text-gray-600 hover:bg-gray-50 transition-colors"
        >
          취소
        </button>
        <button
          onClick={() => onSave(form)}
          className="px-4 py-2 bg-black text-white rounded hover:bg-gray-800 transition-colors"
        >
          저장
        </button>
      </div>
    </div>
  );
}

//
// 수정 폼 컴포넌트
//
function EditMenuForm({
  menu,
  onCancel,
  onSave,
}: {
  menu: Menu;
  onCancel: () => void;
  onSave: (menu: Menu) => void;
}) {
  const [form, setForm] = useState<Menu>(menu);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: name === "price" ? +value : value,
    }));
  };

  return (
    <div className="border rounded-lg shadow-md p-6 mb-6 bg-white">
      <h2 className="text-xl font-semibold mb-4 text-black">메뉴 수정</h2>
  
      <div className="space-y-4">
        <input
          type="text"
          name="name"
          value={form.name}
          onChange={handleChange}
          placeholder="메뉴 이름"
          className="w-full border border-gray-300 p-3 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-black"
        />
        <input
          type="number"
          name="price"
          value={form.price}
          onChange={handleChange}
          placeholder="가격"
          className="w-full border border-gray-300 p-3 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-black"
        />
        <input
          type="text"
          name="description"
          value={form.description}
          onChange={handleChange}
          placeholder="설명"
          className="w-full border border-gray-300 p-3 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-black"
        />
        <input
          type="text"
          name="imageUrl"
          value={form.imageUrl}
          onChange={handleChange}
          placeholder="이미지 URL"
          className="w-full border border-gray-300 p-3 rounded focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-black"
        />
  
        {/* 품절 여부 토글 - 개선된 버전 */}
        <div className="flex items-center justify-between py-2">
          <span className="text-sm text-gray-700 font-medium">품절 여부</span>
          <div className="flex items-center gap-3">
            <span className="text-sm text-gray-600">
              {form.isSoldOut ? "품절" : "재고있음"}
            </span>
            <button
              type="button"
              onClick={() => setForm(prev => ({ ...prev, isSoldOut: !prev.isSoldOut }))}
              className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 ${
                form.isSoldOut 
                  ? 'bg-red-500 focus:ring-red-500' 
                  : 'bg-green-500 focus:ring-green-500'
              }`}
            >
              <span
                className={`inline-block h-4 w-4 transform rounded-full bg-white transition-transform duration-200 ${
                  form.isSoldOut ? 'translate-x-6' : 'translate-x-1'
                }`}
              />
            </button>
          </div>
        </div>
      </div>
  
      <div className="flex justify-end gap-3 mt-6">
        <button
          onClick={onCancel}
          className="px-4 py-2 border border-gray-300 rounded text-gray-600 hover:bg-gray-50 transition-colors"
        >
          취소
        </button>
        <button
          onClick={() => onSave(form)}
          className="px-4 py-2 bg-black text-white rounded hover:bg-gray-800 transition-colors"
        >
          저장
        </button>
      </div>
    </div>
  );
}