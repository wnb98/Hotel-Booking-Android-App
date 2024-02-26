package com.example.hotelbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;


import java.util.ArrayList;
import java.util.List;

public class hotelDetails extends AppCompatActivity implements OnMapReadyCallback {

    TextView hotelNameTextView, cityNameTextView, priceTextView, ratingTextView, descriptionTextView;
    ImageView detailImage;
    Button bookNowBTN;

    // Firebase Firestore instance
    private FirebaseFirestore firestore;
    private CollectionReference hotelsCollection;

    // Google Maps variables
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private double price;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_details);

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance();
        hotelsCollection = firestore.collection("hotels");

        hotelNameTextView = findViewById(R.id.hotelNameTextView);
        cityNameTextView = findViewById(R.id.cityNameTextView);
        priceTextView = findViewById(R.id.hotelPriceTXV);
        ratingTextView = findViewById(R.id.hotalRatingTXV);
        descriptionTextView = findViewById(R.id.hotelDescTextView);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        if (intent != null) {
            String hotelName = intent.getStringExtra("Title");

            // Set the hotel name TextView
            hotelNameTextView.setText(hotelName);

            // Retrieve hotel details from Firebase Firestore using hotelName
            hotelsCollection
                    .whereEqualTo("name", hotelName)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String city = documentSnapshot.getString("city");
                            double price = documentSnapshot.getDouble("price");
                            double rating = documentSnapshot.getDouble("rating");
                            this.price = documentSnapshot.getDouble("price");
                            this.name=documentSnapshot.getString(("name"));

                            String description = documentSnapshot.getString("description");

                            // Set the city name, price, rating, and description TextViews
                            cityNameTextView.setText(city);
                            priceTextView.setText(String.valueOf(price) + "$ / night");
                            ratingTextView.setText(String.valueOf(rating));
                            descriptionTextView.setText(description);

                            GeoPoint location = documentSnapshot.getGeoPoint("location");

                            // Set the hotel location on the map
                            if (location != null) {
                                LatLng hotelLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.addMarker(new MarkerOptions().position(hotelLatLng).title("Hotel Location"));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hotelLatLng, 15));
                            }
                        }
                    });

            // ...

            // Load images and videos from Firebase Firestore and set them in ImageSlider
            ImageSlider imageSlider = findViewById(R.id.imageSlider);
            ArrayList<SlideModel> slideModels = new ArrayList<>();

            hotelsCollection
                    .whereEqualTo("name", hotelName)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            ArrayList<String> imagePaths = (ArrayList<String>) documentSnapshot.get("imagePaths");
                            String videoPaths = (String) documentSnapshot.get("videoPaths");

                            if (imagePaths != null && !imagePaths.isEmpty()) {
                                for (String imagePath : imagePaths) {
                                    slideModels.add(new SlideModel(imagePath, ScaleTypes.FIT));
                                }
                            }

                            if (videoPaths != null ) {
                                slideModels.add(new SlideModel(videoPaths, ScaleTypes.FIT));

                                VideoView videoView = findViewById(R.id.videoView);
                                videoView.setVisibility(View.VISIBLE);

                                MediaController mediaController = new MediaController(hotelDetails.this);
                                mediaController.setAnchorView(videoView);
                                videoView.setMediaController(mediaController);
                                videoView.setVideoPath(videoPaths);
                                videoView.start();
                            }

                            imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                        }
                    });

            bookNowBTN = findViewById(R.id.bookNowBTN);
            bookNowBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(hotelDetails.this, peymentpage.class);
                    intent.putExtra("HotelPrice", price);
                    intent.putExtra("HotelName", hotelName);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
    }
}

