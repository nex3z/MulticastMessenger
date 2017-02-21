package com.example.nex3z.multicastmessenger.interactor;

import com.example.nex3z.multicastmessenger.datasource.DataSource;
import com.example.nex3z.multicastmessenger.executor.PostExecutionThread;
import com.example.nex3z.multicastmessenger.executor.ThreadExecutor;
import com.example.nex3z.multicastmessenger.model.MessageModel;

import javax.inject.Inject;

import io.reactivex.Observable;

public class ReceiveMessage extends UseCase<MessageModel, ReceiveMessage.Params> {
    private static final String LOG_TAG = ReceiveMessage.class.getSimpleName();

    private final DataSource mDataSource;

    @Inject
    public ReceiveMessage(DataSource dataSource, ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mDataSource = dataSource;
    }

    @Override
    Observable<MessageModel> buildUseCaseObservable(Params params) {
        if (params == null) {
            throw new IllegalArgumentException("params cannot be null.");
        }
        return mDataSource.receive(params.getAddress(), params.getPort(), params.getBufferSize());
    }

    public static final class Params {
        private static final int DEFAULT_BUFFER_SIZE = 256;

        private final String mAddress;
        private final int mPort;
        private final int mBufferSize;

        private Params(String address, int port) {
            this(address, port, DEFAULT_BUFFER_SIZE);
        }

        private Params(String address, int port, int bufferSize) {
            mAddress = address;
            mPort = port;
            mBufferSize = bufferSize;
        }

        public static Params forConfig(String address, int port) {
            return new Params(address, port);
        }

        public static Params forConfig(String address, int port, int bufferSize) {
            return new Params(address, port, bufferSize);
        }

        public String getAddress() {
            return mAddress;
        }

        public int getPort() {
            return mPort;
        }

        public int getBufferSize() {
            return mBufferSize;
        }
    }
}
