package net.fpoly.ecommerce.service.impl.order;

import lombok.RequiredArgsConstructor;
import net.fpoly.ecommerce.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OrderExpiryScheduler {
    private static final Logger log = LoggerFactory.getLogger(OrderExpiryScheduler.class);
    private final OrderService orderService;

    @Scheduled(fixedRate = 120000)
    public void expire() {
        orderService.expireWaitingOrders();

    }
}
