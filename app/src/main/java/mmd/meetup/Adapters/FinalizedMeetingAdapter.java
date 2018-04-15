package mmd.meetup.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mmd.meetup.Fragments.FinalizedMeetingListFragment.OnListFragmentInteractionListener;
import mmd.meetup.Models.FinalizedMeeting;
import mmd.meetup.R;


public class FinalizedMeetingAdapter extends RecyclerView.Adapter<FinalizedMeetingAdapter.ViewHolder> implements
        FirebaseAdapter<FinalizedMeeting>{

    private final OnListFragmentInteractionListener mListener;
    private List<FinalizedMeeting> finalizedMeetings;

    public FinalizedMeetingAdapter(OnListFragmentInteractionListener listener) {
        finalizedMeetings = new ArrayList<>();
        mListener = listener;
    }


    @Override
    public void onInsert(FinalizedMeeting meeting) {
        finalizedMeetings.add(meeting);
        notifyItemInserted(finalizedMeetings.size() - 1);
    }

    @Override
    public void onUpdate(FinalizedMeeting meeting) {

    }

    @Override
    public void onFilter(FinalizedMeeting meeting) {

    }

    @Override
    public void onDelete(int index) {

    }

    @Override
    public List<FinalizedMeeting> getFullList() {
        return finalizedMeetings;
    }

    @Override
    public int containsItem(String s) {

        for (int i = 0; i < finalizedMeetings.size(); i++) {
            if (finalizedMeetings.get(i).getId().equals(s))
                return i;
        }

        return -1;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_finalziedmeeting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FinalizedMeeting finalizedMeeting = finalizedMeetings.get(position);

        String formattedText = finalizedMeeting.getDescription().length() > 30 ? finalizedMeeting.getDescription().substring(0, 30).concat("...") : finalizedMeeting.getDescription();

        holder.description.setText(formattedText);
        holder.title.setText(finalizedMeeting.getTitle());

        formattedText = finalizedMeeting.getMeetingPlace().getName();

        if (formattedText.length() < 10) {
            formattedText += ": " +
                    (finalizedMeeting.getMeetingPlace().getAddress().length() > 20
                            ? finalizedMeeting.getMeetingPlace().getAddress().substring(0, 20)
                            : finalizedMeeting.getMeetingPlace().getAddress());
        }

        holder.placeText.setText(formattedText);

        //format is month [space] day, [space] year
        String[] texts = finalizedMeeting.getTimeOption().getDate().split(" ");

        if (texts.length == 3)
            formattedText = "Start: " + texts[0] + texts[1] + " - " + finalizedMeeting.getTimeOption().getStartTime();
        else
            formattedText = "Start: " + finalizedMeeting.getTimeOption().getDate() + " - " + finalizedMeeting.getTimeOption().getStartTime();

        formattedText = formattedText.length() > 30 ?  formattedText.substring(0, 30) : formattedText;

        holder.timeText.setText(formattedText);


    }

    @Override
    public int getItemCount() {
        return finalizedMeetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public TextView title;
        public TextView placeText;
        public TextView timeText;
        public ImageView placeLogo;
        public ImageView clockLogo;

        public ViewHolder(View view) {
            super(view);

            description = view.findViewById(R.id.description);
            title = view.findViewById(R.id.title);
            placeText = view.findViewById(R.id.placeText);
            placeLogo = view.findViewById(R.id.placeLogo);
            timeText = view.findViewById(R.id.timeText);
            clockLogo = view.findViewById(R.id.clockLogo);

        }

    }
}
