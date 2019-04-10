package de.clubber_stuttgart.clubber.business_logic;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import de.clubber_stuttgart.clubber.R;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }




    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    Bundle bundle = new Bundle();

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment= new HomeFragment();
                            break;
                        case R.id.nav_events:
                            selectedFragment= new EventsFragment();
                            putNetworkState(bundle,selectedFragment);
                            break;
                        case R.id.nav_location:
                            selectedFragment= new ClubsFragment();
                            putNetworkState(bundle,selectedFragment);
                            break;
                    }

                    //ToDo: Vorsicht ist noch ineffizient, da wir das Home Fragment komplett zerstören! Die Struktur des Codes sieht das jedoch nicht vor. (
                    //ToDo In der onCreate() wird die business logik ausgeführt und diese sollte nur ausgeführt werden, wenn die App das aller erste mal startet. Das ist bei uns nicht der Fall (siehe HomeFragment methode)
                    //ToDo Die onCreateView() Methode erstellt nur die View und führt keine große Logik aus.
                    //ToDo: hier der Lifecycle: https://developer.android.com/guide/components/fragments

                    //ToDo: Die Methode onDestroy umgehen!
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };


    void putNetworkState(Bundle bundle, Fragment fragment){
        bundle.putBoolean("networkAccess", HomeFragment.networkAccess);
        fragment.setArguments(bundle);

    }



}



