/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dto;

import java.util.List;

/**
 *
 * @author e.hussien
 */
public class EmailData {
    private String subject;
    private String sender;
    private List<String> toList;
    private List<String> ccList;
    private String body;
    private List<AttachmentData> attachments;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getToList() {
        return toList;
    }

    public void setToList(List<String> toList) {
        this.toList = toList;
    }

    public List<String> getCcList() {
        return ccList;
    }

    public void setCcList(List<String> ccList) {
        this.ccList = ccList;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<AttachmentData> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentData> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "EmailData{" + "subject=" + subject + ", sender=" + sender + ", toList=" + toList + ", ccList=" + ccList + ", body=" + body + ", attachments=" + attachments + '}';
    }           
}
