package com.tina;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

public class AppListActivity extends FragmentActivity {

    RecyclerViewFragment fragment;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        if (savedInstanceState == null) {
            fragment = new RecyclerViewFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ListView Demo");

        toolbar.setPopupTheme(R.style.ToolbarPopupTheme);
        //用Toolbar创建menu
        toolbar.inflateMenu(R.menu.menu_main);
        //拿到Menu
        Menu menu = toolbar.getMenu();
        //下面的这段代码是为了让menu菜单折叠样式时,展开能显示icon图标.不然icon图标不会显示.(感觉很坑)
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    MenuBuilder menuBuilder = (MenuBuilder) menu;
                    menuBuilder.setOptionalIconsVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = menuItem -> {
        String msg = "";
        switch (menuItem.getItemId()) {
            case R.id.pause:
                fragment.pauseAll();
                break;
            case R.id.recover:
                fragment.recoverAll();
                break;
            case R.id.cancel:
                fragment.cancelAll();
                break;
        }
        return true;
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
