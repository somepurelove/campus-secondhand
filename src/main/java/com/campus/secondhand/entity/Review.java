package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("reviews")
public class Review {
    @TableId(type = IdType.AUTO)
    private Integer reviewId;
    private Integer orderId;
    private Integer reviewerId;
    private Integer reviewedId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
