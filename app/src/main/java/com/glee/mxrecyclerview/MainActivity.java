package com.glee.mxrecyclerview;

import android.os.Bundle;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseSimpleAdapter<String> adapter = new BaseSimpleAdapter<String>(R.layout.item, new DiffUtil.ItemCallback<String>() {
            @Override
            public boolean areItemsTheSame(@NonNull String s, @NonNull String t1) {
                return s == t1;
            }

            @Override
            public boolean areContentsTheSame(@NonNull String s, @NonNull String t1) {
                return s.equals(t1);
            }
        }) {
            @Override
            void onBind(BaseViewHolder viewHolder, String s) {
                viewHolder.setText(R.id.text, s);
            }
        };
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i + ":");
        }
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.submitList(list);
        adapter.addHeader(new RatingBar(this));
        adapter.addHeader(new RatingBar(this));
        adapter.addHeader(new RatingBar(this));
        adapter.addHeader(new RatingBar(this));
        adapter.addFooter(new RatingBar(this));
        adapter.addFooter(new RatingBar(this));
        adapter.addFooter(new RatingBar(this));
        adapter.addFooter(new RatingBar(this));
        adapter.addFooter(new RatingBar(this));
        adapter.addFooter(new RatingBar(this));
    }
}
