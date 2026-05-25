package com.campus.secondhand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.secondhand.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
    @Select("SELECT * FROM reviews WHERE reviewed_id = #{reviewedId} ORDER BY created_at DESC")
    List<Review> selectByReviewedId(Integer reviewedId);
    
    @Select("SELECT * FROM reviews WHERE order_id = #{orderId}")
    Review selectByOrderId(Integer orderId);
}
