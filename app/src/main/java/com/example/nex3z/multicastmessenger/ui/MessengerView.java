package com.example.nex3z.multicastmessenger.ui;


import com.example.nex3z.multicastmessenger.model.MessageModel;

import java.util.List;

public interface MessengerView {
    void showMessage(String message);
    void renderMessageModelAt(int position);
    void renderMessageModelList(List<MessageModel> messages);
}
