package io.bloc.android.blocly.ui.activity;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import io.bloc.android.blocly.BloclyApplication;
import io.bloc.android.blocly.R;
import io.bloc.android.blocly.api.model.RssFeed;
import io.bloc.android.blocly.api.model.RssItem;
import io.bloc.android.blocly.ui.adaptor.ItemAdapter;
import io.bloc.android.blocly.ui.adaptor.NavigationDrawerAdapter;

/**
 * Created by Dan on 6/27/2015.
 */
//public class BloclyActivity extends ActionBarActivity implements NavigationDrawerAdapter.NavigationDrawerAdapterDelegate {    //public class BloclyActivity extends AppCompatActivity {
public class BloclyActivity extends ActionBarActivity  implements
        NavigationDrawerAdapter.NavigationDrawerAdapterDelegate,
        ItemAdapter.DataSource,
        ItemAdapter.Delegate {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationDrawerAdapter navigationDrawerAdapter;
    private Menu menu;
    private View overflowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocly);


        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_activity_blocly);
        setSupportActionBar(toolbar);


//        Toast.makeText(this,
//                BloclyApplication.getSharedDataSource().getFeeds().get(0).getTitle(),
//                Toast.LENGTH_LONG).show();

        itemAdapter = new ItemAdapter();
        itemAdapter.setDataSource(this);
        itemAdapter.setDelegate(this);

        //RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_activity_blocly);
        recyclerView = (RecyclerView) findViewById(R.id.rv_activity_blocly);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapter);

        navigationDrawerAdapter = new NavigationDrawerAdapter();
        RecyclerView navigationRecyclerView = (RecyclerView) findViewById(R.id.rv_nav_activity_blocly);
        navigationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        navigationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        navigationRecyclerView.setAdapter(navigationDrawerAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.dl_activity_blocly);
        // #6
        // drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                invalidateOptionsMenu();
                // dim when closing
                if (overflowButton != null) {
                    overflowButton.setAlpha(1f);
                    // #7c
                    overflowButton.setEnabled(true);
                }
                if (menu == null) {
                    return;
                }
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem item = menu.getItem(i);

                    item.setEnabled(true);
                    Drawable icon = item.getIcon();
                    if (icon != null) {
                        icon.setAlpha(255);
                    }
                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                invalidateOptionsMenu();
                if (overflowButton != null) {
                    // #7a
                    overflowButton.setEnabled(false);
                }
                if (menu == null) {
                    return;
                }
                for (int i = 0; i < menu.size(); i++) {
                    // #7b
                    menu.getItem(i).setEnabled(false);
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (overflowButton == null) {
                    // #8
                    ArrayList<View> foundViews = new ArrayList<View>();
                    getWindow().getDecorView().findViewsWithText(foundViews,
                            getString(R.string.abc_action_menu_overflow_description),
                            View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                    if (foundViews.size() > 0) {
                        overflowButton = foundViews.get(0);
                    }
                }
                // #9a
                if (overflowButton != null) {
                    overflowButton.setAlpha(1f - slideOffset);
                }
                if (menu == null) {
                    return;
                }
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem item = menu.getItem(i);
                    Drawable icon = item.getIcon();
                    if (icon != null) {
                        // #9b
                        icon.setAlpha((int) ((1f - slideOffset) * 255));
                    }
                }
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        navigationDrawerAdapter.setDelegate(this);
    }

    // #7a
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    // #7b
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    // #7c
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // #6 - don't inflate when open
//        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
//            return super.onCreateOptionsMenu(menu);
//        }
        getMenuInflater().inflate(R.menu.blocly, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

         /*
      * NavigationDrawerAdapterDelegate
      */

    @Override
    public void didSelectNavigationOption(NavigationDrawerAdapter adapter, NavigationDrawerAdapter.NavigationOption navigationOption) {
        // #3a
        drawerLayout.closeDrawers();
        Toast.makeText(this, "Show the " + navigationOption.name(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void didSelectFeed(NavigationDrawerAdapter adapter, RssFeed rssFeed) {
        // #3b
        drawerLayout.closeDrawers();
        Toast.makeText(this, "Show RSS items from " + rssFeed.getTitle(), Toast.LENGTH_SHORT).show();
    }
         /*
      * ItemAdapter.DataSource
      */

    @Override
    public RssItem getRssItem(ItemAdapter itemAdapter, int position) {
        return BloclyApplication.getSharedDataSource().getItems().get(position);
    }

    @Override
    public RssFeed getRssFeed(ItemAdapter itemAdapter, int position) {
        return BloclyApplication.getSharedDataSource().getFeeds().get(0);
    }

    @Override
    public int getItemCount(ItemAdapter itemAdapter) {
        return BloclyApplication.getSharedDataSource().getItems().size();
    }

     /*
      * ItemAdapter.Delegate
      */

    @Override
    public void onItemClicked(ItemAdapter itemAdapter, RssItem rssItem) {
        int positionToExpand = -1;
        int positionToContract = -1;

        if (itemAdapter.getExpandedItem() != null) {
            positionToContract = BloclyApplication.getSharedDataSource().getItems().indexOf(itemAdapter.getExpandedItem());
            View viewToContract = recyclerView.getLayoutManager().findViewByPosition(positionToContract);
            if (viewToContract == null) {
                positionToContract = -1;
            }
        }

        if (itemAdapter.getExpandedItem() != rssItem) {
            positionToExpand = BloclyApplication.getSharedDataSource().getItems().indexOf(rssItem);
            itemAdapter.setExpandedItem(rssItem);
        } else {
            itemAdapter.setExpandedItem(null);
        }
        if (positionToContract > -1) {

            itemAdapter.notifyItemChanged(positionToContract);
        }
        if (positionToExpand > -1) {
            itemAdapter.notifyItemChanged(positionToExpand);
        } else {
            // #1
            return;
        }

        int lessToScroll = 0;
        if (positionToContract > -1 && positionToContract < positionToExpand) {
            lessToScroll = itemAdapter.getExpandedItemHeight() - itemAdapter.getCollapsedItemHeight();
        }

        View viewToExpand = recyclerView.getLayoutManager().findViewByPosition(positionToExpand);
        //recyclerView.smoothScrollBy(0, viewToExpand.getTop());
        recyclerView.smoothScrollBy(0, viewToExpand.getTop() - lessToScroll);
    }
}
