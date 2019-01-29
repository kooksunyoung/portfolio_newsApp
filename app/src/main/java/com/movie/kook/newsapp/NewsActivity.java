package com.movie.kook.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] myDataset = {};
    private RequestQueue queue;
    Button Button_title;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        queue = Volley.newRequestQueue(this);
        getNews();

        // 1. 화면이 로딩 -> 뉴스 정보를 받아온다.
        // 2. 정보 -> 어댑터를 넘겨준다.
        // 3. 어댑터 -> 셋팅
        Button_title = (Button) findViewById(R.id.Button_title);
//        Button_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NewsData news = new NewsData();
//                url = news.getUrl().toString().trim();
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//            }
//        });
    }

    public void getNews() {

        String url ="https://newsapi.org/v2/top-headlines?country=kr&apiKey=c87570a774824c9390a1008d524719c1";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //                       Log.d("NEWS", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray arrayArticles = jsonObject.getJSONArray("articles");

                            // response -> NewsData Class 분류
                            List<NewsData> news = new ArrayList<>();

                            for (int i = 0, j = arrayArticles.length(); i < j; i++){
                                JSONObject obj = arrayArticles.getJSONObject(i);

                                Log.d("NEWS", obj.toString());

                               NewsData newsData = new NewsData();
                                newsData.setTitle(obj.getString("title"));
                                newsData.setUrlToImage( obj.getString("urlToImage"));
                                newsData.setContent(obj.getString("description"));
                                newsData.setUrl(obj.getString("url"));
                                news.add(newsData);
                            }

                            // 인터넷을 사용하려면 manifests에서 <uses-permission android:name="android.permission.INTERNET"/> 을 추가한다.


                            // specify an adapter (see also next example)
                            mAdapter = new MyAdapter(news, NewsActivity.this, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Object obj = v.getTag();
                                    if(obj != null) {
                                        int position = (int) obj;
                                        ((MyAdapter) mAdapter).getNews(position);
                                        Intent intent = new Intent(NewsActivity.this, MyAdapter.class);
                                        //1. 본문만
                                        //2. 전체
                                        //2-1. 하나씩 다 넘기기
                                        //2-2. 한번에 다 넘기기
                                        startActivity(intent);
                                    }
                                }
                            });
                            mRecyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue. 요청을 추가한다.
        queue.add(stringRequest);
    }

}











