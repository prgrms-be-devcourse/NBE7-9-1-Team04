"use client"

import Link from "next/link"
import { Order, ORDER_STATUS, OrderItemCardProps } from "@/types/order"

// ✅ 상태별 텍스트 + 색상 클래스 (AdminOrderCard 기준으로 통일)
const getStatusInfo = (status: string) => {
  const statusMap: Record<string, { text: string; className: string }> = {
    CREATED: { text: "결제 실패", className: "bg-red-50 text-red-600" },
    PAID: { text: "결제 완료", className: "bg-blue-50 text-blue-600" },
    COMPLETED: { text: "배송 완료", className: "bg-gray-200 text-gray-600" },
    CANCELED: { text: "주문 취소", className: "bg-red-50 text-red-600" },
  }
  return statusMap[status] || { text: status, className: "bg-gray-200 text-gray-600" }
}

export function OrderItemCard({ order, onCancel, onDelete }: OrderItemCardProps) {
  const statusInfo = getStatusInfo(order.status)

  return (
    <div className="bg-white border rounded-lg shadow-sm p-6">
      {/* 상단 */}
      <div className="flex justify-between items-center mb-2">
        <h2 className="font-bold text-lg">주문번호 #{order.orderId}</h2>
        <span className={`text-sm px-2 py-0.5 rounded ${statusInfo.className}`}>
          {statusInfo.text}
        </span>
      </div>

      <p className="text-sm text-gray-500 mb-2">
        주문일시: {new Date(order.orderTime).toLocaleString()}
      </p>
      <p className="font-semibold mb-2">
        총 금액: {order.orderAmount.toLocaleString()}원
      </p>
      <p className="text-sm text-gray-600 mb-4">배송주소: {order.address}</p>

      {/* 상품 리스트 */}
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
              <p className="text-sm text-gray-500">{item.quantity}개</p>
            </div>
            <div className="font-semibold">{item.orderPrice.toLocaleString()}원</div>
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

        {order.status === ORDER_STATUS.CANCELED ? (
          <button
            className="flex-1 w-full border rounded py-2 text-sm bg-gray-50 hover:bg-gray-100 text-gray-600 text-center"
            onClick={() => onDelete(order.orderId)}
          >
            삭제
          </button>
        ) : (
          <button
            disabled={order.status === ORDER_STATUS.COMPLETED}
            className={`flex-1 w-full border rounded py-2 text-sm text-center
              ${
                order.status === ORDER_STATUS.COMPLETED
                  ? "bg-gray-200 text-gray-400 cursor-not-allowed"
                  : "bg-red-50 hover:bg-red-100 text-red-600"
              }`}
            onClick={() => onCancel(order.orderId)}
          >
            취소
          </button>
        )}
      </div>
    </div>
  )
}

export function EmptyOrderState() {
  return (
    <div className="text-center py-16">
      <h2 className="text-2xl font-bold mb-2">주문 내역이 없습니다</h2>
      <p className="text-gray-500 mb-6">첫 주문을 시작해보세요</p>
      <Link href="/">
        <button className="px-4 py-2 border rounded bg-black text-white">
          원두 둘러보기
        </button>
      </Link>
    </div>
  )
}
