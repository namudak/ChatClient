package com.sb.android.chatclient.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2015-10-07.
 */
public class TcpClient {

    // private final static String SERVER_HOST = "suwonsmartapp.iptime.org";
    private final static String SERVER_HOST = "192.168.0.222";
    private final static int SERVER_PORT = 5000;

    private Socket mSocket;

    private ClientCallback mClientCallback;

    private DataInputStream mInputStream;
    private DataOutputStream mOutputStream;

    public interface ClientCallback {
        void onReceiveMessage(String message);

        String getNickName();
    }

    public TcpClient(ClientCallback clientCallback) {
        mClientCallback = clientCallback;
    }

    public void connect() {
        connect(SERVER_HOST, SERVER_PORT);
    }

    public void connect(String serverHost, int serverPort) {
        try {
            mSocket = new Socket(serverHost, serverPort);

            String nickName = "namudak";
            if (mClientCallback != null) {
                nickName = mClientCallback.getNickName();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    public ClientCallback getClientCallbak() {
        return mClientCallback;
    }

    public void ClientReceiver(Socket socket, String nickName) {
        try {
            mInputStream = new DataInputStream(socket.getInputStream());
            mOutputStream = new DataOutputStream(socket.getOutputStream());

            mOutputStream.writeUTF(nickName);
            mOutputStream.flush();

            System.out.println("id : " + nickName + "Connection Ok");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("writeUTF IOException");
        }
    }

    public void sendMessage(String message) {
        if (mOutputStream != null) {
            try {
                mOutputStream.writeUTF(message);
                mOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        try {
            // Listening server
            while (mInputStream != null) {
                if (mClientCallback != null) {
                    mClientCallback.onReceiveMessage(mInputStream.readUTF());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // On disconnecting
            mSocket = null;

        }
    }
}