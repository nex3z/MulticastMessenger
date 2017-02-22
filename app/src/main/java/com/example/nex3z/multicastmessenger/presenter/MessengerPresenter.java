package com.example.nex3z.multicastmessenger.presenter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.nex3z.multicastmessenger.R;
import com.example.nex3z.multicastmessenger.interactor.DefaultObserver;
import com.example.nex3z.multicastmessenger.interactor.ReceiveMessage;
import com.example.nex3z.multicastmessenger.interactor.SendMessage;
import com.example.nex3z.multicastmessenger.interactor.UseCase;
import com.example.nex3z.multicastmessenger.model.MessageModel;
import com.example.nex3z.multicastmessenger.ui.MessengerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MessengerPresenter implements Presenter {
    private static final String LOG_TAG = MessengerPresenter.class.getSimpleName();

    private static final String DEFAULT_ADDRESS = "225.0.0.3";
    private static final int DEFAULT_PORT = 8888;

    private UseCase mSendUseCase;
    private UseCase mReceiveUseCase;
    private MessengerView mView;
    private List<MessageModel> mMessages = new ArrayList<>();
    private String mGroupIp = DEFAULT_ADDRESS;
    private int mPort = DEFAULT_PORT;

    @Inject
    public MessengerPresenter(SendMessage sendMessage, ReceiveMessage receiveUseCase) {
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

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mView.context());
        mGroupIp = sharedPref.getString(
                mView.context().getString(R.string.pref_key_address), DEFAULT_ADDRESS);
        mPort = Integer.valueOf(sharedPref.getString(
                mView.context().getString(R.string.pref_key_port), String.valueOf(DEFAULT_PORT)));

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
        mSendUseCase.dispose();
        mReceiveUseCase.dispose();
        mView = null;
    }

    @SuppressWarnings("unchecked")
    private void sendMessage(MessageModel message) {
        mSendUseCase.execute(new SendMessageSubscriber(message), SendMessage.Params.forMessage(message));
    }

    @SuppressWarnings("unchecked")
    private void startReceive() {
        mReceiveUseCase.execute(new ReceiveMessageSubscriber(), ReceiveMessage.Params.forConfig(mGroupIp, mPort));
    }

    private void renderMessage(MessageModel message) {
        mMessages.add(message);
        mView.renderMessageModelAt(mMessages.size() - 1);
    }

    private final class SendMessageSubscriber extends DefaultObserver<Boolean> {
        private MessageModel mMessageModel;

        public SendMessageSubscriber(MessageModel messageModel) {
            mMessageModel = messageModel;
        }

        @Override
        public void onNext(Boolean b) {
            if (b) {
                renderMessage(mMessageModel);
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
    }

    private final class ReceiveMessageSubscriber extends DefaultObserver<MessageModel> {
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
