"use client"

import { useAuth } from "@/context/AuthContext"; 
import { useRouter } from "next/navigation"
import { useEffect } from "react"
import AuthGuard from "@/components/auth/AuthGuard";

// ✅ 변경점: AuthGuard 사용하여 로그인 상태 처리
export default function CartPage() {
  return (
    <AuthGuard>
      <PaymentFailPage />
    </AuthGuard>
  );
}

function PaymentFailPage() {
  const { refetch } = useAuth();
  const router = useRouter()

  useEffect(() => {
    async function fetchData() {
      await refetch();
    }
    fetchData();
  }, [])

  return (
    <div className="min-h-screen bg-gray-50">
      <main className="max-w-2xl mx-auto py-10 px-6">
        <div className="text-center">
          {/* 실패 아이콘 */}
          <div className="w-16 h-16 mx-auto mb-4 flex items-center justify-center">
            <svg className="w-16 h-16 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L4.082 16.5c-.77.833.192 2.5 1.732 2.5z" />
            </svg>
          </div>
          
          <h1 className="text-3xl font-bold mb-2">결제에 실패했습니다</h1>
          <p className="text-gray-600 mb-8">
            결제 처리 중 문제가 발생했습니다. 다시 시도해 주세요.
          </p>

          {/* 실패 안내 정보 */}
          <div className="bg-red-50 border border-red-200 rounded-lg p-6 mb-8">
            <h3 className="font-semibold mb-2 text-red-800">결제 실패 안내</h3>
            <ul className="text-sm text-red-700 space-y-1 text-left">
              <li>• 카드 정보를 다시 확인해 주세요</li>
              <li>• 결제 한도를 확인해 주세요</li>
              <li>• 네트워크 연결 상태를 확인해 주세요</li>
              <li>• 문제가 지속되면 고객센터로 문의해 주세요</li>
            </ul>
          </div>

          {/* 버튼 그룹 */}
          <div className="flex gap-4 justify-center">
            <button 
              onClick={() => router.push('/menu')}
              className="px-6 py-3 bg-black text-white rounded hover:bg-gray-800 flex items-center gap-2"
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
              </svg>
              메뉴로 가기
            </button>
          </div>
        </div>
      </main>
    </div>
  )
}