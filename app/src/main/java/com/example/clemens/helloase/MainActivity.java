package com.example.clemens.helloase;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.restlet.Client;
import org.restlet.data.Method;
import org.restlet.resource.ClientResource;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        if (id == R.id.action_qr) {
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            TextView main_textview = (TextView) findViewById(R.id.textview_content);
            String url = "https://attendancetrackingdesktop.appspot.com/rest/attendance/token/get?studentId=112762937574526790868&weekNumber=1";
            try {
                String rawAnswer = new ClientResource(url).get().getText();
                Bitmap bitmap = encodeAsBitmap(rawAnswer);
                imageView.setImageBitmap(bitmap);
            }
            catch(Exception exc) {
                main_textview.setText(exc.getMessage());
            }

        }
        if (id == R.id.action_download) {
            TextView main_textview = (TextView) findViewById(R.id.textview_content);
            String url = "http://ase2016-148507.appspot.com/rest/guestbook/";
            try {
                String rawAnswer = new ClientResource(url).get().getText();
                main_textview.setText(rawAnswer);
            }
            catch(Exception exc) {
                main_textview.setText(exc.getMessage());
            }
        }

        return super.onOptionsItemSelected(item);
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 400, 400, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}