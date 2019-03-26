package com.kenneycode.samples.renderer

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
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
 *      这是一个Hello World例子，渲染一个最简单的三角形
 *      This is the first sample Hello World, and it will show you how to render a simplest triangle
 *
 **/

class SampleCheckError : GLSurfaceView.Renderer {

    companion object {
        val LOG_TAG = SampleCheckError::class.simpleName
    }


    private var glSurfaceViewWidth = 0
    private var glSurfaceViewHeight = 0

    // 三角形顶点数据
    // The vertex data of a triangle
    private val vertexData = floatArrayOf(0f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f)

    // 每个顶点的成份数
    // The num of components of per vertex
    private val VERTEX_COMPONENT_COUNT = 2

    override fun onDrawFrame(gl: GL10?) {
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
        val vertexShaderCodeWrong =
            "precision mediump float;\n" +
                    "attribute vec4 a_Position;\n" +
                    "void main() {\n" +
                    "    gl_Posss ition = a_Position;\n" +
                    "}"

        val fragmentShaderCode =
            "precision mediump float;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);\n" +
                    "}"
        val vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
        val fragmentShader= GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)
        GLES20.glShaderSource(vertexShader, vertexShaderCodeWrong)
        checkErrorForGL()
        GLES20.glShaderSource(fragmentShader, fragmentShaderCode)
        checkErrorForGL()
        GLES20.glCompileShader(vertexShader)
        checkErrorForGL()
        val compileStatus = intArrayOf(1)
        GLES20.glGetShaderiv(vertexShader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
        if (compileStatus[0] != 0) {
            val info = GLES20.glGetShaderInfoLog(vertexShader)
            Log.e(LOG_TAG, "shader compile info: $info")
        }
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
        GLES20.glVertexAttribPointer(location, 2, GLES20.GL_FLOAT, false,0, buffer)
    }

    fun checkErrorForGL() {
        val error = GLES20.glGetError()
        if (error != GLES20.GL_NO_ERROR) {
            val hexErrorCode = Integer.toHexString(error)
            val s = "gl Error: $hexErrorCode"
            Log.e(LOG_TAG, s)
            throw RuntimeException(s)
        }
    }

}