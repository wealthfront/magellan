package com.wealthfront.magellan.sample.advanced.tide;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.Menu;

import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.sample.advanced.R;

public class TideLocationsScreen extends Screen<TideLocationsView> {

  public enum TideLocations {
    SAN_FRANCISCO(9414290, R.drawable.san_francisco, R.color.san_francisco, "San Francisco"),
    SEATTLE(9447130, R.drawable.seattle, R.color.seattle, "Seattle"),
    ANCHORAGE(9455920, R.drawable.anchorage, R.color.anchorage, "Anchorage"),
    GALVESTON(8771450, R.drawable.galveston, R.color.galveston, "Galveston"),
    MIAMI(8723214, R.drawable.miami, R.color.miami, "Miami"),
    CHARLESTON(8665530, R.drawable.charleston, R.color.charleston, "Charleston"),
    CHESAPEAKE_BAY(8638863, R.drawable.chesapeake, R.color.chesapeake_bay, "Chesapeake Bay"),
    NEW_YORK(8518750, R.drawable.new_york, R.color.new_york, "New York"),
    BOSTON(8443970, R.drawable.boston, R.color.boston, "Boston"),
    WASHINGTON_DC(8594900, R.drawable.washington_dc, R.color.washington_dc, "Washington D.C.");

    int noaaApiId;
    @DrawableRes int imageId;
    @ColorRes int colorId;
    String name;

    TideLocations(int noaaApiId, @DrawableRes int imageId, @ColorRes int colorId, String name) {
      this.noaaApiId = noaaApiId;
      this.imageId = imageId;
      this.colorId = colorId;
      this.name = name;
    }

    public int getNoaaApiId() {
      return noaaApiId;
    }

    public @DrawableRes int getImageId() {
      return imageId;
    }

    public String getName() {
      return name;
    }

    public int getColorId() {
      return colorId;
    }

    public static TideLocations fromNoaaApiId(int noaaApiId) {
      for (TideLocations tideLocation : TideLocations.values()) {
        if (tideLocation.getNoaaApiId() == noaaApiId) {
          return tideLocation;
        }
      }
      return null;
    }

  }

  @Override
  protected TideLocationsView createView(Context context) {
    return new TideLocationsView(context);
  }

  @Override
  public String getTitle(Context context) {
    return "Select A Location";
  }

  @Override
  protected int getActionBarColorRes() {
    return R.color.colorPrimary;
  }

  @Override
  protected void onUpdateMenu(Menu menu) {
    menu.findItem(R.id.animateActionBar).setVisible(true);
  }

  public void tideLocationSelected(int noaaApiId, String tideLocationName) {
    getNavigator().goTo(new TideDetailsScreen(noaaApiId, TideLocations.fromNoaaApiId(noaaApiId).getColorId(), tideLocationName));
  }

}
