package test.nicaragua.com.earthquakeapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import test.nicaragua.com.earthquakeapp.database.EventRepository;
import test.nicaragua.com.earthquakeapp.model.Event;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {

    List<Event> eventList;
    Context mContext;
    static Calendar mStarttime, mEndtime;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance() {
        EventFragment fragment = new EventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventList = new ArrayList<>();
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        mStarttime = Calendar.getInstance();
        mStarttime.add(Calendar.DAY_OF_MONTH, -1);
        mEndtime = Calendar.getInstance();

        final Button btStartTime = (Button) view.findViewById(R.id.bt_starttime);
        btStartTime.setText(df.format(mStarttime.getTime()));
        btStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = mStarttime.get(Calendar.DAY_OF_MONTH);
                int month = mStarttime.get(Calendar.MONTH);
                int year = mStarttime.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mStarttime.set(year, month, dayOfMonth);
                        btStartTime.setText(df.format(mStarttime.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        final Button btEndTime = (Button) view.findViewById(R.id.bt_endTime);
        btEndTime.setText(df.format(mEndtime.getTime()));
        btEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = mEndtime.get(Calendar.DAY_OF_MONTH);
                int month = mEndtime.get(Calendar.MONTH);
                int year = mEndtime.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mEndtime.set(year, month, dayOfMonth);
                        btEndTime.setText(df.format(mEndtime.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        final DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute("");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                eventList = EventRepository.consumeWS(mContext, mStarttime.getTime(), mEndtime.getTime() );
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

}
