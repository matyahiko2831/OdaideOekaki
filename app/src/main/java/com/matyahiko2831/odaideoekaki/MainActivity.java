package com.matyahiko2831.odaideoekaki;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.matyahiko2831.odaideoekaki.utils.OdaiMaker;
import com.matyahiko2831.odaideoekaki.utils.twitter.TwitterOAuthActivity;
import com.matyahiko2831.odaideoekaki.utils.twitter.TwitterUtils;

import java.io.File;
import java.io.FileOutputStream;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawingView drawingView;

    // Twitter連携用
    private Twitter mTwitter;
    private FrameLayout tweetpop;
    private EditText tweetText;
    private ImageView imageView1;
    private TextView tweetTextCount;

    private String appPath;

    private static final String HASH_TAG = " #お題DEお絵かき";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tweetpop = (FrameLayout) this.findViewById(R.id.tweetpop);
        tweetText = (EditText) this.findViewById(R.id.tweetText);
        imageView1 = (ImageView) this.findViewById(R.id.imageView1);
        tweetTextCount = (TextView) this.findViewById(R.id.tweetTextCount);

        appPath = getFilesDir().getPath();

        tweetpop.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // コールバック用文字列取得
        mTwitter = TwitterUtils.getTwitterInstance(this);

        // ナビゲーションビュー
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // シェアボタン
        FloatingActionButton fab_share = (FloatingActionButton) findViewById(R.id.fab_share);
        fab_share.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00aced")));
        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Twitter投稿
                postImageToTwitter();
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

        // お題をタイトルにセットする
        setDrawTheme();
    }


    /**
     * お題をタイトルにセットする
     * */
    private void setDrawTheme(){
        setTitle("お題 " + OdaiMaker.getOdai());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Twitterに画像を投稿する
     * */
    private void postImageToTwitter(){
        // OAuth認証
        if (!TwitterUtils.hasAccessToken(this)) {
            // 未認証時
            Intent intent = new Intent(this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        }else{
            // 認証済時
            // スクリーンショット取得
            View screen = (View) findViewById(R.id.screen_view);
            screen.setDrawingCacheEnabled(true);
            screen.setDrawingCacheBackgroundColor(Color.WHITE);
            Bitmap bitmap1 = Bitmap.createBitmap(screen.getDrawingCache());
            screen.setDrawingCacheEnabled(false);

            // スクリーンショットの保存
            try {
                // 保存先を決定
                File file = new File(appPath);

                if (!file.exists()) {
                    file.mkdir();
                }
                // ファイル名を指定
                String AttachName = file.getAbsolutePath() + "/image.jpg";
                // ファイルを保存
                FileOutputStream out = new FileOutputStream(AttachName);
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                showToast("スクリーンショットの保存失敗");
            }

            // スクリーンショットのサムネイルを表示
            File file = new File(appPath + "/image.jpg");
            if(file.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imageView1.setImageBitmap(myBitmap);
            }

            // ツイート可能文字数を表示
            int length = 140 - tweetText.length() - HASH_TAG.length();
            tweetTextCount.setTextColor(Color.GRAY);
            tweetTextCount.setText(String.valueOf(length));

            // ツイート画面の表示
            tweetpop.setVisibility(View.VISIBLE);
            // ツイート文字にフォーカスをセット
            tweetText.requestFocus();
            // カーソルは最後の文字に
            tweetText.setSelection(tweetText.length());
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(tweetText, 0);
        }
        tweetpop.setVisibility(View.VISIBLE);
    }

    // ツイート子画面内ボタンタップ処理
    public void tapPopBtn(View view) {
        switch(Integer.parseInt(String.valueOf(view.getTag()))){
            case 0:
                // 「キャンセル」ボタン
                tweetpop.setVisibility(View.GONE);
                break;
            case 1:
                // 「投稿」ボタン
                tweet();
                break;
            default:
                break;
        }
    }

    /** 非同期でのツイート処理 **/
    private void tweet() {
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    // 本文セット
                    final StatusUpdate status = new StatusUpdate(params[0] + HASH_TAG);

                    // 画像セット
                    File file = new File(appPath + "/image.jpg");
                    if(file.exists()){
                        status.media(file);
                    }else{
                        status.media(null);
                    }

                    // ツイート
                    mTwitter.updateStatus(status);
                    return true;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    showToast("ツイート完了");
                    tweetpop.setVisibility(View.GONE);
                } else {
                    showToast("ツイート失敗");
                }
            }
        };
        task.execute(tweetText.getText().toString());
    }

    /*
     * トースト表示処理
     * */
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
