// src/types/cart.ts

export type CartItemType = {
  cartId: number;
  menuId: number;
  name: string;
  imageUrl: string;
  price: number;
  quantity: number;
  orderAmount: number; // price * quantity
};

export type CartListType = {
  cartItems: CartItemType[];
  grandTotal: number;
};