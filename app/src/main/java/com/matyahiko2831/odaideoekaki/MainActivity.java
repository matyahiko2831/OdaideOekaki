package com.matyahiko2831.odaideoekaki;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // シェアボタン
        FloatingActionButton fab_share = (FloatingActionButton) findViewById(R.id.fab_share);
        fab_share.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Twitterに文字列を渡す
                Uri uri = Uri.parse("https://twitter.com/intent/tweet?text= てすと");

                Intent twitterIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(twitterIntent);
            }
        });

        // お絵かきビュー読み込み
        this.drawingView = (DrawingView)findViewById(R.id.drawing_view);

        // 削除ボタン
        FloatingActionButton fab_delete = (FloatingActionButton)findViewById(R.id.fab_delete);
        fab_delete.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.delete();
            }
        });

    }

}
