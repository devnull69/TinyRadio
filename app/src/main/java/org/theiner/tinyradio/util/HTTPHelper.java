package org.theiner.tinyradio.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Created by TTheiner on 06.03.2017.
 */

public class HTTPHelper {

    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
    private static final String userAgentMobile = "Mozilla/5.0 (Linux; Android 5.0.1; Galaxy Nexus Build/IMM76B) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 Mobile Safari/535.19";

    public Document getDocumentFromUrl(String strUrl, String referer, boolean isMobile) {
        Document doc = null;
        String ua = userAgent;
        if(isMobile)
            ua = userAgentMobile;
        try {
            doc = Jsoup.connect(strUrl).userAgent(ua).referrer(referer).timeout(15000).get();
        } catch(IOException e) {

        }
        return doc;
    }


    public Document getDocumentFromHTML(String html) {
        return Jsoup.parse(html);
    }

    public String getHtmlFromUrl(String strUrl, String referer, boolean isMobile) {
        return getHtmlFromUrl(strUrl, referer, isMobile, false);
    }

    public String getHtmlFromUrl(String strUrl, String referer, boolean isMobile, boolean isIso) {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder sb = null;
        String returnValue = "";

        try {
            url = new URL(strUrl);
            URLConnection con = url.openConnection();

            // force server to mimic specific Browser
            con.setRequestProperty("User-Agent", userAgent);
            if(isMobile)
                con.setRequestProperty("User-Agent", userAgentMobile);

            con.setRequestProperty("Referer", referer);

            con.setReadTimeout(5000);
            con.connect();

            if(isIso)
                reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.ISO_8859_1));
            else
                reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

            sb = new StringBuilder();

            String line = null;
            while((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            returnValue = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return returnValue;
    }

    public String unPACKED(String[] k, String p, int a) {
        int c = k.length;

        while(c > 0) {
            c--;
            if(!"".equals(k[c]))
                p = p.replaceAll("\\b" + getIntToBase(c, a) + "\\b", k[c]);
        }

        return p;
    }

    private String getIntToBase(int c, int base) {
        int first = c / base;
        int remainder = c - first * base;

        String result = "";
        if(first>0)
            if(first>9) {
                result = Character.toString((char) (first + 87));
            } else {
                result = String.valueOf(first);
            }

        if(remainder>9) {
            result += Character.toString((char) (remainder + 87));
        } else {
            result += String.valueOf(remainder);
        }
        return result;
    }

    public String getHtmlFromPOST(String hosterURL, String postString, boolean isMobile) {
        String result = "";

        try {
            URL theURL = new URL(hosterURL);
            HttpURLConnection con = (HttpURLConnection) theURL.openConnection();

            //add request header
            con.setRequestMethod("POST");
            if(isMobile)
                con.setRequestProperty("User-Agent", userAgentMobile);
            else
                con.setRequestProperty("User-Agent", userAgent);

            con.setRequestProperty("Accept-Language", "de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4");
            //con.setRequestProperty("Accept-Encoding", "deflate");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Referer", hosterURL);

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postString);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append("\n");
            }
            in.close();

            result = response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Bitmap getBitmapFromURL(String imageURL) {
        try {
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
