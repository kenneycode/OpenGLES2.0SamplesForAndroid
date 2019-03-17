package com.kenneycode

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kenneycode.samples.Sample2Triangles
import com.kenneycode.samples.SampleHelloWorld

/**
 *
 *      Coded by kenney
 *
 *      http://www.github.com/kenneycode
 *
 *      Sample展示类
 *      This sample activity
 *
 **/

class SimpleActivity : AppCompatActivity() {

    private val samples = arrayOf(SampleHelloWorld(), Sample2Triangles())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        val glSurfaceView = findViewById<GLSurfaceView>(R.id.glsurfaceview)
        // 设置RGBA颜色缓冲、深度缓冲及stencil缓冲大小
        // Set the size of RGBA、depth and stencil buffer
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0)
        // 设置GL版本，这里设置为2.0
        // Set GL version, here I set it to 2.0
        glSurfaceView.setEGLContextClientVersion(2)
        val sampleName = intent.getStringExtra(GlobalConstants.KEY_SAMPLE_NAME)
        title = sampleName
        val sampleIndex = intent.getIntExtra(GlobalConstants.KEY_SAMPLE_INDEX, -1)
        if (sampleIndex >= 0) {
            // 设置对应sample的渲染器
            // Set the corresponding sample renderer
            glSurfaceView.setRenderer(samples[sampleIndex])
        }
    }

}
