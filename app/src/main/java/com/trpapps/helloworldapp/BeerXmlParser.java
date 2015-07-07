package com.trpapps.helloworldapp;

/**
 * Created by prudnick on 7/2/2015.
 */

import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.trpapps.helloworldapp.Beer;

public class BeerXmlParser {

    private Beer readBeer(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "beer");
        String name = null;
        String alc = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String token = parser.getName();
            if (token.equals("name")) {
                name = readName(parser);
            } else if (token.equals("alc")) {
                alc = readAlc(parser);
            } else {
                skip(parser);
            }
        }
        return new Beer(name, alc);
    }

    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        String tagName = "name";
        parser.require(XmlPullParser.START_TAG, ns, tagName);
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tagName);
        return data;
    }

    private String readAlc(XmlPullParser parser) throws IOException, XmlPullParserException {
        String tagName = "alc";
        parser.require(XmlPullParser.START_TAG, ns, tagName);
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tagName);
        return data;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private static final String ns = null;

    public ArrayList parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readBase(parser);
        } finally {
            in.close();
        }
    }

    private ArrayList readBase(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList entries = new ArrayList();
        parser.require(XmlPullParser.START_TAG, ns, "base");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("beer")) {
                entries.add(readBeer(parser));
            } else {
                skip(parser);
            }
        }

        return entries;
    }
}
