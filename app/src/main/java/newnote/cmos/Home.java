package newnote.cmos;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import newnote.cmos.POCO.Setting;
import newnote.cmos.SQLite.SQLiteHelper;

/**
 * Created by JIN on 1/19/2016.
 */
public class Home  extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu m = navigationView.getMenu();
        SQLiteHelper<Setting> sql = new SQLiteHelper<Setting>(this, Setting.class);
        try {
            sql.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Setting> lst = sql.getAll();
        SubMenu topChannelMenu = m.addSubMenu("Universe");
        for (final Setting r_setting : lst) {
            String strImage = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                strImage = new String(r_setting.Image, StandardCharsets.UTF_8);
            }
            Drawable d = new BitmapDrawable(getResources(), CommonFuc.decodeBase64(strImage));
            topChannelMenu.add(r_setting.Name).setIcon(d).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Toast.makeText(getApplicationContext(),
                            r_setting.Name, Toast.LENGTH_LONG).show();
                    setContentView(R.layout.home_activity);
                    return false;
                }
            });
        }

        navigationView.setNavigationItemSelectedListener(this);
    }


}
