package com.campus.secondhand.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String username;
    private String password;
    private String role; // seller, buyer, auditor
    private String realName;
    private String studentId;
    private String phone;
    private String email;
    private Integer creditScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
