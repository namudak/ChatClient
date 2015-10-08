package com.sb.android.chatclient;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-10-07.
 */
public class ClientItemAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ChatItem> mListItems;

    public ClientItemAdapter(Context context, ArrayList<ChatItem> arrayList){

        mContext= context;

        mListItems = arrayList;
    }

    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return mListItems.size();
    }

    @Override
    //get the data of an item from a specific position
    //i represents the position of the item in the list
    public Object getItem(int i) {
        return null;
    }

    @Override
    //get the position id of the item from the list
    public long getItemId(int i) {
        return 0;
    }

    @Override

    public View getView(int position, View view, ViewGroup viewGroup) {

        ClientItemView itemView;

        //get the string item from the position "position" from array list to put it on the TextView
        ChatItem aItem= mListItems.get(position);

        //check to see if the reused view is null or not, if is not null then reuse it
        if(view== null) {
            itemView= new ClientItemView(mContext, aItem);

        } else {
            itemView= (ClientItemView)view;
        }

        itemView.setText(aItem);

        //this method must return the view corresponding to the data at the specified position.
        return itemView;

    }
}