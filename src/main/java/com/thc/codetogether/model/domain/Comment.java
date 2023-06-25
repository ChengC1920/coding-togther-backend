package com.thc.codetogether.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 评论的目标的类别 1：帖子 2: 评论 支持回复评论
     */
    private Integer entityType;

    /**
     * 
     */
    private Long entityId;

    /**
     * 记录回复指向的人 (只会发生在回复中 判断target_id==0)
     */
    private Long targetId;

    /**
     * 
     */
    private String content;

    /**
     * 评论的目标的类别 1：帖子 2: 评论 支持回复评论
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}