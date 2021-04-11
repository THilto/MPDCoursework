package org.me.gcu.equakestartercode.fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.me.gcu.equakestartercode.Earthquake;
import org.me.gcu.equakestartercode.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ted Hilton - S1708998
 */
public class SearchFragment extends Fragment implements OnClickListener {

    SimpleAdapter arrayAdapter;

    private TextView fromDate;
    private TextView toDate;
    private ListView earthquakeInfoListview;

    private DatePickerDialog.OnDateSetListener beforeDateSetListener;
    private DatePickerDialog.OnDateSetListener toDateSetListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        fromDate = view.findViewById(R.id.fromDate);
        fromDate.setOnClickListener(this);

        toDate = view.findViewById(R.id.toDate);
        toDate.setOnClickListener(this);

        Button searchButton = view.findViewById(R.id.getDatesButton);
        searchButton.setOnClickListener(this);

        earthquakeInfoListview = view.findViewById(R.id.earthquakeInfoList);

        if(earthquakeInfoListview != null) {
            earthquakeInfoListview.setAdapter(arrayAdapter);
        }

        //Labels
        TextView fromText = view.findViewById(R.id.fromText);
        TextView toText = view.findViewById(R.id.toText);

        // Unrderline Text
        fromText.setPaintFlags(fromText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        toText.setPaintFlags(toText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        beforeDateSetListener = (view1, year, month, day) -> {
            month = month + 1; // Month starts counting from 0
            String date = day + "/" + month + "/" + year;
            fromDate.setText(date);
        };

        toDateSetListener = (view2, year, month, day) -> {
            month = month + 1; // Month starts counting from 0
            String date = day + "/" + month + "/" + year;
            toDate.setText(date);
        };
        return view;
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        switch (v.getId()) {
            case R.id.fromDate:
                DatePickerDialog dialogBefore = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                        beforeDateSetListener,
                        year, month, day);
                dialogBefore.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogBefore.show();
                break;
            case R.id.toDate:
                DatePickerDialog dialogTo = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                        toDateSetListener,
                        year, month, day);
                dialogTo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogTo.show();
                break;
            case R.id.getDatesButton:
                if(fromDate.getText().toString().matches("") || toDate.getText().toString().matches("")) {
                    //Dates are null
                    System.out.println("Dates are null!");
                } else {

                    List<Earthquake> tempEarthquakes = new ArrayList<>();

                    for(int i = 0; i < HomeFragment.earthquakes.size(); i++) {
                        try {
                            if((HomeFragment.earthquakes.get(i).getDate().compareTo(formatDate(fromDate.getText().toString())) >= 0)
                            && (HomeFragment.earthquakes.get(i).getDate().compareTo(formatDate(toDate.getText().toString())) <= 0)) {
                                tempEarthquakes.add(HomeFragment.earthquakes.get(i));
                            }

                            String northernLocation = "";
                            String easternLocation = "";
                            String southernLocation = "";
                            String westernLocation = "";

                            String highestMagnitudeLocation = "";
                            String lowestDepthLocation = "";
                            String highestDepthLocation = "";

                            double mostNorthern = -180;
                            double mostEastern = -180;
                            double mostSouthern = 180;
                            double mostWestern = 180;

                            double highestMagnitude = 0;
                            double lowestDepth = 7000;
                            double highestDepth = 0;

                            for(int j = 0; j < tempEarthquakes.size(); j++) {
                                String tempLocation = tempEarthquakes.get(j).getLocation();
                                double tempLat = tempEarthquakes.get(j).getGeoLat();
                                double tempLong = tempEarthquakes.get(j).getGeoLong();
                                double tempMagnitude = tempEarthquakes.get(j).getMagnitude();
                                double tempDepth = tempEarthquakes.get(j).getDepth();

                                // Most northern
                                if(tempLat > mostNorthern) {
                                    northernLocation = tempLocation;
                                    mostNorthern = tempLat;
                                }

                                // Most eastern
                                if(tempLong > mostEastern) {
                                    easternLocation = tempLocation;
                                    mostEastern = tempLong;
                                }

                                // Most Southern
                                if(tempLat < mostSouthern) {
                                    southernLocation = tempLocation;
                                    mostSouthern = tempLat;
                                }

                                // Most Western
                                if(tempLong < mostWestern) {
                                    westernLocation = tempLocation;
                                    mostWestern = tempLong;
                                }

                                if(tempMagnitude > highestMagnitude) {
                                    highestMagnitudeLocation = tempLocation;
                                    highestMagnitude = tempMagnitude;
                                }

                                if(tempDepth < lowestDepth) {
                                    lowestDepthLocation = tempLocation;
                                    lowestDepth = tempDepth;
                                }

                                if(tempDepth > highestDepth) {
                                    highestDepthLocation = tempLocation;
                                    highestDepth = tempDepth;
                                }

                            }

                            List<Map<String, String>> string_list = new ArrayList<>();

                            // Northern Location
                            if(mostNorthern == -180) {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Most Northern Location");
                                data.put("value", "Null");
                                string_list.add(data);
                            } else {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Most Northern Location");
                                data.put("value", "Location: " + northernLocation);
                                string_list.add(data);
                            }

                            // Eastern Location
                            if(mostEastern == -180) {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Most Eastern Location");
                                data.put("value", "Null");
                                string_list.add(data);
                            } else {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Most Eastern Location");
                                data.put("value", "Location: " + easternLocation);
                                string_list.add(data);
                            }

                            // Southern Location
                            if(mostSouthern == 180) {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Most Southern Location");
                                data.put("value", "Null");
                                string_list.add(data);
                            } else {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Most Southern Location");
                                data.put("value", "Location: " + southernLocation);
                                string_list.add(data);
                            }

                            // Western Location
                            if(mostWestern == 180) {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Most Western Location");
                                data.put("value", "Null");
                                string_list.add(data);
                            } else {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Most Western Location");
                                data.put("value", "Location: " + westernLocation);
                                string_list.add(data);
                            }

                            // Highest Magnitude Search
                            if(highestMagnitude == 0) {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Highest Magnitude");
                                data.put("value", "Null");
                                string_list.add(data);
                            } else {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Magnitude");
                                data.put("value", "Location: " + highestMagnitudeLocation + "M: " + highestMagnitude);
                                string_list.add(data);
                            }

                            // Lowest Depth Search
                            if(lowestDepth == 7000) {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Lowest depth");
                                data.put("value", "Null");
                                string_list.add(data);
                            } else {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Lowest depth");
                                data.put("value", "Location: " +lowestDepthLocation + "Depth: " + lowestDepth + "km");
                                string_list.add(data);
                            }

                            // Highest Depth Search
                            if(highestDepth == 0) {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Highest depth");
                                data.put("value", "Null");
                                string_list.add(data);
                            } else {
                                Map<String, String> data = new HashMap<>(2);
                                data.put("title", "Highest depth");
                                data.put("value", "Location: " + highestDepthLocation + "Depth: " + highestDepth + "km");
                                string_list.add(data);
                            }

                            /*arrayAdapter = new ArrayAdapter<String>
                                    (getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, string_list){
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent){
                                    // Get the Item from ListView
                                    View view = super.getView(position, convertView, parent);
                                    // Initialize a TextView for ListView each Item
                                    return view;
                                }
                            };*/

                            arrayAdapter = new SimpleAdapter(getActivity().getApplicationContext(), string_list,
                                    android.R.layout.simple_list_item_2,
                                    new String[] {"title", "value"},
                                    new int[] {android.R.id.text1,
                                            android.R.id.text2});

                            earthquakeInfoListview.setAdapter(arrayAdapter);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:
                break;
        }


    }

    private Date formatDate(String string) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = formatter.parse(string);
        return date;
    }
}
