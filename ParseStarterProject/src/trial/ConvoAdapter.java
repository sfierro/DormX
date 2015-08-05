package trial;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.starter.R;

import java.util.ArrayList;
import java.util.List;

import trial.Listing;

public class ConvoAdapter extends ParseQueryAdapter<Convo> {

    public ConvoAdapter (Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Convo>() {
            public ParseQuery<Convo> create() {
                // Here we can configure a ParseQuery to display
                ParseQuery query = new ParseQuery("Convo");
                query.whereEqualTo("buyer",ParseUser.getCurrentUser());
                ParseQuery query1 = new ParseQuery("Convo");
                query1.whereEqualTo("seller",ParseUser.getCurrentUser());
            //if buyer presses chat seller twice two different convos will come up in chat inbox, can try to fix here
                List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                queries.add(query);
                queries.add(query1);
                ParseQuery mainQuery = ParseQuery.or(queries);
                mainQuery.orderByDescending("createdAt");
                return mainQuery;
            }
        });
    }

    @Override
    public View getItemView(Convo con, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.convo_row, null);
        }

        super.getItemView(con, v, parent);

        //set photo of chat conversation row (of other person's profile picture you are chatting with)
        ParseImageView chatImage = (ParseImageView) v.findViewById(R.id.chat_picture);
        if (con.getBuyer() == ParseUser.getCurrentUser()) {
            try {ParseFile photoFile = con.getBuyer().fetchIfNeeded().getParseFile("prof");
                if (photoFile != null) {
                    chatImage.setParseFile(photoFile);
                    chatImage.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            // nothing to do
                        }
                    });
                }}
        catch (ParseException e) {e.printStackTrace();}}
        else {
            try {ParseFile photoFile = con.getBuyer().fetchIfNeeded().getParseFile("prof");
                if (photoFile != null) {
                    chatImage.setParseFile(photoFile);
                    chatImage.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            // nothing to do
                        }
                    });
                }}
            catch (ParseException e) {e.printStackTrace();}}

        //set other details
        TextView titleTextView = (TextView) v.findViewById(R.id.chatter);
        try {
            if (con.getBuyer() == ParseUser.getCurrentUser()) {titleTextView.setText(con.getSeller().fetchIfNeeded().getString("Name"));}
            else {
        titleTextView.setText(con.getBuyer().fetchIfNeeded().getString("Name"));} }
        catch (ParseException e) {e.printStackTrace();}

        return v;
    }

}

