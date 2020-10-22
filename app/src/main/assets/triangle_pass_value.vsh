#version 320 es
layout (location = 0) in vec3 aPos;
out vec4 vertexColor;//表示这个变量是要输出到别的着色器的
void main() {
    gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
    vertexColor = vec4(0.5, 0.0, 0.0, 1.0);
}
