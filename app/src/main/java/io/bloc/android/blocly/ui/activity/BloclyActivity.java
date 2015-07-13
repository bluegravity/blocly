package io.bloc.android.blocly.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.bloc.android.blocly.R;
import io.bloc.android.blocly.ui.adaptor.ItemAdapter;

/**
 * Created by Dan on 6/27/2015.
 */
public class BloclyActivity extends Activity {
    private ItemAdapter itemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocly);


//        Toast.makeText(this,
//                BloclyApplication.getSharedDataSource().getFeeds().get(0).getTitle(),
//                Toast.LENGTH_LONG).show();

        itemAdapter = new ItemAdapter();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_activity_blocly);
        // #12
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapter);
    }
}
