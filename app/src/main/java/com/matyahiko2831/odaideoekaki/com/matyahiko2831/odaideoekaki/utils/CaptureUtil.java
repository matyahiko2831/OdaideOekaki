package com.matyahiko2831.odaideoekaki.com.matyahiko2831.odaideoekaki.utils;

import android.graphics.Bitmap;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kondoukazuya on 2016/02/05.
 */
public class CaptureUtil {


    /**
     * キャプチャを撮る
     * @param view 撮りたいView
     * @return Bitmap 撮ったキャプチャ
     */
    public static Bitmap getViewCapture(View view) {

        // Viewのキャッシュを有効にする
        view.setDrawingCacheEnabled(true);
        // Viewのキャッシュを取得
        Bitmap cache = view.getDrawingCache();
        Bitmap screenShot = Bitmap.createBitmap(cache);
        // Viewのキャッシュを無効にする
        view.setDrawingCacheEnabled(false);

        return screenShot;
    }

    /**
     * 撮ったキャプチャを保存
     * @param view
     * @param file 書き込み先ファイルfile
     */
    public static void saveCapture(View view, File file) {

        // キャプチャを撮る
        Bitmap capture = getViewCapture(view);

        try(FileOutputStream fos = new FileOutputStream(file, false)) {

            // 画像のフォーマットと画質と出力先を指定して保存
            capture.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
