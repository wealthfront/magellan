package com.wealthfront.magellan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.wealthfront.magellan.transitions.NoAnimationTransition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.fakes.RoboMenu;

import java.util.Deque;

import static com.google.common.truth.Truth.assertThat;
import static com.wealthfront.magellan.NavigationType.NO_ANIM;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.setupActivity;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class NavigatorTest {

  @Mock Screen root;
  @Mock Screen screen;
  @Mock Screen screen2;
  @Mock Screen screen3;
  @Mock ScreenLifecycleListener lifecycleListener;
  NavigatorActivity activity;
  Navigator navigator;
  ScreenContainer container;
  @Mock BaseScreenView<DummyScreen> view;
  DummyScreen dummyScreen;

  @Before
  public void setUp() {
    initMocks(this);

    navigator = Navigator.withRoot(root)
        .transition(new NoAnimationTransition())
        .maxEventsTracked(2)
        .build();

    activity = spy(setupActivity(NavigatorActivity.class));
    container = new ScreenContainer(application);
    container.setId(R.id.magellan_container);
    activity.setContentView(container);
    dummyScreen = new DummyScreen(view);

    when(root.createView(activity)).thenAnswer(
        new Answer<View>() {
          @Override
          public View answer(InvocationOnMock invocation) throws Throwable {
            return new BaseScreenView(application);
          }
        });
    when(screen.createView(activity)).thenReturn(new BaseScreenView(application));
    when(screen2.createView(activity)).thenReturn(new BaseScreenView(application));
    when(screen3.createView(activity)).thenReturn(new BaseScreenView(application));
  }

  @Test
  public void onCreateOnSaveOnDestroy() {
    navigator.onCreate(activity, null);
    verify(root).onRestore(null);
    verify(root).createView(activity);
    verify(root).onShow(activity);

    Bundle state = new Bundle();
    navigator.onSaveInstanceState(state);
    verify(root).onSave(state);

    navigator.onDestroy(activity);
    verify(root).onHide(activity);
  }

  @Test
  public void onCreateOptionsMenu() {
    RoboMenu menu = new RoboMenu();
    menu.add("item0");
    menu.add("item1");

    navigator.onCreateOptionsMenu(menu);

    assertThat(menu.getItem(0).isVisible()).isFalse();
    assertThat(menu.getItem(1).isVisible()).isFalse();
    verify(root).onUpdateMenu(menu);
  }

  @Test
  public void onPrepareOptionsMenu() {
    RoboMenu menu = new RoboMenu();
    menu.add("item0");
    menu.add("item1");

    navigator.onPrepareOptionsMenu(menu);

    assertThat(menu.getItem(0).isVisible()).isFalse();
    assertThat(menu.getItem(1).isVisible()).isFalse();
    verify(root).onUpdateMenu(menu);
  }

  @Test
  public void onResume() {
    navigator.onCreate(activity, null);
    reset(root);
    navigator.onResume(activity);

    verify(root).onResume(activity);
  }

  @Test
  public void onPause() {
    navigator.onCreate(activity, null);
    navigator.onPause(activity);

    verify(root).onPause(activity);
  }

  @Test
  public void onResume_differentActivity() {
    navigator.onCreate(activity, null);
    reset(root);
    verifyNoMoreInteractions(root);
    navigator.onResume(new Activity());
  }

  @Test
  public void onPause_differentActivity() {
    navigator.onCreate(activity, null);
    reset(root);
    verifyNoMoreInteractions(root);
    navigator.onPause(new Activity());
  }

  @Test
  public void onActivityResult() {
    navigator.onCreate(activity, null);
    reset(root);
    navigator.onActivityResult(1138, Activity.RESULT_OK, null);

    verify(root).onActivityResult(1138, Activity.RESULT_OK, null);
  }

  @Test
  public void onActivityResult_differentActivity() {
    navigator.onCreate(activity, null);
    reset(root);
    verifyNoMoreInteractions(root);
    navigator.onActivityResult(1138, Activity.RESULT_OK, null);
  }

  @Test
  public void lifecycleListener() {
    navigator.addLifecycleListener(lifecycleListener);
    navigator.onCreate(activity, null);

    navigator.goTo(screen);
    navigator.goBack();

    verify(lifecycleListener, times(2)).onShow(root);
    verify(lifecycleListener).onShow(screen);
    verify(lifecycleListener).onHide(root);
    verify(lifecycleListener).onHide(screen);

    navigator.removeLifecycleListener(lifecycleListener);
    navigator.goTo(screen);
    navigator.goBack();

    verifyNoMoreInteractions(lifecycleListener);
  }

  @Test
  public void handleBack() {
    navigator.onCreate(activity, null);
    navigator.goTo(screen);

    assertThat(navigator.currentScreen()).isEqualTo(screen);
    boolean handled = navigator.handleBack();
    assertThat(handled).isTrue();
    assertThat(navigator.currentScreen()).isEqualTo(root);

    handled = navigator.handleBack();
    assertThat(handled).isFalse();
  }

  @Test
  public void showHide() {
    RoboMenu menu = new RoboMenu();
    navigator.onCreate(activity, null);
    navigator.onCreateOptionsMenu(menu);
    navigator.show(screen);

    verify(root).onHide(activity);
    verify(activity, times(2)).onNavigate(isA(ActionBarConfig.class));
    assertThat(navigator.currentScreen()).isEqualTo(screen);
    reset(activity);

    navigator.hide(screen);

    assertThat(navigator.currentScreen()).isEqualTo(root);
    verify(screen).onHide(activity);
    verify(root, times(2)).onShow(activity);
    verify(activity).onNavigate(isA(ActionBarConfig.class));
  }

  @Test
  public void goBack() {
    navigator.onCreate(activity, null);
    navigator.goTo(screen);

    verify(root).onHide(activity);
    verify(screen).onShow(activity);
    assertThat(navigator.currentScreen()).isEqualTo(screen);
    reset(activity);

    navigator.goBack();

    verify(screen).onHide(activity);
    verify(root, times(2)).onShow(activity);
    verify(activity).onNavigate(isA(ActionBarConfig.class));
    assertThat(navigator.currentScreen()).isEqualTo(root);
  }

  @Test
  public void goBackToRoot_notAtRoot() {
    navigator.onCreate(activity, null);
    navigator.goTo(screen);
    navigator.goTo(screen2);

    navigator.goBackToRoot(NO_ANIM);

    InOrder inOrder = inOrder(root);
    inOrder.verify(root).onHide(activity);
    inOrder.verify(root).onShow(activity);
    assertThat(navigator.currentScreen()).isEqualTo(root);
  }

  @Test
  public void goBackToRoot_alreadyAtRoot() {
    navigator.onCreate(activity, null);

    navigator.goBackToRoot(NO_ANIM);

    InOrder inOrder = inOrder(root);
    inOrder.verify(root).onHide(activity);
    inOrder.verify(root).onShow(activity);
    assertThat(navigator.currentScreen()).isEqualTo(root);
  }

  @Test
  public void goBackTo_screenNotInHistory() {
    navigator.onCreate(activity, null);
    try {
      navigator.goBackTo(dummyScreen);
    } catch (IllegalArgumentException exception) {
      assertThat(exception.getMessage()).contains("Can't go back to a screen (com.wealthfront.magellan.DummyScreen) that isn't in history. \n" +
          "History: ([root])");
    }
  }

  @Test
  public void goBackTo() {
    navigator.onCreate(activity, null);
    navigator.goTo(screen);
    navigator.goTo(screen2);

    navigator.goBackTo(screen);

    assertThat(navigator.currentScreen()).isEqualTo(screen);
  }

  @Test(expected = IllegalArgumentException.class)
  public void goBackBefore_screenNotInHistory() {
    navigator.onCreate(activity, null);

    navigator.goBackBefore(screen);
  }

  @Test
  public void goBackBefore() {
    navigator.onCreate(activity, null);
    navigator.goTo(screen);
    navigator.goTo(screen2);

    navigator.goBackBefore(screen);

    assertThat(navigator.currentScreen()).isEqualTo(root);
  }

  @Test
  public void saveRestore() {
    navigator.onCreate(activity, null);
    navigator.goTo(screen);

    Bundle state = new Bundle();
    navigator.onSaveInstanceState(state);

    verify(root).onSave(state);
    verify(screen).onSave(state);

    navigator.onDestroy(activity);
    container.removeAllViews();
    navigator.onCreate(activity, state);

    verify(root).onRestore(state);
    verify(screen).onRestore(state);
  }

  @Test
  public void replace() {
    navigator.onCreate(activity, null);
    navigator.replace(screen);

    verify(root).onHide(activity);
    verify(screen).onShow(activity);
    verify(activity, times(2)).onNavigate(isA(ActionBarConfig.class));
    assertThat(navigator.currentScreen()).isEqualTo(screen);

    boolean canGoBack = navigator.handleBack();

    assertThat(canGoBack).isFalse();
  }

  @Test
  public void resetWithRoot() {
    navigator.onCreate(activity, null);
    navigator.goTo(screen);

    assertThat(navigator.currentScreen()).isEqualTo(screen);

    navigator.onDestroy(activity);

    navigator.resetWithRoot(activity, root);

    verify(screen).onHide(activity);
    assertThat(navigator.currentScreen()).isEqualTo(root);
  }

  @Test
  public void resetWithRoot_differentActivity() {
    navigator.onCreate(activity, null);

    navigator.resetWithRoot(new Activity(), root);
  }

  @Test(expected = IllegalStateException.class)
  public void resetWithRoot_afterOnCreate() {
    navigator.onCreate(activity, null);

    navigator.resetWithRoot(activity, root);
  }

  @Test
  public void rewriteHistory() {
    navigator.onCreate(activity, null);
    navigator.goTo(screen);

    assertThat(navigator.currentScreen()).isEqualTo(screen);

    navigator.onDestroy(activity);

    navigator.rewriteHistory(activity, new HistoryRewriter() {
      @Override
      public void rewriteHistory(Deque<Screen> history) {
        history.pop();
      }
    });

    assertThat(navigator.currentScreen()).isEqualTo(root);
  }

  @Test
  public void rewriteHistory_differentActivity() {
    navigator.onCreate(activity, null);

    navigator.rewriteHistory(new Activity(), new HistoryRewriter() {
      @Override
      public void rewriteHistory(Deque<Screen> history) {
      }
    });
  }

  @Test(expected = IllegalStateException.class)
  public void rewriteHistory_afterOnCreate() {
    navigator.onCreate(activity, null);

    navigator.rewriteHistory(activity, new HistoryRewriter() {
      @Override
      public void rewriteHistory(Deque<Screen> history) {
      }
    });
  }

  @Test
  public void actionBarConfig() {
    when(screen.shouldShowActionBar()).thenReturn(true);
    when(screen.shouldAnimateActionBar()).thenReturn(true);
    when(screen.getActionBarColorRes()).thenReturn(42);

    navigator.onCreate(activity, null);
    navigator.goTo(screen);

    verify(screen).onShow(activity);
    verify(activity, times(2)).onNavigate(isA(ActionBarConfig.class));
    assertThat(activity.actionBarConfig.visible()).isTrue();
    assertThat(activity.actionBarConfig.animated()).isTrue();
    assertThat(activity.actionBarConfig.colorRes()).isEqualTo(42);
    assertThat(navigator.currentScreen()).isEqualTo(screen);
  }

  @Test
  public void navigate() {
    when(screen.shouldShowActionBar()).thenReturn(false);
    when(screen2.shouldShowActionBar()).thenReturn(true);

    navigator.onCreate(activity, null);
    navigator.goTo(screen);

    navigator.navigate(new HistoryRewriter() {
      @Override
      public void rewriteHistory(Deque<Screen> history) {
        history.clear();
        history.push(root);
        history.push(screen2);
      }
    });

    verify(screen).onHide(activity);
    verify(screen2).onShow(activity);
    assertThat(navigator.currentScreen()).isEqualTo(screen2);
  }

  @Test
  public void consumeTouchEventsDuringNavigate() {
    Robolectric.getForegroundThreadScheduler().pause();
    navigator.onCreate(activity, null);
    assertThat(container.onInterceptTouchEvent(null)).isFalse();

    navigator.goTo(screen);

    assertThat(container.onInterceptTouchEvent(null)).isTrue();
  }

  @Test(expected = IllegalStateException.class)
  public void goBackTooMuch() {
    navigator.onCreate(activity, null);
    navigator.goBack();
    navigator.goBack();
  }

  @Test(expected = NullPointerException.class)
  public void onCreateNotCalled() {
    navigator.goTo(screen);
  }

  @Test(expected = IllegalStateException.class)
  public void noContainer() {
    activity.findViewById(R.id.magellan_container).setId(0);
    navigator.onCreate(activity, null);
  }

  @Test
  public void getEventsDescription() {
    navigator.onCreate(activity, null);

    navigator.goTo(screen);

    assertThat(navigator.getEventsDescription()).isEqualTo(
        "GO FORWARD - Backstack: root > [screen]\n"
    );

    navigator.goTo(screen2);

    assertThat(navigator.getEventsDescription()).isEqualTo(
        "GO FORWARD - Backstack: root > [screen]\n" +
        "GO FORWARD - Backstack: root > screen > [screen2]\n"
    );

    navigator.goTo(screen3);

    assertThat(navigator.getEventsDescription()).isEqualTo(
        "GO FORWARD - Backstack: root > screen > [screen2]\n" +
        "GO FORWARD - Backstack: root > screen > screen2 > [screen3]\n"
    );

    navigator.goBack();

    assertThat(navigator.getEventsDescription()).isEqualTo(
        "GO FORWARD - Backstack: root > screen > screen2 > [screen3]\n" +
        "GO BACKWARD - Backstack: root > screen > [screen2]\n"
    );

    navigator.show(screen3);

    assertThat(navigator.getEventsDescription()).isEqualTo(
        "GO BACKWARD - Backstack: root > screen > [screen2]\n" +
        "SHOW FORWARD - Backstack: root > screen > screen2 > [screen3]\n"
    );
  }

  private static class NavigatorActivity extends Activity implements NavigationListener {

    ActionBarConfig actionBarConfig;

    @Override
    public void onNavigate(ActionBarConfig actionBarConfig) {
      this.actionBarConfig = actionBarConfig;
    }

  }

}
