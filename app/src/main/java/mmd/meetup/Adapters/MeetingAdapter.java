package mmd.meetup.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mmd.meetup.Fragments.MeetingListFragment.OnListFragmentInteractionListener;
import mmd.meetup.Meeting;
import mmd.meetup.R;


public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private List<Meeting> meetings;

    public MeetingAdapter(OnListFragmentInteractionListener listener) {
        meetings = new ArrayList<>();
        mListener = listener;
    }

    public void insertMeeting(Meeting meeting) {
        meetings.add(meeting);
        notifyDataSetChanged();
//        notifyItemInserted(meetings.size() - 1);
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
