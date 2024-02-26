package com.example.hotelbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class homePage extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        nav = findViewById(R.id.nav);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 // the menu bar actions and moving between the pages...
                switch (item.getItemId()){
                    case R.id.homeFragment:
                        replaceFragment (new HomeFragment());
                       // Toast.makeText(homePage.this, "Home", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.searchFragment:
                        replaceFragment(new searchFragment());
                        //Toast.makeText(homePage.this, "Search", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.accountFragment:
                        replaceFragment(new accountFragment());
                        //Toast.makeText(homePage.this, "Account", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.settingsFragment:
                        replaceFragment(new settingsFragment());
                        //Toast.makeText(homePage.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;

                    default:


                }


                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment,fragment);
        fragmentTransaction.commit();
    }
}

