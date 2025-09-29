// src/context/ToastContext.tsx
"use client";

import { createContext, useContext, useState, ReactNode } from "react";

interface Toast {
  message: string;
  actionLabel?: string;
  onAction?: () => void;
}

interface ToastContextType {
  showToast: (toast: Toast) => void;
}

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export function ToastProvider({ children }: { children: ReactNode }) {
  const [toast, setToast] = useState<Toast | null>(null);

  const showToast = (newToast: Toast) => {
    setToast(newToast);
    setTimeout(() => setToast(null), 5000); // 3초 뒤 자동 닫힘
  };

  return (
    <ToastContext.Provider value={{ showToast }}>
      {children}

      {/* 토스트 UI */}
      {toast && (
        <div className="fixed bottom-6 left-1/2 -translate-x-1/2 z-[9999]">
          <div className="bg-black text-white px-4 py-3 rounded-lg shadow-lg flex items-center gap-4">
            <span>{toast.message}</span>
            {toast.actionLabel && (
              <button
                onClick={toast.onAction}
                className="bg-white text-black px-3 py-1 rounded text-sm"
              >
                {toast.actionLabel}
              </button>
            )}
          </div>
        </div>
      )}
    </ToastContext.Provider>
  );
}

export function useToast() {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error("useToast must be used within a ToastProvider");
  }
  return context;
}
