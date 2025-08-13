/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dto;

import java.io.InputStream;

/**
 *
 * @author e.hussien
 */
public class AttachmentData {
    private String fileName;
    private InputStream fileContent;

    public AttachmentData(String fileName, InputStream fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getFileContent() {
        return fileContent;
    }

    public void setFileContent(InputStream fileContent) {
        this.fileContent = fileContent;
    }  

    @Override
    public String toString() {
        return "AttachmentData{" + "fileName=" + fileName + ", fileContent=" + fileContent + '}';
    }
       
}
