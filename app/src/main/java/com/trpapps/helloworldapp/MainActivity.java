package com.trpapps.helloworldapp;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

//

public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

    BeerXmlParser beerParser;
    ArrayList<Beer> beers;

    ListView beersListView;
    SearchView beersSearchView;
    BeersAdapter beersAdapter;

    Button btn;
    TextView tvXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            Log.e("xml", e.getMessage());
        }
        return outputStream.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        beersSearchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        beersListView = (ListView)findViewById(R.id.beersView);

        AssetManager assetManager = getAssets();
        InputStream inputStream = null;

        try {
            inputStream = assetManager.open("beer.xml");
        } catch (IOException e) {
            Log.e("xml", e.getMessage());
        }

        beerParser = new BeerXmlParser();

        try {
            beers = beerParser.parse(inputStream);
        } catch (XmlPullParserException e) {
            Log.e("parserXML", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("parserIO", e.getMessage());
            e.printStackTrace();
        }

        beersAdapter = new BeersAdapter(this, beers);
        beersListView.setAdapter(beersAdapter);
        beersListView.setTextFilterEnabled(true);
        beersSearchView.setOnQueryTextListener(this);
        beersSearchView.setQueryHint(getString(R.string.search_hint));

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
        else if (id == R.id.action_search) {
           // openSearch();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        if (TextUtils.isEmpty(newText)) {
            beersListView.clearTextFilter();
        } else {
            beersListView.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

}


