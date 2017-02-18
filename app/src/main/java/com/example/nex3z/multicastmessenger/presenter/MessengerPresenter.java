package com.example.nex3z.multicastmessenger.presenter;

import android.util.Log;

import com.example.nex3z.multicastmessenger.interactor.DefaultSubscriber;
import com.example.nex3z.multicastmessenger.interactor.ReceiveMessageArg;
import com.example.nex3z.multicastmessenger.interactor.SendMessageArg;
import com.example.nex3z.multicastmessenger.interactor.UseCase;
import com.example.nex3z.multicastmessenger.model.MessageModel;
import com.example.nex3z.multicastmessenger.ui.MessengerView;

import java.util.ArrayList;
import java.util.List;

public class MessengerPresenter implements Presenter {
    private static final String LOG_TAG = MessengerPresenter.class.getSimpleName();

    private static final String DEFAULT_ADDRESS = "225.0.0.3";
    private static final int DEFAULT_PORT = 8888;
    private static final int BUFFER_SIZE = 256;
    private static final int READ_TIMEOUT = 2000;

    private UseCase mSendUseCase;
    private UseCase mReceiveUseCase;
    private MessengerView mView;
    private List<MessageModel> mMessages = new ArrayList<>();
    private String mGroupIp = DEFAULT_ADDRESS;
    private int mPort = DEFAULT_PORT;

    public MessengerPresenter(UseCase sendMessage, UseCase receiveUseCase) {
        mSendUseCase = sendMessage;
        mReceiveUseCase = receiveUseCase;
    }

    public void setView(MessengerView view) {
        mView = view;
        mView.renderMessageModelList(mMessages);
    }

    public void initialize() {
        if (mView == null) {
            throw new IllegalStateException("Cannot initialize, the view is null.");
        }
        startReceive();
    }

    public void clear() {
        mMessages.clear();
        mView.renderMessageModelList(mMessages);
    }

    public void send(String message) {
        MessageModel messageModel = new MessageModel(MessageModel.SEND, mGroupIp, mPort, message);
        sendMessage(messageModel);
    }

    @Override
    public void resume() {}

    @Override
    public void pause() {}

    @Override
    public void destroy() {
        mSendUseCase.unsubscribe();
        mReceiveUseCase.unsubscribe();
        mView = null;
    }

    @SuppressWarnings("unchecked")
    private void sendMessage(MessageModel message) {
        mSendUseCase.init(new SendMessageArg(message)).execute(new SendMessageSubscriber());
    }

    @SuppressWarnings("unchecked")
    private void startReceive() {
        mReceiveUseCase.init(new ReceiveMessageArg(mGroupIp, mPort))
                .execute(new ReceiveMessageSubscriber());
    }

    private void renderMessage(MessageModel message) {
        Log.v(LOG_TAG, "renderMessage(): message = " + message);
        mMessages.add(message);
        mView.renderMessageModelAt(mMessages.size() - 1);
    }

    private final class SendMessageSubscriber extends DefaultSubscriber<MessageModel> {
        @Override
        public void onNext(MessageModel messageModel) {
            renderMessage(messageModel);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
    }

    private final class ReceiveMessageSubscriber extends DefaultSubscriber<MessageModel> {
        @Override
        public void onNext(MessageModel messageModel) {
            renderMessage(messageModel);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
    }

}
