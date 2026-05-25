package com.campus.secondhand.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.entity.Product;
import com.campus.secondhand.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品服务实现类
 * 
 * 设计模式应用：
 * 1. 单例模式：Spring的@Service注解确保该类为单例
 * 2. 模板方法模式：继承ServiceImpl，复用基础CRUD模板
 * 3. 依赖倒置：实现IProductService接口，供Controller依赖
 * 4. 工厂模式：publishProduct方法可视为简单工厂，创建待审核状态的商品
 */
@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> implements IProductService {
    
    /**
     * 发布商品
     * @param product 商品信息
     * @return 是否发布成功
     */
    @Override
    public boolean publishProduct(Product product) {
        // 设置初始状态为待审核
        product.setStatus("pending");
        product.setViews(0);
        return save(product);
    }

    /**
     * 审核商品
     * @param productId 商品ID
     * @param status 审核状态
     * @return 是否审核成功
     */
    @Override
    public boolean auditProduct(Integer productId, String status) {
        Product product = new Product();
        product.setProductId(productId);
        product.setStatus(status);
        return updateById(product);
    }

    /**
     * 按分类查询商品
     * @param categoryId 分类ID
     * @return 商品列表
     */
    @Override
    public List<Product> getProductsByCategory(Integer categoryId) {
        return baseMapper.selectList(new LambdaQueryWrapper<Product>()
                .eq(Product::getCategoryId, categoryId)
                .eq(Product::getStatus, "approved"));
    }

    /**
     * 按卖家查询商品
     * @param sellerId 卖家ID
     * @return 商品列表
     */
    @Override
    public List<Product> getProductsBySeller(Integer sellerId) {
        return baseMapper.selectList(new LambdaQueryWrapper<Product>()
                .eq(Product::getSellerId, sellerId));
    }

    /**
     * 搜索商品
     * @param keyword 关键词
     * @return 商品列表
     */
    @Override
    public List<Product> searchProducts(String keyword) {
        return baseMapper.selectList(new LambdaQueryWrapper<Product>()
                .like(Product::getProductName, keyword)
                .or()
                .like(Product::getDescription, keyword)
                .eq(Product::getStatus, "approved"));
    }

    /**
     * 增加商品浏览量
     * @param productId 商品ID
     * @return 是否成功
     */
    @Override
    public boolean increaseViews(Integer productId) {
        Product product = getById(productId);
        if (product != null) {
            product.setViews(product.getViews() + 1);
            return updateById(product);
        }
        return false;
    }
}
