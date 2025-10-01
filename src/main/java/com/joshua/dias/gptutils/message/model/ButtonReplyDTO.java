package com.joshua.dias.gptutils.message.model;

public class ButtonReplyDTO {
    private String buttonId;
    private String message;
    private String referenceMessageId;

    public ButtonReplyDTO() {
    }

    public String getButtonId() {
        return buttonId;
    }

    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReferenceMessageId() {
        return referenceMessageId;
    }
    public void setReferenceMessageId(String referenceMessageId) {
        this.referenceMessageId = referenceMessageId;
    }
}
