"use client"

import { useParams } from "next/navigation"
import { useEffect, useState } from "react"
import Link from "next/link"
import { fetchApi } from "@/lib/client"
import AuthGuard from "@/components/auth/AuthGuard"

type PaymentDetail = {
  paymentId: number
  paymentAmount: number
  paymentMethod: string
  paymentStatus: string
  createDate: string
  modifyDate: string
}

type UserInfo = {
  userId: number
  userEmail: string
  phoneNumber: string
  level: number // 0 = admin, 1 = 일반 사용자
}

// ✅ 실제 페이지는 AuthGuard로 감싸서 보호
export default function ProtectedPaymentDetailPage() {
  return (
    <AuthGuard>
      <PaymentDetailPage />
    </AuthGuard>
  )
}

// ❌ default 빼고 export만 해주기
function PaymentDetailPage() {
  const { paymentId } = useParams()
  const [payment, setPayment] = useState<PaymentDetail | null>(null)
  const [user, setUser] = useState<UserInfo | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    async function loadData() {
      try {
        // ✅ 결제 정보
        const res = await fetchApi(`/api/payments/${paymentId}`, { method: "GET" })
        setPayment(res.data)

        // ✅ 로그인한 사용자 정보
        const userRes = await fetchApi("/api/users/my", { method: "GET" })
        setUser(userRes.data) // ApiResponse.data
      } catch (err) {
        console.error("데이터 불러오기 실패:", err)
      } finally {
        setLoading(false)
      }
    }
    if (paymentId) loadData()
  }, [paymentId])

  if (loading) return <div className="p-6">결제 정보를 불러오는 중...</div>
  if (!payment) return <div className="p-6">결제 정보를 찾을 수 없습니다.</div>

  const getPaymentStatusText = (status: string) => {
    const statusMap: Record<string, string> = {
      PENDING: "결제 전",
      COMPLETED: "결제 완료",
      FAILED: "결제 실패",
      CANCELED: "결제 취소",
    }
    return statusMap[status] || status
  }

  // ✅ admin 여부에 따라 이동 경로 분기
  const orderPageHref = user?.level === 0 ? "/orders/admin" : "/orders"

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center py-12 px-4">
      <div className="bg-white border rounded-lg shadow-sm p-6 w-full max-w-2xl">
        <h1 className="text-xl font-bold mb-4">💳 결제 정보</h1>
        <div className="grid grid-cols-2 text-sm text-gray-700 gap-y-2">
          <p>결제 ID</p>
          <p className="text-right">#{payment.paymentId}</p>
          <p>결제 금액</p>
          <p className="text-right">{payment.paymentAmount.toLocaleString()}원</p>
          <p>결제 방법</p>
          <p className="text-right">{payment.paymentMethod}</p>
          <p>결제 상태</p>
          <p
            className={`text-right font-semibold ${
              payment.paymentStatus === "COMPLETED"
                ? "text-green-600"
                : payment.paymentStatus === "PENDING"
                ? "text-yellow-600"
                : "text-red-600"
            }`}
          >
            {getPaymentStatusText(payment.paymentStatus)}
          </p>
          <p>생성 일시</p>
          <p className="text-right">{new Date(payment.createDate).toLocaleString()}</p>
          <p>수정 일시</p>
          <p className="text-right">{new Date(payment.modifyDate).toLocaleString()}</p>
        </div>
      </div>

      <div className="flex gap-4 mt-8">
        <Link href={orderPageHref}>
          <button className="px-6 py-2 border rounded bg-gray-100 hover:bg-gray-200">
            주문내역 보기
          </button>
        </Link>
        <Link href="/">
          <button className="px-6 py-2 border rounded bg-black text-white">
            홈으로 가기
          </button>
        </Link>
      </div>
    </div>
  )
}
