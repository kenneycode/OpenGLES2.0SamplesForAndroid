package com.kenneycode.samples

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
 *      这是一个利用fragment shader渲染彩色三角形例子
 *      This is a sample of using fragment shader to render a colorful triangle
 *
 **/

class SampleFragmentShader : GLSurfaceView.Renderer {

    private val vertexShaderCode =
        "precision mediump float;\n" +
                "attribute vec4 a_Position;\n" +
                "attribute vec4 a_Color;\n" +
                "varying vec4 v_Color;\n" +
                "void main() {\n" +
                "    v_Color = a_Color;\n" +
                "    gl_Position = a_Position;\n" +
                "}"

    private val fragmentShaderCode =
        "precision mediump float;\n" +
                "varying vec4 v_Color;\n" +
                "void main() {\n" +
                "    gl_FragColor = v_Color;\n" +
                "}"

    private var glSurfaceViewWidth = 0
    private var glSurfaceViewHeight = 0

    // 三角形顶点数据
    // The vertex data of a triangle
    private val vertexData = floatArrayOf(0f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f)


    private val colorData = floatArrayOf(
                                        1.0f, 0.0f, 0.0f, 1.0f,
                                        0.0f, 1.0f, 0.0f, 1.0f,
                                        0.0f, 0.0f, 1.0f, 1.0f)

    // 每个顶点的成份数
    // The num of components of per vertex
    private val VERTEX_COMPONENT_COUNT = 2

    // 每个颜色的成份数（RGBA）
    // The num of components of per color(RGBA)
    private val COLOR_COMPONENT_COUNT = 4

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
        val vertexDataBuffer = ByteBuffer.allocateDirect(vertexData.size * java.lang.Float.SIZE)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexDataBuffer.put(vertexData)
        vertexDataBuffer.position(0)

        // 应用GL程序
        // Use the GL program
        GLES20.glUseProgram(programId)

        // 获取字段a_Position在shader中的位置
        // Get the location of a_Position in the shader
        val aPositionLocation = GLES20.glGetAttribLocation(programId, "a_Position")

        // 启动对应位置的参数
        // Enable the parameter of the location
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        // 指定a_Position所使用的顶点数据
        // Specify the vertex data of a_Position
        GLES20.glVertexAttribPointer(aPositionLocation, VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false,0, vertexDataBuffer)

        // 将颜色数据放入buffer中
        // Put the color data into the buffer
        val colorDataBuffer = ByteBuffer.allocateDirect(colorData.size * java.lang.Float.SIZE)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        colorDataBuffer.put(colorData)
        colorDataBuffer.position(0)

        // 获取字段a_Color在shader中的位置
        // Get the location of a_Color in the shader
        val aColorLocation = GLES20.glGetAttribLocation(programId, "a_Color")

        // 启动对应位置的参数
        // Enable the parameter of the location
        GLES20.glEnableVertexAttribArray(aColorLocation)

        // 指定a_Color所使用的顶点数据
        // Specify the vertex data of a_Color
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false,0, colorDataBuffer)
    }

}