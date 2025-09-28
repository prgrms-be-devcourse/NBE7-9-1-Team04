package com.backend.global.scheduler;

import com.backend.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderRepository orderRepository;

    /**
     * 매일 한국시간 14:00에 실행
     * 윈도우: [어제 14:00, 오늘 14:00)
     */
    @Scheduled(cron = "0 0 14 * * *", zone = "Asia/Seoul")
    @Transactional
    public void completeDailyOrders() {
        ZoneId KST = ZoneId.of("Asia/Seoul");

        LocalDate today = LocalDate.now(KST);
        LocalDateTime windowEnd   = today.atTime(14, 0);                // 오늘 14:00
        LocalDateTime windowStart = windowEnd.minusDays(1);             // 어제 14:00

        // 취소/완료가 아닌 주문만 윈도우 내에서 COMPLETED로 변경
        int updated = orderRepository.bulkCompleteBetween(windowStart, windowEnd);
    }
}
