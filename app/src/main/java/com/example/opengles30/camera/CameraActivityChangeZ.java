package com.example.opengles30.camera;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.example.opengles30.R;

public class CameraActivityChangeZ extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    protected GlRender glRender;
    private AppCompatSeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_griangle);
        glSurfaceView = findViewById(R.id.glSurfaceView);
        //必须设置，否则都无法加载shader
        glSurfaceView.setEGLContextClientVersion(3);
        glRender = new GlRender(this);
        glSurfaceView.setRenderer(glRender);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                glRender.setCameraZPosition(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (glRender != null) {
            glRender.destroy();
        }
    }
}