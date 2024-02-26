package com.example.hotelbooking;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class accountFragment extends Fragment {

    // ...

    private RecyclerView bookingRecyclerView;
    private accountAdapter accountAdapter;
    private List<accountDataClass> dataList1;
    private DatabaseReference bookingsRef;
    accountDataClass booking;



    // ...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Initialize RecyclerView and adapter
        bookingRecyclerView = view.findViewById(R.id.bookingRecyclerView);
        dataList1 = new ArrayList<>();

        // accountAdapter = new accountAdapter(requireContext(), dataList1);
        accountAdapter = new accountAdapter(isAdded() ? requireContext() : getContext(), dataList1);



 /*
        // Set layout manager and adapter for the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        bookingRecyclerView.setLayoutManager(layoutManager);
        bookingRecyclerView.setAdapter(accountAdapter);

        // ...
*/
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ...
        bookingsRef = FirebaseDatabase.getInstance().getReference("bookings").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // DatabaseReference userBookingsRef = bookingsRef.child(user.getUid()); //-----------------------
            DatabaseReference userBookingsRef = FirebaseDatabase.getInstance().getReference()
                    .child("bookings").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            userBookingsRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    String bookingDate = dataSnapshot.child("bookingDate").getValue(String.class);
                    String hotelName = dataSnapshot.child("hotelName").getValue(String.class);
                    Integer nights = dataSnapshot.child("nights").getValue(Integer.class);
                    Integer totalAmount = dataSnapshot.child("totalAmount").getValue(Integer.class);


                    if (bookingDate != null && hotelName != null && nights != null && totalAmount != null) {
                        int nightsValue = nights.intValue();
                        int totalAmountValue = totalAmount.intValue();
                        booking = new accountDataClass(hotelName, totalAmountValue, nightsValue, bookingDate);
                        dataList1.add(booking);
                        accountAdapter.notifyDataSetChanged();
                    } {
                        // Toast.makeText(requireContext(), "No booking data found.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } else {
            Toast.makeText(getActivity(), "Please login first.", Toast.LENGTH_SHORT).show();
        }

        // DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Categories");
        bookingsRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList1=new ArrayList<>();
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    //String num=datas.child("bookingDate").getValue().toString();
                    try {
                        String bookingDate=datas.child("bookingDate").getValue().toString();
                        String hotelName=datas.child("hotelName").getValue().toString();
                        String nights=datas.child("nights").getValue().toString();
                        String totalAmount=datas.child("totalAmount").getValue().toString();
                        String UserID=datas.child("UserID").getValue()==null?"":datas.child("UserID").getValue().toString();
                        int nightsValue =Integer.parseInt(nights);
                        int totalAmountValue =Integer.parseInt(totalAmount);
                        booking = new accountDataClass(hotelName, totalAmountValue, nightsValue, bookingDate);
                        dataList1.add(booking);

                        System.out.println(user.getUid());
                        if(user.getUid().equals(UserID)){

                            System.out.println("*****");

                        }

                    }catch (Exception ex){
                      //  Toast.makeText(getActivity(), "Resirvation is done", Toast.LENGTH_SHORT).show();
                    }


                }

             /*   try {
                //    accountAdapter = new accountAdapter(getActivity(), dataList1);

                }catch (Exception ex){
                    Toast.makeText(getActivity(), ""+ex.getMessage(), Toast.LENGTH_SHORT).show();
                } */


                //  accountAdapter = new accountAdapter(getActivity(), dataList1);
                accountAdapter = new accountAdapter(isAdded() ? requireContext() : getContext(), dataList1);



                // Set layout manager and adapter for the RecyclerView
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                bookingRecyclerView.setLayoutManager(layoutManager);
                bookingRecyclerView.setAdapter(accountAdapter);

                accountAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        if (user != null) {
            String username = user.getDisplayName();
            TextView usernameTextView = getView().findViewById(R.id.usernameTextView);
            usernameTextView.setText("Welcome " + username);
        }


    }
}
