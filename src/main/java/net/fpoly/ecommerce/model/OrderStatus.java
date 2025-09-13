package net.fpoly.ecommerce.model;

public enum OrderStatus {
    CART,       // Đơn hàng đang trong giỏ - chưa xác nhận
    PENDING,    // Đơn hàng đã xác nhận, chờ xử lý (COD)
    WAITING,    // Chờ xác nhận thanh toán (Online)
    EXPIRED,    // Đơn hàng hết hạn
    PAID,       // Đã thanh toán thành công,
    READY_TO_SHIP, // Đơn hàng đã sẵn sàng để giao (chờ lấy hàng)
    SHIPPED,    // Đang giao hàng
    DELIVERED,  // Đã giao hàng thành công
    CANCELLED   // Đã huỷ
}
