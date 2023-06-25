package com.thc.codetogether.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thc.codetogether.common.BaseResponse;
import com.thc.codetogether.common.ErrorCode;
import com.thc.codetogether.common.ResultUtils;
import com.thc.codetogether.constant.PostConstant;
import com.thc.codetogether.exception.BusinessException;
import com.thc.codetogether.model.domain.Comment;
import com.thc.codetogether.model.domain.DiscussPost;
import com.thc.codetogether.model.domain.Team;
import com.thc.codetogether.model.domain.User;
import com.thc.codetogether.model.dto.DiscussQuery;
import com.thc.codetogether.model.dto.TeamQuery;
import com.thc.codetogether.model.request.DiscussPostAddRequest;
import com.thc.codetogether.model.request.TeamAddRequest;
import com.thc.codetogether.model.vo.*;
import com.thc.codetogether.service.CommentService;
import com.thc.codetogether.service.DiscussPostService;
import com.thc.codetogether.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jacky_Tang
 * @version 1.0
 */
@RestController
@RequestMapping("/discuss")
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
@Slf4j
public class DiscussPostController implements PostConstant {

    @Resource
    private DiscussPostService discussPostService;

    @Resource
    private UserService userService;

    @Resource
    private CommentService commentService;

    @PostMapping("/add")
    public BaseResponse<Long> addDiscussPost(@RequestBody DiscussPostAddRequest discussPostAddRequest, HttpServletRequest request) {
        if (discussPostAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        DiscussPost discussPost = new DiscussPost();
        BeanUtils.copyProperties(discussPostAddRequest, discussPost);
        long discussPostId = discussPostService.addDiscussPost(discussPost, loginUser);
        return ResultUtils.success(discussPostId);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<DiscussPost>> listDiscussPostsByPage(DiscussQuery discussQuery) {
        if (discussQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DiscussPost discussPost = new DiscussPost();
        BeanUtils.copyProperties(discussQuery, discussPost);
        Page<DiscussPost> page = new Page<>(discussQuery.getPageNum(), discussQuery.getPageSize());
        QueryWrapper<DiscussPost> queryWrapper = new QueryWrapper<>(discussPost);
        Page<DiscussPost> resultPage = discussPostService.page(page, queryWrapper);
        return ResultUtils.success(resultPage);
    }

    @GetMapping("/list")
    public BaseResponse<List<DiscussUserVO>> listDiscussPosts(DiscussQuery discussQuery, HttpServletRequest request) {
        if (discussQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        // 1、查询帖子列表
        List<DiscussUserVO> discussUserVOList = discussPostService.listDiscussPosts(discussQuery, isAdmin);
        return ResultUtils.success(discussUserVOList);
    }

    // todo 帖子详情
    @GetMapping("/detail/{discussPostId}")
    public BaseResponse<DiscussPostDetailVO> getDiscussPostDetail(@PathVariable("discussPostId") Integer discussPostId) {
        if (discussPostId == null || discussPostId < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DiscussPost discussPost = discussPostService.getById(discussPostId);
        if (discussPost == null) {
            throw new BusinessException(ErrorCode.POST_NOT_EXIST);
        }
        // 获取作者信息
        User user = userService.getById(discussPost.getUserId());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        // todo 获取点赞数量和当前用户是否点赞

        // 获取评论列表
        List<Comment> commentList = commentService.getByEntityType(ENTITY_TYPE_POST, discussPost.getId());

        List<CommentVO> commentVOLiST = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                CommentVO commentVO = new CommentVO();
                commentVO.setComment(comment);
                User commentUser = userService.getById(comment.getUserId());
                commentVO.setCreateUser(commentUser);

                // todo 获取点赞数量和当前用户是否点赞评论

                // 获取该评论的回复列表
                List<Comment> replyList = commentService.getByEntityType(ENTITY_TYPE_COMMENT, comment.getId());

                List<ReplyVO> replyVOList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        ReplyVO replyVO = new ReplyVO();
                        replyVO.setReply(reply);
                        User replyUser = userService.getById(reply.getUserId());
                        replyVO.setCreateUser(replyUser);
                        replyVO.setTargetId(comment.getUserId());

                        // todo 获取点赞数量和当前用户是否点赞回复

                        replyVOList.add(replyVO);
                    }
                }
                commentVO.setReplyVoList(replyVOList);
                // 设置回复数量
                long replyCount = commentService.getReplyCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVO.setReplyCount(replyCount);

                commentVOLiST.add(commentVO);
            }
        }

        DiscussPostDetailVO discussPostDetailVO = new DiscussPostDetailVO();
        discussPostDetailVO.setPost(discussPost);
        discussPostDetailVO.setCreateUser(userVO);
        discussPostDetailVO.setCommentVoList(commentVOLiST);
        return ResultUtils.success(discussPostDetailVO);
    }
}
