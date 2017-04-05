package com.wealthfront.magellan.sample.advanced;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.wealthfront.magellan.BaseScreenView;

import butterknife.BindView;

import static butterknife.ButterKnife.bind;

class HomeView extends BaseScreenView<HomeScreen> {

    @BindView(R.id.organizationNameField) EditText organizationNameField;
    @BindView(R.id.findReposButton) Button findReposButton;
    @BindView(R.id.loading) View loading;
    @BindView(R.id.listContainer) View listContainer;
    @BindView(R.id.organizationRepositoriesList) ListView organizationRepositoriesList;

    public HomeView(Context context) {
        super(context);
        inflate(context, R.layout.home, this);
        bind(this);
    }

}
