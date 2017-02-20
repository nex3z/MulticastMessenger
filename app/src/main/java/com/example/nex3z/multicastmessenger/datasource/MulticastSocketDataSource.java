package com.example.nex3z.multicastmessenger.datasource;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.example.nex3z.multicastmessenger.app.App;
import com.example.nex3z.multicastmessenger.model.MessageModel;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import io.reactivex.Observable;

public class MulticastSocketDataSource implements DataSource {
    private static final String LOG_TAG = MulticastSocketDataSource.class.getSimpleName();
    private static final String MULTICAST_TAG = "multicast_tag";
    private static final int READ_TIMEOUT = 500;
    private WifiManager.MulticastLock mMulticastLock;

    public MulticastSocketDataSource() {
        WifiManager wifiManager =
                (WifiManager) App.getAppContext().getSystemService(Context.WIFI_SERVICE);
        mMulticastLock = wifiManager.createMulticastLock(MULTICAST_TAG);
    }

    @Override
    public Observable<Boolean> send(MessageModel messageModel) {
        return Observable.create(emitter -> {
            mMulticastLock.acquire();
            try {
                InetAddress address = InetAddress.getByName(messageModel.getAddress());
                int port = messageModel.getPort();
                String message = messageModel.getMessage();

                MulticastSocket socket = new MulticastSocket(port);
                socket.joinGroup(address);

                DatagramPacket sendPacket = new DatagramPacket(
                        message.getBytes(), message.getBytes().length, address, port);
                socket.send(sendPacket);

                socket.leaveGroup(address);

                emitter.onNext(Boolean.TRUE);
            } catch (Exception e) {
                emitter.onError(e);
            } finally {
                mMulticastLock.release();
                emitter.onComplete();
            }
        });
    }

    @Override
    public Observable<MessageModel> receive(String address, int port, int bufferSize) {
        return Observable.create(emitter -> {
            mMulticastLock.acquire();
            try {
                MulticastSocket socket = new MulticastSocket(port);
                socket.setSoTimeout(READ_TIMEOUT);
                InetAddress group = InetAddress.getByName(address);
                socket.joinGroup(group);

                byte[] buf = new byte[bufferSize];
                while (!emitter.isDisposed()) {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    try {
                        socket.receive(packet);
                    } catch (SocketTimeoutException e) {
                        continue;
                    }
                    String msg = new String(packet.getData(), 0, packet.getLength());
                    emitter.onNext(new MessageModel(MessageModel.RECV,
                            packet.getAddress().getHostAddress(), packet.getPort(), msg));
                }
            } catch (Exception e) {
                emitter.onError(e);
            } finally {
                mMulticastLock.release();
                emitter.onComplete();
            }
        });
    }
}
