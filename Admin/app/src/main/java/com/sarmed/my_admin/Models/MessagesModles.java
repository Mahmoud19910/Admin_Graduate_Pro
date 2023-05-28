package com.sarmed.my_admin.Models;

public class MessagesModles {
    private String msgId;
    private String messageText;
    private String messagePhoto;
    private String senderName;
    private String reciverId;
    private String senderId;
    private String photoUrl;
    private String msgTime;
    private String documentMessgeId;



    public MessagesModles(String msgId, String messageText, String messagePhoto ,  String senderName, String reciverId, String senderId, String photoUrl, String msgTime ,String documentMessgeId ) {
        this.msgId = msgId;
        this.messageText = messageText;
        this.messagePhoto = messagePhoto;
        this.senderName = senderName;
        this.reciverId = reciverId;
        this.senderId = senderId;
        this.photoUrl = photoUrl;
        this.msgTime = msgTime;
        this.setDocumentMessgeId(documentMessgeId);
    }

    public MessagesModles(){

    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getMessagePhoto() {
        return messagePhoto;
    }

    public void setMessagePhoto(String messagePhoto) {
        this.messagePhoto = messagePhoto;
    }


    public String getDocumentMessgeId() {
        return documentMessgeId;
    }

    public void setDocumentMessgeId(String documentMessgeId) {
        this.documentMessgeId = documentMessgeId;
    }
}
