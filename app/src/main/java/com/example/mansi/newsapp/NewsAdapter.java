package com.example.mansi.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        if (rootView == null) {
            rootView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        News currentNews = (News) getItem(position);

        TextView title_news = (TextView) rootView.findViewById(R.id.title_news);
        TextView section = (TextView) rootView.findViewById(R.id.section);
        TextView author = (TextView) rootView.findViewById(R.id.author_article);
        TextView date = (TextView) rootView.findViewById(R.id.date);

        if (currentNews.getTitle() != null) {
            title_news.setText(currentNews.getTitle());
        }
        if (currentNews.getSection() != null) {
            section.setText(currentNews.getSection());
        }
        if (!currentNews.getAuthor().equals(null + " " + null)) {
            author.setVisibility(View.VISIBLE);
            author.setText(currentNews.getAuthor());
        } else {
            author.setVisibility(View.GONE);
        }
        if (currentNews.getDate() != null) {
            String outputDate = formatDate(currentNews.getDate());
            date.setText(outputDate);
        }
        return rootView;
    }

    /**
     * Helper method to format date and return in as desirable format, Here Locale.US
     */
    public String formatDate(String parceableDate) {
        //specifying the input format of date from JSON
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        //specifying the output format of date to be displayed on UI
        SimpleDateFormat outputFormatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        String outputDate = null;
        try {
            //parsing the input date from string and storing it as Date object
            Date dateObject = inputFormat.parse(parceableDate);
            //formatting output date from date object
            outputDate = outputFormatDate.format(dateObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }
}
