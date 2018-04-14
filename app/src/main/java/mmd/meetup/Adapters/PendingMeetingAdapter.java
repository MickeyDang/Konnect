package mmd.meetup.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mmd.meetup.Firebase.FirebaseClient;
import mmd.meetup.Fragments.PendingMeetingListFragment.OnListFragmentInteractionListener;
import mmd.meetup.Models.Meeting;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.R;

import java.util.ArrayList;
import java.util.List;

public class PendingMeetingAdapter extends RecyclerView.Adapter<PendingMeetingAdapter.ViewHolder> implements
        FirebaseAdapter<PendingMeeting> {

    private final OnListFragmentInteractionListener mListener;
    private List<PendingMeeting> meetings;

    public PendingMeetingAdapter(OnListFragmentInteractionListener listener) {
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
    public void onDelete(PendingMeeting meeting) {
        int index = 0;
        while (index < meetings.size() && !meeting.getId().equals(meetings.get(index).getId())) {
            index++;
        }
        //we assume that the meeting must exist in the list because it was checked in fragment before calling onDelete()
        meetings.remove(index);
        notifyItemRemoved(index);
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
                .inflate(R.layout.item_pendingmeeting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PendingMeeting pendingMeeting = meetings.get(position);

        holder.title.setText(pendingMeeting.getTitle());
        holder.description.setText(pendingMeeting.getDescription());

        //vote button is clickable by default
        FirebaseClient.getInstance().getVoteStatus(pendingMeeting.getId(),
                aBoolean -> holder.voteButton.setClickable(aBoolean));

        //resolve button visibility is invisible by default
        FirebaseClient.getInstance().isOwnerOfVote(pendingMeeting.getId(),
                aBoolean -> {
            int i = aBoolean ? View.VISIBLE : View.INVISIBLE;
            holder.resolveButton.setVisibility(i);

            if (i == View.VISIBLE) {
                holder.resolveButton.setOnClickListener(v -> mListener.onResolveVote(pendingMeeting));
            }

        });

        holder.voteButton.setOnClickListener(v -> mListener.onCastVote(pendingMeeting));

    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        Button voteButton;
        Button resolveButton;

        ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            voteButton = view.findViewById(R.id.voteButton);
            resolveButton = view.findViewById(R.id.resolveVoteButton);

        }

    }
}
