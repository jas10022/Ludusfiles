package com.example.conversationsdk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moxtra.binder.sdk.MXMessage;
import com.moxtra.binder.sdk.MXUICustomizer;
import com.moxtra.binder.sdk.OnMessageReceivedCallback;
import com.moxtra.binder.sdk.OnSendMessageCallback;
import com.moxtra.sdk.MXChatManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/25.
 */
public class MessageListFragment extends ListFragment {
    private static final String TAG = BinderListFragment.class.getSimpleName();
    private SimpleBaseAdapter adapter;
    private ArrayList<MXMessage> msgList;
    private MXMessage SENDMSG = new MXMessage();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(msgList == null) {
            msgList = new ArrayList<>();
            msgList.add(SENDMSG);
        }

        adapter = new SimpleBaseAdapter(getActivity(), msgList);
        setListAdapter(adapter);

        MXUICustomizer.setOnMessageReceivedCallback(new OnMessageReceivedCallback() {
            @Override
            public void onMessageReceived(MXMessage mxMessage) {
                if (mxMessage != null) {
                    msgList.add(mxMessage);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        MXUICustomizer.setOnSendMessageCallback(new OnSendMessageCallback() {
            @Override
            public void sendMessageSuccess() {
                Toast.makeText(getActivity().getApplicationContext(), "Send Success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void sendMessageFailed(int i, String s) {
                Toast.makeText(getActivity().getApplicationContext(), "Send failed:" + s , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        MXMessage msg = msgList.get(position);
        if(msg.equals(SENDMSG)){
            //SendBroadCast message
            MXChatManager.getInstance().sendMsgAll("hello!");
        }else{
            //reply message
            MXChatManager.getInstance().replyMsgTo(msg.getMessageId(), "gun!");
        }
     }

    class SimpleBaseAdapter extends BaseAdapter{
        private List<MXMessage> mData;
        private Context context;

        public SimpleBaseAdapter(Context context, List<MXMessage> data){
            this.context = context;
            this.mData = data;
        }

        public void setData(List<MXMessage> data){
            this.mData = data;
        }

        @Override
        public int getCount() {
            if (mData == null)
                return 0;
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            if (mData == null)
                return null;
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            if (mData == null)
                return 0;
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, null);
                viewHolder.sender = (TextView)convertView.findViewById(android.R.id.text1);
                viewHolder.message = (TextView)convertView.findViewById(android.R.id.text2);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            if(mData.get(position).equals(SENDMSG)){
                viewHolder.message.setVisibility(View.GONE);
                viewHolder.sender.setText("Send broadcast Msg");
            }else{
                viewHolder.sender.setText(mData.get(position).getSenderUniqueId());
                viewHolder.message.setText( mData.get(position).getMessageBody());
            }

            return convertView;
        }
    }

    class ViewHolder{
        TextView sender;
        TextView message;
    }
}
