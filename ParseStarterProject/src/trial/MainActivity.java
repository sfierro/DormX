package trial;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.parse.ParseGeoPoint;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.starter.R;

import java.util.List;

import listings.ListingsFrag;
import listings.ListingsFragAdapter;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Listing listing;
    private Listing thisListing;
    private Boolean goBackToPrevious = false;
    private String search;

    private Boolean goBackToListingsFrag;

    /**
     * Used to store the last  titlescreen. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!(ParseUser.getCurrentUser().getBoolean("emailVerified"))) {
            showAlertDialog(MainActivity.this, "Please verify email account",
                    "You must verify your school email account before continuing.", false);
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();
        listing = new Listing();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //when app is first started get user's current location
        ParseGeoPoint point = getLocation();
        ParseUser.getCurrentUser().put("current", point);
        ParseUser.getCurrentUser().saveInBackground();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        // NEW STUFF
        if(position == 0){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ListingsFrag.newInstance())
                    .commit();
        } else if (position == 1){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ProfileFrag.newInstance())
                    .commit();
        } else if (position == 2){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, LocationFrag.newInstance())
                    .commit();
        } else if (position == 3){
            AlertDialog.Builder db = new AlertDialog.Builder(MainActivity.this);
            db.setTitle("Are you sure?");
            db.setPositiveButton("Sign out", new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ParseUser.getCurrentUser().logOut();
                            finish();
                        }
                    });
            db.setNegativeButton("Cancel", new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    });
            db.show();
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section4);
                break;
            case 4:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public void restoreActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    public Listing getCurrentListing() {
        return listing;
    }

    //gets users location
    public ParseGeoPoint getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);
        double lat =  location.getLatitude();
        double lng = location.getLongitude();
        ParseGeoPoint geoPoint = new ParseGeoPoint(lat,lng);
        return geoPoint;
    }

    @Override
    public void onBackPressed() {
        // if Navigation Drawer is open
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.getDrawerLayout().closeDrawers();
        }

        // go back to Listings Frag (ProfileFrag, PaymentFrag, LocationFrag)
        else if (goBackToListingsFrag) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ListingsFrag.newInstance())
                    .commit();
            mNavigationDrawerFragment.selectItem(0);
        }

        // to go home when you press back button (when in ListingsFrag)
        else if (!goBackToPrevious) {
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }

        // regular back button functionality
        else {
            super.onBackPressed();
        }
    }

    public Double getLat() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);
        double lat =  location.getLatitude();
        return lat;
    }

    public Double getLon() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.NETWORK_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);
        double lng = location.getLongitude();
        return lng;
    }

    public void makeNewListing() {
        listing = new Listing();
    }

    public void setListing(Listing lst){
        thisListing = lst;
    }

    public Listing getListing() {
        return thisListing;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
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
                finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    public void goBackToPrevious(Boolean bool) {
        goBackToPrevious = bool;
    }

    public void goBackToListingsFrag(Boolean bool) {
        goBackToListingsFrag = bool;
    }

    public NavigationDrawerFragment getNav(){
        return mNavigationDrawerFragment;
    }

}
