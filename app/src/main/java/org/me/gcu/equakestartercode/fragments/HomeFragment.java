package org.me.gcu.equakestartercode.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.me.gcu.equakestartercode.Earthquake;
import org.me.gcu.equakestartercode.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ted Hilton - S1708998
 */
public class HomeFragment extends Fragment {

    public static List<Earthquake> earthquakes;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        listView = rootView.findViewById(R.id.listView);
        Button refresh = rootView.findViewById(R.id.refreshButton);
        refresh.setOnClickListener(v -> {
            if(earthquakes != null) {
                generateList();
            }
        });

        if(earthquakes != null) {
            generateList();
        }

        listView.setOnItemClickListener((adpterView, view, position, id) -> {
            Fragment earthquakeInfo = new EarthquakeInfoFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Bundle data = new Bundle();

            Format formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");

            data.putString("LOCATION", earthquakes.get(position).getLocation());
            data.putString("DATE", formatter.format(earthquakes.get(position).getDate()));
            data.putInt("DEPTH", earthquakes.get(position).getDepth());
            data.putDouble("MAGNITUDE", earthquakes.get(position).getMagnitude());
            data.putDouble("LAT", earthquakes.get(position).getGeoLat());
            data.putDouble("LONG", earthquakes.get(position).getGeoLong());

            earthquakeInfo.setArguments(data);
            transaction.replace(R.id.fragment_container, earthquakeInfo); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });

        return rootView;
    }

    public void generateList() {
        List<String> string_list = new ArrayList<>();

        for(int i = 0; i < earthquakes.size(); i++) {
            string_list.add("Location: " + earthquakes.get(i).getLocation() + ": Strength: " + earthquakes.get(i).getMagnitude());
        }

        // Create an ArrayAdapter from List
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, string_list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                return super.getView(position, convertView, parent);
            }
        };

        // DataBind ListView with items from ArrayAdapter
        listView.setAdapter(arrayAdapter);
    }

}
