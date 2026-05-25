package com.campus.secondhand.service;

import com.campus.secondhand.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户服务接口
 * 
 * 设计模式应用：
 * 1. 接口隔离原则：定义精简的用户相关操作接口
 * 2. 依赖倒置：Controller依赖此接口而非具体实现
 */
public interface IUserService extends IService<User> {
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User getByUsername(String username);
    
    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户对象
     */
    User getByPhone(String phone);
    
    /**
     * 用户注册
     * @param user 用户信息
     * @return 是否注册成功
     */
    boolean register(User user);
    
    /**
     * 更新用户信用分数
     * @param userId 用户ID
     * @param score 信用分数
     * @return 是否更新成功
     */
    boolean updateCreditScore(Integer userId, Integer score);
}
