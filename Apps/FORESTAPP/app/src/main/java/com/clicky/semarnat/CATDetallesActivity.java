package com.clicky.semarnat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.clicky.semarnat.fragments.MateriaFechaListFragment;
import com.clicky.semarnat.fragments.MateriaImgListFragment;
import com.clicky.semarnat.fragments.MateriaListFragment;

/**
 *
 * Created by Clicky on 3/24/15.
 *
 */
public class CATDetallesActivity extends ActionBarActivity implements MateriaFechaListFragment.OnMateriaSelectedListener,
    MateriaImgListFragment.OnMateriaSelectedListener{

    private static String EXTRA_EMPRESAID = "empresaId";
    private static String EXTRA_EMPRESA = "empresa";

    private String[]titles = {"Materia Prima","Prox. Reembarque","Prox. Remisión","Historial"};
    private String empresaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportista_detalles);

        empresaId = getIntent().getExtras().getString(EXTRA_EMPRESAID);
        String empresa = getIntent().getExtras().getString(EXTRA_EMPRESA);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(empresa);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMateriaSelected(String materia, String doc){
        Intent i = new Intent(CATDetallesActivity.this, CATRegistroActivity.class);
        i.putExtra("materiaId",materia);
        i.putExtra("docId",doc);
        startActivity(i);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     */
    private class SamplePagerAdapter extends FragmentStatePagerAdapter {

        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }
        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return titles.length;
        }

        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return MateriaListFragment.newInstance(empresaId);
                case 1:
                    return MateriaFechaListFragment.newInstance(empresaId,0);
                case 2:
                    return MateriaFechaListFragment.newInstance(empresaId,1);
                case 3:
                    return MateriaImgListFragment.newInstance(empresaId);
                default:
                    return MateriaListFragment.newInstance(empresaId);
            }
        }

    }

}
