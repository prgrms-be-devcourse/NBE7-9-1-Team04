import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: "https",
        hostname: "i.postimg.cc", // ✅ postimg 도메인 허용
      },
      {
        protocol: "https",
        hostname: "**.public.blob.vercel-storage.com", // 기존 설정 유지
      },
    ],
  },
};

export default nextConfig;
