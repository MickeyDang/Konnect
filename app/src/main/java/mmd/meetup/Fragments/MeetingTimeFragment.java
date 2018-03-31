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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import mmd.meetup.Adapters.TimeOptionAdapter;
import mmd.meetup.Models.Meeting;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.R;

public class MeetingTimeFragment extends Fragment {

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
                createTimePicker(c);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(getContext(), listener, year, month, day);
        dialog.show();
    }

    private void createTimePicker(Calendar c) {

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Toast.makeText(getContext(), "details selected!", Toast.LENGTH_SHORT)
                        .show();
            }
        };

        TimePickerDialog dialog = new TimePickerDialog(getContext(), listener, hour, minute, false);
        dialog.show();
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
