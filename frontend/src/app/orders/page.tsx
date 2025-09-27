"use client"

import { useEffect, useState } from "react"
import Link from "next/link"
import { fetchApi } from "@/lib/client"

type OrderItem = {
  productName: string
  quantity: number
  orderPrice: number
  imageUrl?: string
}

type Order = {
  orderId: number
  orderTime: string
  orderAmount: number
  status: string
  address: string
  paymentId?: number
  items: OrderItem[]
}

export default function OrdersPage() {
  const [orders, setOrders] = useState<Order[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    async function loadOrders() {
      try {
        const res = await fetchApi("/api/orders", { method: "GET" })
        setOrders(res.data)
      } catch (err) {
        console.error("주문 조회 실패:", err)
      } finally {
        setLoading(false)
      }
    }
    loadOrders()
  }, [])

  if (loading) return <div className="p-6">불러오는 중...</div>

  const sortedOrders = [...orders].sort(
    (a, b) => new Date(b.orderTime).getTime() - new Date(a.orderTime).getTime()
  )

  // ✅ 주문 취소 (결제 취소 → 주문 취소)
  const handleCancel = async (orderId: number, paymentId?: number) => {
    if (!paymentId) {
      alert("결제 정보가 없습니다.")
      return
    }
    try {
      // 1. 결제 취소
      await fetchApi(`/api/payments/${paymentId}/cancel`, { method: "PUT" })

      // 2. 주문 취소
      await fetchApi(`/api/orders/${orderId}/status`, {
        method: "PUT",
        body: JSON.stringify({ newStatus: "CANCELED" }),
      })

      alert("주문이 취소되었습니다.")

      // UI 상태 업데이트 (status → CANCELED 로 변경)
      setOrders((prev) =>
        prev.map((o) =>
          o.orderId === orderId ? { ...o, status: "CANCELED" } : o
        )
      )
    } catch (err) {
      console.error("주문 취소 실패:", err)
      alert("주문 취소에 실패했습니다.")
    }
  }

  // ✅ 주문 삭제 (결제 삭제 → 주문 삭제)
  const handleDelete = async (orderId: number, paymentId?: number) => {
    if (!paymentId) {
      alert("결제 정보가 없습니다.")
      return
    }
    if (!confirm("정말 삭제하시겠습니까?")) return

    try {
      // 1. 결제 삭제 (CANCELED 상태여야만 성공)
      await fetchApi(`/api/payments/${paymentId}/delete`, { method: "DELETE" })

      // 2. 주문 삭제
      await fetchApi(`/api/orders/${orderId}/delete`, { method: "DELETE" })

      alert("주문이 삭제되었습니다.")

      // UI 상태 업데이트 (리스트에서 제거)
      setOrders((prev) => prev.filter((o) => o.orderId !== orderId))
    } catch (err) {
      console.error("주문 삭제 실패:", err)
      alert("주문 삭제에 실패했습니다.")
    }
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="border-b bg-white p-4">
        <h1 className="text-xl font-bold">카페 원두</h1>
      </header>

      <main className="max-w-3xl mx-auto py-10 px-4">
        <div className="flex items-center gap-2 mb-6">
          <Link href="/" className="text-sm text-gray-600">
            ← 홈으로 돌아가기
          </Link>
          <h1 className="text-2xl font-bold ml-4">주문내역</h1>
        </div>

        {sortedOrders.length === 0 ? (
          <div className="text-center py-16">
            <h2 className="text-2xl font-bold mb-2">주문내역이 없습니다</h2>
            <p className="text-gray-500 mb-6">첫 주문을 시작해보세요</p>
            <Link href="/">
              <button className="px-4 py-2 border rounded bg-black text-white">
                원두 둘러보기
              </button>
            </Link>
          </div>
        ) : (
          <div className="space-y-6">
            {sortedOrders.map((order) => (
              <div
                key={order.orderId}
                className="bg-white border rounded-lg shadow-sm p-6"
              >
                <div className="flex justify-between items-center mb-2">
                  <h2 className="font-bold text-lg">
                    주문번호 #{order.orderId}
                  </h2>
                  <span
                    className={`text-sm border px-2 py-0.5 rounded ${
                      order.status === "CANCELED"
                        ? "text-red-600 border-red-600"
                        : "text-green-600 border-green-600"
                    }`}
                  >
                    {order.status}
                  </span>
                </div>

                <p className="text-sm text-gray-500 mb-2">
                  주문일시: {new Date(order.orderTime).toLocaleString()}
                </p>
                <p className="font-semibold mb-2">
                  총 금액: {order.orderAmount.toLocaleString()}원
                </p>
                <p className="text-sm text-gray-600 mb-4">
                  배송주소: {order.address}
                </p>

                <ul className="mt-2 text-sm text-gray-600 space-y-3">
                  {order.items.map((item, idx) => (
                    <li
                      key={`${order.orderId}-${idx}`}
                      className="flex items-center gap-4 py-2 border-b"
                    >
                      <img
                        src={item.imageUrl}
                        alt={item.productName}
                        className="w-16 h-16 object-cover rounded"
                      />
                      <div className="flex-1">
                        <p className="font-medium">{item.productName}</p>
                        <p className="text-sm text-gray-500">
                          {item.quantity}개
                        </p>
                      </div>
                      <div className="font-semibold">
                        {item.orderPrice.toLocaleString()}원
                      </div>
                    </li>
                  ))}
                </ul>

                {/* 버튼 영역 */}
                <div className="flex gap-2 mt-4">
                  <Link href={`/payment/${order.paymentId}`} className="flex-1">
                    <button className="w-full border rounded py-2 text-sm bg-blue-50 hover:bg-blue-100 text-center">
                      결제확인
                    </button>
                  </Link>

                  {order.status === "CANCELED" ? (
                    <button
                      className="flex-1 w-full border rounded py-2 text-sm bg-gray-50 hover:bg-gray-100 text-gray-600 text-center"
                      onClick={() => handleDelete(order.orderId, order.paymentId)}
                    >
                      삭제
                    </button>
                  ) : (
                    <button
                      className="flex-1 w-full border rounded py-2 text-sm bg-red-50 hover:bg-red-100 text-red-600 text-center"
                      onClick={() => handleCancel(order.orderId, order.paymentId)}
                    >
                      취소
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  )
}
