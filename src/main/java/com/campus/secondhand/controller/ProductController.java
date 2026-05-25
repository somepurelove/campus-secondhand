package com.campus.secondhand.controller;

import com.campus.secondhand.entity.Product;
import com.campus.secondhand.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品控制器
 * 
 * 设计模式应用：
 * 1. 依赖倒置：依赖IProductService接口而非具体实现
 * 2. MVC模式：作为Controller层处理HTTP请求
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    
    private final IProductService productService;
    
    /**
     * 构造控制器
     * @param productService 商品服务接口
     */
    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    /**
     * 发布商品
     * @param product 商品信息
     * @return 发布结果
     */
    @PostMapping("/publish")
    public Map<String, Object> publishProduct(@RequestBody Product product) {
        Map<String, Object> result = new HashMap<>();
        boolean success = productService.publishProduct(product);
        if (success) {
            result.put("code", 200);
            result.put("message", "发布成功，等待审核");
        } else {
            result.put("code", 500);
            result.put("message", "发布失败");
        }
        return result;
    }

    /**
     * 审核商品
     * @param auditInfo 审核信息
     * @return 审核结果
     */
    @PutMapping("/audit")
    public Map<String, Object> auditProduct(@RequestBody Map<String, Object> auditInfo) {
        Map<String, Object> result = new HashMap<>();
        Integer productId = (Integer) auditInfo.get("productId");
        String status = (String) auditInfo.get("status");
        boolean success = productService.auditProduct(productId, status);
        if (success) {
            result.put("code", 200);
            result.put("message", "审核成功");
        } else {
            result.put("code", 500);
            result.put("message", "审核失败");
        }
        return result;
    }

    /**
     * 获取商品详情
     * @param productId 商品ID
     * @return 商品详情
     */
    @GetMapping("/detail/{productId}")
    public Map<String, Object> getProductDetail(@PathVariable Integer productId) {
        Map<String, Object> result = new HashMap<>();
        // 增加浏览量
        productService.increaseViews(productId);
        Product product = productService.getById(productId);
        if (product != null) {
            result.put("code", 200);
            result.put("product", product);
        } else {
            result.put("code", 404);
            result.put("message", "商品不存在");
        }
        return result;
    }

    /**
     * 按分类查询商品
     * @param categoryId 分类ID
     * @return 商品列表
     */
    @GetMapping("/category/{categoryId}")
    public Map<String, Object> getProductsByCategory(@PathVariable Integer categoryId) {
        Map<String, Object> result = new HashMap<>();
        List<Product> products = productService.getProductsByCategory(categoryId);
        result.put("code", 200);
        result.put("products", products);
        return result;
    }

    /**
     * 按卖家查询商品
     * @param sellerId 卖家ID
     * @return 商品列表
     */
    @GetMapping("/seller/{sellerId}")
    public Map<String, Object> getProductsBySeller(@PathVariable Integer sellerId) {
        Map<String, Object> result = new HashMap<>();
        List<Product> products = productService.getProductsBySeller(sellerId);
        result.put("code", 200);
        result.put("products", products);
        return result;
    }

    /**
     * 搜索商品
     * @param keyword 关键词
     * @return 商品列表
     */
    @GetMapping("/search")
    public Map<String, Object> searchProducts(@RequestParam String keyword) {
        Map<String, Object> result = new HashMap<>();
        List<Product> products = productService.searchProducts(keyword);
        result.put("code", 200);
        result.put("products", products);
        return result;
    }

    /**
     * 更新商品信息
     * @param product 商品信息
     * @return 更新结果
     */
    @PutMapping("/update")
    public Map<String, Object> updateProduct(@RequestBody Product product) {
        Map<String, Object> result = new HashMap<>();
        boolean success = productService.updateById(product);
        if (success) {
            result.put("code", 200);
            result.put("message", "更新成功");
        } else {
            result.put("code", 500);
            result.put("message", "更新失败");
        }
        return result;
    }

    /**
     * 删除商品
     * @param productId 商品ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{productId}")
    public Map<String, Object> deleteProduct(@PathVariable Integer productId) {
        Map<String, Object> result = new HashMap<>();
        boolean success = productService.removeById(productId);
        if (success) {
            result.put("code", 200);
            result.put("message", "删除成功");
        } else {
            result.put("code", 500);
            result.put("message", "删除失败");
        }
        return result;
    }
}
