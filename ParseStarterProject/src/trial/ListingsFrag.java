package trial;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.parse.starter.R;

import loginusingparsesdk.CameraFragment;

/**
 * Created by samfierro on 7/4/15.
 */
public class ListingsFrag extends android.support.v4.app.Fragment {

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
}
