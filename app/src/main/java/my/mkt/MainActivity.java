package my.mkt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private long enqueue;
    private DownloadManager dm;
    private static String file_url ="http://10.40.41.140/mnsk/blackbox.php?action=download&user=user&password=111&date=2018-10-19&type=media";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        new DownloadFileFromURL().execute(file_url);
     }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                DownloadManager downloadManager = (DownloadManager)getSystemService(Activity.DOWNLOAD_SERVICE);
                //реквест содержит URI файла и дополнительные настройки DownloadManager'а
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(f_url[0]));

                //нотификейшн будет виден всегда, пока юзер не отменит его вручную или не кликнет
            //    request.setNotificationVisibility(DownloadVisibility.VisibleNotifyCompleted);
                //задаем публичную директорию с загрузками и вторым параметром имя файла + расширение
                request.setDestinationInExternalPublicDir("/Download", "data.7z");
                //разрешаем сканить новый файл МедиаСканнеру
                request.allowScanningByMediaScanner();

                //запускаем скачивание
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("Permission error","You have permission");
                        downloadManager.enqueue(request);
                    }
                }

            } catch (Exception e) {
                Log.e("Error: ", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, "Файл скачен!", Toast.LENGTH_SHORT).show();
        }
    }
}
