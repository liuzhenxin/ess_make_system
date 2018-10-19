package com.clt.ess.entity;

public class Message {
    //消息编码
    private String messageNo;
    //消息发送人
    private String sender;
    //消息接收人
    private String receiver;
    //发送时间
    private String sendTime;
    //消息类型
    private String messageType;
    //消息标题
    private String messageTitle;
    //消息内容
    private String messageContent;
    //操作Id
    private String applyInfoId;
    //阅读状态 0 未读，1已读
    private int readState;
    //状态 0 停用 1 有效
    private int state;

    private User senderUser;

    private User receiverUser;


    public String getMessageNo() {
        return messageNo;
    }

    public void setMessageNo(String messageNo) {
        this.messageNo = messageNo;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getApplyInfoId() {
        return applyInfoId;
    }

    public void setApplyInfoId(String applyInfoId) {
        this.applyInfoId = applyInfoId;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    public User getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(User receiverUser) {
        this.receiverUser = receiverUser;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageNo='" + messageNo + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", messageType='" + messageType + '\'' +
                ", messageTitle='" + messageTitle + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", applyInfoId='" + applyInfoId + '\'' +
                ", readState=" + readState +
                ", state=" + state +
                ", senderUser=" + senderUser +
                ", receiverUser=" + receiverUser +
                '}';
    }
}

