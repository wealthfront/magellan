package com.wealthfront.magellan.sample.advanced.tide;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wealthfront.magellan.BaseScreenView;
import com.wealthfront.magellan.sample.advanced.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

import static butterknife.ButterKnife.bind;

class TideLocationsView extends BaseScreenView<TideLocationsScreen> {

  @BindView(R.id.tideLocationsGrid)
  GridView tideLocationsList;

  TideLocationsView(Context context) {
    super(context);
    inflate(context, R.layout.home, this);
    bind(this);
    tideLocationsList.setAdapter(new TideLocationsListAdapter(context));
  }

  private final class TideLocationsListAdapter extends ArrayAdapter<TideLocationsScreen.TideLocations> {

    private TideLocationsListAdapter(@NonNull Context context) {
      super(context, R.layout.tide_location_grid_item, R.id.tideLocationName,
          TideLocationsScreen.TideLocations.values());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      if (convertView == null) {
        convertView = inflate(getContext(), R.layout.tide_location_grid_item, null);
      }
      final TideLocationsScreen.TideLocations tideLocation = getItem(position);
      ImageView tideLocationImageView = convertView.findViewById(R.id.tideLocationImage);
      TextView tideLocationTextView = convertView.findViewById(R.id.tideLocationName);

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
