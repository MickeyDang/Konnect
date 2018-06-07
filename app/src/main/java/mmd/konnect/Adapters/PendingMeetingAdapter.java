package mmd.konnect.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;

import mmd.konnect.Firebase.FirebaseClient;
import mmd.konnect.Fragments.PendingMeetingListFragment;
import mmd.konnect.KonnectApplication;
import mmd.konnect.Models.PendingMeeting;
import mmd.konnect.R;
import mmd.konnect.Utils;

public class PendingMeetingAdapter extends RecyclerView.Adapter<PendingMeetingAdapter.ViewHolder> {

    private final PendingMeetingListFragment.PendingMeetingInteractionListener mListener;
    private List<PendingMeeting> meetings;

    public PendingMeetingAdapter(PendingMeetingListFragment.PendingMeetingInteractionListener listener) {
        mListener = listener;
        meetings = new ArrayList<>();
    }

    public void onInsert(PendingMeeting meeting) {
        meetings.add(meeting);
        notifyItemInserted(meetings.size() - 1);
    }

    public void onDelete(int index) {
        //we assume that the meeting must exist in the list because it was checked in fragment before calling onDelete()
        meetings.remove(index);
        notifyItemRemoved(index);
    }

    public List<PendingMeeting> getFullList() {
        return meetings;
    }

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
        final PendingMeeting pendingMeeting = meetings.get(position);

        holder.title.setText(pendingMeeting.getTitle());
        holder.description.setText(pendingMeeting.getDescription());

        //vote button is clickable by default
        FirebaseClient.getInstance().getVoteStatus(pendingMeeting.getId(),
                aBoolean -> {
                    holder.voteButton.setClickable(aBoolean);
                    if (!aBoolean) {
                        holder.voteButton.setTextColor(KonnectApplication.getInstance().getResources().getColor(R.color.secondary_text));
                        holder.voteButton.setBackgroundColor(KonnectApplication.getInstance().getResources().getColor(R.color.light_background));
                    }
                });

        //resolve and delete button visibility is invisible by default unless current user owns the meeting
        boolean ownerOfVote = (pendingMeeting.getOrganizerID().contentEquals(FirebaseClient.getInstance().getUserID()));

        if (ownerOfVote) {
            holder.resolveButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.resolveButton.setOnClickListener(v -> mListener.onResolveVote(pendingMeeting));
            holder.deleteButton.setOnClickListener(v -> mListener.onDeleteMeeting(pendingMeeting));
        }

        //set listener for vote button
        holder.voteButton.setOnClickListener(v -> mListener.onCastVote(pendingMeeting));

        String numVoter = String.valueOf(pendingMeeting.getInvitedUsers().size());
        holder.materialIcon.setLetter(numVoter);
        holder.materialIcon.setShapeColor(Utils.getHashedMaterialColor(pendingMeeting.getTitle()));

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
        Button deleteButton;
        MaterialLetterIcon materialIcon;

        ViewHolder(View view) {
            super(view);

            materialIcon = view.findViewById(R.id.materialIcon);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            voteButton = view.findViewById(R.id.voteButton);
            resolveButton = view.findViewById(R.id.resolveVoteButton);
            deleteButton = view.findViewById(R.id.deleteButton);

        }

    }
}
