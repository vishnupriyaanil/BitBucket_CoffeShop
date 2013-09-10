package com.example.coffe;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class SuperAwesomeCardFragment extends SherlockFragment {

	private static final String ARG_POSITION = "position";

	private int position;

	private String asi;

	LayoutParams params;

	FrameLayout fl;
	ArrayList<String> parentItems;
	ArrayList<Object> childItems;

	Button textView, textView1;

	int index = 0;
	private String[] textfirst = { "Large", "Regular" };
	private String[] textsecond = { "Full cream", "Skim", "Soy" };

	public static SuperAwesomeCardFragment newInstance(int position) {
		SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt(ARG_POSITION);
		// asi = savedInstanceState.getString(ARG_POSITION);

		Log.v("asi1", String.valueOf(position));

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.v("asi2", String.valueOf(position));

		params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		fl = new FrameLayout(getActivity());
		fl.setLayoutParams(params);

		final int margin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
						.getDisplayMetrics());

		params.setMargins(margin, margin, margin, margin);
		if (position == 0) {

			parentItems = new ArrayList<String>();
			childItems = new ArrayList<Object>();

			ExpandableListView v = new ExpandableListView(getActivity());

			v.setAdapter(new SavedTabsListAdapter());
			// MyExpandableAdapter adapter = new
			// MyExpandableAdapter(parentItems, childItems);
			// TextView v = new TextView(getActivity());
			// v.setAdapter(adapter);
			v.setLayoutParams(params);
			v.setLayoutParams(params);
			// v.setGravity(Gravity.CENTER);
			v.setBackgroundResource(R.drawable.background_card);
			// v.setText("Ashish");

			fl.addView(v);
		}
		if (position == 1) {
			TextView v = new TextView(getActivity());

			v.setLayoutParams(params);
			v.setLayoutParams(params);
			v.setGravity(Gravity.CENTER);
			v.setBackgroundResource(R.drawable.background_card);
			v.setText("Anil");

			fl.addView(v);
		}
		if (position == 2) {
			TextView v = new TextView(getActivity());

			v.setLayoutParams(params);
			v.setLayoutParams(params);
			v.setGravity(Gravity.CENTER);
			v.setBackgroundResource(R.drawable.background_card);
			v.setText("RAM");

			fl.addView(v);
		}
		if (position == 3) {
			TextView v = new TextView(getActivity());

			v.setLayoutParams(params);
			v.setLayoutParams(params);
			v.setGravity(Gravity.CENTER);
			v.setBackgroundResource(R.drawable.background_card);
			v.setText("Syam");

			fl.addView(v);
		}

		return fl;
	}

	public void setGroupParents() {

		parentItems.add("Capuccino");
		parentItems.add("Cafe Latte");
		parentItems.add("Flat White");
		parentItems.add("Espresso");
		parentItems.add("Long Black");
		parentItems.add("Macchiato");
		parentItems.add("Mocha");
		parentItems.add("Chai Latte");
	}

	// method to set child data of each parent
	public void setChildData() {

		// Add Child Items for Fruits
		ArrayList<String> child = new ArrayList<String>();
		child.add(" ");

		childItems.add(child);

		// Add Child Items for Flowers
		child = new ArrayList<String>();
		child.add(" ");

		childItems.add(child);

		// Add Child Items for Animals
		child = new ArrayList<String>();
		child.add(" ");

		childItems.add(child);

		// Add Child Items for Birds
		child = new ArrayList<String>();
		child.add(" ");

		childItems.add(child);

		// Add Child Items for Birds
		child = new ArrayList<String>();
		child.add(" ");

		childItems.add(child);

		// Add Child Items for Birds
		child = new ArrayList<String>();
		child.add(" ");

		childItems.add(child);

		// Add Child Items for Birds
		child = new ArrayList<String>();
		child.add(" ");

		childItems.add(child);

		child = new ArrayList<String>();
		child.add(" ");

		childItems.add(child);

	}

	public class SavedTabsListAdapter extends BaseExpandableListAdapter {

		private String[] groups = { "Capuccino", "Cafe Latte", "Flat White",
				"Espresso", "Long Black", "Macchiato", "Mocha", "Chai Latte" };

		private String[][] children = { { "Arnold" }, { "Ace" }, { "Fluffy" },
				{ "Goldy" }, { "Arnold" }, { "Ace" }, { "Fluffy" }, { "Goldy" } };

		@Override
		public int getGroupCount() {
			return groups.length;
		}

		@Override
		public int getChildrenCount(int i) {
			return children[i].length;
		}

		@Override
		public Object getGroup(int i) {
			return groups[i];
		}

		@Override
		public Object getChild(int i, int i1) {
			return children[i][i1];
		}

		@Override
		public long getGroupId(int i) {
			return i;
		}

		@Override
		public long getChildId(int i, int i1) {
			return i1;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int i, boolean b, View view,
				ViewGroup viewGroup) {
			TextView textView = new TextView(
					SuperAwesomeCardFragment.this.getActivity());
			textView.setTextSize(20);
			textView.setPadding(50, 15, 10, 15);

			textView.setText(getGroup(i).toString());
			return textView;
		}

		@Override
		public View getChildView(int i, int i1, boolean b, View view,
				ViewGroup viewGroup) {

			textView = new Button(SuperAwesomeCardFragment.this.getActivity());

			textView1 = new Button(SuperAwesomeCardFragment.this.getActivity());

			// textView1.setText("Button2");

			Button textView2 = new Button(
					SuperAwesomeCardFragment.this.getActivity());

			textView2.setText("Extra");

			LinearLayout layout = new LinearLayout(
					SuperAwesomeCardFragment.this.getActivity());
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setGravity(Gravity.CENTER_HORIZONTAL);

			layout.addView(textView);
			layout.addView(textView1);
			layout.addView(textView2);

			textView.setText("Large");
			textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// Toast.makeText(getActivity(), "ASHISH",
					// Toast.LENGTH_LONG).show();
					textView.setText(textfirst[index++]);
					if (index == textfirst.length)
						index = 0;
				}
			});

			textView1.setText("Full cream");
			textView1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// Toast.makeText(getActivity(), "ASHISH",
					// Toast.LENGTH_LONG).show();
					textView1.setText(textsecond[index++]);
					if (index == textsecond.length)
						index = 0;
				}
			});

			return layout;
		}

		@Override
		public boolean isChildSelectable(int i, int i1) {
			return true;
		}

	}

}