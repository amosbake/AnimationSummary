package io.amosbake.animationsummary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class BezierActivity extends AppCompatActivity {

    private android.widget.Button secondBezier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        this.secondBezier = (Button) findViewById(R.id.secondBezier);
    }
}
