package listings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.starter.R;

import java.util.Random;

import trial.Listing;
import trial.MainActivity;
import trial.ProfileFrag;
import venmo.VenmoLibrary;

public class MyListingsFragDetailsFrag extends android.support.v4.app.Fragment {

    private TextView title;
    private TextView price;
    private TextView description;
    private ParseImageView img;
    private Listing listing;

    public static MyListingsFragDetailsFrag newInstance() {
        MyListingsFragDetailsFrag fragment = new MyListingsFragDetailsFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.my_detail_page, parent, false);

        ((MainActivity) getActivity()).goBackToPrevious(true);
        ((MainActivity) getActivity()).goBackToListingsFrag(false);

        listing = ((MainActivity) getActivity()).getListing();

        ((MainActivity) getActivity()).setActionBarTitle("");

        title = (TextView) v.findViewById(R.id.title);
        price = (TextView) v.findViewById(R.id.price);
        description = (TextView) v.findViewById(R.id.description);
        img = (ParseImageView) v.findViewById(R.id.parseimg);

        Random rand = new Random();
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        if (r > 170 && g > 170 && b > 170) {
            title.setTextColor(Color.BLACK);
            price.setTextColor(Color.BLACK);
            description.setTextColor(Color.BLACK);
        }
        int randomColor = Color.argb(255, r, g, b);

        v.setBackgroundColor(randomColor);

        title.setText(listing.getTitle());
        price.setText("$"+listing.getPrice());
        description.setText(listing.getDescription());
        img.setParseFile(listing.getPhotoFile());

        img.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                //
            }
        });

        Button delete = (Button) v.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder db = new AlertDialog.Builder(getActivity());
                db.setTitle("Are you sure?");
                db.setPositiveButton("Delete", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    listing.delete();
                                    android.support.v4.app.Fragment profileFragment = new ProfileFrag();
                                    android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                                            .beginTransaction();
                                    transaction.replace(R.id.container, profileFragment);
                                    transaction.commit();
                                } catch (ParseException e) {
                                    Toast.makeText(getActivity(), "Error with deleting listing: Please try again.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                db.setNegativeButton("Cancel", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //
                            }
                        });
                db.show();
            }
        });

        return v;
    }

}

