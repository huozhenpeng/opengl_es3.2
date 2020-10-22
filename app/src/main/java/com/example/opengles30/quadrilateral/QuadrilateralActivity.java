package com.example.opengles30.quadrilateral;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.opengles30.R;

public class QuadrilateralActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    private GlRender glRender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_griangle);
        glSurfaceView = findViewById(R.id.glSurfaceView);
        //必须设置，否则都无法加载shader
        glSurfaceView.setEGLContextClientVersion(3);
        glRender = new GlRender(this);
        glSurfaceView.setRenderer(glRender);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (glRender != null) {
            glRender.destroy();
        }
    }
}