package com.example.nex3z.multicastmessenger.interactor;

import android.util.Log;

import com.example.nex3z.multicastmessenger.datasource.DataSource;
import com.example.nex3z.multicastmessenger.executor.PostExecutionThread;
import com.example.nex3z.multicastmessenger.executor.ThreadExecutor;
import com.example.nex3z.multicastmessenger.model.MessageModel;

import javax.inject.Inject;

import io.reactivex.Observable;

public class SendMessage extends UseCase<Boolean, SendMessage.Params> {
    private static final String LOG_TAG = SendMessage.class.getSimpleName();

    private final DataSource mDataSource;

    @Inject
    public SendMessage(DataSource dataSource, ThreadExecutor threadExecutor,
                       PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mDataSource = dataSource;
    }

    @Override
    Observable<Boolean> buildUseCaseObservable(Params params) {
        if (params == null) {
            throw new IllegalArgumentException("params cannot be null.");
        }
        Log.v(LOG_TAG, "buildUseCaseObservable(): params = " + params.getMessageModel());

        return mDataSource.send(params.getMessageModel());
    }

    public static final class Params {
        private final MessageModel mMessageModel;

        private Params(MessageModel messageModel) {
            mMessageModel = messageModel;
        }

        public MessageModel getMessageModel() {
            return mMessageModel;
        }

        public static Params forMessage(MessageModel messageModel) {
            return new Params(messageModel);
        }
    }
}
