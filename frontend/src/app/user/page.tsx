"use client"

import { useState,useEffect } from "react"
import { useRouter } from "next/navigation"
import { fetchApi } from "@/lib/client"
import { useAuth } from "@/context/AuthContext"
import Link from "next/link"

export default function LoginPage() {
  const router = useRouter()
  const { refetch } = useAuth() 
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [loading, setLoading] = useState(false)
  const [showLogoutModal, setShowLogoutModal] = useState(false)

// 페이지 로딩 시 쿠키 확인
useEffect(() => {
  const res = fetchApi("/api/users/my", {
    credentials: "include", // 쿠키 전달
  });
  if (res.data) {
    setShowLogoutModal(true)
  }
}, [])

const handleLogout = async () => {
  try {
    await fetchApi("/users/logout", { method: "PUT" })
    setShowLogoutModal(false)
    alert("로그아웃 되었습니다.")
  } catch (err: any) {
    alert("로그아웃 실패: " + err.message)
  }
}

const handleCancelLogout = () => {
  setShowLogoutModal(false)
}
 
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)

    try {
      const res = await fetchApi("/api/users/login", {
        method: "POST",
        body: JSON.stringify({ email, password }),
      })

      alert("로그인 성공!")
      console.log("로그인 응답:", res)

     
    // ✅ 로그인 후 AuthContext 갱신
    await refetch()
    
    // menu 페이지로 이동
    router.push("/menu")
    } catch (err: any) {
     alert("로그인 실패: " + err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
{/* 로그아웃 모달 */}
    {showLogoutModal && (
        <div className="absolute inset-0 flex items-center justify-center bg-black bg-opacity-30 z-10">
          <div className="bg-white p-6 rounded shadow-md w-full max-w-sm space-y-4">
            <p className="text-black text-center">로그인이 되어 있습니다. 로그아웃 하시겠습니까?</p>
            <div className="flex justify-center gap-4">
              <button
                className="bg-black text-white py-2 px-4 rounded"
                onClick={handleLogout}
              >
                확인
              </button>
              <button
                className="bg-gray-300 text-black py-2 px-4 rounded"
                onClick={handleCancelLogout}
              >
                취소
              </button>
            </div>
          </div>
        </div>
      )}
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 rounded shadow-md w-full max-w-sm space-y-4"
      >
        <h1 className="text-2xl text-black font-bold text-center">로그인</h1>

        <input
          type="email"
          placeholder="이메일"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="w-full text-black border p-2 rounded"
          required
        />

        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full text-black border p-2 rounded"
          required
        />

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-black text-white py-2 rounded"
        >
          
          {loading ? "로그인 중..." : "로그인"}
        </button>
        <Link href="user/join"
          className="flex justify-center w-full bg-black text-white py-2 rounded">
            회원 가입
        </Link>
      </form>
    </div>
    </>
  )
}
