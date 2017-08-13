package com.example.mansi.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

class QueryUtils {
    private static List<News> newsList = new ArrayList<>();

    public static List<News> fetchNewsData(String queryUrl) {
        URL url = createURL(queryUrl);
        if (url != null) {
            String json = makeHttpRequest(url);
            newsList = parseJsonResponse(json);
        }
        return newsList;
    }

    private static URL createURL(String queryUrl) {
        URL url = null;
        try {
            url = new URL(queryUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String json = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = urlConnection.getInputStream();
                json = readFromStream(inputStream);
            } else {
                Log.e("Log Message", "Response code error: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        InputStreamReader inputReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputReader);
        String temp = bufferedReader.readLine();
        while (temp != null) {
            output.append(temp);
            temp = bufferedReader.readLine();
        }
        return output.toString();
    }


    private static List<News> parseJsonResponse(String json) {
        List<News> newsList = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(json);
            JSONObject responseObject = rootObject.getJSONObject("response");
            JSONArray resultsArray = responseObject.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject currentNewsArticle = resultsArray.getJSONObject(i);

                //mandatory fields which every article has
                String title = currentNewsArticle.getString("webTitle");

                //optional field which only some article has
                String section = null;
                if (currentNewsArticle.has("sectionName")) {
                    section = currentNewsArticle.getString("sectionName");
                }

                //optional field which only some article has
                String firstName = null;
                if (currentNewsArticle.has("firstName")) {
                    firstName = currentNewsArticle.getString("firstName");
                }

                //optional field which only some article has
                String lastName = null;
                if (currentNewsArticle.has("lastName")) {
                    lastName = currentNewsArticle.getString("lastName");
                }


                //mandatory field which every article has
                String webUrl = currentNewsArticle.getString("webUrl");

                //optional field which only some article has
                String published_date = null;
                if (currentNewsArticle.has("webPublicationDate")) {
                    published_date = currentNewsArticle.getString("webPublicationDate");
                }

                newsList.add(new News(title, section, firstName, lastName, webUrl, published_date));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }
}
