package listings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseQueryAdapter;
import com.parse.starter.R;

import trial.Listing;
import listings.ListingsFragAdapter;
import listings.ListingsFragDetailsFrag;
import trial.MainActivity;
import venmo.VenmoLibrary;

/**
 * Created by samfierro on 7/4/15.
 */
public class ListingsFrag extends android.support.v4.app.Fragment {

    private ParseQueryAdapter<Listing> mainAdapter;
    private ListView list;

    public static ListingsFrag newInstance(){

        ListingsFrag fragment = new ListingsFrag();
        return fragment;

    }


    public ListingsFrag(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.listings_frag,container,false);

        ImageButton btn = (ImageButton) rootView.findViewById(R.id.plusicon);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newListing();
            }
        });

        mainAdapter = new ListingsFragAdapter(this.getActivity());
        ListView lv = (ListView) rootView.findViewById(R.id.list2);
        lv.setAdapter(mainAdapter);

        ((MainActivity)getActivity()).setActionBarTitle("Listings");

        updateListings();

        list = (ListView) rootView.findViewById(R.id.list2);

        // Click event for single list row
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Listing listing = (Listing) parent.getItemAtPosition(position);
                ((MainActivity) getActivity()).setListing(listing);

                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.container, ListingsFragDetailsFrag.newInstance());
                transaction.addToBackStack("ListingsFrag");
                transaction.commit();
            }
        });

        return rootView;

    }


    public void newListing() {
        android.support.v4.app.Fragment AddNewListing = new AddNewListing();
        android.support.v4.app.FragmentTransaction transaction = this.getActivity().getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.container, AddNewListing);
        transaction.addToBackStack("ListingsFrag");
        transaction.commit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(1);

    }

    public void updateListings(){
        mainAdapter.loadObjects();
    }

}
