package com.wealthfront.magellan;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;

import com.wealthfront.magellan.test.DummyScreen;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LegacyViewComponentTest {

  @Mock BaseScreenView<DummyScreen> view;

  private LegacyViewComponent legacyViewComponent;
  private DummyScreen screen;
  private Context context = new Activity();

  @Before
  public void setUp() {
    initMocks(this);
    screen = new DummyScreen(view);
    legacyViewComponent = new LegacyViewComponent(screen);
  }

  @Test
  public void viewComponentLifecycleUntilDestroy() {
    assertThat(screen.getView()).isEqualTo(null);
    assertThat(screen.getActivity()).isEqualTo(null);

    legacyViewComponent.create(context);

    assertThat(screen.getView()).isEqualTo(null);

    legacyViewComponent.show(context);

    assertThat(screen.getView()).isEqualTo(view);
    assertThat(screen.getActivity()).isEqualTo(context);

    legacyViewComponent.resume(context);

    assertThat(screen.getView()).isEqualTo(view);
    verify(view, never()).restoreHierarchyState(isA(SparseArray.class));
    assertThat(screen.getActivity()).isEqualTo(context);

    legacyViewComponent.pause(context);

    assertThat(screen.getView()).isEqualTo(view);
    assertThat(screen.getActivity()).isEqualTo(context);

    legacyViewComponent.hide(context);

    assertThat(screen.getView()).isEqualTo(null);
    verify(view).saveHierarchyState(isA(SparseArray.class));
    assertThat(screen.getActivity()).isEqualTo(null);
  }


  @Test
  public void viewComponentLifecycleConfigChange() {
    legacyViewComponent.create(context);
    legacyViewComponent.show(context);
    legacyViewComponent.resume(context);

    assertThat(screen.getView()).isEqualTo(view);
    assertThat(screen.getActivity()).isEqualTo(context);

    legacyViewComponent.pause(context);
    legacyViewComponent.hide(context);

    assertThat(screen.getView()).isEqualTo(null);
    assertThat(screen.getActivity()).isEqualTo(null);

    legacyViewComponent.show(context);
    legacyViewComponent.resume(context);

    assertThat(screen.getView()).isEqualTo(view);
    assertThat(screen.getActivity()).isEqualTo(context);
  }

  @Test
  public void viewRecreation() {
    viewComponentLifecycleUntilDestroy();

    legacyViewComponent.create(context);
    legacyViewComponent.show(context);
    legacyViewComponent.resume(context);

    assertThat(screen.getView()).isEqualTo(view);
    verify(view).restoreHierarchyState(isA(SparseArray.class));
    assertThat(screen.getActivity()).isEqualTo(context);
  }
}