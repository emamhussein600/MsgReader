/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bass.cms.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *
 * @author e.hussien
 */
public class AttachmentData {

    private String id;
    private String fileName;
    private byte[] content;  // ← Store bytes, not stream

    public AttachmentData() {
        this.id = java.util.UUID.randomUUID().toString();
    }

    // Constructor from byte[]
    public AttachmentData(String fileName, byte[] content) {
        this.id = java.util.UUID.randomUUID().toString();
        this.fileName = fileName;
        this.content = content;
    }

    // Constructor from InputStream (converts to byte[])
    public AttachmentData(String fileName, InputStream inputStream) {
        this.id = java.util.UUID.randomUUID().toString();
        this.fileName = fileName;
        this.content = inputStreamToByteArray(inputStream);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    // Every call returns a NEW InputStream (critical!)
    public InputStream getFileContent() {
        return content != null ? new ByteArrayInputStream(content) : null;
    }

    public void setFileContent(InputStream fileContent) {
        this.content = inputStreamToByteArray(fileContent);
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Helper method to convert InputStream → byte[]
    private byte[] inputStreamToByteArray(InputStream is) {
        if (is == null) {
            return null;
        }
        try {
            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
            byte[] data = new byte[8192];
            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.close();
            return buffer.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "AttachmentData{"
                + "id='" + id + '\''
                + ", fileName='" + fileName + '\''
                + ", size=" + (content != null ? content.length : 0) + " bytes"
                + '}';
    }
}
