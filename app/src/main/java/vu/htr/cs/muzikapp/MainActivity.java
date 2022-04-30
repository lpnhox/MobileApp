package vu.htr.cs.muzikapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.accounts.Account;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private static final int FRAG_ACCOUNT = 0;
    private static final int FRAG_FAVORITE = 1;
    private static final int FRAG_LIST = 2;
    private static final int FRAG_ADD = 3;

    private int currentFrag = FRAG_ACCOUNT;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this::onOptionsItemSelected);

        replaceFrag(new Profile());
        navigationView.getMenu().findItem(R.id.nav_account).setChecked(true);
    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_account:
                if(currentFrag!=FRAG_ACCOUNT) {
                    replaceFrag(new Profile());
                    currentFrag = FRAG_ACCOUNT;
                }
                break;
            case R.id.nav_favorite:
                if(currentFrag!=FRAG_FAVORITE) {
                    replaceFrag(new FavorSong());
                    currentFrag = FRAG_LIST;
                }
                break;
            case R.id.nav_listsong:
                if(currentFrag!=FRAG_LIST) {
                    replaceFrag(new ListSong());
                    currentFrag = FRAG_LIST;
                }
                break;
            case R.id.nav_add:
                if(currentFrag!=FRAG_ADD) {
                    replaceFrag(new AddMusic());
                    currentFrag = FRAG_ADD;
                }
                break;
        }
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
    private void replaceFrag(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
}

