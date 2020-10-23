package com.example.opengles30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import com.example.opengles30.coordinatesystem.Matrix3D_1Activity;
import com.example.opengles30.coordnate_3d_2.Matrix3D_2Activity;
import com.example.opengles30.matrix.MatrixActivity;
import com.example.opengles30.multexture.MulTextureActivity;
import com.example.opengles30.passvalue.PassValueActivity;
import com.example.opengles30.quadrilateral.QuadrilateralActivity;
import com.example.opengles30.texture.TextureActivity;
import com.example.opengles30.triangle.TriangleActivity;
import com.example.opengles30.uniform.PassValueByUniformActivity;
import com.example.opengles30.vertexcolor.VertexColorActivity;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glSurfaceView = findViewById(R.id.glSurfaceView);
        //必须设置，否则都无法加载shader
        glSurfaceView.setEGLContextClientVersion(3);
        glSurfaceView.setRenderer(new GlRender());
    }

    public void drawTriangle(View view) {
        Intent intent = new Intent(this, TriangleActivity.class);
        startActivity(intent);
    }

    public void drawQuad(View view) {
        Intent intent = new Intent(this, QuadrilateralActivity.class);
        startActivity(intent);
    }

    public void passValue(View view) {
        Intent intent = new Intent(this, PassValueActivity.class);
        startActivity(intent);
    }

    public void uniformPassValue(View view) {
        Intent intent = new Intent(this, PassValueByUniformActivity.class);
        startActivity(intent);
    }

    public void verTexColor(View view) {
        Intent intent = new Intent(this, VertexColorActivity.class);
        startActivity(intent);
    }

    public void texTure(View view) {
        Intent intent = new Intent(this, TextureActivity.class);
        startActivity(intent);
    }

    public void mulTexTure(View view) {
        Intent intent = new Intent(this, MulTextureActivity.class);
        startActivity(intent);
    }

    public void matrixRotateScale(View view) {
        Intent intent = new Intent(this, MatrixActivity.class);
        startActivity(intent);
    }

    public void matrix_3d_1(View view) {
        Intent intent = new Intent(this, Matrix3D_1Activity.class);
        startActivity(intent);
    }

    public void matrix_3d_2(View view) {
        Intent intent = new Intent(this, Matrix3D_2Activity.class);
        startActivity(intent);
    }
}