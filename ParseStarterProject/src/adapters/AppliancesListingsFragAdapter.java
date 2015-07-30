package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.starter.R;
import trial.Listing;

public class AppliancesListingsFragAdapter extends ParseQueryAdapter<Listing> {

    public AppliancesListingsFragAdapter (Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Listing>() {
            public ParseQuery<Listing> create() {
                // Here we can configure a ParseQuery to display
                double maxDistance = 5;
                ParseGeoPoint point = ParseUser.getCurrentUser().getParseGeoPoint("current");
                ParseQuery query = new ParseQuery("Listing");
                //only show listings that are within maxDistance
                query.whereWithinMiles("location", point, maxDistance);
                query.whereEqualTo("category","Appliances");
                //order with most recent at top
                query.orderByDescending("createdAt");
                return query;
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

