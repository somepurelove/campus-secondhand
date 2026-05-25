package com.campus.secondhand.controller;

import com.campus.secondhand.entity.User;
import com.campus.secondhand.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * 
 * 设计模式应用：
 * 1. 依赖倒置：依赖IUserService接口而非具体实现
 * 2. MVC模式：作为Controller层处理HTTP请求
 */
@RestController
@RequestMapping("/user")
public class UserController {
    
    private final IUserService userService;
    
    /**
     * 构造控制器
     * @param userService 用户服务接口
     */
    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        
        // 参数验证
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            result.put("code", 400);
            result.put("message", "用户名不能为空");
            return result;
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            result.put("code", 400);
            result.put("message", "密码不能为空");
            return result;
        }
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            result.put("code", 400);
            result.put("message", "手机号不能为空");
            return result;
        }
        
        // 检查用户名是否已存在
        if (userService.getByUsername(user.getUsername()) != null) {
            result.put("code", 400);
            result.put("message", "用户名已存在");
            return result;
        }
        // 检查手机号是否已存在
        if (userService.getByPhone(user.getPhone()) != null) {
            result.put("code", 400);
            result.put("message", "手机号已被注册");
            return result;
        }
        // 注册用户
        boolean success = userService.register(user);
        if (success) {
            result.put("code", 200);
            result.put("message", "注册成功");
        } else {
            result.put("code", 500);
            result.put("message", "注册失败");
        }
        return result;
    }

    /**
     * 用户登录
     * @param loginInfo 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginInfo) {
        Map<String, Object> result = new HashMap<>();
        String username = loginInfo.get("username");
        String password = loginInfo.get("password");
        // 查询用户
        User user = userService.getByUsername(username);
        if (user == null) {
            result.put("code", 400);
            result.put("message", "用户名不存在");
            return result;
        }
        // 验证密码（实际应使用加密密码）
        if (!user.getPassword().equals(password)) {
            result.put("code", 400);
            result.put("message", "密码错误");
            return result;
        }
        result.put("code", 200);
        result.put("message", "登录成功");
        result.put("user", user);
        return result;
    }

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/info/{userId}")
    public Map<String, Object> getUserInfo(@PathVariable Integer userId) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.getById(userId);
        if (user != null) {
            result.put("code", 200);
            result.put("user", user);
        } else {
            result.put("code", 404);
            result.put("message", "用户不存在");
        }
        return result;
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新结果
     */
    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        boolean success = userService.updateById(user);
        if (success) {
            result.put("code", 200);
            result.put("message", "更新成功");
        } else {
            result.put("code", 500);
            result.put("message", "更新失败");
        }
        return result;
    }
}
