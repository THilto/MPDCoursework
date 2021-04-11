package org.me.gcu.equakestartercode.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.gcu.equakestartercode.R;

/**
 * @author Ted Hilton - S1708998
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap gMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        // UK Lat Lng position
        LatLng ukPosition = new LatLng(55.37, 3.43);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(ukPosition));

        for(int i = 0; i < HomeFragment.earthquakes.size(); i++) {
            Double geoLat = HomeFragment.earthquakes.get(i).getGeoLat();
            Double geoLong = HomeFragment.earthquakes.get(i).getGeoLong();
            String title = HomeFragment.earthquakes.get(i).getLocation();

            LatLng position = new LatLng(geoLat, geoLong);

            // Red, Yellow, Green markers depending on the earthquake magnitude
            double magnitude = HomeFragment.earthquakes.get(i).getMagnitude();
            if(magnitude >= 2) {
                createMarker(position, title, BitmapDescriptorFactory.HUE_RED);
            } else if(magnitude >= 1) {
                createMarker(position, title, BitmapDescriptorFactory.HUE_YELLOW);
            } else if(magnitude >= 0) {
                createMarker(position, title, BitmapDescriptorFactory.HUE_GREEN);
            }
        }
    }

    // Code taken from https://stackoverflow.com/questions/30569854/adding-multiple-markers-in-google-maps-api-v2-android
    protected Marker createMarker(LatLng position, String title, float colour) {
        return gMap.addMarker(new MarkerOptions()
                .position(position)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(colour)));
    }

}
