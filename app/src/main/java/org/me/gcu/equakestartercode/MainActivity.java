package org.me.gcu.equakestartercode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.me.gcu.equakestartercode.fragments.HomeFragment;
import org.me.gcu.equakestartercode.fragments.MapFragment;
import org.me.gcu.equakestartercode.fragments.SearchFragment;

/**
 * @author Ted Hilton - S1708998
 */
public class MainActivity extends AppCompatActivity {

    final Fragment homeFragment = new HomeFragment();
    final Fragment mapFragment = new MapFragment();
    final Fragment searchFragment = new SearchFragment();

    private Fragment active = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("Generating earthquakes");
        new GetData().execute();

        loadFragment(homeFragment);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);
        // Selects the home icon
        bottomNavigation.setSelectedItemId(bottomNavigation.getMenu().getItem(1).getItemId());

        //new Thread(new Task(urlSource)).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        boolean startTimer = settings.getBoolean("START_TIMER", false);
        if(!startTimer) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("START_TIMER", true);
            editor.apply();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    System.out.println("TIMER TICKED!");
                    new GetData().execute();
                    handler.postDelayed(this, 10000);
                }
            }, 10000);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("STOPPED!");
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("START_TIMER");
        editor.putBoolean("START_TIMER", false);
        editor.apply();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                switch(item.getItemId()) {
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        break;
                    case R.id.nav_search:
                        getSupportFragmentManager().beginTransaction().hide(active).show(searchFragment).commit();
                        active = searchFragment;
                        break;
                    case R.id.nav_map:
                        getSupportFragmentManager().beginTransaction().hide(active).show(mapFragment).commit();
                        active = mapFragment;
                        break;
                }
                // Return true to change fragment
                //return true;
                loadFragment(active);
                return true;
            };

    public void loadFragment (Fragment fragment){
        FragmentTransaction ft  = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    public class GetData extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");

                BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));

                String inputLine;
                while ((inputLine = input.readLine()) != null) {
                    result.append(inputLine);
                }
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            HomeFragment.earthquakes = null;
            try {
                XMLPullParserHandler parser = new XMLPullParserHandler();
                InputStream is = new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8));
                HomeFragment.earthquakes = parser.parse(is);
                HomeFragment.earthquakes = reorderEarthquake(HomeFragment.earthquakes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    // List which loops through array ordering it by magnitude
    private List<Earthquake> reorderEarthquake(List<Earthquake> unsortedEarthquakes) {

        List<Earthquake> organisedEarthquake = new ArrayList<>();
        int earthquakeSize = unsortedEarthquakes.size();

        for(int i = 0; i < earthquakeSize; i++) {
            int index = 0;
            for(int j = 0; j < unsortedEarthquakes.size(); j++) {
                if(unsortedEarthquakes.get(j).getMagnitude() < unsortedEarthquakes.get(index).getMagnitude()) {
                    index = j;
                }
            }
            organisedEarthquake.add(unsortedEarthquakes.get(index));
            unsortedEarthquakes.remove(index);
        }
        return organisedEarthquake;
    }

}