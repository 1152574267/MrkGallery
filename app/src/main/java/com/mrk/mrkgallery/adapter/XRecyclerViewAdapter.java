package com.mrk.mrkgallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mrk.mrkgallery.R;
import com.mrk.mrkgallery.bean.FileItem;

import java.util.List;

public class XRecyclerViewAdapter<T> extends RecyclerView.Adapter<XRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    private List<T> mDataList;

    public XRecyclerViewAdapter(Context context, List<T> dataList) {
        super();

        mContext = context;
        mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final T object = mDataList.get(position);

        if (object instanceof FileItem) {
            FileItem item = (FileItem) object;
            holder.tv.setText(item.getFileName());
            Glide.with(mContext).load(R.drawable.tab_dictionary).into(holder.img);
        }

        final int index = position;
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(index);
                }
            });
        }

        if (mItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    mItemLongClickListener.onItemLongClick(index);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public T getItem(int position) {
        return mDataList == null ? null : mDataList.get(position);
    }

    public void addItem(List<T> dataList) {
        mDataList.clear();
        for (int i = 0; i < dataList.size(); i++) {
            mDataList.add(dataList.get(i));
        }

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tv;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            img = (ImageView) itemView.findViewById(R.id.img);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }

}
