package com.sb.android.chatclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sb.android.chatclient.tcp.TcpClient;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {
    private ListView mList;
    private ArrayList<ChatItem> mArrayList;
    private ClientItemAdapter mAdapter;
    private TcpClient mTcpClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mArrayList = new ArrayList<ChatItem>();

        final EditText editText = (EditText) findViewById(R.id.editText);
        Button send = (Button) findViewById(R.id.send_button);

        //relate the listView from java to the one created in xml
        mList = (ListView) findViewById(R.id.list);
        mAdapter = new ClientItemAdapter(this, mArrayList);
        mList.setAdapter(mAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString();

                //add the text in the arrayList
                ChatItem chatItem= new ChatItem();
                chatItem.setmDateTime(Calendar.getInstance().getTime().toString());
                chatItem.setmNickName("namudak");
                chatItem.setIsMe(true);
                chatItem.setmMessage(message);
                mArrayList.add(chatItem);

//                ChatItem chatItem2= new ChatItem();
//                chatItem2.setmDateTime(Calendar.getInstance().getTime().toString());
//                chatItem2.setmNickName("maria");
//                chatItem2.setIsMe(false);
//                chatItem2.setmMessage(message);
//                mArrayList.add(chatItem2);

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }

                //refresh the list
                mAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        // disconnect
        //mTcpClient.stopClient();
        mTcpClient.disconnect();
        mTcpClient = null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (mTcpClient != null) {
            // if the client is connected, enable the connect button and disable the disconnect one
            menu.getItem(1).setEnabled(true);
            menu.getItem(0).setEnabled(false);
        } else {
            // if the client is disconnected, enable the disconnect button and disable the connect one
            menu.getItem(1).setEnabled(false);
            menu.getItem(0).setEnabled(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.connect:
                // connect to the server
                new ConnectTask().execute("");
                return true;
            case R.id.disconnect:
                // disconnect
                //mTcpClient.stopClient();
                mTcpClient.disconnect();
                mTcpClient = null;
                // clear the data set
                mArrayList.clear();
                // notify the adapter that the data set has changed.
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * AsyncTask for tcpConnecting
     */
    public class ConnectTask extends AsyncTask<String, String, TcpClient> {
        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TcpClient(new TcpClient.ClientCallback() {
                @Override
                //here the messageReceived method is implemented
                public void onReceiveMessage(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }

                @Override
                public String getNickName() {
                    return "namudak";
                }
            });
            mTcpClient.connect();
            mTcpClient.ClientReceiver(mTcpClient.getSocket(), "Namudak");
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //in the arrayList we add the messaged received from server
            ChatItem chatItem= new ChatItem();
            chatItem.setmDateTime(Calendar.getInstance().getTime().toString());
            chatItem.setmNickName(mTcpClient.getClientCallbak().getNickName());
            chatItem.setIsMe(false);
            chatItem.setmMessage(values[0]);
            mArrayList.add(chatItem);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter.notifyDataSetChanged();
        }
    }
}
