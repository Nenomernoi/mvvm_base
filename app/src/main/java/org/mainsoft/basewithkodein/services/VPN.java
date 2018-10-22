package org.mainsoft.basewithkodein.services;

import android.content.Intent;
import android.net.VpnService;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


public class VPN extends VpnService implements Handler.Callback, Runnable {
    private static final String TAG = "VpnService";

    private String mServerAddress = "10.0.0.1";
    private int mServerPort = 55555;

    private Handler mHandler;
    private Thread mThread;

    private ParcelFileDescriptor mInterface;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mHandler == null) {
            mHandler = new Handler(this);
        }

        if (mThread != null) {
            mThread.interrupt();
        }
        mThread = new Thread(this, "VpnThread");
        mThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mThread != null) {
            mThread.interrupt();
        }
        super.onDestroy();
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message != null) {
            Toast.makeText(this, (String) message.obj, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public synchronized void run() {
        try {
            Log.i(TAG, "Starting");
            InetSocketAddress server = new InetSocketAddress(mServerAddress, mServerPort);
            run(server);
        } catch (Exception e) {
            Log.e(TAG, "Got " + e.toString());
            try {
                mInterface.close();
            } catch (Exception e2) {
            }
            Message msgObj = mHandler.obtainMessage();
            msgObj.obj = "Disconnected";
            mHandler.sendMessage(msgObj);
        } finally {

        }
    }

    DatagramChannel mTunnel = null;

    protected boolean run(InetSocketAddress server) throws Exception {
        boolean connected;

        mTunnel = DatagramChannel.open();

        if (!protect(mTunnel.socket())) {
            throw new IllegalStateException("Cannot protect the tunnel");
        }

        mTunnel.connect(server);

        mTunnel.configureBlocking(false);
        handshake();

        connected = true;
        Message msgObj = mHandler.obtainMessage();
        msgObj.obj = "Connected";
        mHandler.sendMessage(msgObj);

        new Thread() {

            public void run() {
                FileInputStream in = new FileInputStream(mInterface.getFileDescriptor());
                ByteBuffer packet = ByteBuffer.allocate(1024);

                FileOutputStream out = new FileOutputStream(mInterface.getFileDescriptor());
                int length;
                String destIP;

                try {

                    while (true) {

                        while ((length = in.read(packet.array())) > 0) {
                            packet.limit(length);
                            Log.d(TAG, "Total Length:" + mTunnel.socket().getInetAddress());

                            mTunnel.write(packet);
                            packet.flip();

                            TCP_IP TCP_debug = new TCP_IP(packet);
                            TCP_debug.debug();
                            destIP = TCP_debug.getDestination();

                            InetAddress address = InetAddress.getByName(destIP);
                            Log.d(TAG, address.getHostAddress());
                            Log.d(TAG, address.getHostName());

                            out.write(packet.array(), 0, length);
                            packet.clear();

                            Thread.sleep(100);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return connected;
    }

    private void makeConnection(String destination, int port) {
        try {
            run(new InetSocketAddress(destination, port));
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
    }

    private void handshake() throws Exception {

        if (mInterface == null) {
            Builder builder = new Builder();

            //builder.setMtu(1500);
            builder.addAddress("10.0.2.0", 32);
            builder.addDnsServer("176.103.130.130");
            builder.addDnsServer("176.103.130.131");

            //  builder.addDisallowedApplication("com.android.chrome");
            //  builder.addDisallowedApplication("com.opera.browser");
            // builder.addRoute("0.0.0.0", 0);
            try {
                mInterface.close();
            } catch (Exception e) {
                // ignore
            }

            mInterface = builder.setSession("VPN'as").establish();
        }
    }
}

