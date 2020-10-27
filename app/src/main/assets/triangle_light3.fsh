#version 320 es
precision highp float;
out vec4 FragColor;
uniform vec3 objectColor;
uniform vec3 lightColor;
in vec3 Normal;
uniform vec3 lightPos;//光源的位置
in vec3 FragPos;
uniform vec3 viewPos;// 眼睛的位置，也就是摄像机位置
void main()
{
    //模拟环境光照
    //我们用光的颜色乘以一个很小的常量环境因子，再乘以物体的颜色
    //这个物体非常暗，但由于应用了环境光照，也不是完全黑的
    float ambientStrength = 0.1f;
    vec3 ambient = ambientStrength * lightColor;

    //光的方向向量是光源位置向量与片段位置向量之间的向量差
    //对相关向量进行标准化，来保证它们是真正地单位向量
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(lightPos - FragPos);

    //下一步，我们对norm和lightDir向量进行点乘，计算光源对当前片段实际的漫发射影响
    //如果两个向量之间的角度大于90度，点乘的结果就会变成负数，这样会导致漫反射分量变为负数。
    //为此，我们使用max函数返回两个参数之间较大的参数，从而保证漫反射分量不会变成负数。负数颜色的光照是没有定义的，所以最好避免它
//    float diff = max(dot(norm, lightDir), 0);注意要写0.0
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;
    //有了环境光分量和漫反射分量，我们把它们相加，然后把结果乘以物体的颜色，来获得片段最后的输出颜色。
    //vec3 result = (ambient + diffuse) * objectColor;


    //镜面反射，我们计算视线方向向量，和对应的沿着法线轴的反射向量：
    float specularStrength = 0.5;//高光强度,设置为1.0f，我们会得到一个非常亮的镜面光分量
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);

    //计算镜面分量,这个32是高光的反光度
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0f);
    vec3 specular = specularStrength * spec * lightColor;

    vec3 result = (ambient + diffuse + specular) * objectColor;

    FragColor = vec4(result, 1.0);
}