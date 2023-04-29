package com.example.getmypix;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.getmypix.Models.DataManager;
import com.example.getmypix.Models.DownloadImage;
import com.example.getmypix.Models.Firebase;
import com.example.getmypix.Models.MainModel;
import com.example.getmypix.Models.PostAsyncDao;
import com.example.getmypix.Models.Posts;
import com.example.getmypix.Models.TakePhoto;
import com.example.getmypix.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    NavController navController;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(TakePhoto.hasPemissions(this)) {
            DataManager.SyncAllPosts(this);
        } else {
            TakePhoto.grantWriteStoragePermissions(this);
        }
    }

    private void initApp() {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navController = Navigation.findNavController(this, R.id.get_my_pix_nav_graph);

        if (Users.isAuthenticated()) {
            this.prepareViewForLoggedInUser(Users.getUser());
        } else {
            if (GetMyPixApplication.isInternetAvailable()) {
                FirebaseAuth.getInstance().signInAnonymously();
            }

            this.prepareViewForGuest();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navigationView, navController);
        getSupportActionBar().setTitle("GetMyPix!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initApp();
        if (TakePhoto.hasPemissions(this)) {
            DataManager.SyncAllPosts(this);
        } else {
            TakePhoto.grantWriteStoragePermissions(this);
        }
    }

    public void prepareViewForGuest() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.login).setVisible(true);
        navigationView.getMenu().findItem(R.id.myFeeds).setVisible(false);
        navigationView.getMenu().findItem(R.id.removeUser).setVisible(false);
        navigationView.getMenu().findItem(R.id.logout).setVisible(false);
        ((ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_profile_pix)).setImageResource(R.drawable.launcher_icon);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.display_name)).setText("");

        navController.popBackStack(R.id.listFeeds, false);
    }

    public void prepareViewForLoggedInUser(FirebaseUser user) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.login).setVisible(false);
        navigationView.getMenu().findItem(R.id.myFeeds).setVisible(true);
        navigationView.getMenu().findItem(R.id.removeUser).setVisible(true);
        navigationView.getMenu().findItem(R.id.logout).setVisible(true);
        new DownloadImage((ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_profile_pix)).execute(user.getPhotoUrl().toString());
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.display_name)).setText(user.getDisplayName());

        navController.popBackStack(R.id.listFeeds, false);
    }
}
