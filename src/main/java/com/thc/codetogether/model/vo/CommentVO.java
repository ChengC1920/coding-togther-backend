package com.thc.codetogether.model.vo;

import com.thc.codetogether.model.domain.Comment;
import com.thc.codetogether.model.domain.User;
import lombok.Data;

import java.util.List;

/**
 * @author jacky_Tang
 * @version 1.0
 */
@Data
public class CommentVO {

    private Comment comment;

    private User createUser;

    private Long likeCount;

    private Integer likeStatus;

    private List<ReplyVO> replyVoList;

    private Long replyCount;

}
