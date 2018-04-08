package mmd.meetup.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mmd.meetup.Fragments.FinalizedMeetingListFragment.OnListFragmentInteractionListener;
import mmd.meetup.Models.FinalizedMeeting;
import mmd.meetup.Models.Meeting;
import mmd.meetup.R;


public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> implements
        FirebaseAdapter<FinalizedMeeting>{

    private final OnListFragmentInteractionListener mListener;
    private List<FinalizedMeeting> meetings;

    public MeetingAdapter(OnListFragmentInteractionListener listener) {
        meetings = new ArrayList<>();
        mListener = listener;
    }


    @Override
    public void onInsert(FinalizedMeeting meeting) {
        meetings.add(meeting);
        notifyItemInserted(meetings.size() - 1);
    }

    @Override
    public void onUpdate(FinalizedMeeting meeting) {

    }

    @Override
    public void onFilter(FinalizedMeeting meeting) {

    }

    @Override
    public List<FinalizedMeeting> getFullList() {
        return meetings;
    }

    @Override
    public boolean containsItem(String s) {

        for (Meeting m : meetings) {
            if (m.getId().equals(s)) return true;
        }

        return false;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Meeting meeting = meetings.get(position);

        holder.description.setText(meeting.getDescription());
        holder.title.setText(meeting.getTitle());
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public TextView title;

        public ViewHolder(View view) {
            super(view);

            description = view.findViewById(R.id.description);
            title = view.findViewById(R.id.title);

        }

    }
}
