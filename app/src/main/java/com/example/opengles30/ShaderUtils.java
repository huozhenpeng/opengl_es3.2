package com.example.opengles30;

import android.content.Context;
import android.opengl.GLES32;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ShaderUtils {
    private static String TAG = ShaderUtils.class.getName();
    /**
     *
     * @param type GL_VERTEX_SHADER GL_FRAGMENT_SHADER
     * @param shaderCode
     * @return
     */
    public static int loadShader(int type, String shaderCode) {
        //创建着色器
        int shader = GLES32.glCreateShader(type);
        //将着色器源码附加到着色器对象上
        GLES32.glShaderSource(shader, shaderCode);
        //编译着色器
        GLES32.glCompileShader(shader);
        int[] result = new int[1];
        GLES32.glGetShaderiv(shader, GLES32.GL_COMPILE_STATUS, result, 0);
        if (result[0] == GLES32.GL_FALSE) {
            String log = GLES32.glGetShaderInfoLog(shader);
            VLog.Loge(TAG,log);
        }
        return shader;
    }

    public static int loadShaderFromAssets(Context context, int type, String name) {
        try {
            InputStream inputStream = context.getAssets().open(name);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int ch = 0;
            while ((ch = inputStream.read()) != -1) {
                byteArrayOutputStream.write(ch);
            }
            byte[] buff = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            inputStream.close();
            String result = new String(buff, "UTF-8");
            result = result.replaceAll("\\r\\n", "\n");
            return loadShader(type, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
