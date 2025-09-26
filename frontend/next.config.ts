import type { NextConfig } from "next";

/** @type {import('next').NextConfig} */
const nextConfig = {
  // 이 부분을 추가해주세요!
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: '**.public.blob.vercel-storage.com',
      },
      // 만약 다른 도메인을 사용한다면 여기에 추가하면 됩니다.
    ],
  },
};
export default nextConfig;
