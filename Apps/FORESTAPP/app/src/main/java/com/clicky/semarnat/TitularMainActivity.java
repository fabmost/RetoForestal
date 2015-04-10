package com.clicky.semarnat;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.clicky.semarnat.fragments.FolioListFragment;
import com.clicky.semarnat.fragments.NavigationDrawerFragment;
import com.clicky.semarnat.fragments.ReportesListFragment;
import com.clicky.semarnat.fragments.TransportistasListFragment;
import com.clicky.semarnat.utils.Utils;


public class TitularMainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        FolioListFragment.OnFolioSelectedListener,
        TransportistasListFragment.OnTransportistaSelectedListener,
        ReportesListFragment.OnReporteSelectedListener{

    private static int ALTA = 1;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titular_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();



        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                toolbar);

        onNavigationDrawerItemSelected(1,"Remisiones/Reembarques");

    }

    @Override
    public void onNavigationDrawerItemSelected(int position, String title) {
        // update the main content by replacing fragments
        Fragment frag = null;
        switch (position){
            case 1:
                frag = new FolioListFragment();
                break;
            case 2:
                frag = new TransportistasListFragment();
                break;
            case 3:
                frag = new FolioListFragment();
                break;
            case 4:
                frag = new ReportesListFragment();
                break;
        }
        if(frag != null) {
            setTitle(title);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, frag)
                    .commit();
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ALTA) {
            if (resultCode == RESULT_OK) {
                onNavigationDrawerItemSelected(1,"Remisiones/Reembarques");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.global, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                update();
                return true;
            case R.id.action_logout:
                Utils.logOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // turn on the Navigation Drawer image;
        // this is called in the LowerLevelFragments
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onAltaFolio(){
        Intent i = new Intent(TitularMainActivity.this, TitularAltaActivity.class);
        startActivityForResult(i, ALTA);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    @Override
    public void onFolioSelected(String folio){
        Intent i = new Intent(TitularMainActivity.this, TitularDetallesActivity.class);
        i.putExtra("docId", folio);
        startActivity(i);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    @Override
    public void onTransportistaSelected(String transportista){
        Intent i = new Intent(TitularMainActivity.this, TitularTransportistaActivity.class);
        i.putExtra("transportista", transportista);
        startActivity(i);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    @Override
    public void onReporteSelected(String reporte, String documento){
        Intent i = new Intent(TitularMainActivity.this, TitularReporteInfoActivity.class);
        i.putExtra("reporteId", reporte);
        i.putExtra("docId", documento);
        startActivity(i);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    private void setTitle(String title){
        mTitle = title;
        restoreActionBar();
    }

    private void update(){
        Intent i = new Intent(TitularMainActivity.this, UpdateActivity.class);
        startActivity(i);
        finish();
    }
}
