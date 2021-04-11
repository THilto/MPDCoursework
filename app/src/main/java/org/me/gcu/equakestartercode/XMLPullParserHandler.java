package org.me.gcu.equakestartercode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ted Hilton - S1708998
 */
public class XMLPullParserHandler {

    private String text;

    public List<Earthquake> parse(InputStream is) {
        List<Earthquake> earthquakes = new ArrayList<>();
        Earthquake earthquake = null;
        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("item") || tagName.equalsIgnoreCase("channel")) {
                            earthquake = new Earthquake();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase("item")) {
                            earthquakes.add(earthquake);
                        }  else if (tagName.equalsIgnoreCase("description")) {
                            earthquake.setDescription(text);
                        } else if (tagName.equalsIgnoreCase("pubDate")) {
                            earthquake.setDate(convertDate(text));
                        } else if (tagName.equalsIgnoreCase("lat")) {
                            earthquake.setGeoLat(Double.parseDouble(text));
                        } else if (tagName.equalsIgnoreCase("long")) {
                            earthquake.setGeoLong(Double.parseDouble(text));
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException | IOException | ParseException ignored) { }
        return earthquakes;
    }

    private Date convertDate(String date) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        return formatter.parse(date);
    }

}
