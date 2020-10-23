package com.example.opengles30.camera_circle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.example.opengles30.R;
import com.example.opengles30.ShaderUtils;
import com.example.opengles30.VLog;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GlRender implements GLSurfaceView.Renderer {
    private static String TAG = GlRender.class.getName();
    int BYTES_PER_FLOAT = 4;
    //    float vertices[] = {
////     ---- 位置 ----       ---- 颜色 ----     - 纹理坐标 -
//            0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 0.0f,   // 右上
//            0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 1.0f,   // 右下
//            -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 1.0f,   // 左下
//            -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 0.0f    // 左上
//    };
    float vertices[] = {
            //后面
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, //左下
            0.5f, -0.5f, -0.5f,  1.0f, 0.0f, // 右下
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f, // 右上
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f, // 右上
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,// 左上
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,//左下

            //前面
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, //左下
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f, // 右下
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f, // 有上
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f, // 右上
            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f, //左上
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, //左下

            //左边
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, //近上
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f, //远上
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, //远下
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, //远下
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, //近下
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,//近上

            //右边
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,//近上
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,//远上
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,//远下
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,//远下
            0.5f, -0.5f,  0.5f,  0.0f, 0.0f,//近下
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,//近上

            //下边
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,//远左
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f,//远右
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,//近右
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,//近右
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,//近左
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,//远左

            //上边
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,//远左
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,//远右
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,//近右
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,//近右
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,//近左
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f//远左
    };

    //为每个立方体定义一个位移向量来指定它在世界空间的位置
    float worlds[][] = new float[][]{
            { 0.0f,  0.0f,  0.0f },
            { 2.0f,  5.0f, -15.0f },
            { -1.5f, -2.2f, -2.5f },
            { -3.8f, -2.0f, -12.3f },
            {  2.4f, -0.4f, -3.5f },
            { -1.7f,  3.0f, -7.5f },
            {  1.3f, -2.0f, -2.5f },
            { 1.5f,  2.0f, -2.5f },
            { 1.5f,  0.2f, -1.5f },
            { -1.3f,  1.0f, -1.5f }
    };

    float rotates[][] = new float[][]{
            { 0.5f,  1.0f,  0.0f },
            { 0.3f,  0.5f, 0.5f },
            { 0.4f,  1.0f,  0.4f },
            { 0.6f,  0.6f, 0.1f },
            { 1f,  1.0f,  0.0f },
            { 0.5f,  0.7f, 0.5f },
            { 0.2f,  1.0f,  0.7f },
            { 0.3f,  0.1f, 0.5f },
            { 0.1f,  1.0f,  0.8f },
            { 0.5f,  0.1f, 0.5f },
    };
    //2、定义一个顶点缓冲对象
    FloatBuffer mVBFloatBuffer;

    int[] vBos = new int[1];
    int vShader;
    int fShader;
    int shaderProgram;
    int[] vAos = new int[1];
    Context context;
    int[] texTures = new int[2];

    FloatBuffer modelFloatBuffer;
    FloatBuffer viewFloatBuffer;
    FloatBuffer projectionFloatBuffer;
    //单位矩阵
    float[] unitMatrix;
    float screenWidth = 1080;
    float screenHeight = 2073;
    long startTime;

    public GlRender(Context context) {
        this.context = context;
    }

    /**
     * 初始化代码，只需要运行一次
     */
    private void init(Context context) {
        startTime = System.currentTimeMillis() / 1000;
    }

    public void reset() {
        //mOpMatrix转化为单位阵
        unitMatrix = new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1,
        };
    }
    long mills = 1 ;
    /**
     * 模型矩阵
     */
    private void modelMatrix() {
        reset();
        mills ++;
        Matrix.rotateM(unitMatrix, 0, mills + 50, 0.5f, 1, 0);
        modelFloatBuffer = ByteBuffer.allocateDirect(unitMatrix.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        modelFloatBuffer.put(unitMatrix);
        modelFloatBuffer.position(0);
    }

    /**
     * 观察矩阵
     */
    private void viewMatrix() {
        reset();
        float[] unitMatrix = getCameraMatrix();
        viewFloatBuffer = ByteBuffer.allocateDirect(unitMatrix.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        viewFloatBuffer.put(unitMatrix);
        viewFloatBuffer.position(0);
    }

    /**
     * 投影矩阵
     */
    private void projectionMatrix() {
        reset();
        Matrix.perspectiveM(unitMatrix, 0, 45.0f, screenWidth / screenHeight, 0.1f, 100.0f);
        projectionFloatBuffer = ByteBuffer.allocateDirect(unitMatrix.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        projectionFloatBuffer.put(unitMatrix);
        projectionFloatBuffer.position(0);
    }

    float radius = 10;
    private float[] getCameraMatrix() {
        //创建观察矩阵

        float mill = mills * 0.01f;
        float cameraX = (float) (Math.sin(mill) * radius);
        float cameraZ = (float) (Math.cos(mill) * radius);
        float[] results = new float[16];
        Matrix.setLookAtM(
                results,
                0,
                cameraX,
                0,
                cameraZ,
                0,
                0,
                0,
                0,
                1,
                0
        );
        return results;
    }

    private void genTexture() {
        int[] pics = {R.mipmap.texturebox, R.mipmap.face};
        for (int i = 0; i < texTures.length; i++) {
            //创建纹理对象
            int[] array = new int[1];
            GLES32.glGenTextures(array.length, array, 0);
            texTures[i] = array[0];
            //绑定纹理
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, texTures[i]);
            //为当前绑定的纹理对象设置环绕、过滤方式
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_S, GLES32.GL_REPEAT);
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_WRAP_T, GLES32.GL_REPEAT);
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MIN_FILTER, GLES32.GL_LINEAR);
            GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D, GLES32.GL_TEXTURE_MAG_FILTER, GLES32.GL_LINEAR);
            //利用图片数据生成一个纹理图像
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
//        options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), pics[i], options);
            /**
             * level:参数为纹理指定多级渐远纹理的级别,0也就是基本级别。
             * border:应该总是被设为0（历史遗留的问题）
             */
            //给当前绑定的纹理对象附加上纹理图像
            GLUtils.texImage2D(GLES32.GL_TEXTURE_2D, 0, bitmap, 0);
            //为当前绑定的纹理自动生成所有需要的多级渐远纹理
            GLES32.glGenerateMipmap(GLES32.GL_TEXTURE_2D);

            //释放图片内存
            bitmap.recycle();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        init(context);
        //开启深度测试
        GLES32.glEnable(GLES32.GL_DEPTH_TEST);

        //顶点数组对象
        int[] array = new int[1];
        GLES32.glGenVertexArrays(array.length, array, 0);
        vAos[0] = array[0];

        genTexture();


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

        modelMatrix();
        viewMatrix();
        projectionMatrix();



        GLES32.glBindVertexArray(vAos[0]);
        //把新创建的缓冲对象绑定到GL_ARRAY_BUFFER目标上(OpenGL有很多缓冲对象类型，顶点缓冲对象的缓冲类型是GL_ARRAY_BUFFER)
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, vBos[0]);
        /**
         * 最后一个参数
         * GL_STATIC_DRAW ：数据不会或几乎不会改变。
         * GL_DYNAMIC_DRAW：数据会被改变很多。
         * GL_STREAM_DRAW ：数据每次绘制时都会改变。
         * 三角形的位置数据不会改变，每次渲染调用时都保持原样，所以它的使用类型最好是GL_STATIC_DRAW。
         * 如果，比如说一个缓冲中的数据将频繁被改变，那么使用的类型就是GL_DYNAMIC_DRAW或GL_STREAM_DRAW，
         * 这样就能确保显卡把数据放在能够高速写入的内存部分。
         */
        //把之前定义的顶点数据复制到缓冲的内存中
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER, vertices.length * BYTES_PER_FLOAT, mVBFloatBuffer, GLES32.GL_STATIC_DRAW);


        //end---------------现在我们已经把顶点数据储存在显卡的内存中，用VBO这个顶点缓冲对象管理---------------------------------
        //着色器
        vShader = ShaderUtils.loadShaderFromAssets(context, GLES32.GL_VERTEX_SHADER, "triangle_3d_2.vsh");
        fShader = ShaderUtils.loadShaderFromAssets(context, GLES32.GL_FRAGMENT_SHADER, "triangle_3d_2.fsh");


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
        //定义哪个uniform采样器对应哪个纹理单元 ,注意必须在激活程序之后才行
        GLES32.glUniform1i(GLES32.glGetUniformLocation(shaderProgram, "texture1"), 0);
        GLES32.glUniform1i(GLES32.glGetUniformLocation(shaderProgram, "texture2"), 1);


        GLES32.glUniformMatrix4fv(GLES32.glGetUniformLocation(shaderProgram, "model"), 1, false, modelFloatBuffer);
        GLES32.glUniformMatrix4fv(GLES32.glGetUniformLocation(shaderProgram, "view"), 1, false, viewFloatBuffer);
        GLES32.glUniformMatrix4fv(GLES32.glGetUniformLocation(shaderProgram, "projection"), 1, false, projectionFloatBuffer);



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
                5 * BYTES_PER_FLOAT,//步长,连续的顶点属性组之间的间隔
                0
        );
        //以顶点属性位置值作为参数，启用顶点属性；顶点属性默认是禁用的
        GLES32.glEnableVertexAttribArray(0);

        //在渲染前指定OpenGL该如何解释顶点数据
        GLES32.glVertexAttribPointer(
                1,//layout(location = 1)
                2,//顶点属性的大小,顶点属性是一个vec3，它由3个值组成，所以大小是3
                GLES32.GL_FLOAT,//数据的类型, vec*都是由浮点数值组成的
                false,//是否希望数据被标准化,如果我们设置为GL_TRUE，所有数据都会被映射到0（对于有符号型signed数据是-1）到1之间
                5 * BYTES_PER_FLOAT,//步长,连续的顶点属性组之间的间隔
                3 * BYTES_PER_FLOAT
        );
        //以顶点属性位置值作为参数，启用顶点属性；顶点属性默认是禁用的
        GLES32.glEnableVertexAttribArray(1);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        screenWidth = width;
        screenHeight = height;
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
        //清空屏幕的颜色缓冲,清除深度缓冲
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);

        mills ++;
        for (int i = 0; i < worlds.length; i++) {
            reset();
            Matrix.translateM(unitMatrix, 0, worlds[i][0], worlds[i][1], worlds[i][2]);
            Matrix.rotateM(unitMatrix, 0, mills + 50, rotates[i][0], rotates[i][1], rotates[i][2]);
            modelFloatBuffer = ByteBuffer.allocateDirect(unitMatrix.length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            modelFloatBuffer.put(unitMatrix);
            modelFloatBuffer.position(0);

            viewMatrix();


            GLES32.glUseProgram(shaderProgram);
            GLES32.glUniformMatrix4fv(GLES32.glGetUniformLocation(shaderProgram, "model"), 1, false, modelFloatBuffer);
            GLES32.glUniformMatrix4fv(GLES32.glGetUniformLocation(shaderProgram, "view"), 1, false, viewFloatBuffer);
            GLES32.glEnableVertexAttribArray(0);

            //绑定
            //先激活
            //绑定两个纹理到对应的纹理单元
            GLES32.glActiveTexture(GLES32.GL_TEXTURE0);
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, texTures[0]);
            GLES32.glActiveTexture(GLES32.GL_TEXTURE1);
            GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, texTures[1]);

            GLES32.glBindVertexArray(vAos[0]);
            GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, 36);

            //解绑
            GLES32.glBindVertexArray(0);

            GLES32.glDisableVertexAttribArray(0);
        }
    }

    public void destroy() {
        GLES32.glDeleteVertexArrays(1, vAos,0);
        GLES32.glDeleteBuffers(1, vBos, 0);
        GLES32.glDeleteProgram(shaderProgram);
    }

    float progress = 3;
}
