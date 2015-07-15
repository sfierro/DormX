package trial;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.parse.starter.R;

/**
 * Created by samfierro on 7/4/15.
 */
public class ProfileFrag extends Fragment {


    private VideoView video1;

    public static ProfileFrag newInstance(){

        ProfileFrag fragment = new ProfileFrag();
        return fragment;

    }

    public ProfileFrag(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.profile_frag, container, false);

        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(2);

    }

}
