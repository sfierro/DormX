package trial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.starter.R;

import org.w3c.dom.Text;

import listings.CameraFragment2;
import listings.ListingsFragAdapter;
import listings.ListingsFragDetailsFrag;
import listings.MyListingsFragAdapter;
import listings.MyListingsFragDetailsFrag;
import venmo.VenmoLibrary;

public class ProfileFrag extends Fragment {


    private EditText enter_name;
    private TextView name;
    private Button save_name_btn;
    private Button edit_name_btn;
    private String prof_name;
    private ParseUser user;
    private ParseImageView prof_pic;
    private Button add_pic;
    private ParseFile prof;
    private ImageButton cam;
    private EditText venmo_edit_text;
    private TextView venmo_name;
    private Button submit;
    private MyListingsFragAdapter mainAdapter;
    private ListView list;

    public static ProfileFrag newInstance(){

        ProfileFrag fragment = new ProfileFrag();
        return fragment;

    }

    public ProfileFrag(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_frag, container, false);

        ((MainActivity) getActivity()).goBackToPrevious(false);
        ((MainActivity) getActivity()).goBackToListingsFrag(true);
        ((MainActivity) getActivity()).setActionBarTitle("Profile");

        //set listview adapter
        mainAdapter = new MyListingsFragAdapter(this.getActivity());
        ListView lv = (ListView) rootView.findViewById(R.id.list2);
        lv.setAdapter(mainAdapter);
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
                transaction.replace(R.id.container, MyListingsFragDetailsFrag.newInstance());
                transaction.addToBackStack("ProfileFrag");
                transaction.commit();
            }
        });

        add_pic = (Button) rootView.findViewById(R.id.profpic);
        enter_name = (EditText) rootView.findViewById(R.id.enter_name);
        name = (TextView) rootView.findViewById(R.id.name);
        save_name_btn = (Button) rootView.findViewById(R.id.save_name_btn);
        edit_name_btn = (Button) rootView.findViewById(R.id.edit_name_btn);
        user = ParseUser.getCurrentUser();
        name.setText(user.getString("Name"));
        prof_pic = (ParseImageView) rootView.findViewById(R.id.profpic1);
        cam = (ImageButton) rootView.findViewById(R.id.cam);
        venmo_edit_text = (EditText) rootView.findViewById(R.id.venmo_edit_text);
        venmo_name = (TextView) rootView.findViewById(R.id.venmo_name);
        submit = (Button) rootView.findViewById(R.id.submit);

        prof = ParseUser.getCurrentUser().getParseFile("prof");
        ParseUser.getCurrentUser().saveInBackground();

        //if user has already set a profile picture, load it.
        if (prof != null) {
            prof_pic.setParseFile(prof);
            prof_pic.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, com.parse.ParseException e) {
                    prof_pic.setVisibility(View.VISIBLE);
                    cam.setVisibility(View.VISIBLE);
                    add_pic.setVisibility(View.GONE);
                }
            });
        }


        if (user.getString("Name")==null) {
            enter_name.setVisibility(View.VISIBLE);
            edit_name_btn.setVisibility(View.GONE);
            save_name_btn.setVisibility(View.VISIBLE);

        }

        if (user.getString("venmo") != null) {
            venmo_name.setText(user.getString("venmo"));
            venmo_name.setVisibility(View.VISIBLE);
            venmo_edit_text.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                venmo_name.setText(venmo_edit_text.getText().toString());
                ParseUser.getCurrentUser().put("venmo",venmo_edit_text.getText().toString());
                venmo_name.setVisibility(View.VISIBLE);
                venmo_edit_text.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
            }
            });

        save_name_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                prof_name = enter_name.getText().toString();
                user.put("Name", prof_name);
                user.saveInBackground();
                name.setText(user.getString("Name"));
                name.setVisibility(View.VISIBLE);
                edit_name_btn.setVisibility(View.VISIBLE);
                enter_name.setVisibility(View.GONE);
                save_name_btn.setVisibility(View.GONE);
            }
        });

        edit_name_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                name.setVisibility(View.GONE);
                edit_name_btn.setVisibility(View.GONE);
                enter_name.setText(name.getText().toString());
                enter_name.setVisibility(View.VISIBLE);
                save_name_btn.setVisibility(View.VISIBLE);
            }
        });

        add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(enter_name.getWindowToken(), 0);
                startCamera();
            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(enter_name.getWindowToken(), 0);
                startCamera();
            }
        });

        return rootView;

    }


    public void startCamera() {
        android.support.v4.app.Fragment cameraFragment = new CameraFragment2();
        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.container, cameraFragment);
        transaction.addToBackStack("ProfileFrag");
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListings();
        ParseFile photoFile = ParseUser.getCurrentUser().getParseFile("prof");
        ParseUser.getCurrentUser().saveInBackground();
        if (photoFile != null) {
            prof_pic.setParseFile(photoFile);
            prof_pic.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, com.parse.ParseException e) {
                    prof_pic.setVisibility(View.VISIBLE);
                    cam.setVisibility(View.VISIBLE);
                    add_pic.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(2);

    }

    public void updateListings(){
        mainAdapter.loadObjects();
    }

}
