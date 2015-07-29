package trial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.parse.starter.R;

import venmo.VenmoLibrary;

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
        final Context c = (Context) this.getActivity();
        Button venmo = (Button) rootView.findViewById(R.id.venmo);
        venmo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VenmoLibrary.isVenmoInstalled(c)) {
                    Intent venmoIntent = VenmoLibrary.openVenmoPayment("2768", "DormX", "Lauren-Benitez-1", "0.10", "test", "charge");
                    startActivityForResult(venmoIntent, 1); }
                else {
                    Toast.makeText(c, "Please intall Venmo app from Play Store", Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(3);

    }
}
