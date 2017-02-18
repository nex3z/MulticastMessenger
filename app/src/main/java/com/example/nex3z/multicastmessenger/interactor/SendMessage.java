package com.example.nex3z.multicastmessenger.interactor;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.nex3z.multicastmessenger.app.App;
import com.example.nex3z.multicastmessenger.executor.PostExecutionThread;
import com.example.nex3z.multicastmessenger.executor.ThreadExecutor;
import com.example.nex3z.multicastmessenger.model.MessageModel;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import rx.Observable;
import rx.Subscriber;

public class SendMessage extends UseCase<SendMessageArg> {
    private static final String LOG_TAG = SendMessage.class.getSimpleName();
    private static final String MULTICAST_TAG = "multicast_send";

    public SendMessage(ThreadExecutor threadExecutor,
                       PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Log.v(LOG_TAG, "buildUseCaseObservable(): mArg = " + mArg);
        if (mArg == null) {
            throw new IllegalArgumentException("Arg cannot be null.");
        }
        return Observable.create(new Observable.OnSubscribe<MessageModel>() {
            @Override
            public void call(Subscriber<? super MessageModel> sub) {
                WifiManager wifiManager = (WifiManager) App.getAppContext().getSystemService(Context.WIFI_SERVICE);
                WifiManager.MulticastLock multicastLock = wifiManager.createMulticastLock(MULTICAST_TAG);
                multicastLock.acquire();
                try {
                    InetAddress address = InetAddress.getByName(
                            mArg.getMessageModel().getAddress());
                    int port = mArg.getMessageModel().getPort();
                    String message = mArg.getMessageModel().getMessage();

                    MulticastSocket socket = new MulticastSocket(port);
                    socket.joinGroup(address);

                    DatagramPacket sendPacket = new DatagramPacket(
                            message.getBytes(), message.getBytes().length, address, port);
                    socket.send(sendPacket);

                    socket.leaveGroup(address);

                    sub.onNext(mArg.getMessageModel());
                } catch (Exception e) {
                    sub.onError(e);
                } finally {
                    multicastLock.release();
                    sub.onCompleted();
                }
            }
            }
        );
    }
}
