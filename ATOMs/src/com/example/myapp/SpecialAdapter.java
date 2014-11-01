package com.example.myapp;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SpecialAdapter extends SimpleAdapter {
	private TextView orderColor;
	
	public SpecialAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {
		super(context, items, resource, from, to);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	  View view = super.getView(position, convertView, parent);
	  orderColor = (TextView) view.findViewById(R.id.ColColor);
	  view.setBackgroundColor(Color.parseColor((String) orderColor.getText()));
	  return view;
	}
}