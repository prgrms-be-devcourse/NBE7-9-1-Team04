// src/context/AuthContext.tsx
"use client";

import { fetchApi } from "@/lib/client";
import { createContext, useContext, useState, useEffect, ReactNode } from "react";

// 사용자 정보 타입 정의 (email 필드 사용)
type User = {
  userId: number;
  email: string; // username 대신 email 사용
  level: number;
};

type AuthContextType = {
  user: User | null;
  cartCount: number;
  isLoading: boolean;
  refetch: () => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [cartCount, setCartCount] = useState(0);
  const [isLoading, setIsLoading] = useState(true);

  const fetchAuthStatus = async () => {
    setIsLoading(true); // 로딩 시작
    try {
      const userRes = await fetchApi("/api/users/my", { method: "GET" });
      if (userRes.data) {
        setUser(userRes.data);
        const cartRes = await fetchApi("/api/carts", { method: "GET" });
        setCartCount(cartRes.data.cartItems.length || 0);
      }
    } catch (error) {
      setUser(null);
      setCartCount(0);
    } finally {
      setIsLoading(false); 
    }
  };

  useEffect(() => {
    fetchAuthStatus();
  }, []);

  const value = { user, cartCount, isLoading, refetch: fetchAuthStatus };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}