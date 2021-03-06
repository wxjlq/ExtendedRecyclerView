package com.leo.extendedrecyclerview.adapters;

import com.leo.extendedrecyclerview.models.ViewItem;

import com.leo.extendedrecyclerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangliugeng on 2015/6/29.
 */
public abstract class LoadMoreAdapter extends CommonAdapter<ViewItem> {
    public static final int VIEW_TYPE_ITEM_LOAD_MORE = 2015063009;
    private int loadMoreLayoutId = R.layout.layout_more_progress;
    private boolean isLoading;
    private boolean isLoadingCompleted;
    private int pageCount = 25;


    private ILoadMoreCallback callback;

    public LoadMoreAdapter(List<ViewItem> mDatas) {
        super(mDatas);
        if (this.mDatas.size() >= pageCount) {
            this.mDatas.add(getLoadMoreItem());
        }
    }

    public LoadMoreAdapter(int pageCount, List<ViewItem> mDatas) {
        super(mDatas);
        this.pageCount = pageCount;
        if (this.mDatas.size() >= pageCount) {
            this.mDatas.add(getLoadMoreItem());
        }
    }

    public LoadMoreAdapter(List<ViewItem> mDatas, int loadMoreLayoutId) {
        super(mDatas);
        this.loadMoreLayoutId = loadMoreLayoutId;
        if (this.mDatas.size() >= pageCount) {
            this.mDatas.add(getLoadMoreItem());
        }
    }

    public LoadMoreAdapter(int pageCount, List<ViewItem> mDatas, int loadMoreLayoutId) {
        super(mDatas);
        this.pageCount = pageCount;
        this.loadMoreLayoutId = loadMoreLayoutId;
        if (this.mDatas.size() >= pageCount) {
            this.mDatas.add(getLoadMoreItem());
        }
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == VIEW_TYPE_ITEM_LOAD_MORE)
            return loadMoreLayoutId;
        return getNormalLayoutId();
    }

    public abstract int getNormalLayoutId();


    @Override
    public void onBindViewHolder(CommonAdapter.CommonViewHolder holder, int position) {

        if (position == mDatas.size() - 1 && position >= pageCount - 1 && !isLoading && !isLoadingCompleted) {
            isLoading = true;
            if (callback != null) {
                callback.loadMore(position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        ViewItem viewItem = mDatas.get(position);
        return viewItem.viewType;
    }


    @Override
    public void addAll(List<ViewItem> newData) {
        hideLoadMore();
        if (newData == null || newData.size() < pageCount) {
            isLoadingCompleted = true;
        } else if (!isLoadingCompleted) {
            newData.add(getLoadMoreItem());
        }

        if (newData != null && newData.size() > 0) {
            int start = mDatas.size();
            mDatas.addAll(newData);
            notifyItemRangeInserted(start, mDatas.size() - 1);
        }
        isLoading = false;
    }

    @Override
    public void replaceAll(List<ViewItem> newData) {
        clearAll();
        addAll(newData);
    }

    public void setLoadMoreCallback(ILoadMoreCallback callback) {
        this.callback = callback;
    }

    public boolean getLoadingCompleted() {
        return isLoadingCompleted;
    }

    public void setLoadingCompleted(boolean isLoadingCompleted) {
        this.isLoadingCompleted = isLoadingCompleted;
    }

    private ViewItem getLoadMoreItem() {
        return new ViewItem(VIEW_TYPE_ITEM_LOAD_MORE, null);
    }

    private void hideLoadMore() {
        int lastPosition = mDatas.size() - 1;
        if (lastPosition >= 0) {
            ViewItem viewItem = mDatas.get(lastPosition);
            if (viewItem.viewType == VIEW_TYPE_ITEM_LOAD_MORE) {
                mDatas.remove(lastPosition);
                notifyDataSetChanged();
//                notifyItemRemoved(lastPosition);
            }
        }
    }

    public interface ILoadMoreCallback {
        void loadMore(int position);
    }
}
