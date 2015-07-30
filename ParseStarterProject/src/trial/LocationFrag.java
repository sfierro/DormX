package trial;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;
import com.parse.starter.R;

import java.lang.reflect.Field;

/**
 * Created by samfierro on 7/4/15.
 */
public class LocationFrag extends android.support.v4.app.Fragment {

    com.google.android.gms.maps.SupportMapFragment fragment;
    com.google.android.gms.maps.GoogleMap map;
    Button mi1; Button mi5; Button mi10; Button mi20;
    Circle circle;
    Double lat;
    Double lon;
    LatLng coordinate;

    public static LocationFrag newInstance(){

        LocationFrag fragment = new LocationFrag();
        return fragment;

    }

    public LocationFrag(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.location_frag,container,false);

        ((MainActivity) getActivity()).goBackToListingsFrag(true);
        ((MainActivity) getActivity()).goBackToPrevious(false);

        lat = ((MainActivity) getActivity()).getLat();
        lon = ((MainActivity) getActivity()).getLon();

        coordinate = new LatLng(lat, lon);

        mi1 = (Button) rootView.findViewById(R.id.mi1);
        mi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setRadius(1609.34);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 10.0f));
                ParseUser.getCurrentUser().put("miles", "1");
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        mi5 = (Button) rootView.findViewById(R.id.mi5);
        mi5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setRadius(8046.72);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 10.0f));
                ParseUser.getCurrentUser().put("miles", "1");
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        mi10 = (Button) rootView.findViewById(R.id.mi10);
        mi10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setRadius(16093.4);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 10.0f));
                ParseUser.getCurrentUser().put("miles", "1");
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        mi20 = (Button) rootView.findViewById(R.id.mi20);
        mi20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setRadius(32186.9);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 10.0f));
                ParseUser.getCurrentUser().put("miles", "1");
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(3);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, fragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = fragment.getMap();
            setUpMap();
        }
    }

    public void setUpMap() {

        map.setMyLocationEnabled(true);
        //starting location (current location) and zoom level
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 10.0f));
        //built-in toolbar disabled
        map.getUiSettings().setMapToolbarEnabled(false);

        circle = map.addCircle(new CircleOptions()
                .center(coordinate)
                .radius(8046.72)
                .strokeColor(Color.argb(100, 127, 255, 0))
                .fillColor(Color.argb(100, 127, 255, 0)));
    }}