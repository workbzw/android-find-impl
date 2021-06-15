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
import java.util.Set;

public class MainActivity extends AppCompatActivity {

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
        Set<String> keySet = map.keySet();

        for (String s : keySet) {
            Log.i(TAG, "onCreate: " + s);
            Class<? extends IService> instance = map.get(s);
            try {
//                Class<?> service = Class.forName("com.workbzw.service.image.ImageLoadService.class");
//                Object o = service.getConstructor().newInstance();
                if (s.equals("com.workbzw.service.image.ImageLoadService")) {
//                if (instance instanceof o) {
                    ImageLoadService iService = (ImageLoadService) instance.getConstructor().newInstance();
                    iService.loadImage();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private static final String TAG = "MainActivity";
}