package com.example.opengles30.passvalue;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;

import com.example.opengles30.ShaderUtils;
import com.example.opengles30.VLog;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GlRender implements GLSurfaceView.Renderer {
    private static String TAG = GlRender.class.getName();
    int BYTES_PER_FLOAT = 4;
    //定义顶点，不重复
    float vertices[] = {
            0.5f, 0.5f, 0.0f,   // 右上角
            0.5f, -0.5f, 0.0f,  // 右下角
            -0.5f, -0.5f, 0.0f, // 左下角
            -0.5f, 0.5f, 0.0f   // 左上角
    };
    //定义索引
    int indices[] = { // 注意索引从0开始!
            0, 1, 3, // 第一个三角形
            1, 2, 3  // 第二个三角形
    };

    //定义一个顶点缓冲对象
    FloatBuffer mVBFloatBuffer;
    //定义一个索引缓冲对象
    IntBuffer mEBIntBuffer;
    int[] eBos = new int[1];

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
        //生成一个缓冲对象
        GLES32.glGenBuffers(array.length, array, 0);
        vBos[0] = array[0];

        mVBFloatBuffer = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVBFloatBuffer.put(vertices);
        mVBFloatBuffer.position(0);

        GLES32.glBindVertexArray(vAos[0]);
        //把新创建的缓冲对象绑定到GL_ARRAY_BUFFER目标上(OpenGL有很多缓冲对象类型，顶点缓冲对象的缓冲类型是GL_ARRAY_BUFFER)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vBos[0]);

        //把之前定义的顶点数据复制到缓冲的内存中
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, vertices.length * BYTES_PER_FLOAT, mVBFloatBuffer, GLES32.GL_STATIC_DRAW);


        //-------------------先绘制顶点缓冲，再绘制索引缓冲
        //索引缓冲对象
        array = new int[1];
        GLES32.glGenBuffers(array.length, array, 0);
        eBos[0] = array[0];
        mEBIntBuffer = ByteBuffer.allocateDirect(indices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
        mEBIntBuffer.put(indices);
        mEBIntBuffer.position(0);
        //先绑定EBO然后用glBufferData把索引复制到缓冲里
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, eBos[0]);
        GLES32.glBufferData(GLES32.GL_ELEMENT_ARRAY_BUFFER, indices.length * BYTES_PER_FLOAT, mEBIntBuffer, GLES32.GL_STATIC_DRAW);




        //end---------------现在我们已经把顶点数据储存在显卡的内存中，用VBO这个顶点缓冲对象管理---------------------------------
        //着色器
        vShader = ShaderUtils.loadShaderFromAssets(context, GLES32.GL_VERTEX_SHADER, "triangle_pass_value.vsh");
        fShader = ShaderUtils.loadShaderFromAssets(context, GLES32.GL_FRAGMENT_SHADER, "triangle_pass_value.fsh");


        //着色器程序
        //如果要使用刚才编译的着色器我们必须把它们链接(Link)为一个着色器程序对象，
        // 然后在渲染对象的时候激活这个着色器程序。已激活着色器程序的着色器将在我们发送渲染调用的时候被使用。
        shaderProgram = GLES32.glCreateProgram();
        GLES32.glAttachShader(shaderProgram, vShader);
        GLES32.glAttachShader(shaderProgram, fShader);
        GLES32.glLinkProgram(shaderProgram);
        //判断链接是否成功
        int [] result = new int[1];
        GLES32.glGetProgramiv(shaderProgram, GLES32.GL_LINK_STATUS, result, 0);
        if (result[0] == GLES32.GL_FALSE) {
            String log = GLES32.glGetProgramInfoLog(shaderProgram);
            VLog.Loge(TAG, log);
        }

        //激活程序
        GLES32.glUseProgram(shaderProgram);
        //着色器对象链接到程序对象以后，记得删除着色器对象，我们不再需要它们了
        GLES32.glDeleteShader(vShader);
        GLES32.glDeleteShader(fShader);

        //--------------------现在，我们已经把输入顶点数据发送给了GPU，并指示了GPU如何在顶点和片段着色器中处理它了-----------

        //-------OpenGL还不知道它该如何解释内存中的顶点数据，以及它该如何将顶点数据链接到顶点着色器的属性上。我们需要告诉OpenGL怎么做---------


        //在渲染前指定OpenGL该如何解释顶点数据
        GLES32.glVertexAttribPointer(
                0,//layout(location = 0)
                3,//顶点属性的大小,顶点属性是一个vec3，它由3个值组成，所以大小是3
                GLES32.GL_FLOAT,//数据的类型, vec*都是由浮点数值组成的
                false,//是否希望数据被标准化,如果我们设置为GL_TRUE，所有数据都会被映射到0（对于有符号型signed数据是-1）到1之间
                3 * BYTES_PER_FLOAT,//步长,连续的顶点属性组之间的间隔
                0
        );

        //以顶点属性位置值作为参数，启用顶点属性；顶点属性默认是禁用的
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


        //顶点绘制
//        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, 3);
        //索引绘制
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, 6, GLES32.GL_UNSIGNED_INT, 0);

        GLES32.glBindVertexArray(0);
        GLES32.glDisableVertexAttribArray(0);

    }

    public void destroy() {
        GLES32.glDeleteVertexArrays(1, vAos,0);
        GLES32.glDeleteBuffers(1, vBos, 0);
        GLES32.glDeleteProgram(shaderProgram);
    }
}
