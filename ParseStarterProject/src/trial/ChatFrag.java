package trial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.R;

import java.util.ArrayList;
import java.util.List;

import listings.ListingsFrag;

public class ChatFrag extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private EditText etMessage;
    private Button btSend;
    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private Handler handler = new Handler();
    Boolean sameId;
    String id;

    public static ChatFrag newInstance(){

        ChatFrag fragment = new ChatFrag();
        return fragment;

    }

    Convo convo;
    ParseObject message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.activity_chat, parent, false);
        ((MainActivity) getActivity()).goBackToPrevious(true);
        ((MainActivity) getActivity()).goBackToListingsFrag(false);
        ((MainActivity) getActivity()).setActionBarTitle("Chat");
        convo = ((MainActivity) getActivity()).getConvo();

        etMessage = (EditText) v.findViewById(R.id.etMessage);
        btSend = (Button) v.findViewById(R.id.btSend);
        lvChat = (ListView) v.findViewById(R.id.lvChat);
        mMessages = new ArrayList<Message>();
        mAdapter = new ChatListAdapter(getActivity(), ParseUser.getCurrentUser(), mMessages);
        lvChat.setAdapter(mAdapter);
        setupMessagePosting();
        handler.postDelayed(runnable, 100);

        return v;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, 100);
        }
    };

    private void refreshMessages() {
        receiveMessage();
    }

    private void setupMessagePosting() {
        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                message = ((MainActivity) getActivity()).getCurrentMessage();
                message.put("Author", ParseUser.getCurrentUser());
                message.put("body", data);
                //puts the same object ID as convo to message ID so they are correlated
                message.put("ID", convo.getObjectId());
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                    }
                });

                ((MainActivity) getActivity()).makeNewMessage();
                etMessage.setText("");
            }
        });
    }

    private void receiveMessage() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    //Parses through all messages in Parse, if message goes with convo then add to listview
                    for (int i = 0; i < messages.size(); i++) {
                        if (messages.get(i).getString("ID") == null) {
                            continue;
                        }
                        if (messages.get(i).getID().equals(convo.getObjectId())) {
                            mMessages.add(messages.get(i));
                        }
                    }
                    mAdapter.notifyDataSetChanged(); // update adapter
                    lvChat.invalidate(); // redraw listview
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });}

    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(1);
    }



}
