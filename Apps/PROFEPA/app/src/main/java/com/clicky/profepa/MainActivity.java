package com.clicky.profepa;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

import com.clicky.profepa.data.PROFEPAPreferences;
import com.clicky.profepa.fragments.ListFragment;
import com.clicky.profepa.fragments.ReportFragment;
import com.clicky.profepa.fragments.ResultFragment;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class MainActivity extends ActionBarActivity implements View.OnClickListener,
        ListFragment.OnFolioSelectedListener{

    private static int SCANNER_ACTIVITY = 100;

    private ImageButton fab;

    private String folioActual;
    private int windowHeight;
    private float fabY;
    private boolean mReturningWithResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = (ImageButton)findViewById(R.id.fab);

        windowHeight = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        fabY = fab.getY();
        fab.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction().add(R.id.content, new ListFragment()).commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mReturningWithResult) {
            // Commit your transactions here.
            hideFab(0);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out)
                    .replace(R.id.content, ResultFragment.newInstance(folioActual))
                    .addToBackStack(null)
                    .commit();
        }
        // Reset the boolean flag back to false for next time.
        mReturningWithResult = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNER_ACTIVITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
                folioActual = data.getExtras().getString("folio");
                mReturningWithResult = true;
            }
        }

    }

    @Override
    public void onClick(View v){
        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Intent i = new Intent(MainActivity.this, ScannerActivity.class);
            startActivityForResult(i, SCANNER_ACTIVITY);
        }else{
            hideFab(2);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out)
                    .replace(R.id.content, ReportFragment.newInstance(folioActual))
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void update(){
        Intent i = new Intent(MainActivity.this, UpdateActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void logOut(){
        if(ParseUser.getCurrentUser() != null){
            PROFEPAPreferences pref = new PROFEPAPreferences(this);
            pref.setIsFirst(true);
            ParseUser.logOutInBackground();
            ParseObject.unpinAllInBackground();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }
    }

    @Override
    public void onFolioSelected(String folio){
        folioActual = folio;
        //fab.setBackgroundResource(R.drawable.fab_red);
        hideFab(0);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_right_in, R.anim.push_right_out)
                .replace(R.id.content, ResultFragment.newInstance(folioActual))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            if(getSupportFragmentManager().getBackStackEntryCount() == 1){
                hideFab(1);
                getSupportActionBar().setTitle(R.string.title_list);
            }else
                hideFab(3);
        }else
            super.onBackPressed();
    }

    private void hideFab(int type){
        switch (type) {
            case 0:
                fab.animate().translationY(windowHeight - fabY).setDuration(400).setInterpolator(new LinearInterpolator()).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fab.setBackgroundResource(R.drawable.fab_red);
                        fab.setImageResource(R.drawable.reportar);
                        fab.animate().translationY(fabY).setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
                    }
                }).start();
                break;
            case 1:
                fab.animate().translationY(windowHeight - fabY).setDuration(400).setInterpolator(new AccelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fab.setBackgroundResource(R.drawable.fab);
                        fab.setImageResource(R.drawable.fab_qr);
                        fab.animate().translationY(fabY).setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
                    }
                }).start();
                break;
            case 2:
                fab.animate().translationY(windowHeight - fabY).setDuration(400).setInterpolator(new LinearInterpolator()).setListener(null).start();
                break;
            case 3:
                fab.animate().translationY(fabY).setDuration(300).setInterpolator(new DecelerateInterpolator()).setListener(null).start();
                break;
        }
    }
}
