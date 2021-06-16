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

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

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

        ServiceManager.routingTable();
        Map<String, Class<? extends IService>> map = ServiceManager.getMap();
        try {
            Class<ImageLoadService> service = getService(map, ImageLoadService.class);
            ImageLoadService iService = service.getConstructor().newInstance();
            Log.i(TAG, "onCreate: " + iService.getClass().getName());
            Log.i(TAG, "------------------");
            iService.loadImage();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private <T extends Class> T getService(Map<String, Class<? extends IService>> map, T t) {
        String name = t.getCanonicalName();
        Log.i(TAG, "getService: "+name);
        Class<? extends IService> service = map.get(name);
        return (T) service;
    }
}