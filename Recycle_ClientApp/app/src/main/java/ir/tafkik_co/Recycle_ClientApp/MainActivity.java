package ir.tafkik_co.Recycle_ClientApp;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    static DrawerLayout tmpDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_xmlfile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ImageView imgDrawer =  toolbar.findViewById(R.id.imgDrawer);
        imgDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar.setNavigationIcon(null);
                if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.closeDrawer(Gravity.RIGHT);
                } else {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_garbageSelection,R.id.nav_myscores, R.id.nav_userInformation, R.id.nav_requestsReport,
                R.id.nav_requestsPayment, R.id.nav_contactSupport, R.id.nav_introducingUs, R.id.nav_about_us,
                R.id.nav_logoutApp, R.id.nav_exitApp)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //This line used to hide left navigation menu.
        //this line also repeated in all fragments of navigation drawer
        toolbar.setNavigationIcon(null);
        //this variable use in changeTitle function
        tmpDrawerLayout = findViewById(R.id.drawer_layout);
    }

    //Use This function to change title of toolbar from another fragments
    static public void changeTitle(String newTitle){
        TextView tv = tmpDrawerLayout.findViewById(R.id.txtTitle1);
        tv.setText(newTitle);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
