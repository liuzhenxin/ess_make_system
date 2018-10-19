package com.clt.ess.service.impl;

import com.clt.ess.dao.IMessageDao;

import com.clt.ess.entity.Message;
import com.clt.ess.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private IMessageDao messageDao = null;

    @Override
    public boolean addMessage(Message message_new) {

        int result = messageDao.addMessage(message_new);
        if(result==1){
            return true;
        }
        return false;
    }

    @Override
    public List<Message> findMessage(Message message) {
        return messageDao.findMessage(message);
    }


    @Override
    public boolean updateMessage(Message message) {

        int result = messageDao.updateMessage(message);
        if(result==1){
            return true;
        }
        return false;
    }


    @Override
    public Message findMessageOnly(Message message) {
        return  messageDao.findMessageOnly(message);
    }
}
