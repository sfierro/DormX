package trial;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.starter.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import loginusingparsesdk.CameraFragment;
import venmo.VenmoLibrary;
import venmo.VenmoLibrary.VenmoResponse;

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
        final Context c = (Context) this.getActivity();
        Button venmo = (Button) rootView.findViewById(R.id.venmo);
        venmo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VenmoLibrary.isVenmoInstalled(c)) {
                Intent venmoIntent = VenmoLibrary.openVenmoPayment("2768", "DormX", "Lauren-Benitez-1", "0.10", "test", "charge");
                startActivityForResult(venmoIntent, 1); }
                else {
                    Toast.makeText(c,"Please intall Venmo app from Play Store",Toast.LENGTH_LONG).show();
                }
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
