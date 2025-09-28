"use client"

import { useEffect, useState } from "react"
import { useRouter, useSearchParams } from "next/navigation"
import { fetchApi } from "@/lib/client"
import Link from "next/link"

// 백엔드 API 응답 타입 정의
interface OrderSummaryDetailResponse {
  productName: string
  quantity: number
  orderPrice: number
  imageUrl?: string
}

interface OrderSummaryResponse {
  orderId: number
  orderTime: string
  orderAmount: number
  status: string
  address: string
  paymentId: number
  items: OrderSummaryDetailResponse[]
}

interface ApiResponse<T> {
  code: string
  message: string
  data: T
}

export default function OrderSuccessPage() {
  const router = useRouter()
  const searchParams = useSearchParams()
  const orderId = searchParams.get('orderId')
  
  const [order, setOrder] = useState<OrderSummaryResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    async function updateAndLoadOrder() {
      try {
        setLoading(true)
        setError(null)

        const res = await fetchApi(`/api/orders/${orderId}`, { method: "GET" })
        setOrder(res.data)
      } catch (err) {
        console.error("주문 처리 실패:", err)
        setError(err instanceof Error ? err.message : '주문 정보를 가져오는데 실패했습니다.')
      } finally {
        setLoading(false)
      }
    }

    if (orderId) {
      updateAndLoadOrder()
    } else {
      setError("주문 번호가 없습니다.")
      setLoading(false)
    }
  }, [orderId])

  // 날짜 포맷팅 함수
  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString("ko-KR", {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  // 주문 상태 한글 변환
  const getOrderStatusText = (status: string) => {
    switch (status) {
      case 'CREATED': return '주문완료'
      case 'PAID': return '결제완료'
      case 'COMPLETED': return '배송완료'
      case 'CANCELED': return '주문취소'
      default: return status
    }
  }

  // 로딩 상태
  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50">
        <main className="max-w-2xl mx-auto py-10 px-6">
          <div className="text-center">
            <div className="animate-pulse">
              <div className="h-16 w-16 bg-gray-200 rounded-full mx-auto mb-4"></div>
              <div className="h-8 bg-gray-200 rounded mb-2 w-1/2 mx-auto"></div>
              <div className="h-4 bg-gray-200 rounded w-3/4 mx-auto"></div>
            </div>
          </div>
        </main>
      </div>
    )
  }

  // 에러 상태
  if (error || !order) {
    return (
      <div className="min-h-screen bg-gray-50">
        <main className="max-w-2xl mx-auto py-10 px-6">
          <div className="text-center">
            <div className="w-16 h-16 mx-auto mb-4 flex items-center justify-center">
              <svg className="w-16 h-16 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L4.082 16.5c-.77.833.192 2.5 1.732 2.5z" />
              </svg>
            </div>
            <h1 className="text-3xl font-bold mb-2">오류가 발생했습니다</h1>
            <p className="text-gray-600 mb-8">
              {error || "주문 정보를 찾을 수 없습니다."}
            </p>
            <div className="flex gap-4 justify-center">
              <Link href="/orders">
                <button className="px-6 py-2 border border-gray-300 rounded text-gray-700 hover:bg-gray-50">
                  주문내역 보기
                </button>
              </Link>
              <Link href="/">
                <button className="px-6 py-2 bg-black text-white rounded hover:bg-gray-800">
                  홈으로 가기
                </button>
              </Link>
            </div>
          </div>
        </main>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <main className="max-w-2xl mx-auto py-10 px-6">
        <div className="text-center">
          {/* 성공 아이콘 */}
          <div className="w-16 h-16 mx-auto mb-4 flex items-center justify-center">
            <svg className="w-16 h-16 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          
          <h1 className="text-3xl font-bold mb-2">주문이 완료되었습니다!</h1>
          <p className="text-gray-600 mb-8">
            주문해 주셔서 감사합니다. 신선한 원두를 정성껏 준비하여 배송해드리겠습니다.
          </p>

          {/* 주문 정보 카드 */}
          <div className="bg-white border rounded-lg mb-8">
            <div className="border-b px-6 py-4">
              <div className="flex items-center gap-2 justify-center">
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                </svg>
                <h2 className="text-lg font-bold">주문 정보</h2>
              </div>
            </div>
            
            <div className="p-6 space-y-4">
              <div className="grid grid-cols-2 gap-4 text-sm">
                <div>
                  <span className="text-gray-500">주문번호</span>
                  <div className="font-medium">#{order.orderId}</div>
                </div>
                <div>
                  <span className="text-gray-500">주문일시</span>
                  <div className="font-medium">{formatDate(order.orderTime)}</div>
                </div>
                <div>
                  <span className="text-gray-500">결제금액</span>
                  <div className="font-medium text-blue-600">{order.orderAmount.toLocaleString()}원</div>
                </div>
                <div>
                  <span className="text-gray-500">주문상태</span>
                  <div className="font-medium text-green-600">{getOrderStatusText(order.status)}</div>
                </div>
              </div>

              {order.address && (
                <div className="text-left">
                  <span className="text-gray-500 text-sm">배송주소</span>
                  <div className="font-medium">{order.address}</div>
                </div>
              )}

              <div className="text-left">
                <span className="text-gray-500 text-sm">주문상품</span>
                <div className="space-y-2 mt-2">
                  {order.items.map((item, index) => (
                    <div key={index} className="flex justify-between text-sm">
                      <span>
                        {item.productName} x {item.quantity}
                      </span>
                      <span className="font-medium">{item.orderPrice.toLocaleString()}원</span>
                    </div>
                  ))}
                </div>
              </div>

              {order.paymentId && (
                <div className="text-left">
                  <span className="text-gray-500 text-sm">결제번호</span>
                  <div className="font-medium">#{order.paymentId}</div>
                </div>
              )}
            </div>
          </div>

          {/* 배송 안내 */}
          <div className="bg-gray-100 rounded-lg p-6 mb-8">
            <h3 className="font-semibold mb-2">배송 안내</h3>
            <ul className="text-sm text-gray-600 space-y-1 text-left">
              <li>• 금일 오후 2시부터 익일 오후 2시까지 결제된 건에 대해서는 익일 오후 2시에 일괄 배송 처리됩니다</li>
              <li>• 배송 조회는 주문내역에서 확인 가능합니다</li>
            </ul>
          </div>

          {/* 버튼 그룹 */}
          <div className="flex gap-4 justify-center">
            <Link href="/orders">
              <button className="px-6 py-3 border border-gray-300 rounded text-gray-700 hover:bg-gray-50 flex items-center gap-2">
                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
                주문내역 보기
              </button>
            </Link>
            <Link href="/">
              <button className="px-6 py-3 bg-black text-white rounded hover:bg-gray-800 flex items-center gap-2">
                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
                </svg>
                홈으로 가기
              </button>
            </Link>
          </div>
        </div>
      </main>
    </div>
  )
}