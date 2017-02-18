package com.example.nex3z.multicastmessenger.interactor;

public class ReceiveMessageArg {
    private static final int DEFAULT_BUFFER_SIZE = 256;

    private final String mAddress;
    private final int mPort;
    private final int mBufferSize;

    public ReceiveMessageArg(String address, int port) {
        this(address, port, DEFAULT_BUFFER_SIZE);
    }

    public ReceiveMessageArg(String address, int port, int bufferSize) {
        mAddress = address;
        mPort = port;
        mBufferSize = bufferSize;
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
