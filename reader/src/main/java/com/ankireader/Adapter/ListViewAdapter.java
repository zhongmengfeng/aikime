package com.ankireader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ankireader.model.Book;

import com.chaojiyiji.geometerplus.zlibrary.ui.android.R;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */

public class ListViewAdapter extends ArrayAdapter {

    private int resourceId;

    public ListViewAdapter(Context context, int textViewResourceId, List<Book> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = (Book) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView bookImage = (ImageView) view.findViewById(R.id.book_image);
        TextView bookName = (TextView) view.findViewById(R.id.book_name);
        TextView bookProgress = (TextView) view.findViewById(R.id.book_progress);
        bookImage.setImageResource(R.drawable.icon_book);
        bookName.setText(book.getBookName());
        bookProgress.setText(book.getBookProgress());

        return view;
    }
}
