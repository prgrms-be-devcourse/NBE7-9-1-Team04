"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function SignupPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [countdown, setCountdown] = useState(3);

  const router = useRouter();

  // 이메일 유효성 체크
  const isEmailValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

  // 비밀번호 8~20자
  const isPasswordValid = /^.{8,20}$/.test(password);

  // 전화번호 000-0000-0000
  const isPhoneValid = /^\d{3}-\d{4}-\d{4}$/.test(phoneNumber);

  // 모든 입력 유효할 때
  const isFormValid = email && password && phoneNumber && isEmailValid && isPasswordValid && isPhoneValid;

  const handleSignup = async () => {
    if (!isFormValid) return;

    setLoading(true);

    try {
      const res = await fetch("http://localhost:8080/api/users/join", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password, phoneNumber }),
      });

      if (!res.ok) throw new Error("회원가입 실패");

      setSuccess(true);
      setCountdown(3);

      // 3초 카운트 후 이동
      const interval = setInterval(() => {
        setCountdown((prev) => {
          if (prev <= 1) {
            clearInterval(interval);
            router.push("http://localhost:3000/user/");
          }
          return prev - 1;
        });
      }, 1000);
    } catch (err) {
      console.error(err);
      alert("회원가입 실패");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
    {/* 헤더 */}
    <header className="flex items-center justify-between p-4 border-b border-gray-300 relative">
        <button className="text-2xl">☰</button>
        <Link href="/" className="text-xl font-bold">
          Home
        </Link>
        <div className="w-6"></div>
      </header>

    <div className="min-h-screen bg-white flex flex-col items-center justify-center p-6 text-black">
        
      <div className=" shadow-md p-8 rounded-lg w-full max-w-md space-y-4">
        <h2 className="text-2xl font-bold border-b pb-2 mb-4 text-center">
          회원가입
        </h2>

        {/* 이메일 */}
        <div className="flex flex-col">
          <label className="font-semibold mb-1">Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="border border-gray-300 rounded p-2"
            placeholder="이메일 입력"
          />
          {!isEmailValid && email && (
            <span className="text-red-500 text-sm mt-1">유효한 이메일을 입력하세요.</span>
          )}
        </div>

        {/* 비밀번호 */}
        <div className="flex flex-col">
          <label className="font-semibold mb-1">Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="border border-gray-300 rounded p-2"
            placeholder="비밀번호 입력"
          />
          {!isPasswordValid && password && (
            <span className="text-red-500 text-sm mt-1">
              비밀번호는 8자 이상 20자 이내로 입력해주세요.
            </span>
          )}
        </div>

        {/* 전화번호 */}
        <div className="flex flex-col">
          <label className="font-semibold mb-1">Phone Number</label>
          <input
            type="text"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            className="border border-gray-300 rounded p-2"
            placeholder="000-0000-0000"
          />
          {!isPhoneValid && phoneNumber && (
            <span className="text-red-500 text-sm mt-1">
              전화번호는 000-0000-0000 형식으로 입력해주세요.
            </span>
          )}
        </div>

        {/* 회원가입 버튼 */}
        <button
          onClick={handleSignup}
          disabled={!isFormValid || loading}
          className={`w-full py-2 rounded text-white ${
            isFormValid
              ? "bg-blue-500 hover:bg-blue-600"
              : "bg-gray-400 cursor-not-allowed"
          }`}
        >
          {loading ? "가입 중..." : "회원가입"}
        </button>

        {/* 성공 메시지 */}
        {success && (
            <p className="text-green-600 text-center mt-2">
            회원가입 성공! {countdown}초 후 로그인 페이지로 이동합니다...
          </p>
        )}
      </div>
    </div>
    </>
  );
}
