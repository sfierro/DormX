package trial;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.starter.R;

/**
 * Created by samfierro on 7/4/15.
 */
public class PaymentFrag extends Fragment {

    public static PaymentFrag newInstance(){

        PaymentFrag fragment = new PaymentFrag();
        return fragment;

    }

    public PaymentFrag(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.payment_frag,container,false);
        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(3);

    }
}
