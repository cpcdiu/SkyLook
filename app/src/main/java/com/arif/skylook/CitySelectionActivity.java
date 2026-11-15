package com.arif.skylook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CitySelectionActivity extends AppCompatActivity {

    private TextInputEditText etSearch;
    private ProgressBar progressBar;
    private ListView listViewCities;

    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private List<City> cityList = new ArrayList<>();
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, String>> listData = new ArrayList<>();
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        databaseHelper = new DatabaseHelper(this);

        etSearch = findViewById(R.id.etSearch);
        progressBar = findViewById(R.id.progressBar);
        listViewCities = findViewById(R.id.listViewCities);

        adapter = new SimpleAdapter(this, listData, R.layout.item_city, new String[]{"name", "details"}, new int[]{R.id.tvCityName, R.id.tvCityDetails});
        listViewCities.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                String query = s.toString().trim();
                if (query.length() < 2) {
                    cityList.clear();
                    listData.clear();
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        searchCities(query);
                    }
                };
                searchHandler.postDelayed(searchRunnable, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listViewCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < cityList.size()) {
                    City selectedCity = cityList.get(position);
                    databaseHelper.saveSelectedCity(selectedCity);
                    Intent intent = new Intent(CitySelectionActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void searchCities(String query) {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<City> results = ApiClient.searchCities(query);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cityList.clear();
                        cityList.addAll(results);
                        listData.clear();
                        for (City city : cityList) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("name", city.getName());
                            String details = city.getCountry() + " (Lat: " + city.getLatitude() + ", Lon: " + city.getLongitude() + ")";
                            map.put("details", details);
                            listData.add(map);
                        }
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }
}
