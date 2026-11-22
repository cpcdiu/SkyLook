package com.arif.skylook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<City> cities = new ArrayList<>();
    private OnCityClickListener listener;

    public interface OnCityClickListener {
        void onCityClick(City city);
    }

    public CityAdapter(OnCityClickListener listener) {
        this.listener = listener;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = cities.get(position);
        holder.bind(city);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class CityViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCityName;
        private TextView tvCityDetails;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            tvCityDetails = itemView.findViewById(R.id.tvCityDetails);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onCityClick(cities.get(position));
                    }
                }
            });
        }

        public void bind(City city) {
            tvCityName.setText(city.getName());
            String details = city.getCountry() + " (Lat: " + city.getLatitude() + ", Lon: " + city.getLongitude() + ")";
            tvCityDetails.setText(details);
        }
    }
}