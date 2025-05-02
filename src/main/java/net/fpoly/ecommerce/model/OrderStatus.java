package net.fpoly.ecommerce.model;

public enum OrderStatus {
    CART,       // Đơn hàng đang trong giỏ - chưa xác nhận
    PENDING,    // Đơn hàng đã xác nhận, chờ xử lý (COD)
    WAITING,    // Chờ xác nhận thanh toán (Online)
    PAID,       // Đã thanh toán thành công
    SHIPPED,    // Đang giao hàng
    DELIVERED,  // Đã giao hàng thành công
    CANCELLED   // Đã huỷ
}
