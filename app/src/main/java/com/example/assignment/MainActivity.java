package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.assignment.auth.Auth;
import com.example.assignment.task.TaskAdapter;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle("Signed in as " + Auth.username);
        Log.i("auth_from_main", Auth.username + " " + Auth.user_id);
        setSupportActionBar(toolbar);

        //set drawer
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TodoFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_todo);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_todo) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TodoFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_todo);
        } else if (item.getItemId() == R.id.nav_done) {
            navigationView.setCheckedItem(R.id.nav_done);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DoneFragment()).commit();
        } else if (item.getItemId() == R.id.nav_exit) {
            TaskAdapter.taskData.clear();
            //go back to login activity
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();

            //completely exit app
            finishAffinity();
            System.exit(0);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}