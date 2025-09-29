// src/app/layout.tsx

import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { Header } from "@/components/header"; 
import { AuthProvider } from "@/context/AuthContext";
import { ToastProvider } from "@/context/ToastContext";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "카페 원두",
  description: "신선한 원두를 집 앞으로",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ko">
      <body className={inter.className}>
        <AuthProvider>
          <ToastProvider>
            <Header />
            <main>{children}</main>
          </ToastProvider>
        </AuthProvider>
      </body>
    </html>
  );
}