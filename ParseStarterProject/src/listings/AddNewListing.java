package listings;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.R;

import trial.Listing;
import listings.ListingsFrag;
import trial.MainActivity;
import listings.NothingSelectedSpinnerAdapter;

/*
 * This fragment manages the data entry for a
 * new Listing object. It lets the user input a
 * listing name, take a
 * photo, etc.
 */
public class AddNewListing extends android.support.v4.app.Fragment {

    public static AddNewListing newInstance(){

        AddNewListing fragment = new AddNewListing();
        return fragment;

    }

    private ImageButton photoButton;
    private ParseImageView listingPreview;
    private EditText editTextTitle;
    private EditText editTextPrice;
    private EditText editTextDescription;
    private Spinner spinner;
    private int pos;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.add_new_listing, parent, false);


        spinner = (Spinner) v.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.category_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Please select one");

        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.contact_spinner_row_nothing_selected,
                        this.getActivity()));

        editTextTitle = (EditText) v.findViewById(R.id.add_title);
        editTextPrice = (EditText) v.findViewById(R.id.add_price);
        editTextDescription = (EditText) v.findViewById(R.id.add_description);

        editTextTitle.setText("");
        editTextPrice.setText("");
        editTextDescription.setText("");

        Button post = (Button) v.findViewById(R.id.post_btn);
        post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Listing listing = ((MainActivity) getActivity()).getCurrentListing();

                // When the user clicks "Save," upload the listing to Parse
                // Add data to the listing object:
                listing.setTitle(editTextTitle.getText().toString());
                listing.setPrice("$"+editTextPrice.getText().toString());
                listing.setDescription(editTextDescription.getText().toString());
                listing.setCategory(spinner.getSelectedItem().toString());

                // Associate the listing with the current user
                listing.setAuthor(ParseUser.getCurrentUser());

                ParseGeoPoint geoPoint = ((MainActivity) getActivity()).getLocation();

                listing.setLocation(geoPoint);

                // If the user added a photo, that data will be
                // added in the CameraFragment

                // Save the Listing and return
                listing.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                        } else {
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "Error saving: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });

                postListing();
            }

        });


        photoButton = ((ImageButton) v.findViewById(R.id.photo_button));
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pos = spinner.getSelectedItemPosition();
                ((MainActivity) getActivity()).getCurrentListing().setTitle(editTextTitle.getText().toString());
                ((MainActivity) getActivity()).getCurrentListing().setPrice(editTextPrice.getText().toString());
                ((MainActivity) getActivity()).getCurrentListing().setDescription(editTextDescription.getText().toString());
                ((MainActivity) getActivity()).getCurrentListing().setPos(pos);
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextTitle.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editTextPrice.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editTextDescription.getWindowToken(), 0);

                startCamera();
            }
        });

        // Until the user has taken a photo, hide the preview
        listingPreview = (ParseImageView) v.findViewById(R.id.meal_preview_image);
        listingPreview.setVisibility(View.INVISIBLE);

        return v;
    }


    public void postListing(){
        ((MainActivity) getActivity()).makeNewListing();
        android.support.v4.app.Fragment listings = new ListingsFrag();
        android.support.v4.app.FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,listings);
        transaction.commit();
//        android.support.v4.app.FragmentManager fm = this.getActivity().getSupportFragmentManager();
//		fm.popBackStack("ListingsFrag",
//				FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    /*
     * All data entry about a Listing object is managed from the MainActivity.
     * When the user wants to add a photo, we'll start up a custom
     * CameraFragment that will let them take the photo and save it to the Listing
     * object owned by the MainActivity. Create a new CameraFragment, and swap
     * the contents of the fragment container.
     */
    public void startCamera() {
        android.support.v4.app.Fragment cameraFragment = new CameraFragment();
        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.container, cameraFragment);
        transaction.commit();
    }

    /*
     * On resume, check and see if a listing photo has been set from the
     * CameraFragment. If it has, load the image in this fragment and make the
     * preview image visible.
     */
    @Override
    public void onResume() {
        super.onResume();
        editTextTitle.setText(((MainActivity) getActivity()).getCurrentListing().getTitle());
        editTextPrice.setText(((MainActivity) getActivity()).getCurrentListing().getPrice());
        editTextDescription.setText(((MainActivity) getActivity()).getCurrentListing().getDescription());
        spinner.setSelection(((MainActivity) getActivity()).getCurrentListing().getPos());

        ParseFile photoFile = ((MainActivity) getActivity())
                .getCurrentListing().getPhotoFile();
        if (photoFile != null) {
            listingPreview.setParseFile(photoFile);
            listingPreview.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, com.parse.ParseException e) {
                    listingPreview.setVisibility(View.VISIBLE);
                }
            });
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(1);
    }

}

