"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { fetchApi } from "@/lib/client";
import Link from "next/link";

export default function AddressPage() {
  const [address, setAddress] = useState("");
  const [addressDetail, setAddressDetail] = useState("");
  const [postNumber, setPostNumber] = useState("");
  const [loading, setLoading] = useState(false);

  const router = useRouter();

  const isFormValid = address && addressDetail && postNumber;

  // 📌 Kakao 주소 검색 스크립트 로드
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
        // 선택한 주소 정보
        const fullAddress = data.address; // 전체 주소
        const zoneCode = data.zonecode; // 우편번호

        setAddress(fullAddress);
        setPostNumber(zoneCode);
      },
    }).open();
  };

  const handleSave = async () => {
    if (!isFormValid) return;

    setLoading(true);

    try {
      const res = await fetchApi("/api/users/address/add", {
        method: "POST",
        body: JSON.stringify({
          address,
          addressDetail,
          postNumber,
        }),
      });

      alert("주소가 저장되었습니다!");
      router.push("/user/address/list");
    } catch (err) {
      console.error(err);
      alert("주소 저장 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-white flex flex-col items-center p-6 text-black">
  

      {/* 카드 */}
      <div className="bg-gray-50 shadow-md p-8 rounded-lg w-full max-w-md space-y-4">
        <h2 className="text-2xl font-bold border-b pb-2 mb-4 text-center">
          주소 저장
        </h2>

        {/* 주소 입력 */}
        <div className="flex flex-col">
          <label className="font-semibold mb-1">주소</label>
          <input
            type="text"
            value={address}
            readOnly
            className="border border-gray-300 rounded p-2 bg-gray-100 cursor-not-allowed"
            placeholder="도/시 @@시/군/구 ##면/리/동"
          />
        </div>

        {/* 상세주소 입력 */}
        <div className="flex flex-col">
          <label className="font-semibold mb-1">상세 주소</label>
          <input
            type="text"
            value={addressDetail}
            onChange={(e) => setAddressDetail(e.target.value)}
            className="border border-gray-300 rounded p-2"
            placeholder="상세 주소 입력"
          />
        </div>

        {/* 우편번호 입력 */}
        <div className="flex flex-col">
          <label className="font-semibold mb-1">우편번호</label>
          <div className="flex gap-2">
            <input
              type="text"
              value={postNumber}
              readOnly
              className="border border-gray-300 rounded p-2 flex-1 bg-gray-100 cursor-not-allowed"
              placeholder="우편번호"
            />
            <button
              type="button"
              onClick={handlePostSearch}
              className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
            >
              찾기
            </button>
          </div>
        </div>

        {/* 저장 버튼 */}
        <button
          onClick={handleSave}
          disabled={!isFormValid || loading}
          className={`w-full py-2 rounded text-white ${
            isFormValid
              ? "bg-green-500 hover:bg-green-600"
              : "bg-gray-400 cursor-not-allowed"
          }`}
        >
          {loading ? "저장 중..." : "주소 저장"}
        </button>
      </div>
    </div>
  );
}
