"use client"

import { useParams } from "next/navigation"
import { useEffect, useState } from "react"
import Link from "next/link"
import { fetchApi } from "@/lib/client"

type OrderItem = {
  productName: string
  quantity: number
  orderPrice: number
}

type OrderDetail = {
  orderId: number
  orderTime: string
  orderAmount: number
  status: string
  address: string
  items: OrderItem[]
}

export default function OrderCompletePage() {
  const { orderId } = useParams()
  const [order, setOrder] = useState<OrderDetail | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    async function updateAndLoadOrder() {
      try {
        // 1. 상태 업데이트 (PAID)
        await fetchApi(`/api/orders/${orderId}/status`, {
          method: "PUT",
          body: JSON.stringify({ newStatus: "PAID" }),
        })

        // 2. 주문 상세 조회
        const res = await fetchApi(`/api/orders/${orderId}`, { method: "GET" })
        setOrder(res.data)
      } catch (err) {
        console.error("주문 처리 실패:", err)
      } finally {
        setLoading(false)
      }
    }

    if (orderId) updateAndLoadOrder()
  }, [orderId])

  if (loading) return <div className="p-6">주문 정보를 불러오는 중...</div>
  if (!order) return <div className="p-6">주문 정보를 찾을 수 없습니다.</div>

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center py-12 px-4">
      {/* 상단 메시지 */}
      <div className="text-center mb-8">
        <div className="text-6xl mb-4">✅</div>
        <h1 className="text-2xl font-bold mb-2">주문이 완료되었습니다!</h1>
        <p className="text-gray-600">
          주문해주셔서 감사합니다. 신선한 원두를 정성껏 준비하여 배송해드리겠습니다.
        </p>
      </div>

      {/* 주문 정보 카드 */}
      <div className="bg-white border rounded-lg shadow-sm p-6 w-full max-w-2xl">
        <h2 className="text-lg font-semibold mb-4">📦 주문 정보</h2>
        <div className="grid grid-cols-2 text-sm text-gray-700 gap-y-2 mb-4">
          <p>주문번호</p>
          <p className="text-right">#{order.orderId}</p>
          <p>주문일시</p>
          <p className="text-right">
            {new Date(order.orderTime).toLocaleString()}
          </p>
          <p>결제금액</p>
          <p className="text-right">{order.orderAmount.toLocaleString()}원</p>
          <p>주문상태</p>
          <p className="text-right text-green-600 font-semibold">
            {order.status}
          </p>
        </div>

        <div className="border-t pt-4 text-sm">
          <p className="font-medium mb-1">배송주소</p>
          <p className="text-gray-700">{order.address}</p>
        </div>

        <div className="border-t pt-4 text-sm">
          <p className="font-medium mb-1">주문상품</p>
          <ul className="space-y-1">
            {order.items.map((item, idx) => (
              <li key={idx} className="flex justify-between">
                <span>
                  {item.productName} x {item.quantity}
                </span>
                <span>{item.orderPrice.toLocaleString()}원</span>
              </li>
            ))}
          </ul>
        </div>
      </div>

      {/* 배송 안내 */}
      <div className="w-full max-w-2xl mt-6 p-6 bg-white border rounded-lg shadow-sm text-sm text-gray-600">
        <h3 className="font-semibold mb-2">🚚 배송 안내</h3>
        <ul className="list-disc pl-5 space-y-1">
          <li>주문 확인 후 1~2일 내 배송이 시작됩니다.</li>
          <li>배송 시작 시 SMS로 안내드립니다.</li>
          <li>원두는 주문 후 로스팅하여 신선하게 배송됩니다.</li>
          <li>배송 조회는 주문내역에서 확인 가능합니다.</li>
        </ul>
      </div>

      {/* 버튼 */}
      <div className="flex gap-4 mt-8">
        <Link href="/orders">
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
