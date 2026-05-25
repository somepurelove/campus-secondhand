package com.campus.secondhand.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 * 
 * 设计模式应用：
 * 1. 单例模式：Spring的@Service注解确保该类为单例
 * 2. 模板方法模式：继承ServiceImpl，复用基础CRUD模板
 * 3. 依赖倒置：实现IUserService接口，供Controller依赖
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    @Override
    public User getByUsername(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
    }

    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 用户对象
     */
    @Override
    public User getByPhone(String phone) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone));
    }

    /**
     * 注册用户
     * @param user 用户信息
     * @return 是否注册成功
     */
    @Override
    public boolean register(User user) {
        // 设置初始信用分数
        user.setCreditScore(100);
        // 设置默认角色为买家
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("buyer");
        }
        return save(user);
    }

    /**
     * 更新用户信用分数
     * @param userId 用户ID
     * @param score 信用分数
     * @return 是否更新成功
     */
    @Override
    public boolean updateCreditScore(Integer userId, Integer score) {
        User user = new User();
        user.setUserId(userId);
        user.setCreditScore(score);
        return updateById(user);
    }
}
