"use client"

import { useAuth } from "@/context/AuthContext";
import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"
import { fetchApi } from "@/lib/client"
import Link from "next/link"
import AuthGuard from "@/components/auth/AuthGuard";

type Address = {
  addressId: number
  userId: number
  address: string
  addressDetail: string
  postNumber: string
}

type User = {
  userId: number
  userEmail: string
  phoneNumber: string
}

type CartItem = {
  cartId: number
  menuId: number
  name: string
  imageUrl: string
  price: number
  quantity: number
  orderAmount: number
}

type CartResponse = {
  cartItems: CartItem[]
  grandTotal: number
}

// ✅ 변경점: AuthGuard 사용하여 로그인 상태 처리
export default function CartPage() {
  return (
    <AuthGuard>
      <CheckoutPage />
    </AuthGuard>
  );
}

function CheckoutPage() {
  const router = useRouter()

  const [addresses, setAddresses] = useState<Address[]>([])
  const [selectedId, setSelectedId] = useState<number | null>(null)
  const [user, setUser] = useState<User | null>(null)
  const [cart, setCart] = useState<CartResponse | null>(null)
  const { refetch } = useAuth();
  
  // 초기 데이터 불러오기
  useEffect(() => {
    async function loadData() {
      try {
        const addrRes = await fetchApi("/api/users/address/list", { method: "GET" })
        setAddresses(addrRes.data)
        if (addrRes.data.length > 0) setSelectedId(addrRes.data[0].addressId)

        const userRes = await fetchApi("/api/users/my", { method: "GET" })
        setUser(userRes.data)

        const cartRes = await fetchApi("/api/carts", { method: "GET" })
        setCart(cartRes.data)
      } catch (err) {
        console.error("데이터 불러오기 실패:", err)
      }
    }
    loadData()
  }, [])

  // 주소 변경
  const handleAddressChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedId(Number(e.target.value))
  }

  // 주문 생성
  const handleOrder = async () => {
    if (selectedId === null) {
      alert("배송지를 선택해주세요.")
      return
    }
    if (!cart || cart.cartItems.length === 0) {
      alert("장바구니가 비어있습니다.")
      return
    }

    try {
      const shippingFee = cart.grandTotal < 50000 ? 3000 : 0
      const totalAmount = cart.grandTotal + shippingFee

      const items = cart.cartItems.map((c) => ({
        productId: c.menuId,
        menuName: c.name,
        quantity: c.quantity,
        orderPrice: c.orderAmount,
      }))

      const orderRes = await fetchApi("/api/orders", {
        method: "POST",
        body: JSON.stringify({
          amount: totalAmount,
          addressId: selectedId,
          items,
        }),
      })

      const orderId = orderRes.data.orderId

      await fetchApi("/api/payments/create", {
        method: "POST",
        body: JSON.stringify({
          orderId,
          paymentAmount: totalAmount,
          paymentMethod: "CARD",
        }),
      })

      router.push(`/payment/success?orderId=${orderId}`)
      await refetch();
    } catch (err) {
      console.error("주문/결제 실패:", err)
      router.push(`/payment/fail`)
    }
  }

  const shippingFee = cart && cart.grandTotal < 50000 ? 3000 : 0
  const productTotal = cart?.grandTotal ?? 0
  const totalAmount = productTotal + shippingFee

  return (
    <div className="min-h-screen bg-gray-50">
      <main className="max-w-5xl mx-auto py-10 px-6 flex gap-8">
        {/* 배송 정보 */}
        <div className="flex-1 bg-white border rounded-lg p-6 space-y-6">
          <h2 className="text-xl font-bold mb-4">배송 정보</h2>

          {addresses.length > 0 ? (
            <div className="mb-4">
              <label className="block text-sm mb-1">배송지 선택</label>
              <select
                value={selectedId ?? ""}
                onChange={handleAddressChange}
                className="w-full border rounded px-3 py-2"
              >
                {addresses.map((addr) => (
                  <option key={addr.addressId} value={addr.addressId}>
                    [{addr.postNumber}] {addr.address} {addr.addressDetail}
                  </option>
                ))}
              </select>
            </div>
          ) : (
            <div className="bg-red-50 border border-red-200 rounded-lg p-6 mb-8">
            <h3 className="font-semibold mb-2 text-red-800">주소 등록 안내</h3>
            <ul className="text-sm text-red-700 space-y-1 text-left">
              <li>• 마이페이지에서 주소를 등록해 주세요</li>
              <Link href="/user/address/add">
                <button className="px-6 py-3 bg-black text-white rounded hover:bg-gray-800 flex items-center gap-2">
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
                  </svg>
                  주소 등록하러 가기
                </button>
              </Link>
            </ul>
          </div>
          )}

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm mb-1">받는 분 이메일 *</label>
              <input
                type="text"
                value={user?.userEmail ?? ""}
                className="w-full border rounded px-3 py-2"
                readOnly
              />
            </div>
            <div>
              <label className="block text-sm mb-1">연락처 *</label>
              <input
                type="text"
                value={user?.phoneNumber ?? ""}
                className="w-full border rounded px-3 py-2"
                readOnly
              />
            </div>
          </div>
        </div>

        {/* 결제 요약 */}
        <div className="w-80 space-y-6">
          <div className="bg-white border rounded-lg p-6 space-y-3">
            <h2 className="text-lg font-bold">결제 정보</h2>

            {cart?.cartItems.map((item) => (
              <div key={item.cartId} className="flex gap-3 items-center border-b pb-2">
                <img
                  src={item.imageUrl}
                  alt={item.name}
                  className="w-12 h-12 object-cover rounded"
                />
                <div className="flex-1">
                  <p className="text-sm font-medium">{item.name}</p>
                  <p className="text-xs text-gray-500">수량: {item.quantity}개</p>
                </div>
                <span className="text-sm font-semibold">{item.orderAmount}원</span>
              </div>
            ))}

            {/* 상품 금액, 배송비, 총 결제 금액 */}
            <div className="space-y-1">
              <div className="flex justify-between text-sm">
                <span>상품 금액</span>
                <span>{productTotal.toLocaleString()}원</span>
              </div>
              <div className="flex justify-between text-sm">
                <span>배송비</span>
                <span>{shippingFee === 0 ? "무료" : `${shippingFee.toLocaleString()}원`}</span>
              </div>
              <div className="border-t pt-3 flex justify-between font-bold">
                <span>총 결제 금액</span>
                <span>{totalAmount.toLocaleString()}원</span>
              </div>
            </div>

            <button
              onClick={handleOrder}
              className="w-full py-3 mt-4 rounded bg-black text-white font-semibold hover:bg-gray-800"
            >
              {totalAmount.toLocaleString()}원 결제하기
            </button>
          </div>
        </div>
      </main>
    </div>
  )
}
