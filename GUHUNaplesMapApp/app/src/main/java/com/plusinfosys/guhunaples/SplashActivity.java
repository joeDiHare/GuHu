package com.plusinfosys.guhunaples;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by pitwin002 on 1/22/2015.
 */
public class SplashActivity extends ActionBarActivity {
    ProgressBar pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        pd = (ProgressBar) findViewById(R.id.progressBar);
        pd.setVisibility(View.GONE);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getApplicationContext().getPackageName() + "/Naples.mbtiles");
        if (file.exists()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        } else {
            new copyAsstestFile().execute();
        }
    }

    //Copy Map database to the sdcard
    public class copyAsstestFile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            pd.setVisibility(View.VISIBLE);
            pd.setProgress(0);
            pd.setMax(100);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String path = "map/Naples.mbtiles";
            String dest = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getApplicationContext().getPackageName();

            InputStream is = null;


            try {
                // try uncompressed
                is = getAssets().open(path);
                long lengthOfFile =9105408;
                long total = 0;
                File f = new File(dest + "/");
                if (!f.exists()) {
                    f.mkdir();
                }

                OutputStream outs = new FileOutputStream(dest + "/Naples.mbtiles");

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    total += length;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    outs.write(buffer, 0, length);
                }
                outs.flush();
                outs.close();
                is.close();
            } catch (IOException e) {
                // try zip

            }


            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            pd.setProgress(Integer.parseInt(values[0]));
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            super.onPostExecute(s);
        }
    }


}
