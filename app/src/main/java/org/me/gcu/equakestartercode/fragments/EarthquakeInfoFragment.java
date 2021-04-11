package org.me.gcu.equakestartercode.fragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.me.gcu.equakestartercode.R;

/**
 * @author Ted Hilton - S1708998
 */
public class EarthquakeInfoFragment extends Fragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_earthquake_info, container, false);

        TextView locationLabel = view.findViewById(R.id.locationLabel);
        TextView dateLabel = view.findViewById(R.id.dateLabel);
        TextView depthLabel = view.findViewById(R.id.depthLabel);
        TextView magnitudeLabel = view.findViewById(R.id.magnitudeLabel);
        TextView latlongLabel = view.findViewById(R.id.latlongLabel);

        locationLabel.setPaintFlags(locationLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        dateLabel.setPaintFlags(dateLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        depthLabel.setPaintFlags(depthLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        magnitudeLabel.setPaintFlags(magnitudeLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        latlongLabel.setPaintFlags(latlongLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView location = view.findViewById(R.id.locationText);
        TextView date = view.findViewById(R.id.dateText);
        TextView depth = view.findViewById(R.id.depthText);
        TextView magnitude = view.findViewById(R.id.magnitudeText);
        TextView latlong = view.findViewById(R.id.latlongText);

        Button back = view.findViewById(R.id.backButton);
        back.setOnClickListener(this);

        String locationString = getArguments().getString("LOCATION");
        String dateString = getArguments().getString("DATE");
        int depthInt = getArguments().getInt("DEPTH");
        double depthMagnitude = getArguments().getDouble("MAGNITUDE");
        double latDouble = getArguments().getDouble("LAT");
        double latLong = getArguments().getDouble("LONG");

        location.setText(locationString);
        date.setText(dateString);
        String depthText = depthInt + "km";
        depth.setText(depthText);
        String magnitudeText = "" + depthMagnitude;
        magnitude.setText(magnitudeText);
        String latLongText = latDouble + " / " + latLong;
        latlong.setText(latLongText);

        return view;
    }

    @Override
    public void onClick(View v) {
        getFragmentManager().popBackStack();
    }
}