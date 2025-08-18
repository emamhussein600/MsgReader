/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bass.cms.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author e.hussien
 */
public class EmailData {

    private String subject;
    private String sender;
    private List<String> toList = new ArrayList<>();
    private List<String> ccList = new ArrayList<>();
    private String body;
    private List<AttachmentData> attachments = new ArrayList<>();
    private String id;

    public EmailData() {
        this.id = java.util.UUID.randomUUID().toString();
    }

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

    public String getToListAsString() {
        return (toList != null) ? String.join(", ", toList) : "";
    }

    public void setToListAsString(String toStr) {
        this.toList = Arrays.stream(toStr.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public String getCcListAsString() {
        return (ccList != null) ? String.join(", ", ccList) : "";
    }

    public void setCcListAsString(String ccStr) {
        this.ccList = Arrays.stream(ccStr.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EmailData{" + "subject=" + subject + ", sender=" + sender + ", toList=" + toList + ", ccList=" + ccList + ", body=" + body + ", attachments=" + attachments + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailData)) {
            return false;
        }
        EmailData that = (EmailData) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
