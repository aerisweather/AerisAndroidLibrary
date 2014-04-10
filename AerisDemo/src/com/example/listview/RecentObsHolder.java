package com.example.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demoaerisproject.R;
import com.example.util.FormatUtil;
import com.hamweather.aeris.model.ObservationPeriod;
import com.hamweather.aeris.util.FileUtil;

public class RecentObsHolder implements AdapterHolder<ObservationPeriod> {

	ImageView weatherIcon;
	TextView day;
	TextView date;
	TextView weatherDesc;
	TextView temp;

	@Override
	public View inflateview(LayoutInflater mInflater) {
		View v = mInflater.inflate(R.layout.listview_extended_forecast, null,
				false);
		weatherIcon = (ImageView) v.findViewById(R.id.ivListIcon);
		weatherDesc = (TextView) v.findViewById(R.id.tvListDesc);
		temp = (TextView) v.findViewById(R.id.tvListHigh);
		date = (TextView) v.findViewById(R.id.tvListDate);
		day = (TextView) v.findViewById(R.id.tvListDay);
		return v;
	}

	@Override
	public void populateView(ObservationPeriod t, int position) {
		weatherIcon.setImageResource(FileUtil.getDrawableByName(t.ob.icon,
				day.getContext()));
		day.setText(FormatUtil.getTimehhmmFromISO(t.ob.dateTimeISO));
		date.setText(FormatUtil.getDayFromISO(t.ob.dateTimeISO, true));
		weatherDesc.setText(t.ob.weather);
		temp.setText(t.ob.tempF.toString());
	}

}