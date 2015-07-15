package trial;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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

import com.parse.ParseImageView;
import com.parse.starter.R;

import loginusingparsesdk.CameraFragment;

/*
 * This fragment manages the data entry for a
 * new Meal object. It lets the user input a
 * meal name, give it a rating, and take a
 * photo. If there is already a photo associated
 * with this meal, it will be displayed in the
 * preview at the bottom, which is a standalone
 * ParseImageView.
 */
public class AddNewListing extends android.support.v4.app.Fragment {

    public static AddNewListing newInstance(){

        AddNewListing fragment = new AddNewListing();
        return fragment;

    }

    private ImageButton photoButton;
    private Button saveButton;
    private Button cancelButton;
    private TextView mealName;
    private Spinner mealRating;
    private ParseImageView mealPreview;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle SavedInstanceState) {

        View v = inflater.inflate(R.layout.add_new_listing, parent, false);

        Spinner spinner = (Spinner) v.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.category_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Please select one");

        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.contact_spinner_row_nothing_selected,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this.getActivity()));

        Button post = (Button) v.findViewById(R.id.post_btn);
        post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                postListing();
            }

        });

        mealName = ((EditText) v.findViewById(R.id.add_title));

        // The mealRating spinner lets people assign favorites of meals they've
        // eaten.
        // Meals with 4 or 5 ratings will appear in the Favorites view.
        mealRating = ((Spinner) v.findViewById(R.id.rating_spinner));
//        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
//                .createFromResource(getActivity(), R.array.ratings_array,
//                        android.R.layout.simple_spinner_dropdown_item);
//        mealRating.setAdapter(spinnerAdapter);

        photoButton = ((ImageButton) v.findViewById(R.id.photo_button));
//        photoButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager) getActivity()
//                        .getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mealName.getWindowToken(), 0);
//                startCamera();
//            }
//        });

        saveButton = ((Button) v.findViewById(R.id.post_btn));
//        saveButton.setOnClickListener(new View.OnClickListener() {

//            @Override
//            public void onClick(View v) {
//                Meal meal = ((NewMealActivity) getActivity()).getCurrentMeal();
//
//                // When the user clicks "Save," upload the meal to Parse
//                // Add data to the meal object:
//                meal.setTitle(mealName.getText().toString());
//
//                // Associate the meal with the current user
//                meal.setAuthor(ParseUser.getCurrentUser());
//
//                // Add the rating
//                meal.setRating(mealRating.getSelectedItem().toString());
//
//                // If the user added a photo, that data will be
//                // added in the CameraFragment
//
//                // Save the meal and return
//                meal.saveInBackground(new SaveCallback() {
//
//                    @Override
//                    public void done(ParseException e) {
//                        if (e == null) {
//                            getActivity().setResult(Activity.RESULT_OK);
//                            getActivity().finish();
//                        } else {
//                            Toast.makeText(
//                                    getActivity().getApplicationContext(),
//                                    "Error saving: " + e.getMessage(),
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                });
//
//            }
//        });


        // Until the user has taken a photo, hide the preview
        mealPreview = (ParseImageView) v.findViewById(R.id.meal_preview_image);
        mealPreview.setVisibility(View.INVISIBLE);

        return v;
    }

    public void postListing(){
        android.support.v4.app.FragmentManager fm = this.getActivity().getSupportFragmentManager();
		fm.popBackStack("ListingsFrag",
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    /*
     * All data entry about a Meal object is managed from the NewMealActivity.
     * When the user wants to add a photo, we'll start up a custom
     * CameraFragment that will let them take the photo and save it to the Meal
     * object owned by the NewMealActivity. Create a new CameraFragment, swap
     * the contents of the fragmentContainer (see activity_new_meal.xml), then
     * add the NewMealFragment to the back stack so we can return to it when the
     * camera is finished.
     */
    public void startCamera() {
        Fragment cameraFragment = new CameraFragment();
        FragmentTransaction transaction = getActivity().getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.container, cameraFragment);
        transaction.addToBackStack("NewMealFragment");
        transaction.commit();
    }

    /*
     * On resume, check and see if a meal photo has been set from the
     * CameraFragment. If it has, load the image in this fragment and make the
     * preview image visible.
     */
//    @Override
//    public void onResume() {
//        super.onResume();
//        ParseFile photoFile = ((NewMealActivity) getActivity())
//                .getCurrentMeal().getPhotoFile();
//        if (photoFile != null) {
//            mealPreview.setParseFile(photoFile);
//            mealPreview.loadInBackground(new GetDataCallback() {
//                @Override
//                public void done(byte[] data, ParseException e) {
//                    mealPreview.setVisibility(View.VISIBLE);
//                }
//            });
//        }
//    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(1);
    }

}

