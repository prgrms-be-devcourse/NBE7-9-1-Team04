"use client"

import { useEffect, useState } from "react"
import { fetchApi } from "@/lib/client"

type OrderItem = {
  productName: string
  quantity: number
  orderPrice: number
}

type AdminOrder = {
  orderId: number
  orderTime: string
  orderAmount: number
  status: string
  userEmail: string
  userPhone: string
  address: string
  items: OrderItem[]
}

export default function AdminOrdersPage() {
  const [orders, setOrders] = useState<AdminOrder[]>([])
  const [loading, setLoading] = useState(true)

  async function loadOrders() {
    try {
      const res = await fetchApi("/api/admin/orders", { method: "GET" })
      setOrders(res.data)
    } catch (err) {
      console.error("주문 목록 조회 실패:", err)
    } finally {
      setLoading(false)
    }
  }

  async function updateStatus(orderId: number, newStatus: string) {
    try {
      await fetchApi(`/api/orders/${orderId}/status`, {
        method: "PUT",
        body: JSON.stringify({ newStatus }),
      })
      alert(`주문 #${orderId} 상태가 ${newStatus}로 변경되었습니다.`)
      loadOrders()
    } catch (err) {
      console.error("상태 변경 실패:", err)
      alert("상태 변경 실패")
    }
  }

  useEffect(() => {
    loadOrders()
  }, [])

  if (loading) return <div className="p-6">관리자 주문 목록 불러오는 중...</div>

  return (
    <div className="min-h-screen bg-gray-50">
      <main className="max-w-4xl mx-auto py-10 px-4">
        <h1 className="text-2xl font-bold mb-6">관리자 주문 관리</h1>

        {orders.length === 0 ? (
          <div className="text-center py-16">
            <h2 className="text-2xl font-bold mb-2">주문내역이 없습니다</h2>
            <p className="text-gray-500 mb-6">아직 들어온 주문이 없습니다.</p>
          </div>
        ) : (
          <div className="space-y-6">
            {orders.map((order) => (
              <div
                key={order.orderId}
                className="bg-white border rounded-lg shadow-sm p-6"
              >
                {/* 상단 */}
                <div className="flex justify-between items-center mb-2">
                  <h2 className="font-bold text-lg">
                    주문번호 #{order.orderId}
                  </h2>
                  <span
                    className={`text-sm px-2 py-0.5 rounded ${
                      order.status === "PAID"
                        ? "bg-blue-100 text-blue-600"
                        : order.status === "COMPLETED"
                        ? "bg-green-100 text-green-600"
                        : "bg-gray-100 text-gray-600"
                    }`}
                  >
                    {order.status}
                  </span>
                </div>

                {/* 메타 정보 */}
                <p className="text-sm text-gray-500 mb-2">
                  주문일시: {new Date(order.orderTime).toLocaleString()}
                </p>
                <p className="font-semibold mb-2">
                  총 금액: {order.orderAmount.toLocaleString()}원
                </p>
                <p className="text-sm text-gray-600">주소: {order.address}</p>
                <p className="text-sm text-gray-600">
                  사용자: {order.userEmail} ({order.userPhone})
                </p>

                {/* 상품 */}
                <ul className="mt-2 text-sm text-gray-700 space-y-1">
                  {order.items.map((item, idx) => (
                    <li key={idx} className="flex justify-between">
                      <span>
                        {item.productName} ({item.quantity}개)
                      </span>
                      <span>{item.orderPrice.toLocaleString()}원</span>
                    </li>
                  ))}
                </ul>

                {/* 버튼 */}
                <div className="flex gap-2 mt-4">
                  <button
                    onClick={() => updateStatus(order.orderId, "PAID")}
                    className="flex-1 border rounded py-2 text-sm bg-blue-50 hover:bg-blue-100"
                  >
                    결제 완료(PAID)
                  </button>
                  <button
                    onClick={() => updateStatus(order.orderId, "COMPLETED")}
                    className="flex-1 border rounded py-2 text-sm bg-green-50 hover:bg-green-100"
                  >
                    배송 처리(COMPLETED)
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  )
}
