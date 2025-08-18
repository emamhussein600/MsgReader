/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package ManagedBean;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import com.auxilii.msgparser.MsgParser;
import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.FileAttachment;
import com.bass.cms.util.AttachmentData;
import com.bass.cms.util.EmailData;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.primefaces.model.file.UploadedFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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
    private boolean fileSelected;
    private EmailData selectedEmail;
    private EmailData originalEmail;
    private UploadedFile newAttachment;
    private List<EmailData> list;

    @PostConstruct
    public void init() {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
    }

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
                // String desktopPath = System.getProperty("user.home") + "/Desktop/";

                //for set Attach List
                List<AttachmentData> attachmentList = msg.getAttachments().stream()
                        .map(att -> {
                            if (att instanceof FileAttachment) {
                                FileAttachment fa = (FileAttachment) att;
                                try {
                                    byte[] data = fa.getData(); // ← already a byte[]
                                    return new AttachmentData(fa.getLongFilename(), data); // uses byte[] constructor
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return new AttachmentData("error.dat", new byte[0]);
                                }
                            } else {
                                return new AttachmentData("[Non-file attachment]", new byte[0]);
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
        EmailData processed = msgReadFrom();
        if (processed != null) {
            processed.setId(java.util.UUID.randomUUID().toString());
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(processed); // Add new email data to the list
        }
        System.out.println("The object is From Print method : " + processed);
    }
    // inside your ManagedBean

    public StreamedContent downloadAttachment(AttachmentData attachment) {
        return DefaultStreamedContent.builder()
                .name(attachment.getFileName())
                .contentType("application/octet-stream")
                .stream(() -> attachment.getFileContent())
                .build();
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
        this.fileSelected = (uploadedFile != null);
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

    public boolean isFileSelected() {
        return fileSelected;
    }

    public EmailData getSelectedEmail() {
        return selectedEmail;
    }

    public void setSelectedEmail(EmailData selectedEmail) {
        this.selectedEmail = selectedEmail;
    }

    public void removeAttachment(AttachmentData att) {
        if (selectedEmail != null && selectedEmail.getAttachments() != null) {
            selectedEmail.getAttachments().remove(att);
        }
    }

    public void addAttachment() {
        if (newAttachment == null || selectedEmail == null) {
            return;
        }

        try {
            // Read stream into byte[] immediately
            byte[] fileBytes = inputStreamToByteArray(newAttachment.getInputStream());

            AttachmentData attach = new AttachmentData();
            attach.setId(java.util.UUID.randomUUID().toString());
            attach.setFileName(newAttachment.getFileName());
            attach.setContent(fileBytes); // ← store bytes

            if (selectedEmail.getAttachments() == null) {
                selectedEmail.setAttachments(new ArrayList<>());
            }
            selectedEmail.getAttachments().add(attach);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Attachment added: " + newAttachment.getFileName()));

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add attachment"));
        } finally {
            newAttachment = null; // Clear uploaded file
        }
    }

    // Add this helper method to MsgReaderBean
    private byte[] inputStreamToByteArray(InputStream is) throws Exception {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        byte[] data = new byte[8192];
        int nRead;
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.close();
        return buffer.toByteArray();
    }

    public void prepareEdit(EmailData email) {
        if (email != null) {
            // store original
            this.selectedEmail = email;    // clone for editing
            System.out.println("=== PREPARE EDIT: cloned email with ID " + selectedEmail.getId());
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        if (selectedEmail == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No email selected."));
            return;
        }

        try {
            byte[] fileBytes = inputStreamToByteArray(uploadedFile.getInputStream());
            AttachmentData att = new AttachmentData(uploadedFile.getFileName(), fileBytes);

            if (selectedEmail.getAttachments() == null) {
                selectedEmail.setAttachments(new ArrayList<>());
            }
            selectedEmail.getAttachments().add(att);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Added: " + uploadedFile.getFileName()));

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upload failed", e.getMessage()));
        }
    }

    // Save changes from cloned object back to original list
    public void saveEditedEmail() {
        if (selectedEmail == null) {
            return;
        }
        EmailData original = list.stream()
                .filter(e -> e.getId().equals(selectedEmail.getId()))
                .findFirst()
                .orElse(null);

        if (original != null) {
            original.setSubject(selectedEmail.getSubject());
            original.setSender(selectedEmail.getSender());
            original.setToList(selectedEmail.getToList());
            original.setCcList(selectedEmail.getCcList());
            original.setBody(selectedEmail.getBody());
            List<AttachmentData> updatedAttachments = selectedEmail.getAttachments().stream()
                    .map(att -> new AttachmentData(att.getFileName(), att.getContent()))
                    .collect(Collectors.toList());
            original.setAttachments(updatedAttachments);
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Success",
                        "Email updated Successfully"));
        System.out.println("Saving email with ID: " + selectedEmail.getId()
                + ", attachments count: " + selectedEmail.getAttachments().size());

        selectedEmail = null;
    }

    public EmailData getOriginalEmail() {
        return originalEmail;
    }

    public void setOriginalEmail(EmailData originalEmail) {
        this.originalEmail = originalEmail;
    }

    public void cancelEdit() {
        selectedEmail = new EmailData();
    }

    public UploadedFile getNewAttachment() {
        return newAttachment;
    }

    public void setNewAttachment(UploadedFile newAttachment) {
        this.newAttachment = newAttachment;
    }

}
