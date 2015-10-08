package com.sb.android.chatclient.tcp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Administrator on 2015-10-07.
 */
public class TcpClient {

    public static final String SERVER_IP = "192.168.0.222";
    public static final int SERVER_PORT = 5000;
    public static final String CLOSED_CONNECTION = "namudak_closed_connection";
    public static final String LOGIN_NAME = "namudak_login_name";
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(String message) {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        // send mesage that we are closing the connection
        sendMessage(CLOSED_CONNECTION+"Sb");

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVER_PORT);

            try {

                //sends the message to the server
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // send login name
                sendMessage(LOGIN_NAME+"Sb");

                //in this while the client listens for the messages sent by the server
                while (mRun) {

                    mServerMessage = mBufferIn.readLine();

                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                    }

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        void messageReceived(String message);
        String getNickName();
    }

/*    //	private final static String SERVER_HOST = "suwonsmartapp.iptime.org";
    private final static String SERVER_HOST = "192.168.0.222";
    private final static int SERVER_PORT = 5000;

    private Socket mSocket;

    private ClientReceiver mReceiveThread;

    private ClientCallback mClientCallback;

    public interface ClientCallback {
        void onReceiveMessage(String message);

        String getNickName();
    }

    public void setClientCallback(ClientCallback callback) {
        mClientCallback = callback;
    }

    public static void main(String[] args) {
        new TcpClient().connect();
    }

    public void connect() {
        connect(SERVER_HOST, SERVER_PORT);
    }

    public void connect(String serverHost, int serverPort) {
        try {
            mSocket = new Socket(serverHost, serverPort);

            String nickName = "무명씨";
            if (mClientCallback != null) {
                nickName = mClientCallback.getNickName();
            }

            mReceiveThread = new ClientReceiver(mSocket, nickName);
            mReceiveThread.start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            mReceiveThread = null;
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        mReceiveThread.sendMessage(message);
    }

    class ClientReceiver extends Thread {

        private DataInputStream mInputStream;
        private DataOutputStream mOutputStream;
//		ObjectInputStream	// Java 전용
//		ObjectOutputStream	// Java 전용

        public ClientReceiver(Socket socket, String nickName) {
            try {
                mInputStream = new DataInputStream(socket.getInputStream());
                mOutputStream = new DataOutputStream(socket.getOutputStream());

                mOutputStream.writeUTF(nickName);
                mOutputStream.flush();

                System.out.println("id : " + nickName + "접속 완료");

//				try {
//					Thread.sleep(5000);
//					mOutputStream.writeUTF("exit");
//					mOutputStream.flush();
//					System.out.println("id : " + nickName + "접속 종료");
//					System.exit(0);
//				} catch (InterruptedException e) {
//				}

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

        @Override
        public void run() {
            try {
                // 계속 듣기만
                while (mInputStream != null) {
//					System.out.println(mInputStream.readUTF());
                    if (mClientCallback != null) {
                        mClientCallback.onReceiveMessage(mInputStream.readUTF());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 접속 종료시
                mSocket = null;

            }
        }
    }*/
}