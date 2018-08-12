package com.mrk.mrkgallery.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrk.mrkgallery.R;
import com.bumptech.glide.Glide;
import com.mrk.mrkgallery.bean.PhotoItem;
import com.mrk.mrkgallery.util.TfAIUtil;

import java.util.List;

public class MRecyclerViewAdapter<T> extends RecyclerView.Adapter<MRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    private List<T> mDataList;
    private int mModuleIndex;

    public MRecyclerViewAdapter(Context context, List<T> dataList, int moduleIndex) {
        super();

        mContext = context;
        mDataList = dataList;
        mModuleIndex = moduleIndex;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.m_recyclerview_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final T object = mDataList.get(position);

        if (object instanceof PhotoItem) {
            PhotoItem item = (PhotoItem) object;
            holder.tv_name.setText("name: " + item.getPhotoName());
            if (mModuleIndex == TfAIUtil.MODULE_SCENE_DETECT) {
                holder.tv_category.setText("sceneType: " + item.getPhotoCategory());
                holder.tv_type.setText(" ");
            } else if (mModuleIndex == TfAIUtil.MODULE_LABEL_DETECT) {
                holder.tv_category.setText("category: " + item.getPhotoCategory());
                holder.tv_type.setText("labelContent: " + item.getPhotoLabel());
            } else if (mModuleIndex == TfAIUtil.MODULE_TF_DETECT) {
                holder.tv_category.setText("objectType: " + item.getPhotoCategory());
                holder.tv_type.setText(" ");
            } else if (mModuleIndex == TfAIUtil.MODULE_MNIST_DETECT) {
                holder.tv_category.setText("mnistType: " + item.getPhotoCategory());
                holder.tv_type.setText(" ");
            }

            int width = ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams params = holder.img.getLayoutParams();
            // 设置图片的相对于屏幕的宽高比
            params.width = width / 2;
            params.height = (int) (200 + Math.random() * 400);
            holder.img.setLayoutParams(params);
            if (mModuleIndex == TfAIUtil.MODULE_MNIST_DETECT) {
                holder.img.setImageResource(item.getPhotoResId());
            } else {
                Glide.with(mContext).load(item.getPhotoPath()).into(holder.img);
            }
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

    public void addAll(List<T> dataList) {
        mDataList.clear();
        for (int i = 0; i < dataList.size(); i++) {
            mDataList.add(dataList.get(i));
        }

        notifyDataSetChanged();
    }

    public void addItem(T itemData) {
        mDataList.add(itemData);

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tv_name;
        TextView tv_category;
        TextView tv_type;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            img = (ImageView) itemView.findViewById(R.id.img);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_category = (TextView) itemView.findViewById(R.id.tv_category);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
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
