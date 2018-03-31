package mmd.meetup.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mmd.meetup.Fragments.VoteListFragment.OnListFragmentInteractionListener;
import mmd.meetup.Models.Meeting;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.R;

import java.util.ArrayList;
import java.util.List;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> implements
        FirebaseAdapter<PendingMeeting> {

    private final OnListFragmentInteractionListener mListener;
    private List<PendingMeeting> meetings;

    public VoteAdapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
        meetings = new ArrayList<>();
    }

    @Override
    public void onInsert(PendingMeeting meeting) {
        meetings.add(meeting);
        notifyItemInserted(meetings.size() - 1);
    }

    @Override
    public void onUpdate(PendingMeeting meeting) {

    }

    @Override
    public void onFilter(PendingMeeting meeting) {

    }

    @Override
    public List<PendingMeeting> getFullList() {
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
                .inflate(R.layout.item_vote, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Meeting meeting = meetings.get(position);

        holder.title.setText(meeting.getTitle());
        holder.description.setText(meeting.getDescription());

    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);

        }

    }
}
