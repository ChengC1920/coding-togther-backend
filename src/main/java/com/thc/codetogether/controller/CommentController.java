package com.thc.codetogether.controller;

import com.thc.codetogether.common.BaseResponse;
import com.thc.codetogether.common.ErrorCode;
import com.thc.codetogether.common.ResultUtils;
import com.thc.codetogether.exception.BusinessException;
import com.thc.codetogether.model.domain.Comment;
import com.thc.codetogether.model.domain.DiscussPost;
import com.thc.codetogether.model.domain.User;
import com.thc.codetogether.model.request.CommentAddRequest;
import com.thc.codetogether.service.CommentService;
import com.thc.codetogether.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author jacky_Tang
 * @version 1.0
 */
@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
@Slf4j
public class CommentController {

    @Resource
    private UserService userService;

    @Resource
    private CommentService commentService;

    @PostMapping("/add/{discussPostId}")
    public BaseResponse<Long> addComment(@PathVariable("discussPostId") Long discussPostId,@RequestBody CommentAddRequest commentAddRequest, HttpServletRequest request) {

        if (commentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentAddRequest, comment);
        long commentId = commentService.addDiscussPost(comment, loginUser);
        return ResultUtils.success(commentId);

    }

}
