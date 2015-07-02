package com.trpapps.helloworldapp;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.content.res.AssetManager;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

//

public class MainActivity extends ActionBarActivity {

    BeerXmlParser beerParser;
    List beers;

    Button btn;
    TextView tvXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beerParser = new BeerXmlParser();

        Button btn = (Button) findViewById(R.id.button_1);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AssetManager assetManager = getAssets();
                InputStream inputStream = null;

                try {
                    inputStream = assetManager.open("beer.xml");
                } catch (IOException e) {
                    Log.e("xml", e.getMessage());
                }

                try {
                    beers = beerParser.parse(inputStream);
                } catch (XmlPullParserException e) {
                    Log.e("parserXML", e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("parserIO", e.getMessage());
                    e.printStackTrace();
                }

                if (beers != null &&
                        beers.size() > 0) {
                    Beer firstBeer = (Beer) beers.get(0);
                    TextView tv = (TextView) findViewById(R.id.textView_1);
                    tv.setText(firstBeer.getName());
                }
            }
        });
    }

    private String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        int a = 5;
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


