package listings;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.starter.R;

import java.util.List;

import adapters.AppliancesListingsFragAdapter;
import adapters.ClothesListingsFragAdapter;
import adapters.ElectronicsListingsFragAdapter;
import adapters.FurnitureListingsFragAdapter;
import adapters.OtherListingsFragAdapter;
import adapters.SearchListingsFragAdapter;
import adapters.TextbooksListingsFragAdapter;
import adapters.TicketsListingsFragAdapter;
import trial.Listing;
import trial.MainActivity;

public class ListingsFrag extends android.support.v4.app.Fragment {

    private ParseQueryAdapter<Listing> mainAdapter;
    private Spinner spinner;
    private EditText search;
    private ImageButton search_button;
    private Boolean keyboard=false;

    private PullToRefreshListView pullToRefreshListView;

    public static ListingsFrag newInstance(){

        ListingsFrag fragment = new ListingsFrag();
        return fragment;

    }

    public ListingsFrag(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.listings_frag,container,false);

        ((MainActivity) getActivity()).goBackToPrevious(false);
        ((MainActivity) getActivity()).goBackToListingsFrag(false);
        ((MainActivity) getActivity()).setActionBarTitle("Listings");

        ImageButton btn = (ImageButton) rootView.findViewById(R.id.plusicon);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newListing();
            }
        });

        //set listView adapter
        mainAdapter = new ListingsFragAdapter(this.getActivity());
//        ListView lv = (ListView) rootView.findViewById(R.id.list2);
//        lv.setAdapter(mainAdapter);

        ((MainActivity)getActivity()).setActionBarTitle("Listings");
        updateListings();

        pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pullDownList);
        pullToRefreshListView.setAdapter(mainAdapter);

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                updateListings();
                new GetDataTask().execute();
            }
        });

        //list = (ListView) rootView.findViewById(R.id.list2);

        // Click event for single list row
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

        spinner = (Spinner) rootView.findViewById(R.id.filter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mainAdapter = new ListingsFragAdapter(getActivity());
                    pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pullDownList);
                    pullToRefreshListView.setAdapter(mainAdapter);
                }
                if (position == 1) {
                    mainAdapter = new FurnitureListingsFragAdapter(getActivity());
                    pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pullDownList);
                    pullToRefreshListView.setAdapter(mainAdapter);
                }
                if (position == 2) {
                    mainAdapter = new ElectronicsListingsFragAdapter(getActivity());
                    pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pullDownList);
                    pullToRefreshListView.setAdapter(mainAdapter);
                }
                if (position == 3) {
                    mainAdapter = new AppliancesListingsFragAdapter(getActivity());
                    pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pullDownList);
                    pullToRefreshListView.setAdapter(mainAdapter);
                }
                if (position == 4) {
                    mainAdapter = new TicketsListingsFragAdapter(getActivity());
                    pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pullDownList);
                    pullToRefreshListView.setAdapter(mainAdapter);
                }
                if (position == 5) {
                    mainAdapter = new TextbooksListingsFragAdapter(getActivity());
                    pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pullDownList);
                    pullToRefreshListView.setAdapter(mainAdapter);
                }
                if (position == 6) {
                    mainAdapter = new ClothesListingsFragAdapter(getActivity());
                    pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pullDownList);
                    pullToRefreshListView.setAdapter(mainAdapter);
                }
                if (position == 7) {
                    mainAdapter = new OtherListingsFragAdapter(getActivity());
                    pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pullDownList);
                    pullToRefreshListView.setAdapter(mainAdapter);
                }

                search = (EditText) rootView.findViewById(R.id.search);
                search_button = (ImageButton) rootView.findViewById(R.id.search_icon);

                search_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ParseUser.getCurrentUser().put("search", search.getText().toString());
                        ParseUser.getCurrentUser().saveInBackground();
                        mainAdapter = new SearchListingsFragAdapter(getActivity());
                        pullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pullDownList);
                        pullToRefreshListView.setAdapter(mainAdapter);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootView;
    }

    private class GetDataTask extends AsyncTask<Void, Void, ParseQueryAdapter<Listing>> {

        @Override
        protected ParseQueryAdapter<Listing> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                //
            }
            return mainAdapter;
        }

        @Override
        protected void onPostExecute(ParseQueryAdapter<Listing> result) {

            // Call onRefreshComplete when the list has been refreshed.
            pullToRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        updateListings();
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
//        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) vv
//                .getLayoutParams();
//        mlp.setMargins(0, 0, 0, 0);
    }

}
