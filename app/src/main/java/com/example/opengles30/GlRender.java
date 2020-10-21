package com.example.opengles30;

import android.opengl.GLES32;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GlRender implements GLSurfaceView.Renderer {
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

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

    }
}
