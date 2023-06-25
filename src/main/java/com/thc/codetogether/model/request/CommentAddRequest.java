package com.thc.codetogether.model.request;

import lombok.Data;

import java.util.Date;

/**
 * @author jacky_Tang
 * @version 1.0
 */
@Data
public class CommentAddRequest {

    private Long userId;

    private int entityType;

    private Long entityId;

    private Long targetId;

    private String content;

}
