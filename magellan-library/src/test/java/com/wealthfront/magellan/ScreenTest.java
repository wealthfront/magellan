package com.wealthfront.magellan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ScreenTest {

  @Mock BaseScreenView<DummyScreen> view;
  @Mock Dialog dialog;
  @Captor ArgumentCaptor<SparseArray<Parcelable>> sparseArrayCaptor;
  private DummyScreen screen;

  @Before
  public void setUp() {
    initMocks(this);
    screen = new DummyScreen(view);
  }

  @Test
  public void recreateView() {
    View v = screen.recreateView(null, null);

    assertThat(screen.getView()).isEqualTo(v);
    assertThat(view).isEqualTo(v);
    assertThat(view.getScreen()).isEqualTo(screen);
  }

  @Test
  public void destroyView() {
    screen.recreateView(null, null);
    screen.destroyView();

    verify(view).saveHierarchyState(isA(SparseArray.class));
  }

  @Test
  public void createDialog() {
    screen.showDialog(new DialogCreator() {
      @Override
      public Dialog createDialog(Activity activity) {
        return dialog;
      }
    });
    screen.createDialog();

    verify(dialog, times(2)).show();
  }

  @Test
  public void destroyDialog() {
    screen.showDialog(new DialogCreator() {
      @Override
      public Dialog createDialog(Activity activity) {
        return dialog;
      }
    });
    screen.destroyDialog();

    verify(dialog).setOnDismissListener(null);
    verify(dialog).dismiss();
  }

  @Test
  public void saveRestore() {
    final Bundle state42 = new Bundle();
    state42.putString("key", "value");
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        sparseArrayCaptor.getValue().put(42, state42);
        return null;
      }
    }).when(view).saveHierarchyState(sparseArrayCaptor.capture());

    screen.recreateView(null, null);
    Bundle bundle = new Bundle();
    screen.save(bundle);
    screen.restore(bundle);
    screen.recreateView(null, null);

    verify(view).saveHierarchyState(isA(SparseArray.class));
    verify(view).restoreHierarchyState(sparseArrayCaptor.capture());
    assertThat(((Bundle) sparseArrayCaptor.getValue().get(42)).getString("key")).isEqualTo("value");
  }

  private static class DummyScreen extends Screen<BaseScreenView<DummyScreen>> {

    private BaseScreenView<DummyScreen> view;

    DummyScreen(BaseScreenView<DummyScreen> view) {
      this.view = view;
    }

    @Override
    protected BaseScreenView<DummyScreen> createView(Context context) {
      return view;
    }

  }

}
