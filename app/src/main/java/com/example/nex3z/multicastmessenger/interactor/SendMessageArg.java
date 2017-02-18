package com.example.nex3z.multicastmessenger.interactor;

import com.example.nex3z.multicastmessenger.model.MessageModel;

public class SendMessageArg {
    private final MessageModel mMessageModel;

    public SendMessageArg(MessageModel messageModel) {
        mMessageModel = messageModel;
    }

    public MessageModel getMessageModel() {
        return mMessageModel;
    }
}
