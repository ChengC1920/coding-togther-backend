package com.thc.codetogether.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.thc.codetogether.model.domain.DiscussPost;
import com.thc.codetogether.model.domain.User;
import com.thc.codetogether.model.dto.DiscussQuery;
import com.thc.codetogether.model.vo.DiscussUserVO;

import java.util.List;

/**
* @author 86186
* @description 针对表【discuss_post】的数据库操作Service
* @createDate 2023-06-14 22:16:15
*/
public interface DiscussPostService extends IService<DiscussPost> {

    long addDiscussPost(DiscussPost discussPost, User loginUser);

    List<DiscussUserVO> listDiscussPosts(DiscussQuery discussQuery, boolean isAdmin);
}
