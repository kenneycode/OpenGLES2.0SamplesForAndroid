package com.kenneycode.samples.fragment

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kenneycode.R
import com.kenneycode.samples.renderer.Sample2TrianglesRenderer
import com.kenneycode.samples.renderer.SampleHelloWorldRenderer

/**
 *
 *      Coded by kenney
 *
 *      http://www.github.com/kenneycode
 *
 *      在第一个例子Hello World的基础上稍作修改，通过Sample2TrianglesRenderer渲染2个三角形的例子
 *      This sample demonstrates how to render 2 triangles in Sample2TrianglesRenderer base on the code of our first sample
 *
 **/

class Sample2Triangles : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_common_sample, container,  false)
        val glSurfaceView = rootView.findViewById<GLSurfaceView>(R.id.glsurfaceview)
        // 设置RGBA颜色缓冲、深度缓冲及stencil缓冲大小
        // Set the size of RGBA、depth and stencil buffer
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0)
        // 设置GL版本，这里设置为2.0
        // Set GL version, here I set it to 2.0
        glSurfaceView.setEGLContextClientVersion(2)
        // 设置对应sample的渲染器
        // Set the corresponding sample renderer
        glSurfaceView.setRenderer(Sample2TrianglesRenderer())
        return rootView
    }
}