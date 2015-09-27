package com.afrolkin.chameleon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
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
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private TextView vibrantDarkView;
        private TextView vibrantLightView;
        private TextView mutedView;
        private TextView mutedDarkView;
        private TextView mutedLightView;
        private Button mCaptureButton;
        private ImageView image;
        private TextView vibrantView;


        private final int CAMERA_REQUEST_CODE = 2222;
        private Palette.Swatch vibrant;
        private Palette.Swatch vibrantDark;
        private Palette.Swatch vibrantLight;
        private Palette.Swatch muted;
        private Palette.Swatch mutedDark;
        private Palette.Swatch mutedLight;


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mCaptureButton = (Button) rootView.findViewById(R.id.capture_button);
            image = (ImageView) rootView.findViewById(R.id.captured_image);
            vibrantView = (TextView) rootView.findViewById(R.id.vibrant);
            vibrantDarkView = (TextView) rootView.findViewById(R.id.vibrant_dark);
            vibrantLightView = (TextView) rootView.findViewById(R.id.vibrant_light);
            mutedView = (TextView) rootView.findViewById(R.id.muted);
            mutedLightView = (TextView) rootView.findViewById(R.id.muted_light);
            mutedDarkView = (TextView) rootView.findViewById(R.id.muted_dark);


            mCaptureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                }
            });

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // To Handle Camera Result
            if (data != null && requestCode == CAMERA_REQUEST_CODE) {

                Bitmap pictureObject = (Bitmap) data.getExtras().get("data");

                image.setImageBitmap(pictureObject);

                Palette.generateAsync(pictureObject, new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        vibrant = palette.getVibrantSwatch();
                        vibrantDark = palette.getDarkVibrantSwatch();
                        vibrantLight = palette.getLightVibrantSwatch();
                        muted = palette.getMutedSwatch();
                        mutedDark = palette.getDarkMutedSwatch();
                        mutedLight = palette.getLightMutedSwatch();

                        if (vibrant != null) {
                            vibrantView.setText("Vibrant");
                            vibrantView.setBackgroundColor(vibrant.getRgb());
                            vibrantView.setTextColor(vibrant.getTitleTextColor());
                        }

                        if (vibrantLight != null) {
                            vibrantLightView.setText("Vibrant Light");
                            vibrantLightView.setBackgroundColor(vibrantLight.getRgb());
                            vibrantLightView.setTextColor(vibrantLight.getTitleTextColor());
                        }

                        if (vibrantDark != null) {
                            vibrantDarkView.setText("Vibrant Dark");
                            vibrantDarkView.setBackgroundColor(vibrantDark.getRgb());
                            vibrantDarkView.setTextColor(vibrantDark.getTitleTextColor());
                        }

                        if (muted != null) {
                            mutedView.setText("Muted");
                            mutedView.setBackgroundColor(muted.getRgb());
                            mutedView.setTextColor(muted.getTitleTextColor());
                        }

                        if (mutedLight != null) {
                            mutedLightView.setText("Muted Light");
                            mutedLightView.setBackgroundColor(mutedLight.getRgb());
                            mutedLightView.setTextColor(mutedLight.getTitleTextColor());
                        }

                        if (mutedDark != null) {
                            mutedDarkView.setText("Muted Dark");
                            mutedDarkView.setBackgroundColor(mutedDark.getRgb());
                            mutedDarkView.setTextColor(mutedDark.getTitleTextColor());
                        }
                    }
                });
            }
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
