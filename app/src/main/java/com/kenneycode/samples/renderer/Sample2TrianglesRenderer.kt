package com.kenneycode.samples.renderer

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 *
 *      Coded by kenney
 *
 *      http://www.github.com/kenneycode
 *
 *      在第一个例子Hello World的基础上稍作修改，渲染2个三角形的例子
 *      This sample demonstrates how to render 2 triangles base on the code of our first sample
 *
 **/

class Sample2TrianglesRenderer : GLSurfaceView.Renderer {

    private val vertexShaderCode =
        "precision mediump float;\n" +
                "attribute vec4 a_Position;\n" +
                "void main() {\n" +
                "    gl_Position = a_Position;\n" +
                "}"

    private val fragmentShaderCode =
        "precision mediump float;\n" +
                "void main() {\n" +
                "    gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);\n" +
                "}"

    private var glSurfaceViewWidth = 0
    private var glSurfaceViewHeight = 0

    // 三角形顶点数据
    // The vertex data of a triangle
    private val vertexData = floatArrayOf(-0.5f, 1f, -1f, 0f, 0f, 0f, 0.5f, 0f, 0f, -1f, 1f, -1f)

    // 每个顶点的成份数
    // The num of components of per vertex
    private val VERTEX_COMPONENT_COUNT = 2

    override fun onDrawFrame(gl: GL10?) {
        // 设置清屏颜色
        // Set the color which the screen will be cleared to
        GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1f)

        // 清屏
        // Clear the screen
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // 设置视口，这里设置为整个GLSurfaceView区域
        // Set the viewport to the full GLSurfaceView
        GLES20.glViewport(0, 0, glSurfaceViewWidth, glSurfaceViewHeight)

        // 调用draw方法用TRIANGLES的方式执行渲染，顶点数量为3个
        // Call the draw method with GL_TRIANGLES to render 3 vertices
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexData.size / VERTEX_COMPONENT_COUNT)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // 记录GLSurfaceView的宽高
        // Record the width and height of the GLSurfaceView
        glSurfaceViewWidth = width
        glSurfaceViewHeight = height
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 创建GL程序
        // Create GL program
        val programId = GLES20.glCreateProgram()

        // 加载、编译vertex shader和fragment shader
        // Load and compile vertex shader and fragment shader
        val vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
        val fragmentShader= GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)
        GLES20.glShaderSource(vertexShader, vertexShaderCode)
        GLES20.glShaderSource(fragmentShader, fragmentShaderCode)
        GLES20.glCompileShader(vertexShader)
        GLES20.glCompileShader(fragmentShader)

        // 将shader程序附着到GL程序上
        // Attach the compiled shaders to the GL program
        GLES20.glAttachShader(programId, vertexShader)
        GLES20.glAttachShader(programId, fragmentShader)

        // 链接GL程序
        // Link the GL program
        GLES20.glLinkProgram(programId)

        // 将三角形顶点数据放入buffer中
        // Put the triangle vertex data into the buffer
        val buffer = ByteBuffer.allocateDirect(vertexData.size * java.lang.Float.SIZE)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        buffer.put(vertexData)
        buffer.position(0)

        // 应用GL程序
        // Use the GL program
        GLES20.glUseProgram(programId)

        // 获取字段a_Position在shader中的位置
        // Get the location of a_Position in the shader
        val location = GLES20.glGetAttribLocation(programId, "a_Position")

        // 启动对应位置的参数
        // Enable the parameter of the location
        GLES20.glEnableVertexAttribArray(location)

        // 指定a_Position所使用的顶点数据
        // Specify the vertex data of a_Position
        GLES20.glVertexAttribPointer(location, VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false,0, buffer)
    }

}