package com.mrk.mrkgallery.kotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mrk.mrkgallery.R
import com.mrk.mrkgallery.adapter.XRecyclerViewAdapter
import com.mrk.mrkgallery.bean.FileItem
import com.mrk.mrkgallery.decoration.MyDecoration
import com.mrk.mrkgallery.util.HiAIUtil
import com.mrk.mrkgallery.view.SceneDetectActivity

import kotlinx.android.synthetic.main.fragment_photo.*

class KtSceneListFragment : Fragment(), XRecyclerViewAdapter.OnItemClickListener, XRecyclerViewAdapter.OnItemLongClickListener {
    private val TAG = KtSceneListFragment::class.java.simpleName

    private var mContext: Context? = null
    private var mAdapter: XRecyclerViewAdapter<FileItem>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        HiAIUtil.initSceneContents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d(TAG, "onCreateView")

        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
    }

    @Override
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated")

        val layoutManager = GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false)
        tablist.layoutManager = layoutManager
        tablist.addItemDecoration(MyDecoration(mContext, MyDecoration.HORIZONTAL_LIST))

        val mFileList: MutableList<FileItem> = ArrayList()
        mFileList.clear()
        for (i in 0 until HiAIUtil.SCENE_CONTENTS.size()) {
            val item = FileItem()
            item.fileName = HiAIUtil.SCENE_CONTENTS.get(i)
            mFileList.add(item)
        }

        mAdapter = XRecyclerViewAdapter(mContext, mFileList)
        tablist.adapter = mAdapter
        mAdapter?.setOnItemClickListener(this)
        mAdapter?.setOnItemLongClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onDetach() {
        super.onDetach()

        mContext = null
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d(TAG, "setUserVisibleHint isVisibleToUser: $isVisibleToUser")
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(activity, SceneDetectActivity::class.java)
        intent.putExtra("scene_type", mAdapter?.getItem(position)?.fileName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }

    @Override
    override fun onItemLongClick(position: Int) {

    }

}
