package test.nicaragua.com.earthquakeapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment EventFragment.
     */
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

        final ImageButton bt_search = (ImageButton) view.findViewById(R.id.bt_search);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute("");
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

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
                mAdapter = new EventAdapter(eventList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(mAdapter);
            } else {
                // TODO: Alert dialog
            }
        }
    }

    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

        private List<Event> eventList;
        private int lastPosition = -1;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView magnitude, time, place;
            public ImageView indicator;

            public MyViewHolder(View view) {
                super(view);
                magnitude = (TextView) view.findViewById(R.id.magnitude);
                time = (TextView) view.findViewById(R.id.time);
                place = (TextView) view.findViewById(R.id.place);
                indicator = (ImageView) view.findViewById(R.id.indicator);
            }
        }


        public EventAdapter(List<Event> eventList) {
            this.eventList = eventList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_list_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Event event = eventList.get(position);
            holder.magnitude.setText(event.getMagnitude().toString());
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(event.getTime());
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            holder.time.setText(df.format(time.getTime()));
            holder.place.setText(event.getDirection());
            if (event.getMagnitude() < 3 ) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    holder.indicator.setImageDrawable(getActivity().getDrawable(R.mipmap.green_circle));
                } else{
                    holder.indicator.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.green_circle));
                }
            } else {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    holder.indicator.setImageDrawable(getActivity().getDrawable(R.mipmap.red_circle));
                } else{
                    holder.indicator.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.red_circle));
                }
            }
            setAnimation(holder.itemView, position);
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        private void setAnimation(View viewToAnimate, int position)
        {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition)
            {
                Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }
    }

}
