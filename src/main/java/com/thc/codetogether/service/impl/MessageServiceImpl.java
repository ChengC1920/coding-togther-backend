package com.thc.codetogether.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thc.codetogether.model.domain.Message;
import com.thc.codetogether.mapper.MessageMapper;
import com.thc.codetogether.service.MessageService;
import org.springframework.stereotype.Service;

/**
* @author 86186
* @description 针对表【message】的数据库操作Service实现
* @createDate 2023-06-14 22:16:43
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

}




