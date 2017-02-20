package com.example.nex3z.multicastmessenger.datasource;


import com.example.nex3z.multicastmessenger.model.MessageModel;

import io.reactivex.Observable;


public interface DataSource {

    Observable<Boolean> send(MessageModel messageModel);

    Observable<MessageModel> receive(String address, int port, int bufferSize);

}
