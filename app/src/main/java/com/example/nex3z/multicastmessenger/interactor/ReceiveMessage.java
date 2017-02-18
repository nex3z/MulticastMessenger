package com.example.nex3z.multicastmessenger.interactor;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.example.nex3z.multicastmessenger.app.App;
import com.example.nex3z.multicastmessenger.executor.PostExecutionThread;
import com.example.nex3z.multicastmessenger.executor.ThreadExecutor;
import com.example.nex3z.multicastmessenger.model.MessageModel;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import rx.Observable;
import rx.Subscriber;

public class ReceiveMessage extends UseCase<ReceiveMessageArg> {
    private static final String LOG_TAG = ReceiveMessage.class.getSimpleName();
    private static final String MULTICAST_TAG = "multicast_recv";
    private static final int READ_TIMEOUT = 500;

    public ReceiveMessage(ThreadExecutor threadExecutor,
                       PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<MessageModel>() {
            @Override
            public void call(Subscriber<? super MessageModel> subscriber) {
                WifiManager wifiManager = (WifiManager) App.getAppContext().getSystemService(Context.WIFI_SERVICE);
                WifiManager.MulticastLock multicastLock = wifiManager.createMulticastLock(MULTICAST_TAG);
                multicastLock.acquire();
                try {
                    MulticastSocket socket = new MulticastSocket(mArg.getPort());
                    socket.setSoTimeout(READ_TIMEOUT);
                    InetAddress address = InetAddress.getByName(mArg.getAddress());
                    socket.joinGroup(address);

                    byte[] buf = new byte[mArg.getBufferSize()];
                    while (!subscriber.isUnsubscribed()) {
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        try {
                            socket.receive(packet);
                        } catch (SocketTimeoutException e) {
                            continue;
                        }
                        String msg = new String(packet.getData(), 0, packet.getLength());
                        subscriber.onNext(new MessageModel(MessageModel.RECV,
                                packet.getAddress().getHostAddress(), packet.getPort(), msg));
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                } finally {
                    multicastLock.release();
                    subscriber.onCompleted();
                }
            }
        });
    }
}
