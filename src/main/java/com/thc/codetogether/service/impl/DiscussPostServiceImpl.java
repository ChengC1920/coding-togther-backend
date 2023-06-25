package com.thc.codetogether.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thc.codetogether.common.ErrorCode;
import com.thc.codetogether.exception.BusinessException;
import com.thc.codetogether.model.domain.DiscussPost;
import com.thc.codetogether.mapper.DiscussPostMapper;
import com.thc.codetogether.model.domain.User;
import com.thc.codetogether.model.dto.DiscussQuery;
import com.thc.codetogether.model.enums.DiscussPostStatusEnum;
import com.thc.codetogether.model.vo.DiscussUserVO;
import com.thc.codetogether.model.vo.UserVO;
import com.thc.codetogether.service.DiscussPostService;
import com.thc.codetogether.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author 86186
* @description 针对表【discuss_post】的数据库操作Service实现
* @createDate 2023-06-14 22:16:15
*/
@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost>
    implements DiscussPostService{

    @Resource
    private UserService userService;

    @Override
    public long addDiscussPost(DiscussPost discussPost, User loginUser) {
        // 1. 请求参数是否为空？
        if (discussPost == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        final long userId = loginUser.getId();
        // todo 完成校验信息
        // 3. 校验信息
        //   1. 帖子标题
        //   2. 帖子内容
        //   3.

        discussPost.setId(null);
        discussPost.setStatus(0);
        discussPost.setType(0);
        boolean result = save(discussPost);
        Long discussPostId = discussPost.getId();
        if (!result || discussPostId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }

        return discussPostId;
    }

    @Override
    public List<DiscussUserVO> listDiscussPosts(DiscussQuery discussQuery, boolean isAdmin) {
        QueryWrapper<DiscussPost> queryWrapper = new QueryWrapper<>();
        //组合条件查询
        if (discussQuery != null) {
            Long id = discussQuery.getId();
            if (id != null && id > 0) {
                queryWrapper.eq("id", id);
            }
            List<Long> idList = discussQuery.getIdList();
            if (CollectionUtils.isNotEmpty(idList)) {
                queryWrapper.in("id", idList);
            }
            String searchText = discussQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
            }
            String title = discussQuery.getTitle();
            if (StringUtils.isNotBlank(title)) {
                queryWrapper.like("title", title);
            }
            String content = discussQuery.getContent();
            if (StringUtils.isNotBlank(content)) {
                queryWrapper.like("content", content);
            }
            // 根据创建人查询
            Long userId = discussQuery.getUserId();
            if (userId != null && userId > 0) {
                queryWrapper.eq("userId", userId);
            }
            // 根据状态查询
            Integer status = discussQuery.getStatus();
            DiscussPostStatusEnum statusEnum = DiscussPostStatusEnum.getEnumByValue(status);
            if (statusEnum == null) {
                statusEnum = DiscussPostStatusEnum.NORMAL;
            }
            if (!isAdmin && statusEnum.equals(DiscussPostStatusEnum.BLOCK)) {
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            queryWrapper.eq("status", statusEnum.getValue());
        }

        // todo 用户拉黑的不查

        List<DiscussPost> discussPostList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(discussPostList)) {
            return new ArrayList<>();
        }
        List<DiscussUserVO> discussUserVOList = new ArrayList<>();
        // 关联查询创建人的用户信息
        for (DiscussPost discussPost : discussPostList) {
            Long userId = discussPost.getUserId();
            if (userId == null) {
                continue;
            }
            User user = userService.getById(userId);
            DiscussUserVO discussUserVO = new DiscussUserVO();
            BeanUtils.copyProperties(discussPost, discussUserVO);
            // 脱敏用户信息
            if (user != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                discussUserVO.setCreateUser(userVO);
            }
            discussUserVOList.add(discussUserVO);
        }
        return discussUserVOList;
    }
}




