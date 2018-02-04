package com.tonywis.movieexplorer.ui.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.tonywis.movieexplorer.BuildConfig;
import com.tonywis.movieexplorer.R;
import com.tonywis.movieexplorer.models.contents.lists.Movie;
import com.tonywis.movieexplorer.ui.MainActivity;

import java.util.List;

/**
 * Created by Tony on 03/02/2018.
 */

public class GridListRecyclerAdapter extends RecyclerView.Adapter<GridListRecyclerAdapter.ViewHolder> {

    private List<Movie> mDatas;
    private MainActivity mMainActivity;
    private ItemClickListener mClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AppCompatImageView mPosterView;
        public AppCompatTextView mTitleView;

        public ViewHolder(View itemView) {
            super(itemView);
            mPosterView = itemView.findViewById(R.id.item_tile_poster);
            mTitleView = itemView.findViewById(R.id.item_tile_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    /**
     *
     * @param mainActivity
     * @param datas
     */
    public GridListRecyclerAdapter(MainActivity mainActivity, List<Movie> datas) {
        mMainActivity = mainActivity;
        mDatas = datas;
    }

    @Override
    public GridListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tile, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GridListRecyclerAdapter.ViewHolder holder, int position) {
        Movie m = mDatas.get(position);
        holder.mTitleView.setText(m.title);
        if (m.poster_path != null) {
            Picasso.with(mMainActivity)
                    .load(BuildConfig.URL_TMDB_IMAGES+m.poster_path)
                    .error(R.drawable.ic_placeholder)
                    .into(holder.mPosterView);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     *
     * @param position
     * @return Movie
     */
    public Movie getItem(int position) {
        return mDatas.get(position);
    }

    /**
     *
     * @param itemClickListener
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
