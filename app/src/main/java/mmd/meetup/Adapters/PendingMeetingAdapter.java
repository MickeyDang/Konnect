package mmd.meetup.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import mmd.meetup.Firebase.FirebaseClient;
import mmd.meetup.Fragments.PendingMeetingListFragment.OnListFragmentInteractionListener;
import mmd.meetup.MeetUpApplication;
import mmd.meetup.Models.Meeting;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.R;
import mmd.meetup.Utils;

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
    public void onDelete(int index) {
        //we assume that the meeting must exist in the list because it was checked in fragment before calling onDelete()
        meetings.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public List<PendingMeeting> getFullList() {
        return meetings;
    }

    @Override
    public int containsItem(String s) {

        for (int i = 0; i < meetings.size(); i++) {
            if (meetings.get(i).getId().equals(s))
                return i;
        }

        return -1;
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
                aBoolean -> {
                    holder.voteButton.setClickable(aBoolean);
                    if (!aBoolean) {
                        holder.voteButton.setTextColor(MeetUpApplication.getInstance().getResources().getColor(R.color.secondary_text));
                        holder.voteButton.setBackgroundColor(MeetUpApplication.getInstance().getResources().getColor(R.color.light_background));
                    }
                });

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

        String numVoter = String.valueOf(pendingMeeting.getInvitedUsers().size());
        holder.materialIcon.setLetter(numVoter);
        holder.materialIcon.setShapeColor(Utils.getRandomMaterialColors(pendingMeeting.getTitle()));

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
        MaterialLetterIcon materialIcon;

        ViewHolder(View view) {
            super(view);

            materialIcon = view.findViewById(R.id.materialIcon);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            voteButton = view.findViewById(R.id.voteButton);
            resolveButton = view.findViewById(R.id.resolveVoteButton);

        }

    }
}
