#version 320 es
layout (location = 0) in vec3 aPos; //位置变量的属性位置值为0
layout (location = 1) in vec3 aColor; //颜色变量的属性位置值为1
layout (location = 2) in vec2 aTexCoord;
out vec3 ourColor; //向片元着色器输出一个颜色
out vec2 TexCoord;
uniform mat4 model;//定义模型矩阵
uniform mat4 view;//定义观察矩阵
uniform mat4 projection;//定义投影矩阵
void main() {
    //注意乘法要从右往左读
    gl_Position = projection * view * model * vec4(aPos.x, aPos.y, aPos.z, 1.0);
    ourColor = aColor;//将ourColor设置为我们从顶点数据那里得到的输入颜色
    TexCoord = aTexCoord;
}
