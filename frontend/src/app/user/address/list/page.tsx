"use client";

import { useState, useEffect } from "react";
import Link from "next/link";
export default function AddressListPage() {
  const [addresses, setAddresses] = useState([]);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({ address: "", addressDetail: "", postNumber: "" });

  // 주소 목록 불러오기
  const fetchAddresses = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/users/address/list", {
        credentials: "include",
      });
      if (!res.ok) throw new Error("주소 목록 불러오기 실패");
      const data = await res.json();
      setAddresses(data.data); // data 안의 배열
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchAddresses();
  }, []);

  // 카카오 주소 API 스크립트 로드
  useEffect(() => {
    const script = document.createElement("script");
    script.src = "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
    script.async = true;
    document.body.appendChild(script);
  }, []);

  const handlePostSearch = () => {
    // eslint-disable-next-line no-undef
    new daum.Postcode({
      oncomplete: function (data) {
        setFormData((prev) => ({
          ...prev,
          address: data.address,
          postNumber: data.zonecode,
        }));
      },
    }).open();
  };

  const handleSave = async (addressId) => {
    try {
      const res = await fetch(`http://localhost:8080/api/users/address/modify/${addressId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify(formData),
      });
      if (!res.ok) throw new Error("주소 수정 실패");
      await fetchAddresses(); // 갱신
      setEditingId(null);
      setFormData({ address: "", addressDetail: "", postNumber: "" });
    } catch (err) {
      console.error(err);
      alert("주소 수정에 실패했습니다.");
    }
  };

  const handleDelete = async (addressId) => {
    if (!window.confirm("정말 삭제하시겠습니까?")) {
      return;
    }
    try {
      const res = await fetch(`http://localhost:8080/api/users/address/delete/${addressId}`, {
        method: "DELETE",
        credentials: "include",
      });
      if (!res.ok) throw new Error("주소 삭제 실패");
      await fetchAddresses(); // 최신 데이터 갱신
    } catch (err) {
      console.error(err);
      alert("주소 삭제에 실패했습니다.");
    }
  };

  return (
    <div className="min-h-screen bg-white text-black flex flex-col items-center p-6">
      <h2 className="text-2xl font-bold mb-6">주소 목록</h2>
      {addresses ? (
         <div className="w-full max-w-md space-y-4">
         {addresses.map((addr) => (
           <div key={addr.addressId} className="bg-gray-50 shadow-md p-4 rounded-lg">
             <p><strong>주소:</strong> {addr.address}</p>
             <p><strong>상세주소:</strong> {addr.addressDetail}</p>
             <p><strong>우편번호:</strong> {addr.postNumber}</p>
 
             {editingId === addr.addressId ? (
               <div className="mt-4 space-y-2">
                 {/* 주소 입력 */}
                 <input
                   type="text"
                   value={formData.address}
                   readOnly
                   placeholder="주소"
                   className="w-full border rounded p-2 bg-gray-100"
                 />
 
                 {/* 상세주소 입력 */}
                 <input
                   type="text"
                   value={formData.addressDetail}
                   onChange={(e) => setFormData((prev) => ({ ...prev, addressDetail: e.target.value }))}
                   placeholder="상세 주소"
                   className="w-full border rounded p-2"
                 />
 
                 {/* 우편번호 입력 + 찾기 버튼 */}
                 <div className="flex gap-2">
                   <input
                     type="text"
                     value={formData.postNumber}
                     readOnly
                     placeholder="우편번호"
                     className="border rounded p-2 flex-1 bg-gray-100"
                   />
                   <button
                     type="button"
                     onClick={handlePostSearch}
                     className="px-3 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                   >
                     우편번호 찾기
                   </button>
                 </div>
 
                 {/* 액션 버튼 */}
                 <div className="flex gap-2 mt-2">
                   <button
                     onClick={() => handleSave(addr.addressId)}
                     disabled={!formData.address || !formData.addressDetail || !formData.postNumber}
                     className="flex-1 bg-black hover:bg-green-600 text-white py-2 rounded disabled:bg-gray-400"
                   >
                     주소 수정하기
                   </button>
                   <button
                     onClick={() => {
                       setEditingId(null);
                       setFormData({ address: "", addressDetail: "", postNumber: "" });
                     }}
                     className="flex-1 bg-gray-500 hover:bg-gray-600 text-white py-2 rounded"
                   >
                     취소
                   </button>
                 </div>
               </div>
             ) : (
               <div className="flex gap-2 mt-4">
                 <button
                   onClick={() => {
                     setEditingId(addr.addressId);
                     setFormData({
                       address: addr.address,
                       addressDetail: addr.addressDetail,
                       postNumber: addr.postNumber,
                     });
                   }}
                   className="flex-1  bg-gray-500 hover:bg-gray-600 text-white py-2 rounded"
                 >
                   주소 수정
                 </button>
                 <button
                   onClick={() => handleDelete(addr.addressId)}
                   className="flex-1 bg-red-500 hover:bg-red-600 text-white py-2 rounded"
                 >
                   주소 삭제
                 </button>
               </div>
             )}
           </div>
         ))}
       </div>
        ) : (
          <h2 className="text-2xl font-bold mb-6">주소가 없습니다. 추가해주세요</h2>
        )}
      <div className=" flex mt-4">
        <Link href="/user/address/add"
          className="bg-black text-white px-4 py-2 rounded m-2 hover:bg-green-800">
          주소 추가
        </Link>
        <Link href="/payment"
          className="bg-black text-white px-4 py-2 rounded m-2 hover:bg-blue-800">
          결제하러 가기
        </Link>
      </div>
    </div>
  );
}
