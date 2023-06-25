package com.thc.codetogether.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thc.codetogether.common.ErrorCode;
import com.thc.codetogether.constant.PostConstant;
import com.thc.codetogether.exception.BusinessException;
import com.thc.codetogether.mapper.CommentMapper;
import com.thc.codetogether.model.domain.Comment;
import com.thc.codetogether.model.domain.User;
import com.thc.codetogether.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86186
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2023-06-14 22:13:59
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService , PostConstant {

    @Override
    public List<Comment> getByEntityType(int entityTypePost, Long id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entityType", entityTypePost);
        queryWrapper.eq("entityId", id);
        List<Comment> list = list(queryWrapper);
        return list;
    }

    @Override
    public Long getReplyCount(int entityTypeComment, Long id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("entityType", entityTypeComment);
        Long count = baseMapper.selectCount(queryWrapper);

        return count;
    }

    @Override
    public Long addDiscussPost(Comment comment, User loginUser) {
        // 1. 请求参数是否为空？
        if (comment == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        final long userId = loginUser.getId();
        // todo 完成校验信息
        // 3. 校验信息
        //   1. 评论内容

        comment.setId(null);

        boolean result = save(comment);
        Long commentId = comment.getId();
        if (!result || commentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评论失败");
        }

//        if (comment.getStatus() == ENTITY_TYPE_COMMENT) {
//            // todo 更新帖子评论数
//        }

        return commentId;
    }
}




