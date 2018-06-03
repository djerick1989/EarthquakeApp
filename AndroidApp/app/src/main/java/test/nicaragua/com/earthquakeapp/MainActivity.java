package test.nicaragua.com.earthquakeapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import test.nicaragua.com.earthquakeapp.database.EventRepository;
import test.nicaragua.com.earthquakeapp.model.Event;

public class MainActivity extends AppCompatActivity {

    List<Event> eventList;
    ViewPager viewPager;
    SlidePagerAdapter slidePagerAdapter;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);

        slidePagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(slidePagerAdapter);

        eventList = new ArrayList<>();
        mContext = getBaseContext();

        final DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                Calendar yesterday = Calendar.getInstance();
                yesterday.add(Calendar.DAY_OF_MONTH, -1);
                Calendar today = Calendar.getInstance();
                Date starttime = yesterday.getTime();
                Date endtime = today.getTime();
                eventList = EventRepository.consumeWS(mContext, starttime, endtime );
                data = "1";
            } catch (Exception e) {
                e.printStackTrace();
                data = "-1";
            }
            return data;
        }

        // Una vez finalizada la descarga de datos
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("1")) {
                // TODO: Load elements in RecyclerView
            } else {
                // TODO: Alert dialog
            }
        }
    }

    public class SlidePagerAdapter extends FragmentStatePagerAdapter {

        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int arg0) {
            Bundle bundle = new Bundle();
            Fragment fragment;
            if(arg0 == 1) {
                fragment = EventFragment.newInstance();
            } else {
                // TODO: Cargar fragment del mapa
                fragment = EventFragment.newInstance();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
