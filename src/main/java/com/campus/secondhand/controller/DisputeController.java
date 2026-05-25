package com.campus.secondhand.controller;

import com.campus.secondhand.entity.Dispute;
import com.campus.secondhand.service.IDisputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 纠纷控制器
 * 
 * 设计模式应用：
 * 1. 依赖倒置：依赖IDisputeService接口而非具体实现
 * 2. MVC模式：作为Controller层处理HTTP请求
 */
@RestController
@RequestMapping("/dispute")
public class DisputeController {
    
    private final IDisputeService disputeService;
    
    /**
     * 构造控制器
     * @param disputeService 纠纷服务接口
     */
    @Autowired
    public DisputeController(IDisputeService disputeService) {
        this.disputeService = disputeService;
    }

    /**
     * 提交纠纷
     * @param dispute 纠纷信息
     * @return 提交结果
     */
    @PostMapping("/submit")
    public Map<String, Object> submitDispute(@RequestBody Dispute dispute) {
        Map<String, Object> result = new HashMap<>();
        boolean success = disputeService.submitDispute(dispute);
        if (success) {
            result.put("code", 200);
            result.put("message", "纠纷提交成功");
        } else {
            result.put("code", 500);
            result.put("message", "纠纷提交失败");
        }
        return result;
    }

    /**
     * 处理纠纷
     * @param handleInfo 处理信息
     * @return 处理结果
     */
    @PutMapping("/handle")
    public Map<String, Object> handleDispute(@RequestBody Map<String, Object> handleInfo) {
        Map<String, Object> result = new HashMap<>();
        Integer disputeId = (Integer) handleInfo.get("disputeId");
        Integer auditorId = (Integer) handleInfo.get("auditorId");
        String status = (String) handleInfo.get("status");
        String resolution = (String) handleInfo.get("resolution");
        boolean success = disputeService.handleDispute(disputeId, auditorId, status, resolution);
        if (success) {
            result.put("code", 200);
            result.put("message", "纠纷处理成功");
        } else {
            result.put("code", 500);
            result.put("message", "纠纷处理失败");
        }
        return result;
    }

    /**
     * 获取纠纷详情
     * @param disputeId 纠纷ID
     * @return 纠纷详情
     */
    @GetMapping("/detail/{disputeId}")
    public Map<String, Object> getDisputeDetail(@PathVariable Integer disputeId) {
        Map<String, Object> result = new HashMap<>();
        Dispute dispute = disputeService.getById(disputeId);
        if (dispute != null) {
            result.put("code", 200);
            result.put("dispute", dispute);
        } else {
            result.put("code", 404);
            result.put("message", "纠纷不存在");
        }
        return result;
    }

    /**
     * 按申请人查询纠纷
     * @param applicantId 申请人ID
     * @return 纠纷列表
     */
    @GetMapping("/applicant/{applicantId}")
    public Map<String, Object> getDisputesByApplicant(@PathVariable Integer applicantId) {
        Map<String, Object> result = new HashMap<>();
        List<Dispute> disputes = disputeService.getDisputesByApplicant(applicantId);
        result.put("code", 200);
        result.put("disputes", disputes);
        return result;
    }

    /**
     * 按状态查询纠纷
     * @param status 纠纷状态
     * @return 纠纷列表
     */
    @GetMapping("/status/{status}")
    public Map<String, Object> getDisputesByStatus(@PathVariable String status) {
        Map<String, Object> result = new HashMap<>();
        List<Dispute> disputes = disputeService.getDisputesByStatus(status);
        result.put("code", 200);
        result.put("disputes", disputes);
        return result;
    }

    /**
     * 按订单查询纠纷
     * @param orderId 订单ID
     * @return 纠纷信息
     */
    @GetMapping("/order/{orderId}")
    public Map<String, Object> getDisputeByOrder(@PathVariable Integer orderId) {
        Map<String, Object> result = new HashMap<>();
        Dispute dispute = disputeService.getDisputeByOrder(orderId);
        if (dispute != null) {
            result.put("code", 200);
            result.put("dispute", dispute);
        } else {
            result.put("code", 404);
            result.put("message", "纠纷不存在");
        }
        return result;
    }
}
