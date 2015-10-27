package com.example.conversationsdk.fragment;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.conversationsdk.ChatActionCallback;
import com.example.conversationsdk.activity.Phone_MainActivity;
import com.example.conversationsdk.activity.Tablet_MainActivity;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXChatCustomizer;
import com.moxtra.sdk.MXGroupChatSession;
import com.moxtra.sdk.MXGroupChatSessionCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BinderListFragment extends ListFragment {
    public static final String GET_BINDERS_TYPE = "get_binders_type";
    public static final String ALL_BINDERS = "all_binders";
    public static final String ALL_CHAT_BINDERS = "all_chat_binders";

    private ChatActionCallback callback;
    private static final String TAG = BinderListFragment.class.getSimpleName();
    private SimpleBaseAdapter adapter;
    protected List<MXGroupChatSession> sessions = null;
    private String currentSession = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new SimpleBaseAdapter(getActivity(), getSessions());
        setListAdapter(adapter);

        //Long click to show and copy binderId.
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final MXGroupChatSession session = sessions.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("BinderID").setMessage(session.getSessionID()).setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("boardId", session.getSessionID());
                        cmb.setPrimaryClip(clipData);
                    }
                });
                builder.show();
                return true;
            }
        });

        //set group chat/meet session change callback.
        MXChatManager.getInstance().setGroupChatSessionCallback(new MXGroupChatSessionCallback() {
            @Override
            public void onGroupChatSessionCreated(MXGroupChatSession session) {
                adapter.setData(getSessions());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onGroupChatSessionUpdated(MXGroupChatSession session) {
                adapter.setData(getSessions());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onGroupChatSessionDeleted(MXGroupChatSession session) {
                adapter.setData(getSessions());
                adapter.notifyDataSetChanged();
                if(session.isAMeet())
                    return;

                if(!MXChatCustomizer.isForceTablet()) {
                    //clear current opened binder id for phone mode.
                    if(session.getSessionID().equals(Phone_MainActivity.getCurrentBinderId()))
                        Phone_MainActivity.setCurrentBinderId(null);
                    return;
                }

                //Open first session of the list
                boolean ret = false;
                if(currentSession == null || session.getSessionID().equals(currentSession))
                  ret = openFirstChat();

                //show no chat/binder hint for tablet ui mode.
                if(!ret && getActivity() instanceof Tablet_MainActivity){
                    ((Tablet_MainActivity)getActivity()).hideRightFragment();
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        MXGroupChatSession session = sessions.get(position);
        openItemAction(session);
    }

    //set callback for different mode to open chat.
	public void setOnOpenChatActionCallback(ChatActionCallback callback){
        this.callback=callback;
    }

    public boolean openFirstChat(){
       return openItemAction(getFirstChatSession());
    }


    //open chat for phone/tablet mode. If session is a chat, open it, and if it is a meet, join it.
    private boolean openItemAction(MXGroupChatSession session){
        if(session == null)
            return false;
        if(MXChatCustomizer.isForceTablet() && currentSession != null && session.getSessionID().equals(currentSession)){
            return false;
        }

        if (session.isAChat()) {
            if(callback  != null)
                callback.openChatAction(session.getSessionID());
            currentSession = session.getSessionID();
        } else {
            Phone_MainActivity.joinMeet(session.getMeetID());
        }

        return true;
    }


    //get all chat/meet session.
    private List<? extends Map<String, ?>> getSessions() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        sessions = MXChatManager.getInstance().getGroupChatSessions();
        for (MXGroupChatSession session : sessions) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("topic", session.getTopic());
            map.put("type", session.isAChat() ? "Chat" : "Meet");

            list.add(map);
        }

        return list;
    }

    private MXGroupChatSession getFirstChatSession(){
        for(MXGroupChatSession session:sessions){
            if(session.isAChat())
                return session;
        }

        return null;
    }

    public void setCurrentSession(String sessionId){
        if(!TextUtils.isEmpty(sessionId))
            currentSession = sessionId;
    }

    class SimpleBaseAdapter extends BaseAdapter{
        private List<? extends Map<String, ?>> mData;
        private Context context;

        public SimpleBaseAdapter(Context context, List<?extends Map<String,?>> data){
            this.context = context;
            this.mData = data;
        }

        public void setData(List<?extends Map<String,?>> data){
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
                viewHolder.topic = (TextView)convertView.findViewById(android.R.id.text1);
                viewHolder.type = (TextView)convertView.findViewById(android.R.id.text2);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.topic.setText((String) mData.get(position).get("topic"));
            viewHolder.type.setText((String) mData.get(position).get("type"));

            return convertView;
        }
    }

    class ViewHolder{
        TextView topic;
        TextView type;
    }
}