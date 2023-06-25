package com.thc.codetogether.model.vo;

import com.thc.codetogether.model.domain.DiscussPost;
import com.thc.codetogether.model.domain.User;
import lombok.Data;

import java.util.List;

/**
 * @author jacky_Tang
 * @version 1.0
 */
@Data
public class DiscussPostDetailVO {

    private DiscussPost post;

    /**
     * 创建人用户信息
     */
    private UserVO createUser;

    private Long likeCount;

    private Integer likeStatus;

    private List<CommentVO> commentVoList;

}

