package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("products")
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer productId;
    private Integer sellerId;
    private Integer categoryId;
    private String productName;
    private String description;
    private Double price;
    private Integer stock;
    private String status; // pending, approved, rejected, sold, removed
    private Integer views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
