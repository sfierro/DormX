package trial;

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

import java.util.ArrayList;
import java.util.List;

import adapters.AppliancesListingsFragAdapter;
import adapters.ClothesListingsFragAdapter;
import adapters.ElectronicsListingsFragAdapter;
import adapters.FurnitureListingsFragAdapter;
import adapters.OtherListingsFragAdapter;
import adapters.SearchListingsFragAdapter;
import adapters.TextbooksListingsFragAdapter;
import adapters.TicketsListingsFragAdapter;
import listings.ListingsFragAdapter;
import listings.ListingsFragDetailsFrag;
import trial.Listing;
import trial.MainActivity;

public class Inbox extends android.support.v4.app.Fragment {

    ParseQueryAdapter<Convo> convoAdapter;
    ListView messages;

    public static Inbox newInstance(){

        Inbox fragment = new Inbox();
        return fragment;

    }

    public Inbox(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       final View rootView = inflater.inflate(R.layout.inbox,container,false);

        ((MainActivity) getActivity()).goBackToPrevious(false);
        ((MainActivity) getActivity()).goBackToListingsFrag(false);
        ((MainActivity)getActivity()).setActionBarTitle("Chat Inbox");

        messages = (ListView) rootView.findViewById(R.id.messages);
        //set listView adapter for conversations
        convoAdapter = new ConvoAdapter(getActivity());
        messages.setAdapter(convoAdapter);

        messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //sends the conversation that is specified with which item clicked on listview
                Convo convo = (Convo) parent.getItemAtPosition(position);
                ((MainActivity) getActivity()).setConvo(convo);

                Message message = ((MainActivity)getActivity()).getCurrentMessage();
                message.put("ID",convo.getObjectId());

                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.container, ChatFrag.newInstance());
                transaction.addToBackStack("Inbox");
                transaction.commit();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(3);

    }

}
