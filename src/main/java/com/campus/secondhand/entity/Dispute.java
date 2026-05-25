package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("disputes")
public class Dispute {
    @TableId(type = IdType.AUTO)
    private Integer disputeId;
    private Integer orderId;
    private Integer applicantId;
    private String reason;
    private String status; // pending, processing, resolved, rejected
    private Integer auditorId;
    private String resolution;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
