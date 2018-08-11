package com.mrk.mrkgallery.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mrk.mrkgallery.R;
import com.mrk.mrkgallery.adapter.XRecyclerViewAdapter;
import com.mrk.mrkgallery.bean.FileItem;
import com.mrk.mrkgallery.decoration.MyDecoration;

import java.util.ArrayList;
import java.util.List;

public class MnistListFragment extends Fragment
        implements XRecyclerViewAdapter.OnItemClickListener, XRecyclerViewAdapter.OnItemLongClickListener {
    private static final String TAG = MnistListFragment.class.getSimpleName();

    private Context mContext;
    private RecyclerView mRecyclerView;
    private XRecyclerViewAdapter<FileItem> mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.tablist);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new MyDecoration(mContext, MyDecoration.HORIZONTAL_LIST));

        List<FileItem> mFileList = new ArrayList<FileItem>();
        mFileList.clear();

        FileItem item = new FileItem();
        item.setFileName("mnist detect");
        mFileList.add(item);

        mAdapter = new XRecyclerViewAdapter<FileItem>(mContext, mFileList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mContext = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint isVisibleToUser: " + isVisibleToUser);
    }

    @Override
    public void onItemClick(int position) {
//        Toast.makeText(mContext, "onItemClick: " + position, Toast.LENGTH_SHORT).show();

//        Intent intent = new Intent(getActivity(), SceneDetectActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getActivity().startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
//        Toast.makeText(mContext, "onItemLongClick: " + position, Toast.LENGTH_SHORT).show();
    }

}
