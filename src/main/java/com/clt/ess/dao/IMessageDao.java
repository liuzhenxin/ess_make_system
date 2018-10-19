package com.clt.ess.dao;


import com.clt.ess.entity.Message;

import java.util.List;

public interface IMessageDao {
    int addMessage(Message message);
    int updateMessage(Message message);


    List<Message> findMessage(Message message);

    Message findMessageOnly(Message message);
}
