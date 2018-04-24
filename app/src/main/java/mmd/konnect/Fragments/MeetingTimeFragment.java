package mmd.konnect.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mmd.konnect.Adapters.TimeOptionAdapter;
import mmd.konnect.Adapters.ViewHolderUpdater;
import mmd.konnect.Models.Meeting;
import mmd.konnect.Models.TimeOption;
import mmd.konnect.R;

public class MeetingTimeFragment extends Fragment implements NullFieldAsserter{

    private final String LOG_TAG = this.getClass().getSimpleName();
    private OnTimeOptionInteractionListener mListener;
    private Meeting mMeeting;

    private RecyclerView mRecyclerView;
    private FloatingActionButton mAddTime;
    private TimeOptionAdapter mAdapter;

    private ViewHolderUpdater mViewHolderUpdater = new ViewHolderUpdater() {
        @Override
        public void updateIconPositionNumbers() {
            if (mRecyclerView == null) return;

            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

            int startIndex = layoutManager.findFirstVisibleItemPosition();
            int endIndex = layoutManager.findLastVisibleItemPosition();

            for (int i = startIndex; i <= endIndex; i++) {
                mAdapter.notifyItemChanged(i);
            }

        }
    };

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
        mAdapter = new TimeOptionAdapter(mViewHolderUpdater);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void createCalendarPicker(final Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                final long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

                try {
                    TimeOption to = new TimeOption();

                    SimpleDateFormat sdf = new SimpleDateFormat("MM dd, yyyy");
                    Date dateObject = sdf.parse((++i1) + " " + i2 + ", " + i);
                    to.setDate(new SimpleDateFormat("MMMM dd, yyyy").format(dateObject));
                    to.setStartTimeMillis(dateObject.getTime());

                    if (assertTimeInFuture(to.getStartTimeMillis(), System.currentTimeMillis() - MILLIS_IN_DAY)) {
                        createTimePicker(c, to, dateObject, false);
                    } else {
                        createCalendarPicker(c);
                    }

                } catch (ParseException pe) {
                    Log.e(LOG_TAG, pe.getMessage());
                }
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(getContext(), listener, year, month, day);
        dialog.setTitle(R.string.title_date);
        dialog.show();
    }

    private void createTimePicker(final Calendar c, final TimeOption to, final Date dateObj, final boolean isEndTime) {

        int hour = c.get(Calendar.HOUR_OF_DAY);
        final StringBuilder titleBuilder = new StringBuilder();

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    dateObj.setHours(i);
                    dateObj.setMinutes(i1);
                    if (!isEndTime) {
                        titleBuilder.append(getString(R.string.title_start));
                        to.setStartTimeMillis(dateObj.getTime());

                        if (assertTimeInFuture(to.getStartTimeMillis(), System.currentTimeMillis())) {
                            to.setStartTime(new SimpleDateFormat("h:mm aa").format(sdf.parse(i + ":" + i1)));
                            createTimePicker(c, to, dateObj, true);
                        } else {
                            createTimePicker(c, to, dateObj, false);
                        }

                    } else {
                        titleBuilder.append(getString(R.string.title_end));
                        to.setEndtimeMillis(dateObj.getTime());

                        if (assertTimeInFuture(to.getEndtimeMillis(), to.getStartTimeMillis())) {
                            to.setEndTime(new SimpleDateFormat("h:mm aa").format(sdf.parse(i + ":" + i1)));
                            mAdapter.updateAdapter(to);
                        } else {
                            createTimePicker(c, to, dateObj, true);
                        }
                    }

                } catch (ParseException pe) {
                    Log.e(LOG_TAG, pe.getMessage());
                }

            }
        };

        TimePickerDialog dialog = new TimePickerDialog(getContext(), listener, hour, 0, false);
        dialog.setTitle(titleBuilder.toString());
        dialog.show();
    }

    private boolean assertTimeInFuture(long futureTime, long pastTime) {
        if (futureTime > pastTime) {
            return true;
        } else {
            Toast.makeText(getContext(), getString(R.string.toast_invalid_time), Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
    }

    public ArrayList<TimeOption> getTimeOptions() {
        if (mAdapter == null) {
            mAdapter = new TimeOptionAdapter(mViewHolderUpdater);
        }
        return mAdapter.getTimeOptions();
    }

    @Override
    public boolean hasNullFields() {
        if (mAdapter.getItemCount() == 0) {
            Toast.makeText(getContext(), getString(R.string.toast_empty_field), Toast.LENGTH_SHORT)
                    .show();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTimeOptionInteractionListener) {
            mListener = (OnTimeOptionInteractionListener) context;
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

    public interface OnTimeOptionInteractionListener {}
}
