package com.example.githubsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private List<Search_list> array_search_list = new ArrayList<>();
    private String search="";
    private int page = 1;
    private Searchlist_adapter adapter_main;
    private ProgressDialog progressDialog;
    private LinearProgressIndicator linearProgressIndicator;
    private TextView git_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        git_textview = findViewById(R.id.git_textview);
        SearchView simpleSearchView = findViewById(R.id.simpleSearchView);
        linearProgressIndicator = findViewById(R.id.linearProgressIndicator);

        linearProgressIndicator.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        adapter_main =  new Searchlist_adapter(array_search_list, getBaseContext(), MainActivity.this);

        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                git_textview.setVisibility(View.GONE);
                array_search_list.clear();
                adapter_main.notifyDataSetChanged();
                progressDialog.show();
                page = 1;
                search=query;
                getdata(query,page+"");
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }});

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(array_search_list.get(position).html_url));
                startActivity(browserIntent);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = listView.getCount();
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listView.getLastVisiblePosition() >= count - threshold  ) {
                        linearProgressIndicator.setVisibility(View.VISIBLE);
                        getdata(search,""+page++);
                    }}
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
    public void getdata(String input,String page)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.Github_link)+input+"&page="+page+"&per_page=9";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        insertdata(response);
                        linearProgressIndicator.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                linearProgressIndicator.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Call Limit Exceed/Api Error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void insertdata(String output)
    {
        try {
            JSONObject json = new JSONObject(output);
            JSONArray jsonArray = json.getJSONArray("items");
            for (int i=0; i<jsonArray.length(); i++)
            {
                Search_list searchList = new Search_list();
                JSONObject obj = jsonArray.getJSONObject(i);
                searchList.fullname = obj.getString("full_name").trim();
                if(!obj.getString("description").trim().equals("null"))
                {
                    searchList.description = obj.getString("description").trim();
                }
                else
                {
                    searchList.description = "No Description Found";
                }

                searchList.rating = formatValue(Double.parseDouble(obj.getString("stargazers_count").trim()));

                if(!obj.getString("language").trim().equals("null"))
                {
                    searchList.language = obj.getString("language").trim();
                }
                else
                {
                    searchList.language = "";
                }

                try
                {
                    searchList.license = obj.getJSONObject("license").getString("name");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                searchList.html_url = obj.getString("html_url").trim();
                array_search_list.add(searchList);
                searchList = null;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
        progressDialog.dismiss();
        Parcelable state = listView.onSaveInstanceState();
        adapter_main.notifyDataSetChanged();
        listView.setAdapter(adapter_main);
        listView.onRestoreInstanceState(state);
    }
    public static String formatValue(double value)
    {
        if (value == 0)
        {
            return "0";
        }
        try {
            int power;
            String suffix = " kmbt";
            String formattedNumber = "";
            NumberFormat formatter = new DecimalFormat("#,###.#");
            power = (int)StrictMath.log10(value);
            value = value/(Math.pow(10,(power/3)*3));
            formattedNumber=formatter.format(value);
            formattedNumber = formattedNumber + suffix.charAt(power/3);
            return formattedNumber.length()>4 ?  formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;
        } catch (Exception e)
        {
            Log.e("Format",e.toString());
        }
        return  value+"";
    }
}