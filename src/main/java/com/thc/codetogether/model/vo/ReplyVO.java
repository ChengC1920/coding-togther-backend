package com.thc.codetogether.model.vo;

import com.thc.codetogether.model.domain.Comment;
import com.thc.codetogether.model.domain.User;
import lombok.Data;

/**
 * @author jacky_Tang
 * @version 1.0
 */
@Data
public class ReplyVO {

    private Comment reply;

    private Long targetId;

    private User createUser;

    private Long likeCount;

    private Integer likeStatus;



}
