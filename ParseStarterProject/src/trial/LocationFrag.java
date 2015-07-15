package trial;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.starter.R;

/**
 * Created by samfierro on 7/4/15.
 */
public class LocationFrag extends android.support.v4.app.Fragment {

    public static LocationFrag newInstance(){

        LocationFrag fragment = new LocationFrag();
        return fragment;

    }

    public LocationFrag(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.location_frag,container,false);
        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(4);

    }
}
