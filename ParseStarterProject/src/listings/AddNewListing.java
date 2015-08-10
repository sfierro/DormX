package listings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import trial.Listing;
import trial.MainActivity;
import listings.NothingSelectedSpinnerAdapter;
import trial.ProfileFrag;

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
    int clicked;
    private int PICK_IMAGE_REQUEST = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.add_new_listing, parent, false);

        ((MainActivity) getActivity()).goBackToPrevious(true);
        ((MainActivity) getActivity()).goBackToListingsFrag(false);
        ((MainActivity) getActivity()).setActionBarTitle("New Listing");

        if (ParseUser.getCurrentUser().getString("venmo") == null) {
            showAlertDialog(this.getActivity(), "No Venmo username found",
                    "Please enter your venmo username before posting a listing.", false);
        }

        //setting up category spinner
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

                if (editTextTitle.getText().toString().equals("") || editTextPrice.getText().toString().equals("") || editTextDescription.getText().toString().equals("") || spinner.getSelectedItem() == null
                        || listingPreview.getDrawable() == null) {
                    showAlertDialog2(getActivity(), "Not all fields completed",
                            "Please make sure you have completed all fields.", false);
                } else {

                Listing listing = ((MainActivity) getActivity()).getCurrentListing();

                // When the user clicks "Save," upload the listing to Parse
                // Add data to the listing object:
                listing.setTitle(editTextTitle.getText().toString());
                listing.setPrice(editTextPrice.getText().toString());
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
//                            Toast.makeText(
//                                    getActivity(),
//                                    "Error saving: " + e.getMessage(),
//                                    Toast.LENGTH_SHORT).show();
                        }} });
                postListing();
            }}

        });


        photoButton = ((ImageButton) v.findViewById(R.id.photo_button));
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //putting users inputs into Listing class before taking picture to retrieve when frag
                //is started again so edit texts aren't empty
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

                CharSequence decisions[] = new CharSequence[] {"Take picture", "Upload photo"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(decisions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on decisions[which]
                        clicked = which;
                        if (which == 0) {
                            startCamera();
                        } else {

                            Intent intent = new Intent();

                            // Show only images, no videos or anything else
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);

                            // Always show the chooser (if there are multiple options available)
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                        }
                    }

                });
                builder.show();

            }
        });

        // Until the user has taken a photo, hide the preview
        listingPreview = (ParseImageView) v.findViewById(R.id.meal_preview_image);
        listingPreview.setVisibility(View.INVISIBLE);

        return v;
    }


    public void postListing(){
        //make new listing in main activity so setters and getters are null again
        ((MainActivity) getActivity()).makeNewListing();

        android.support.v4.app.Fragment listings = new ListingsFrag();
        android.support.v4.app.FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, listings);
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
        transaction.addToBackStack("AddNewListing");
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
        ((MainActivity) getActivity()).goBackToPrevious(true);
        ((MainActivity) getActivity()).goBackToListingsFrag(false);
        ((MainActivity) getActivity()).setActionBarTitle("New Listing");
        //if user has inputted info into edit texts set the text to what is was before taking pic
        editTextTitle.setText(((MainActivity) getActivity()).getCurrentListing().getTitle());
        editTextPrice.setText(((MainActivity) getActivity()).getCurrentListing().getPrice());
        editTextDescription.setText(((MainActivity) getActivity()).getCurrentListing().getDescription());
        spinner.setSelection(((MainActivity) getActivity()).getCurrentListing().getPos());

        //setting photo
        if (clicked == 0) {
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
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(1);
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon(R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                android.support.v4.app.Fragment profileFrag = new ProfileFrag();
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.container, profileFrag);
                transaction.addToBackStack("AddNewListing");
                ((MainActivity) getActivity()).getNav().selectItem(1);
                transaction.commit();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void showAlertDialog2(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

                // Setting alert dialog icon
                alertDialog.setIcon(R.drawable.fail);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                //nothing
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);



                Bitmap listingImageScaled = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
//                Bitmap listingImageScaled = Bitmap.createScaledBitmap(bitmap, 400, 700
//                        * bitmap.getHeight() / bitmap.getWidth(), false);


                // Override Android default landscape orientation and save portrait


                Matrix matrix = new Matrix();

                matrix.postRotate(getOrientation(getActivity(), uri));


                Bitmap rotatedScaledListingImage = Bitmap.createBitmap(listingImageScaled, 0,
                        0, listingImageScaled.getWidth(), listingImageScaled.getHeight(), matrix,true);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                rotatedScaledListingImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                byte[] scaledData = bos.toByteArray();

                // Save the scaled image to Parse
                final ParseFile photoFile = new ParseFile("listing_photo.jpg", scaledData);
                photoFile.saveInBackground(new SaveCallback() {

                    public void done(ParseException e) {
                        if (e != null) {
                            Toast.makeText(getActivity(),
                                    "Error saving: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            ((MainActivity) getActivity()).getCurrentListing().setPhotoFile(
                                    photoFile);

                            ParseUser.getCurrentUser().saveInBackground();
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
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                null, null, null);

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            } else {
                return -1;
            }
        } finally {
            cursor.close();
        }
    }

}

