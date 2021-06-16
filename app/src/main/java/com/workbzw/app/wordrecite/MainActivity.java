package com.workbzw.app.wordrecite;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.workbzw.lib.base.IService;
import com.workbzw.lib.base.ServiceManager;
import com.workbzw.service.image.ImageLoadService;
import com.workbzw.service.network.NetworkService;

import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ImageLoader";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        ImageLoadService service = ServiceManager.getService(ImageLoadService.class);
        NetworkService networkService = ServiceManager.getService(NetworkService.class);
        Log.i(TAG, "onCreate: " + service.getClass().getName());
        Log.i(TAG, "------------------");
        service.loadImage();
        networkService.request();
        printRoutingTable();
    }

    private void printRoutingTable() {
        Set<Map.Entry<String, Class<? extends IService>>> entrySet = ServiceManager.getRoutingTable().entrySet();
        for (Map.Entry<String, Class<? extends IService>> entry : entrySet) {
            Log.i(TAG, "Entry:" + "\nK:" + entry.getKey() + "\nV:" + entry.getValue());
        }
    }

}