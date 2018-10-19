package com.clt.ess.service;

import com.clt.ess.entity.Message;

import java.util.List;

public interface IMessageService {
    boolean addMessage(Message message_new);

    List<Message> findMessage(Message message);

    boolean updateMessage(Message message);

    Message findMessageOnly(Message message);
}
