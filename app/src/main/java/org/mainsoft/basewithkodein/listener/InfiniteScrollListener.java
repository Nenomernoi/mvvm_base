package org.mainsoft.basewithkodein.listener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.mainsoft.basewithkodein.adapter.base.SpannedGridLayoutManager;

public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private static final int VISIBLE_THRESHOLD = 5;

    private final RecyclerView.LayoutManager layoutManager;
    private final FirstVisibleItemPositionFetcher firstVisiblePositionFetcher;
    private Boolean isLoad = false;

    public InfiniteScrollListener(@NonNull final LinearLayoutManager llm) {
        this.layoutManager = llm;
        firstVisiblePositionFetcher = llm::findFirstVisibleItemPosition;
    }

    public InfiniteScrollListener(@NonNull final SpannedGridLayoutManager sglm) {
        this.layoutManager = sglm;
        firstVisiblePositionFetcher = sglm::getFirstVisibleItemPosition;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy < 0 || isLoad) return;

        final int visibleItemCount = recyclerView.getChildCount();
        final int totalItemCount = layoutManager.getItemCount();
        final int firstVisibleItem = firstVisiblePositionFetcher.getFirstVisibleItemPosition();

        if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
            isLoad = true;
            onLoadMore();
        }
    }

    public void setLoad(Boolean isLoad) {
        this.isLoad = isLoad;
    }

    public void resetState() {
        //
    }

    public abstract void onLoadMore();

    private interface FirstVisibleItemPositionFetcher {
        int getFirstVisibleItemPosition();
    }

}