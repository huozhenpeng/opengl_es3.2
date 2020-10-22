### 顶点着色器

    注意gl_Position  P是大写
    
    #version 320 es
    layout (location = 0) in vec3 aPos;
    void main() {
        gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
    }
    
    * 每个顶点都是一个3D坐标，我们创建一个vec3类型的输入变量aPos
    * layout (location = 0)设定了输入变量的位置值(location)
    * gl_Position是预定义的变量，类型是vec4
    
    在GLSL中一个向量有最多4个分量，每个分量值都代表空间中的一个坐标，它们可以通过vec.x、vec.y、vec.z和vec.w来获取。
    注意vec.w分量不是用作表达空间中的位置的（我们处理的是3D不是4D），而是用在所谓透视除法(Perspective Division)上
    

    
    
### 片元着色器

    #version 320 es
    precision mediump float;
    out vec4 FragColor;
    
    void main()
    {
        FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);
    }

    注意加上 precision mediump float;
    片段着色器所做的是计算像素最后的颜色输出
    片段着色器只需要一个输出变量，这个变量是一个4分量向量，它表示的是最终的输出颜色