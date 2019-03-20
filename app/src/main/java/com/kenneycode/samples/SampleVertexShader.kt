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
 *      这是一个使用vertex shader做顶点变换的例子，例子中将演示平移、缩放和旋转变换
 *      This sample demonstrates how to do vertex translation，scale and rotation using vertex shader
 *
 **/

class SampleVertexShader : GLSurfaceView.Renderer {

    private val vertexShaderCode =
            "precision mediump float;\n" +
            "attribute vec4 a_Position;\n" +
            "\n" +
            "uniform vec2 u_Translate;\n" +
            "uniform float u_Scale;\n" +
            "uniform float u_Rotate;\n" +
            "uniform float u_Ratio;\n" +
            "\n" +
            "void main() {\n" +
            "   vec4 p = a_Position;\n" +
            "   p.y = p.y / u_Ratio;\n" +
            "   mat4 translateMatrix = mat4(1.0, 0.0, 0.0, 0.0,\n" +
            "                              0.0, 1.0, 0.0, 0.0,\n" +
            "                              0.0, 0.0, 1.0, 0.0,\n" +
            "                              u_Translate.x, u_Translate.y, 0.0, 1.0);\n" +
            "   mat4 scaleMatrix = mat4(u_Scale, 0.0, 0.0, 0.0,\n" +
            "                        0.0, u_Scale, 0.0, 0.0,\n" +
            "                        0.0, 0.0, 1.0, 0.0,\n" +
            "                        0.0, 0.0, 0.0, 1.0);\n" +
            "   mat4 rotateMatrix = mat4(cos(u_Rotate), sin(u_Rotate), 0.0, 0.0,\n" +
            "                         -sin(u_Rotate), cos(u_Rotate), 0.0, 0.0,\n" +
            "                         0.0, 0.0, 1.0, 0.0,\n" +
            "                         0.0, 0.0, 0.0, 1.0);\n" +
            "    p = translateMatrix * rotateMatrix * scaleMatrix * p;\n" +
            "    p.y = p.y * u_Ratio;\n" +
            "    gl_Position = p;\n" +
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
    private val vertexData = floatArrayOf(0f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f)

    // 每个顶点的成份数
    // The num of components of per vertex
    private val VERTEX_COMPONENT_COUNT = 2

    // GL程序id
    // The GL program id
    private var programId: Int = 0

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

        // 获取字段u_Ratio在shader中的位置
        // Get the location of u_Rotate in the shader
        val uRatioLocation = GLES20.glGetUniformLocation(programId, "u_Ratio")

        // 启动对应位置的参数
        // Enable the parameter of the location
        GLES20.glEnableVertexAttribArray(uRatioLocation)

        // 指定u_Ratio所使用的顶点数据
        // Specify the vertex data of u_Ratio
        GLES20.glUniform1f(uRatioLocation, glSurfaceViewWidth * 1.0f / glSurfaceViewHeight)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 创建GL程序
        // Create GL program
        programId = GLES20.glCreateProgram()

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
        val aPositionLocation = GLES20.glGetAttribLocation(programId, "a_Position")

        // 启动对应位置的参数
        // Enable the parameter of the location
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        // 指定a_Position所使用的顶点数据
        // Specify the vertex data of a_Position
        GLES20.glVertexAttribPointer(aPositionLocation, 2, GLES20.GL_FLOAT, false,0, buffer)

        // 获取字段u_Offset在shader中的位置
        // Get the location of translate in the shader
        val uTranslateLocation = GLES20.glGetUniformLocation(programId, "u_Translate")

        // 启动对应位置的参数
        // Enable the parameter of the location
        GLES20.glEnableVertexAttribArray(uTranslateLocation)

        // 指定u_Offset所使用的顶点数据
        // Specify the vertex data of translate
        GLES20.glUniform2f(uTranslateLocation, 0.3f, 0.3f)

        // 获取字段u_Offset在shader中的位置
        // Get the location of u_Scale in the shader
        val uScaleLocation = GLES20.glGetUniformLocation(programId, "u_Scale")

        // 启动对应位置的参数
        // Enable the parameter of the location
        GLES20.glEnableVertexAttribArray(uScaleLocation)

        // 指定u_Scale所使用的顶点数据
        // Specify the vertex data of u_Scale
        GLES20.glUniform1f(uScaleLocation, 0.5f)

        // 获取字段u_Offset在shader中的位置
        // Get the location of u_Rotate in the shader
        val uRotateLocation = GLES20.glGetUniformLocation(programId, "u_Rotate")

        // 启动对应位置的参数
        // Enable the parameter of the location
        GLES20.glEnableVertexAttribArray(uRotateLocation)

        // 指定u_Rotate所使用的顶点数据
        // Specify the vertex data of u_Rotate
        GLES20.glUniform1f(uRotateLocation, Math.toRadians(45.0).toFloat())

    }

}