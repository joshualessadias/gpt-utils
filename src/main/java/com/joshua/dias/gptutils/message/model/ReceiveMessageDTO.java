package com.joshua.dias.gptutils.message.model;

/**
 * DTO for receiving messages from external systems.
 */
public class ReceiveMessageDTO {
    private boolean isStatusReply;
    private String senderLid;
    private String connectedPhone;
    private boolean waitingMessage;
    private boolean isEdit;
    private boolean isGroup;
    private boolean isNewsletter;
    private String instanceId;
    private String messageId;
    private String phone;
    private boolean fromMe;
    private long momment;
    private String status;
    private String chatName;
    private String senderPhoto;
    private String senderName;
    private String participantPhone;
    private String participantLid;
    private String photo;
    private boolean broadcast;
    private String type;
    private TextDTO text;
    private ReactionDTO reaction;
    private ImageDTO image;
    private AudioDTO audio;
    private VideoDTO video;

    // Default constructor
    public ReceiveMessageDTO() {
    }

    // Getters and setters
    public boolean isStatusReply() {
        return isStatusReply;
    }

    public void setStatusReply(boolean statusReply) {
        isStatusReply = statusReply;
    }

    public String getSenderLid() {
        return senderLid;
    }

    public void setSenderLid(String senderLid) {
        this.senderLid = senderLid;
    }

    public String getConnectedPhone() {
        return connectedPhone;
    }

    public void setConnectedPhone(String connectedPhone) {
        this.connectedPhone = connectedPhone;
    }

    public boolean isWaitingMessage() {
        return waitingMessage;
    }

    public void setWaitingMessage(boolean waitingMessage) {
        this.waitingMessage = waitingMessage;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public boolean isNewsletter() {
        return isNewsletter;
    }

    public void setNewsletter(boolean newsletter) {
        isNewsletter = newsletter;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isFromMe() {
        return fromMe;
    }

    public void setFromMe(boolean fromMe) {
        this.fromMe = fromMe;
    }

    public long getMomment() {
        return momment;
    }

    public void setMomment(long momment) {
        this.momment = momment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getSenderPhoto() {
        return senderPhoto;
    }

    public void setSenderPhoto(String senderPhoto) {
        this.senderPhoto = senderPhoto;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getParticipantPhone() {
        return participantPhone;
    }

    public void setParticipantPhone(String participantPhone) {
        this.participantPhone = participantPhone;
    }

    public String getParticipantLid() {
        return participantLid;
    }

    public void setParticipantLid(String participantLid) {
        this.participantLid = participantLid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TextDTO getText() {
        return text;
    }

    public void setText(TextDTO text) {
        this.text = text;
    }

    public ReactionDTO getReaction() {
        return reaction;
    }

    public void setReaction(ReactionDTO reaction) {
        this.reaction = reaction;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }

    public AudioDTO getAudio() {
        return audio;
    }

    public void setAudio(AudioDTO audio) {
        this.audio = audio;
    }

    public VideoDTO getVideo() {
        return video;
    }

    public void setVideo(VideoDTO video) {
        this.video = video;
    }
}