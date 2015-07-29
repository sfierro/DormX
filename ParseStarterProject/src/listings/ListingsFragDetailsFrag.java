package listings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.starter.R;

import trial.Listing;
import trial.MainActivity;

/**
 * Created by laurenbenitez on 7/28/15.
 */
public class ListingsFragDetailsFrag extends android.support.v4.app.Fragment {

    private TextView title;
    private TextView price;
    private TextView description;
    private ParseImageView img;

    public static ListingsFragDetailsFrag newInstance() {
        ListingsFragDetailsFrag fragment = new ListingsFragDetailsFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.detail_page, parent, false);

        Listing listing = ((MainActivity) getActivity()).getListing();

        ((MainActivity) getActivity()).setActionBarTitle("");

        title = (TextView) v.findViewById(R.id.title);
        price = (TextView) v.findViewById(R.id.price);
        description = (TextView) v.findViewById(R.id.description);
        img = (ParseImageView) v.findViewById(R.id.parseimg);

        title.setText(listing.getTitle());
        price.setText(listing.getPrice());
        description.setText(listing.getDescription());
        img.setParseFile(listing.getPhotoFile());

        img.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                //
            }
        });

        return v;
    }
}

