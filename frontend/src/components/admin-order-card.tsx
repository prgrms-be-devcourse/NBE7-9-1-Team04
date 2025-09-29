"use client"

import Link from "next/link"
import { AdminOrder } from "@/types/order"

interface Props {
  order: AdminOrder
  onUpdateStatus: (orderId: number, newStatus: string) => void
}

// ✅ 상태별 텍스트 + 색상 클래스 (OrderItemCard와 동일한 border 스타일 적용)
const getStatusInfo = (status: string) => {
  const statusMap: Record<
    string,
    { text: string; className: string }
  > = {
    CREATED: { text: "결제 실패", className: "text-red-600 border-red-600" },
    PAID: { text: "결제 완료", className: "text-blue-600 border-blue-600" },
    COMPLETED: { text: "배송 완료", className: "text-gray-600 border-gray-400" },
    CANCELED: { text: "주문 취소", className: "text-red-600 border-red-600" },
  }
  return statusMap[status] || { text: status, className: "text-gray-600 border-gray-400" }
}

export function AdminOrderCard({ order, onUpdateStatus }: Props) {
  const statusInfo = getStatusInfo(order.status)

  return (
    <div className="bg-white border rounded-lg shadow-sm p-6">
      {/* 상단 */}
      <div className="flex justify-between items-center mb-2">
        <h2 className="font-bold text-lg">주문번호 #{order.orderId}</h2>
        <span
          className={`text-sm border px-2 py-0.5 rounded ${statusInfo.className}`}
        >
          {statusInfo.text}
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
      <ul className="mt-2 text-sm text-gray-700 space-y-2">
        {order.items.map((item, idx) => (
          <li key={idx} className="flex items-center gap-3 border-b pb-2">
            {item.imageUrl && (
              <img
                src={item.imageUrl}
                alt={item.productName}
                className="w-12 h-12 object-cover rounded"
              />
            )}
            <div className="flex-1">
              <span className="font-medium">{item.productName}</span>{" "}
              <span className="text-gray-500">({item.quantity}개)</span>
            </div>
            <span className="font-semibold">
              {item.orderPrice.toLocaleString()}원
            </span>
          </li>
        ))}
      </ul>

      {/* 버튼 */}
      <div className="flex gap-2 mt-4">
        {/* 결제 내역 */}
        {order.paymentId ? (
          <Link href={`/payment/${order.paymentId}`} className="flex-1">
            <button className="w-full border rounded py-2 text-sm bg-blue-50 hover:bg-blue-100 text-center">
              결제 내역 보기
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

        {/* 배송 처리 */}
        <button
          onClick={() => onUpdateStatus(order.orderId, "COMPLETED")}
          disabled={order.status === "COMPLETED"}
          className={`flex-1 border rounded py-2 text-sm text-center
    ${order.status === "COMPLETED"
              ? "bg-gray-200 text-gray-400 cursor-not-allowed"
              : "bg-green-50 hover:bg-green-100"
            }`}
        >
          배송 처리(COMPLETED)
        </button>
      </div>
    </div>
  )
}

export function AdminOrderCardEmptyState() {
  return (
    <div className="text-center py-16">
      <h2 className="text-2xl font-bold mb-2">주문내역이 없습니다</h2>
      <p className="text-gray-500 mb-6">아직 들어온 주문이 없습니다.</p>
    </div>
  )
}
