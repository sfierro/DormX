package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.Parse;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import trial.Listing;
import trial.MainActivity;

public class SearchListingsFragAdapter extends ParseQueryAdapter<Listing> {

    public SearchListingsFragAdapter (Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Listing>() {
            public ParseQuery<Listing> create() {
                String search = ParseUser.getCurrentUser().getString("search");
                // Here we can configure a ParseQuery to display
                ParseQuery query = new ParseQuery("Listing");
                query.whereContains("title", search);
                //order with most recent at top
                ParseQuery query1 = new ParseQuery("Listing");
                query1.whereContains("description", search);
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
    public View getItemView(Listing lst, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.list_row, null);
        }

        super.getItemView(lst, v, parent);

        //set photo
        ParseImageView mealImage = (ParseImageView) v.findViewById(R.id.icon);
        ParseFile photoFile = lst.getParseFile("photo");
        if (photoFile != null) {
            mealImage.setParseFile(photoFile);
            mealImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    // nothing to do
                }
            });
        }

        //set other details
        TextView titleTextView = (TextView) v.findViewById(R.id.title);
        titleTextView.setText(lst.getTitle());
        TextView priceTextView = (TextView) v.findViewById(R.id.price);
        priceTextView.setText("$"+lst.getPrice());
        TextView descriptionTextView = (TextView) v.findViewById(R.id.description);
        descriptionTextView.setText(lst.getDescription());

        return v;
    }

}

