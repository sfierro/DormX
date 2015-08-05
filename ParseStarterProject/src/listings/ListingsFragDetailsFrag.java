package listings;

import android.content.Context;
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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.starter.R;

import java.util.Random;

import trial.ChatFrag;
import trial.Convo;
import trial.Listing;
import trial.MainActivity;
import trial.Message;
import venmo.VenmoLibrary;

public class ListingsFragDetailsFrag extends android.support.v4.app.Fragment {

    private TextView title;
    private TextView price;
    private TextView description;
    private ParseImageView img;
    private Listing listing;

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

        listing = ((MainActivity) getActivity()).getListing();

        ((MainActivity) getActivity()).setActionBarTitle("");

        ((MainActivity) getActivity()).goBackToPrevious(true);

        ((MainActivity) getActivity()).goBackToListingsFrag(false);

        ((MainActivity)getActivity()).makeNewConvo();

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

        Button chat = (Button) v.findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //gets the current convo and message from Main Activity
                Message message = ((MainActivity)getActivity()).getCurrentMessage();
                Convo convo = ((MainActivity) getActivity()).getCurrentConvo();

                //sets convo data
                convo.setSeller(listing.getAuthor());
                convo.setBuyer(ParseUser.getCurrentUser());
                convo.saveInBackground();
                ((MainActivity)getActivity()).setConvo(convo);

                //sets message data
                message.put("seller", listing.getAuthor());
                message.saveInBackground();

                android.support.v4.app.Fragment ChatFrag = new ChatFrag();
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.container, ChatFrag);
                transaction.addToBackStack("ListingsFragDetailsFrag");
                transaction.commit();
            }
        });

        Button venmo = (Button) v.findViewById(R.id.venmo);
        venmo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pay();
            }
        });

        return v;
    }

    public void pay(){
        final Context c = (Context) this.getActivity();
        if (VenmoLibrary.isVenmoInstalled(c)) {
            try {
                Intent venmoIntent = VenmoLibrary.openVenmoPayment("2768", "DormX", listing.getAuthor().fetchIfNeeded().getString("venmo"), listing.getPrice(), listing.getTitle(), "charge");
                startActivityForResult(venmoIntent, 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(c, "Please intall Venmo app from Play Store", Toast.LENGTH_LONG).show();
        }
    }

}

