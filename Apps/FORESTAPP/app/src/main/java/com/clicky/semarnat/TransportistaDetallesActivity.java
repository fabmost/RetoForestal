package com.clicky.semarnat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.clicky.semarnat.fragments.QRFragment;
import com.clicky.semarnat.fragments.TransportistaInfoFragment;

/**
 *
 * Created by Clicky on 3/23/15.
 *
 */
public class TransportistaDetallesActivity extends ActionBarActivity{

    private static String EXTRA_FOLIO = "folio";

    private String[]titles = {"Información","QR"};
    private String folio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportista_detalles);

        folio = getIntent().getExtras().getString(EXTRA_FOLIO);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     */
    private class SamplePagerAdapter extends FragmentStatePagerAdapter{

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
                    return TransportistaInfoFragment.newInstance(folio);
                case 1:
                    return QRFragment.newInstance(folio);
                default:
                    return TransportistaInfoFragment.newInstance(folio);
            }
        }

    }

}
