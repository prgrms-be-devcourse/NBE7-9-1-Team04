export function fetchApi(url: string, options?: RequestInit) {
  if (options?.body) {
    const headers = new Headers(options.headers || {});
    headers.set("Content-Type", "application/json");
    options.headers = headers;
  }

  return fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}${url}`, {
    ...options,
    credentials: "include", // 세션 쿠키도 전송
  }).then(async (res) => {
    if (!res.ok) { // 200~299가 아닌 경우
      let rsData: any = {};
      try {
        rsData = await res.json();
      } catch {
        // JSON이 아니어도 에러는 던지게
      }
      throw new Error(rsData.msg || "API 요청 실패");
    }
    return res.json();
  });
}