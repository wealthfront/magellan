package com.wealthfront.magellan.sample.advanced;

import com.wealthfront.magellan.sample.advanced.cerealcollection.BrowseCollectionJourney;
import com.wealthfront.magellan.sample.advanced.designcereal.DesignCerealJourney;
import com.wealthfront.magellan.sample.advanced.ordertickets.OrderTicketsBasketStep;
import com.wealthfront.magellan.sample.advanced.ordertickets.OrderTicketsConfirmationStep;
import com.wealthfront.magellan.sample.advanced.ordertickets.OrderTicketsSuccessStep;
import com.wealthfront.magellan.sample.advanced.paymentinfo.PaymentInfoJourney;
import com.wealthfront.magellan.sample.advanced.suggestexhibit.SuggestConfirmationStep;
import com.wealthfront.magellan.sample.advanced.suggestexhibit.SuggestExhibitJourney;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

  void inject(MainActivity mainActivity);

  void inject(MainMenuStep mainMenuStep);

  void inject(OrderTicketsBasketStep orderTicketsBasketStep);

  void inject(OrderTicketsSuccessStep orderTicketsSuccessStep);

  void inject(OrderTicketsConfirmationStep orderTicketsConfirmationStep);

  void inject(SuggestExhibitJourney suggestExhibitJourney);

  void inject(SuggestConfirmationStep suggestConfirmationStep);

  void inject(PaymentInfoJourney paymentInfoJourney);

  void inject(DesignCerealJourney mainMenuStep);

  void inject(BrowseCollectionJourney journey);
}
