package io.amosbake.animationsummary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.amosbake.animationsummary.wiget.HeartRaiseView;

public class BezierActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        final HeartRaiseView _view = (HeartRaiseView) findViewById(R.id.heart);
        _view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _view.addHeart();
            }
        });
    }
}
