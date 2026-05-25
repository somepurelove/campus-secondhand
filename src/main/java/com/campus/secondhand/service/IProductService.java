package com.campus.secondhand.service;

import com.campus.secondhand.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 商品服务接口
 * 
 * 设计模式应用：
 * 1. 接口隔离原则：定义精简的商品相关操作接口
 * 2. 依赖倒置：Controller依赖此接口而非具体实现
 */
public interface IProductService extends IService<Product> {
    
    /**
     * 发布商品
     * @param product 商品信息
     * @return 是否发布成功
     */
    boolean publishProduct(Product product);
    
    /**
     * 审核商品
     * @param productId 商品ID
     * @param status 审核状态
     * @return 是否审核成功
     */
    boolean auditProduct(Integer productId, String status);
    
    /**
     * 根据分类查询商品
     * @param categoryId 分类ID
     * @return 商品列表
     */
    List<Product> getProductsByCategory(Integer categoryId);
    
    /**
     * 根据卖家查询商品
     * @param sellerId 卖家ID
     * @return 商品列表
     */
    List<Product> getProductsBySeller(Integer sellerId);
    
    /**
     * 搜索商品
     * @param keyword 关键词
     * @return 商品列表
     */
    List<Product> searchProducts(String keyword);
    
    /**
     * 增加商品浏览量
     * @param productId 商品ID
     * @return 是否成功
     */
    boolean increaseViews(Integer productId);
}
