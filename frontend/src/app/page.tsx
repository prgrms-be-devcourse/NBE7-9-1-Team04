"use client";

// import { Header } from "@/components/header"
// import { CoffeeCard } from "@/components/coffee-card"
// import { useStore } from "@/lib/store"
// import { Toaster } from "@/components/ui/toaster"

export default function Home() {
  return (
    <div className="min-h-screen bg-background">
      {/* <Header /> */}
      {console.log(process.env.NEXT_PUBLIC_API_BASE_URL)}
      <main className="container py-8">
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold mb-4 text-balance">프리미엄 원두 패키지</h1>
          <p className="text-lg text-muted-foreground text-pretty max-w-2xl mx-auto">
            엄선된 원두로 만든 특별한 커피를 집에서 즐겨보세요. 신선하게 로스팅된 원두를 직접 배송해드립니다.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {/* {packages.map((coffee) => (
            <CoffeeCard key={coffee.id} coffee={coffee} />
          ))} */}
        </div>
      </main>
      {/* <Toaster /> */}
    </div>
  )
}