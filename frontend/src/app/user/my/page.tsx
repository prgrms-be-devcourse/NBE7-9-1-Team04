"use client";

import { useEffect, useState } from "react";
import Link from "next/link";

export default function ProfilePage() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [phoneInput, setPhoneInput] = useState("");

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/users/my", {
          credentials: "include", // 쿠키 전달
        });
        if (!res.ok) throw new Error("Failed to fetch user info");
        const data = await res.json();
        setUser(data);
        setPhoneInput(data.data.phoneNumber); // 초기값 세팅
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchUser();
  }, []);

  const handleEditToggle = () => {
    setEditing(!editing);
    if (!editing && user) {
      setPhoneInput(user.data.phoneNumber);
    }
  };

  const handleSave = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/users/modify", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include", // 쿠키 전달
        body: JSON.stringify({ phoneNumber: phoneInput }),
      });
      if (!res.ok) throw new Error("Failed to update phone number");

      const updatedData = await res.json();

      // 백엔드 응답 구조에 맞춰서 user state 업데이트
      setUser(updatedData);
      setEditing(false);
      alert("전화번호가 성공적으로 수정되었습니다!");
    } catch (err) {
      console.error(err);
      alert("전화번호 수정 실패");
    }
  };

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  if (!user) {
    return <div className="flex justify-center items-center h-screen">User info not found</div>;
  }

  return (
    <div className="min-h-screen bg-white text-black flex flex-col">
      {/* 헤더 */}
      <header className="flex items-center justify-between p-4 border-b border-gray-300 relative">
        <button className="text-2xl">☰</button>
        <Link href="/" className="text-xl font-bold">
          Home
        </Link>
        <div className="w-6"></div>
      </header>

      {/* 프로필 카드 */}
      <main className="flex flex-col items-center justify-center flex-1 p-6">
        <div className="bg-gray-50 shadow-md p-8 rounded-lg w-full max-w-md text-center space-y-4">
          <h2 className="text-2xl font-bold border-b pb-2 mb-4">My Profile</h2>
          
          <p>
            <strong>Email:</strong> {user.data.userEmail}
          </p>

          {editing ? (
            <div className="flex flex-col items-center space-y-2">
              <label className="font-semibold">Phone Number:</label>
              <input
                type="text"
                value={phoneInput}
                onChange={(e) => setPhoneInput(e.target.value)}
                className="border border-gray-300 rounded p-2 w-full text-center"
              />
            </div>
          ) : (
            <p>
              <strong>Phone Number:</strong> {user.data.phoneNumber}
            </p>
          )}

          <p>
            <strong>Joined:</strong>{" "}
            {new Date(user.data.createDate).toLocaleDateString("ko-KR", {
              year: "numeric",
              month: "long",
              day: "numeric",
            })}
          </p>

          {/* 수정 / 취소 버튼 */}
          <div className="flex justify-center space-x-4 mt-4">
            {editing ? (
              <>
                <button
                  onClick={handleSave}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                  수정하기
                </button>
                <button
                  onClick={handleEditToggle}
                  className="bg-gray-300 text-black px-4 py-2 rounded hover:bg-gray-400"
                >
                  취소
                </button>
              </>
            ) : (
              <button
                onClick={handleEditToggle}
                className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
              >
                내 정보 수정
              </button>
            )}
          </div>
        </div>
      </main>
    </div>
  );
}
