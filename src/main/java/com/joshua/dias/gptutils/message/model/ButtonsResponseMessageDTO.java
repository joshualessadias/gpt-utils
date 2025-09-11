package com.joshua.dias.gptutils.message.model;

public class ButtonsResponseMessageDTO {
    private String buttonId;
    private String message;

    public ButtonsResponseMessageDTO() {
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
}
