package com.example.hotelbooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class searchFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<DataClass> dataList;
    private MyAdapter adapter;
    private SearchView searchView;

    private FirebaseFirestore db;
    private CollectionReference hotelsCollection;

    public searchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        hotelsCollection = db.collection("hotels");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();
        adapter = new MyAdapter(requireContext(), dataList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void searchList(String city) {
        Query query = hotelsCollection.whereEqualTo("city", city);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                dataList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String name = document.getString("name");
                    String hotelCity = document.getString("city");
                    String imageUrl = document.getString("image");

                    DataClass data = new DataClass(name, "", hotelCity, imageUrl);
                    dataList.add(data);
                }
                adapter.notifyDataSetChanged();

                if (dataList.isEmpty()) {
                  //  Toast.makeText(requireContext(), "No hotels found in " + city, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Error retrieving hotels", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
