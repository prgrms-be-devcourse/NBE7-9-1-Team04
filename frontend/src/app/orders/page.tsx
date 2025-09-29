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
        setOrders(res.data) // ApiResponse 구조라면 .data
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
    (a, b) =>
      new Date(b.orderTime).getTime() - new Date(a.orderTime).getTime()
  )

  // ✅ 주문 취소 → status 변경 + 버튼 변경
  const handleCancel = async (orderId: number) => {
    try {
      await fetchApi(`/api/orders/${orderId}/cancel`, {
        method: "PUT",
      })
      alert("주문이 취소되었습니다.")

      setOrders((prev) =>
        prev.map((o) =>
          o.orderId === orderId ? { ...o, status: "CANCELED" } : o
        )
      )
    } catch (err) {
      alert("취소 실패")
    }
  }

  // ✅ 주문 삭제 → API 호출 + UI 제거
  const handleDelete = async (orderId: number) => {
    if (confirm("정말 삭제하시겠습니까?")) {
      try {
        await fetchApi(`/api/orders/${orderId}/delete`, {
          method: "DELETE",
        })

        alert("주문이 삭제되었습니다.")
        setOrders((prev) => prev.filter((o) => o.orderId !== orderId))
      } catch (err) {
        console.error("삭제 실패:", err)
        alert("주문 삭제에 실패했습니다.")
      }
    }
  }

  // status 변환 함수
  const getStatusText = (status: string) => {
    const statusMap: Record<string, string> = {
      CREATED: "결제 실패",
      PAID: "결제 완료",
      COMPLETED: "배송 완료",
      CANCELED: "주문 취소"
    }
    return statusMap[status] || status
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header />

      <main className="max-w-3xl mx-auto py-10 px-4">
        <div className="flex items-center gap-2 mb-6">
          <h1 className="text-2xl font-bold ml-4">주문 내역</h1>
        </div>

        {sortedOrders.length === 0 ? (
          <div className="text-center py-16">
            <h2 className="text-2xl font-bold mb-2">주문 내역이 없습니다</h2>
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
                    className={`text-sm border px-2 py-0.5 rounded ${order.status === "CANCELED"
                      ? "text-red-600 border-red-600"
                      : order.status === "CREATED"
                        ? "text-red-600 border-red-600"
                        : order.status === "COMPLETED"
                          ? "text-gray-600 border-gray-400"
                          : "text-green-600 border-green-600"
                      }`}
                  >
                    {getStatusText(order.status)}
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
                      className="flex items-center gap-4 py-2 border-b">
                      {/* 상품 이미지 */}
                      <img
                        src={item.imageUrl}
                        alt={item.productName}
                        className="w-16 h-16 object-cover rounded"
                      />

                      {/* 상품명 + 수량 */}
                      <div className="flex-1">
                        <p className="font-medium">{item.productName}</p>
                        <p className="text-sm text-gray-500">{item.quantity}개</p>
                      </div>

                      {/* 가격 */}
                      <div className="font-semibold">
                        {item.orderPrice.toLocaleString()}원
                      </div>
                    </li>
                  ))}
                </ul>


                {/* 버튼 영역 */}
                <div className="flex gap-2 mt-4">
                  {order.paymentId ? (
                    <Link href={`/payment/${order.paymentId}`} className="flex-1">
                      <button className="w-full border rounded py-2 text-sm bg-blue-50 hover:bg-blue-100 text-center">
                        결제확인
                      </button>
                    </Link>
                  ) : (
                    <button
                      disabled
                      className="flex-1 w-full border rounded py-2 text-sm bg-gray-200 text-gray-400 cursor-not-allowed"
                    >
                      결제 내역 없음
                    </button>
                  )}

                  {order.status === "CANCELED" ? (
                    <button
                      className="flex-1 w-full border rounded py-2 text-sm bg-gray-50 hover:bg-gray-100 text-gray-600 text-center"
                      onClick={() => handleDelete(order.orderId)}
                    >
                      삭제
                    </button>
                  ) : (
                    <button
                      disabled={order.status === "COMPLETED"}
                      className={`flex-1 w-full border rounded py-2 text-sm text-center
      ${order.status === "COMPLETED"
                          ? "bg-gray-200 text-gray-400 cursor-not-allowed"
                          : "bg-red-50 hover:bg-red-100 text-red-600"
                        }`}
                      onClick={() => handleCancel(order.orderId)}
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
