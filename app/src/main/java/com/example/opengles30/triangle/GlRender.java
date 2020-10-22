package com.example.opengles30.triangle;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;

import com.example.opengles30.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GlRender implements GLSurfaceView.Renderer {
    int BYTES_PER_FLOAT = 4;
    //1、顶点输入
    float vertices[] = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f,  0.5f, 0.0f
    };

    //2、定义一个顶点缓冲对象
    FloatBuffer mVBFloatBuffer;

    int[] vBos = new int[1];
    int vShader;
    int fShader;
    int shaderProgram;
    int[] vAos = new int[1];
    Context context;
    public GlRender(Context context) {
        this.context = context;
    }

    /**
     * 初始化代码，只需要运行一次
     */
    private void init(Context context) {
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        init(context);

        //顶点数组对象
        int[] array = new int[1];
        GLES32.glGenVertexArrays(array.length, array, 0);
        vAos[0] = array[0];


        //顶点缓冲对象
        array = new int[1];
        GLES32.glGenBuffers(array.length, array, 0);
        vBos[0] = array[0];

        mVBFloatBuffer = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVBFloatBuffer.put(vertices);
        mVBFloatBuffer.position(0);

        GLES32.glBindVertexArray(vAos[0]);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vBos[0]);
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, vertices.length * BYTES_PER_FLOAT, mVBFloatBuffer, GLES32.GL_STATIC_DRAW);

        //着色器
        vShader = ShaderUtils.loadShaderFromAssets(context, GLES32.GL_VERTEX_SHADER, "triangle.vsh");
        fShader = ShaderUtils.loadShaderFromAssets(context, GLES32.GL_FRAGMENT_SHADER, "triangle.fsh");


        //着色器程序
        shaderProgram = GLES32.glCreateProgram();
        GLES32.glAttachShader(shaderProgram, vShader);
        GLES32.glAttachShader(shaderProgram, fShader);
        GLES32.glLinkProgram(shaderProgram);
        GLES32.glUseProgram(shaderProgram);
        GLES32.glDeleteShader(vShader);
        GLES32.glDeleteShader(fShader);


        GLES32.glVertexAttribPointer(
                0,//layout(location = 0)
                3,//顶点属性的大小,顶点属性是一个vec3，它由3个值组成，所以大小是3
                GLES32.GL_FLOAT,//数据的类型, vec*都是由浮点数值组成的
                false,//是否希望数据被标准化,如果我们设置为GL_TRUE，所有数据都会被映射到0（对于有符号型signed数据是-1）到1之间
                3 * BYTES_PER_FLOAT,//步长,连续的顶点属性组之间的间隔
                0
        );

        GLES32.glEnableVertexAttribArray(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        //告诉OpenGL渲染窗口的尺寸大小，即视口(Viewport)，这样OpenGL才能知道怎样根据窗口大小显示数据和坐标
        GLES32.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        /**
         * 当调用glClear函数，清除颜色缓冲之后，整个颜色缓冲都会被填充为glClearColor里所设置的颜色
         * glClearColor函数是一个状态设置函数，而glClear函数则是一个状态使用的函数，它使用了当前的状态来获取应该清除为的颜色。
         */
        //设置清空屏幕所用的颜色
        GLES32.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        //清空屏幕的颜色缓冲
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT);

        GLES32.glUseProgram(shaderProgram);
        GLES32.glEnableVertexAttribArray(0);

        GLES32.glBindVertexArray(vAos[0]);


        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, 3);

        GLES32.glBindVertexArray(0);
        GLES32.glDisableVertexAttribArray(0);

    }

    public void destroy() {
        GLES32.glDeleteVertexArrays(1, vAos,0);
        GLES32.glDeleteBuffers(1, vBos, 0);
        GLES32.glDeleteProgram(shaderProgram);
    }
}
