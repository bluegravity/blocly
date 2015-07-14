package io.bloc.android.blocly.ui.adaptor;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import io.bloc.android.blocly.BloclyApplication;
import io.bloc.android.blocly.R;
import io.bloc.android.blocly.api.DataSource;
import io.bloc.android.blocly.api.model.RssFeed;
import io.bloc.android.blocly.api.model.RssItem;

/**
 * Created by Dan on 7/12/2015.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemAdapterViewHolder> {

    // for logcat
    private static String TAG = ItemAdapter.class.getSimpleName();

    @Override
    public ItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rss_item, viewGroup, false);
        return new ItemAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ItemAdapterViewHolder itemAdapterViewHolder, int index) {
        DataSource sharedDataSource = BloclyApplication.getSharedDataSource();
        itemAdapterViewHolder.update(sharedDataSource.getFeeds().get(0), sharedDataSource.getItems().get(index));
    }

    @Override
    public int getItemCount() {
        return BloclyApplication.getSharedDataSource().getItems().size();
    }

    // class ItemAdapterViewHolder extends RecyclerView.ViewHolder {
    //class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements ImageLoadingListener {
    class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements ImageLoadingListener, View.OnClickListener {

        TextView title;
        TextView feed;
        TextView content;
        View headerWrapper;
        ImageView headerImage;

        RssItem rssItem;

        public ItemAdapterViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tv_rss_item_title);
            feed = (TextView) itemView.findViewById(R.id.tv_rss_item_feed_title);
            content = (TextView) itemView.findViewById(R.id.tv_rss_item_content);
            // downcast because we'll only need the basic object
            headerWrapper = itemView.findViewById(R.id.fl_rss_item_image_header);
            headerImage = (ImageView) headerWrapper.findViewById(R.id.iv_rss_item_image);
            itemView.setOnClickListener(this);
        }

        void update(RssFeed rssFeed, RssItem rssItem) {
            this.rssItem = rssItem;
            feed.setText(rssFeed.getTitle());
            title.setText(rssItem.getTitle());
            content.setText(rssItem.getDescription());
            //imageURL = rssItem.getImageUrl();
            // if (imageURL != null) {
            if (rssItem.getImageUrl() != null) {
                // show frame
                headerWrapper.setVisibility(View.VISIBLE);
                headerImage.setVisibility(View.INVISIBLE);
                // ImageLoader.getInstance().loadImage(imageURL, this);
                ImageLoader.getInstance().loadImage(rssItem.getImageUrl(), this);
            } else {
                // hide
                headerWrapper.setVisibility(View.GONE);
            }
        }

                 /*
          * ImageLoadingListener
          */

        @Override
        public void onLoadingStarted(String imageUri, View view) {
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            Log.e(TAG, "onLoadingFailed: " + failReason.toString() + " for URL: " + imageUri);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            // set if image just loaded matches URL for the most recent
            //if (imageUri.equals(imageURL)) {
                if (imageUri.equals(rssItem.getImageUrl())) {
                headerImage.setImageBitmap(loadedImage);
                headerImage.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            // Attempt a retry
            ImageLoader.getInstance().loadImage(imageUri, this);
        }
                /*
          * OnClickListener
          */

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), rssItem.getTitle(), Toast.LENGTH_SHORT).show();
        }

    }

}
