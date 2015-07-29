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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.starter.R;

import listings.CameraFragment2;
import venmo.VenmoLibrary;

/**
 * Created by samfierro on 7/4/15.
 */
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

    public static ProfileFrag newInstance(){

        ProfileFrag fragment = new ProfileFrag();
        return fragment;

    }

    public ProfileFrag(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_frag, container, false);
        add_pic = (Button) rootView.findViewById(R.id.profpic);
        enter_name = (EditText) rootView.findViewById(R.id.enter_name);
        name = (TextView) rootView.findViewById(R.id.name);
        save_name_btn = (Button) rootView.findViewById(R.id.save_name_btn);
        edit_name_btn = (Button) rootView.findViewById(R.id.edit_name_btn);
        user = ParseUser.getCurrentUser();
        name.setText(user.getString("Name"));
        prof_pic = (ParseImageView) rootView.findViewById(R.id.profpic1);
        cam = (ImageButton) rootView.findViewById(R.id.cam);

        prof = ParseUser.getCurrentUser().getParseFile("prof");
        ParseUser.getCurrentUser().saveInBackground();

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
                startCamera();
            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
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

}
