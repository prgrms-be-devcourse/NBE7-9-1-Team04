// src/types/menu.ts

export interface Menu {
    menuId?: number;
    name: string;
    price: number;
    isSoldOut: boolean;
    description: string;
    imageUrl: string;
  }  