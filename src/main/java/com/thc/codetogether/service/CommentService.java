package com.thc.codetogether.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.thc.codetogether.model.domain.Comment;
import com.thc.codetogether.model.domain.User;

import java.util.List;

/**
* @author 86186
* @description 针对表【comment】的数据库操作Service
* @createDate 2023-06-14 22:13:59
*/
public interface CommentService extends IService<Comment> {

    List<Comment> getByEntityType(int entityTypePost, Long id);

    Long getReplyCount(int entityTypeComment, Long id);

    Long addDiscussPost(Comment comment, User loginUser);
}
