/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package ManagedBean;

import Dto.AttachmentData;
import Dto.EmailData;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import com.auxilii.msgparser.MsgParser;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.FileAttachment;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.primefaces.model.file.UploadedFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author e.hussien
 */
@Named(value = "msgReaderBean")
@ViewScoped
public class MsgReaderBean implements Serializable {

    /**
     * Creates a new instance of MsgReaderBean
     */
    private EmailData emailData;
    private UploadedFile uploadedFile;

    private List<EmailData> list;

    public EmailData msgReadFrom() {
        if (uploadedFile != null) {
            try {
                byte[] fileBytes = uploadedFile.getContent(); // get the file content
                Message msg = new MsgParser().parseMsg(new ByteArrayInputStream(fileBytes));

                Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
                emailData = new EmailData();

                // Subject & Body
                emailData.setSubject(msg.getSubject());
                emailData.setSender(msg.getFromEmail());
                emailData.setBody(msg.getBodyText());
                emailData.setToList(extractEmailsFromHeader(msg.getHeaders(), "to", emailPattern));
                emailData.setCcList(extractEmailsFromHeader(msg.getHeaders(), "cc", emailPattern));
                // Save attachments to Desktop
                String desktopPath = System.getProperty("user.home") + "/Desktop/";

                //for set Attach List
                List<AttachmentData> attachmentList = msg.getAttachments().stream()
                        .map(att -> {
                            if (att instanceof FileAttachment) {
                                FileAttachment fa = (FileAttachment) att;
                                try {
                                    String filePath = desktopPath + fa.getLongFilename();
                                    Files.write(Paths.get(filePath), fa.getData());
                                    //System.out.println("Saved attachment: " + filePath);
                                    return new AttachmentData(fa.getLongFilename(), new ByteArrayInputStream(fa.getData()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return new AttachmentData(fa.getLongFilename(), null);
                                }
                            } else {
                                return new AttachmentData("[Non-file attachment]", null);
                            }
                        }).collect(Collectors.toList());
                emailData.setAttachments(attachmentList);

//                // Print attachments
//                if (emailData.getAttachments() != null) {
//                    System.out.println("Attachments:");
//                    for (AttachmentData attData : emailData.getAttachments()) {
//                        System.out.println("Filename: " + attData.getFileName());
//
//                        if (attData.getFileContent() != null) {
//                            try {
//                                // Read content from stream (Java 8 compatible)
//                                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//                                byte[] data = new byte[1024];
//                                int nRead;
//                                while ((nRead = attData.getFileContent().read(data, 0, data.length)) != -1) {
//                                    buffer.write(data, 0, nRead);
//                                }
//                                buffer.flush();
//                                byte[] contentBytes = buffer.toByteArray();
//                                System.out.println("Content length: " + contentBytes.length + " bytes");
//
//                                // Optional: recreate InputStream if you want to reuse it
//                                attData.setFileContent(new ByteArrayInputStream(contentBytes));
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            System.out.println("No content available.");
//                        }
//                    }
//                }

//                // Optional: print email info in console
//                System.out.println("Subject: " + emailData.getSubject());
//                System.out.println("Body: " + emailData.getBody());
//                System.out.println("From email: " + emailData.getSender());
//                if (emailData.getToList() != null) {
//                    System.out.println("To:");
//                    emailData.getToList().forEach(t -> System.out.println(" - " + t));
//                }
//                if (emailData.getCcList() != null) {
//                    System.out.println("Cc:");
//                    emailData.getCcList().forEach(c -> System.out.println(" - " + c));
//                }

                System.out.println("MSG file processed successfully!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No file uploaded!");
        }
        return emailData;
    }

    // Helper Method To Extract CC And To List Emails
    private List<String> extractEmailsFromHeader(String headers, String headerName, Pattern emailPattern) {
        String[] lines = headers.split("\r\n|\r|\n");
        StringBuilder headerBuilder = new StringBuilder();
        boolean inTargetHeader = false;

        for (String line : lines) {
            if (line.toLowerCase().startsWith(headerName.toLowerCase() + ":")) {
                inTargetHeader = true;
                headerBuilder.append(line);
            } else if (inTargetHeader) {
                if (line.matches("^[A-Za-z-]+:.*")) {
                    // another header started → stop
                    break;
                }
                if (line.startsWith(" ") || line.startsWith("\t")) {
                    headerBuilder.append(" ").append(line.trim());
                }
            }
        }

        String headerValue = headerBuilder.toString()
                .replaceFirst("(?i)^" + headerName + ":", "")
                .trim();

        List<String> result = new ArrayList<>();
        Matcher matcher = emailPattern.matcher(headerValue);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    public void print() {
        System.out.println("The object is From Print method : "+msgReadFrom());
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public EmailData getEmailData() {
        return emailData;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public List<EmailData> getList() {
        return list;
    }

    public void setList(List<EmailData> list) {
        this.list = list;
    }

}
