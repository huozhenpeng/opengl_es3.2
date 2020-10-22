#version 320 es
precision mediump float;
out vec4 FragColor;
uniform vec4 outColor; //直接在代码中给这个变量赋值

void main()
{
    FragColor = outColor;
}