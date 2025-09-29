export type OrderItem = {
  productName: string
  quantity: number
  orderPrice: number
  imageUrl?: string
}

export type Order = {
  orderId: number
  orderTime: string
  orderAmount: number
  status: string
  address: string
  paymentId?: number
  items: OrderItem[]
}


export type AdminOrder = {
  orderId: number
  orderTime: string
  orderAmount: number
  status: string
  userEmail: string
  userPhone: string
  address: string
  paymentId?: number
  items: OrderItem[]
}

export const ORDER_STATUS = {
  CREATED: "CREATED",
  PAID: "PAID",
  COMPLETED: "COMPLETED",
  CANCELED: "CANCELED",
} as const

export type OrderItemCardProps = {
  order: Order
  onCancel: (orderId: number) => void
  onDelete: (orderId: number) => void
}

export type OrderStatus = typeof ORDER_STATUS[keyof typeof ORDER_STATUS]
