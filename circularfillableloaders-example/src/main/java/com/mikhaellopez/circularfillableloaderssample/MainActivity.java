package com.mikhaellopez.circularfillableloaderssample;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.larswerkman.lobsterpicker.OnColorListener;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

public class MainActivity extends AppCompatActivity {

    private CircularFillableLoaders circularFillableLoaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circularFillableLoaders = findViewById(R.id.circularFillableLoaders);

        // PROGRESS
        ((SeekBar) findViewById(R.id.seekBarProgress)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circularFillableLoaders.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // BORDER
        ((SeekBar) findViewById(R.id.seekBarBorderWidth)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circularFillableLoaders.setBorderWidth(progress * getResources().getDisplayMetrics()
                        .density);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // AMPLITUDE
        ((SeekBar) findViewById(R.id.seekBarAmplitude)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circularFillableLoaders.setAmplitudeRatio((float) progress / 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // COLOR
        ((LobsterShadeSlider) findViewById(R.id.shadeslider)).addOnColorListener(new OnColorListener() {
            @Override
            public void onColorChanged(@ColorInt int color) {
                circularFillableLoaders.setColor(color);
            }

            @Override
            public void onColorSelected(@ColorInt int color) {
            }
        });
    }
}
