package de.clubber_stuttgart.clubber.business_logic;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import de.clubber_stuttgart.clubber.R;



public class MainActivity extends FragmentActivity {


    //ToDo: Baut sich das Event Fragment komplett neu auf, wenn refreshed wird (obwohl keine neuen Einträge vorhanden sind)?

    private final String LOG = "MainActiviy";
    //To ensure that the HomeFragment won't start the DBConnectionService again after it has done it once and been called another time.
    static boolean initSetupDatabase = true;
    private static Bundle bundle = new Bundle();

    private BottomNavigationView bottomNav;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }





    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_events:
                            selectedFragment = new EventsFragment();
                            break;
                        case R.id.nav_location:
                            selectedFragment = new ClubsFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };


    static void setDateInBundle(Fragment fragment, String date) {
        bundle.putString("selectedDate", date);
        fragment.setArguments(bundle);
    }

}



