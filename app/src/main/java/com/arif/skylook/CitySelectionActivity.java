package com.arif.skylook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CitySelectionActivity extends AppCompatActivity {

    private TextInputEditText etSearch;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewCities;

    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private List<City> cityList = new ArrayList<>();
    private CityAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        databaseHelper = new DatabaseHelper(this);

        etSearch = findViewById(R.id.etSearch);
        progressBar = findViewById(R.id.progressBar);
        recyclerViewCities = findViewById(R.id.recyclerViewCities);

        recyclerViewCities.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CityAdapter(new CityAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(City city) {
                databaseHelper.saveSelectedCity(city);
                Intent intent = new Intent(CitySelectionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        recyclerViewCities.setAdapter(adapter);

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
                    adapter.setCities(cityList);
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
                        adapter.setCities(cityList);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }
}
