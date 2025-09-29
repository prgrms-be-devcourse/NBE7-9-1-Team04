"use client"

import { useEffect, useState } from "react"
import Link from "next/link"
import { fetchApi } from "@/lib/client"
import { Order, OrderItem, ORDER_STATUS } from "@/types/order";
import { OrderItemCard , EmptyOrderState} from "@/components/order-item";


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

  return (
    <div className="min-h-screen bg-gray-50">
      <header />

      <main className="max-w-3xl mx-auto py-10 px-4">
        <div className="flex items-center gap-2 mb-6">
          <h1 className="text-2xl font-bold ml-4">주문 내역</h1>
        </div>

        {sortedOrders.length === 0 ? (
          <EmptyOrderState />
        ) : (
          <div className="space-y-6">
            {sortedOrders.map((order) => (
              <OrderItemCard
                key={order.orderId}
                order={order}
                onCancel={handleCancel}
                onDelete={handleDelete}
              />
            ))}
          </div>
        )}
      </main>
    </div>
  )
}
