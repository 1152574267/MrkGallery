package com.mrk.mrkgallery.kotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.mrk.mrkgallery.MainActivity
import com.mrk.mrkgallery.R
import com.mrk.mrkgallery.view.MnistDetectActivity
import com.mrk.mrkgallery.view.ObjectDetectActivity
import kotlinx.android.synthetic.main.fragment_normal_home.*
import org.tensorflow.demo.DetectorActivity

class KtHomePageActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_normal_home)

        initView()
    }

    private fun initView() {
        ll_home_scene_detect.setOnClickListener(this)
        rl_home_label_detect.setOnClickListener(this)
        ll_home_object_detect.setOnClickListener(this)
        ll_home_mnist_detect.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var intent = Intent()

        when (v?.id) {
            R.id.ll_home_scene_detect -> {
                intent.setClass(this, MainActivity::class.java)
            }
            R.id.rl_home_label_detect -> {
                intent.setClass(this, DetectorActivity::class.java)
            }
            R.id.ll_home_object_detect -> {
                intent.setClass(this, ObjectDetectActivity::class.java)
            }
            R.id.ll_home_mnist_detect -> {
                intent.setClass(this, MnistDetectActivity::class.java)
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}
