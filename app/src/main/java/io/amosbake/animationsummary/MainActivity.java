package io.amosbake.animationsummary;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private ImageView ivAnimVector;
    private ImageView ivAnimColor;
    private ImageView ivAnimSearch;
    private ImageView ivAnimStar;
    private ImageView ivAnimFiveStar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupTransitionAnim();
        setupColorAnim();
        setupSearchAnim();
        setupPathTrimAnim();
        setupXmlMorphAnim();

    }

    private void setupXmlMorphAnim() {
        ivAnimFiveStar = (ImageView) findViewById(R.id.ivAnimFiveStar);
        findViewById(R.id.btnFiveStarAnim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable _drawable = ivAnimFiveStar.getDrawable();
                if (_drawable instanceof Animatable){
                    ((Animatable) _drawable).start();
                }
            }
        });
    }

    private void setupPathTrimAnim() {
        ivAnimStar = (ImageView) findViewById(R.id.ivAnimStar);
        findViewById(R.id.btnStarAnim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable _drawable = ivAnimStar.getDrawable();
                if (_drawable instanceof Animatable){
                    ((Animatable) _drawable).start();
                }
            }
        });
    }

    /**搜索框收缩动画*/
    private void setupSearchAnim() {
        ivAnimSearch = (ImageView) findViewById(R.id.ivAnimSearch);
        findViewById(R.id.btnSearchAnim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable _drawable = ivAnimSearch.getDrawable();
                if (_drawable instanceof Animatable){
                    ((Animatable) _drawable).start();
                }
            }
        });
    }
    /**颜色变换动画*/
    private void setupColorAnim() {
        ivAnimColor = (ImageView) findViewById(R.id.ivAnimColor);
        findViewById(R.id.btnColorAnim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable _drawable = ivAnimColor.getDrawable();
                if (_drawable instanceof Animatable){
                    ((Animatable) _drawable).start();
                }
            }
        });
    }
    /**group translateX 动画*/
    private void setupTransitionAnim() {
        ivAnimVector = (ImageView) findViewById(R.id.ivAnimVector);
        findViewById(R.id.btnVectorAnim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable _drawable = ivAnimVector.getDrawable();
                if (_drawable instanceof Animatable){
                    ((Animatable) _drawable).start();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent _intent = new Intent(this,BezierActivity.class);
            this.startActivity(_intent);
            this.finish();
            return true;
        }else if (id == R.id.action_morph){
            Intent _intent = new Intent(this,PathMorphActivity.class);
            this.startActivity(_intent);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
