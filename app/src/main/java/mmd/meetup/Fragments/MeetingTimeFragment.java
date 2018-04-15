package mmd.meetup.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mmd.meetup.Adapters.TimeOptionAdapter;
import mmd.meetup.Models.Meeting;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.Models.TimeOption;
import mmd.meetup.R;

public class MeetingTimeFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private OnFragmentInteractionListener mListener;
    private Meeting mMeeting;

    private RecyclerView mRecyclerView;
    private FloatingActionButton mAddTime;
    private TimeOptionAdapter mAdapter;

    public MeetingTimeFragment() {
        // Required empty public constructor
    }

    public static MeetingTimeFragment newInstance(Meeting meeting) {
        MeetingTimeFragment fragment = new MeetingTimeFragment();
        fragment.mMeeting = meeting;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meeting_time, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.list);
        mAddTime = view.findViewById(R.id.addTime);

        mAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCalendarPicker(Calendar.getInstance());
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TimeOptionAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    private void createCalendarPicker(final Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                try {
                    TimeOption to = new TimeOption();

                    SimpleDateFormat sdf = new SimpleDateFormat("MM dd, yyyy");

                    Date dateObject = sdf.parse((++i1) + " " + i2 + ", " + i);

                    to.setDate(new SimpleDateFormat("MMMM dd, yyyy").format(dateObject));

                    createTimePicker(c, to, dateObject, false);

                } catch (ParseException pe) {
                    Log.e(LOG_TAG, pe.getMessage());
                }
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(getContext(), listener, year, month, day);
        dialog.show();
    }

    private void createTimePicker(final Calendar c, final TimeOption to, final Date dateObj, final boolean isEndTime) {

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

                    if (!isEndTime) {
                        dateObj.setHours(i);
                        dateObj.setMinutes(i1);

                        to.setStartTime(new SimpleDateFormat("h:mm aa").format(sdf.parse(i + ":" + i1)));
                        createTimePicker(c, to, dateObj, true);
                    } else {
                        to.endTime = sdf.format(sdf.parse(i + ":" + i1));
                        mAdapter.updateAdapter(to);
                    }

                } catch (ParseException pe) {
                    Log.e(LOG_TAG, pe.getMessage());
                }

            }
        };

        TimePickerDialog dialog = new TimePickerDialog(getContext(), listener, hour, minute, false);
        dialog.show();
    }

    public ArrayList<TimeOption> getTimeOptions() {
        if (mAdapter == null) {
            mAdapter = new TimeOptionAdapter();
        }
        return mAdapter.getTimeOptions();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
