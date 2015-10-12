package com.sb.android.chatclient;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2015-10-07.
 */
public class ClientItemView extends LinearLayout {

    View mView;
    LinearLayout content;
    LinearLayout contentWithBG;
    private TextView mDateTime;
    private TextView mMessage;

    public ClientItemView(Context context) {
        super(context);
    }

    public ClientItemView(Context context, ChatItem aItem) {
        super(context);

        LayoutInflater layoutInflater= (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        mView= layoutInflater.inflate(R.layout.list_item, this, true);
        content= (LinearLayout) mView.findViewById(R.id.content);
        contentWithBG= (LinearLayout) mView.findViewById(R.id.contentWithBackground);

        mDateTime = (TextView)findViewById(R.id.txtInfo);
        mDateTime.setText(aItem.getmNickName() + ":" + aItem.getmMessage());

        mMessage = (TextView)findViewById(R.id.list_item_text_view);
        mMessage.setText(aItem.getmNickName()+ ":"+ aItem.getmMessage());
    }

    // Set time editview as customed
    public void setText(ChatItem aItem) {

        mDateTime.setText(aItem.getmDateTime());
        mMessage.setText(aItem.getmNickName() + ":" + aItem.getmMessage());

        if(!aItem.isMe) {
            contentWithBG.setBackgroundResource(R.drawable.in_message_bg);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            content.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) mDateTime.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            mDateTime.setLayoutParams(layoutParams);
            layoutParams = (LinearLayout.LayoutParams) mMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            mMessage.setLayoutParams(layoutParams);

        } else {
            contentWithBG.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            content.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) mDateTime.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            mDateTime.setLayoutParams(layoutParams);
            layoutParams = (LinearLayout.LayoutParams) mMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            mMessage.setLayoutParams(layoutParams);

        }

    }
}
