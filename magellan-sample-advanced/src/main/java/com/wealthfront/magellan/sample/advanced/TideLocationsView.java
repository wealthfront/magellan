package com.wealthfront.magellan.sample.advanced;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wealthfront.magellan.BaseScreenView;
import com.wealthfront.magellan.sample.advanced.TideLocationsScreen.TideLocations;

import java.util.EnumSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.bind;
import static butterknife.ButterKnife.findById;

class TideLocationsView extends BaseScreenView<TideLocationsScreen> {

    @BindView(R.id.tideLocationsGrid)
    GridView tideLocationsList;

    public TideLocationsView(Context context) {
        super(context);
        inflate(context, R.layout.home, this);
        bind(this);
        tideLocationsList.setAdapter(new TideLocationsListAdapter(context));
    }

    private class TideLocationsListAdapter extends ArrayAdapter<TideLocations> {

        private TideLocationsListAdapter(@NonNull Context context) {
            super(context, R.layout.tide_location_grid_item, R.id.tideLocationName, TideLocations.values());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = inflate(getContext(), R.layout.tide_location_grid_item, null);
            }
            final TideLocations tideLocation = getItem(position);
            ImageView tideLocationImageView = ButterKnife.findById(convertView, R.id.tideLocationImage);
            TextView tideLocationTextView = ButterKnife.findById(convertView, R.id.tideLocationName);

            tideLocationImageView.setImageResource(tideLocation.getImageId());
            tideLocationTextView.setText(tideLocation.getName());
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    getScreen().tideLocationSelected(tideLocation.getNoaaApiId(), tideLocation.getName());
                }
            });
            return convertView;
        }

    }


}
