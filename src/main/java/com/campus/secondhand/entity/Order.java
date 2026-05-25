package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer orderId;
    private Integer buyerId;
    private Integer sellerId;
    private Integer productId;
    private Integer quantity;
    private Double totalPrice;
    private String orderStatus; // pending, paid, shipped, completed, cancelled, disputed
    private String paymentStatus; // unpaid, paid, refunded
    private String paymentMethod;
    private Integer shippingAddressId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
