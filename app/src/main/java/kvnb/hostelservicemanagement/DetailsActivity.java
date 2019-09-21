package kvnb.hostelservicemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;


public class DetailsActivity extends AppCompatActivity {
    WebView webView;
    ProgressBar loader;
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";
    String url = "";
    String title = "";
    String description = "";
    String author = "";
    String publishedat = "";
    String filename = "SocialCopsNewsAPPSaved";
    FileOutputStream outputStream;
    FileInputStream inputStream;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbardetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        url = intent.getStringExtra(KEY_URL);
        author = intent.getStringExtra(KEY_AUTHOR);
        title = intent.getStringExtra(KEY_TITLE);
        description = intent.getStringExtra(KEY_DESCRIPTION);
        publishedat = intent.getStringExtra(KEY_PUBLISHEDAT);
        loader = (ProgressBar) findViewById(R.id.loader);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);


        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress >= 70) {
                    loader.setVisibility(View.GONE);
                } else {
                    loader.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                // search action
                return true;
            case R.id.action_star:
                // location found
                HashMap<String, String> map = new HashMap<>();
                map.put(KEY_URL, url);
                map.put(KEY_AUTHOR, author);
                map.put(KEY_DESCRIPTION, description);
                map.put(KEY_PUBLISHEDAT, publishedat);
                map.put(KEY_TITLE, title);
                Variables.Saved.add(map);
                File file = new File(getFilesDir(), filename);
                try {
                    outputStream = new FileOutputStream(file);
                    objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(Variables.Saved);
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
