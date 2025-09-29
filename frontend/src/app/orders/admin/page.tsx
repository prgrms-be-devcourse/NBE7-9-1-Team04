"use client"

import { useEffect, useState } from "react"
import { fetchApi } from "@/lib/client"
import { OrderItem,AdminOrder } from "@/types/order"
import { AdminOrderCardEmptyState } from "@/components/admin-order-card";
import { AdminOrderCard } from "@/components/admin-order-card";

export default function AdminOrdersPage() {
  const [orders, setOrders] = useState<AdminOrder[]>([])
  const [loading, setLoading] = useState(true)

  async function loadOrders() {
    try {
      const res = await fetchApi("/api/admin/orders", { method: "GET" })
      setOrders(res.data as AdminOrder[])
      console.log("주문 목록:", res.data)
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
          <AdminOrderCardEmptyState />
        ) : (
          <div className="space-y-6">
            {orders.map((order) => (
              <AdminOrderCard
                key={order.orderId}
                order={order}
                onUpdateStatus={updateStatus}
              />
            ))}
          </div>
        )}
      </main>
    </div>
  )
}
