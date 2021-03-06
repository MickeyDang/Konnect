package mmd.konnect.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mmd.konnect.Fragments.FinalizedMeetingListFragment.MeetingInteractionListener;
import mmd.konnect.KonnectApplication;
import mmd.konnect.Models.FinalizedMeeting;
import mmd.konnect.R;


public class FinalizedMeetingAdapter extends RecyclerView.Adapter<FinalizedMeetingAdapter.ViewHolder> {

    private final MeetingInteractionListener mListener;
    private List<FinalizedMeeting> finalizedMeetings;

    public FinalizedMeetingAdapter(MeetingInteractionListener listener) {
        finalizedMeetings = new ArrayList<>();
        mListener = listener;
    }


    public void onInsert(FinalizedMeeting meeting) {
        finalizedMeetings.add(meeting);
        notifyItemInserted(finalizedMeetings.size() - 1);
    }

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
        final long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
        Context context = KonnectApplication.getInstance().getApplicationContext();

        FinalizedMeeting finalizedMeeting = finalizedMeetings.get(position);

        holder.title.setText(finalizedMeeting.getTitle());

        String formattedText = finalizedMeeting.getMeetingPlace().getName();

        if (formattedText.length() < 15) {
            formattedText += ": " +
                    (finalizedMeeting.getMeetingPlace().getAddress().length() > 20
                            ? finalizedMeeting.getMeetingPlace().getAddress().substring(0, 20) + "..."
                            : finalizedMeeting.getMeetingPlace().getAddress());
        }

        holder.placeText.setText(formattedText);

        //format is month [space] day, [space] year
        String[] texts = finalizedMeeting.getTimeOption().getDate().split(" ");

        if (texts.length == 3)
            formattedText = "Start: " + texts[0] + " " + texts[1] + " " + finalizedMeeting.getTimeOption().getStartTime();
        else
            formattedText = "Start: " + finalizedMeeting.getTimeOption().getDate() + " " + finalizedMeeting.getTimeOption().getStartTime();

        formattedText = formattedText.length() > 30 ? formattedText.substring(0, 30) : formattedText;

        holder.timeText.setText(formattedText);

        if (finalizedMeeting.getTimeOption().getStartTimeMillis() > System.currentTimeMillis() + 3 * MILLIS_IN_DAY) {
            holder.notifLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_notification_green));
        } else if (finalizedMeeting.getTimeOption().getStartTimeMillis() > System.currentTimeMillis() + MILLIS_IN_DAY) {
            holder.notifLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_notification_yellow));
        } else {
            holder.notifLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_notification_red));
        }

        holder.itemView.setOnClickListener(v -> mListener.onFinalizedMeetingSelected(finalizedMeeting));
    }

    @Override
    public int getItemCount() {
        return finalizedMeetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView placeText;
        public TextView timeText;
        public ImageView placeLogo;
        public ImageView clockLogo;
        public ImageView notifLogo;
        public View itemView;

        public ViewHolder(View view) {
            super(view);

            itemView = view;
            title = view.findViewById(R.id.title);
            placeText = view.findViewById(R.id.placeText);
            placeLogo = view.findViewById(R.id.placeLogo);
            timeText = view.findViewById(R.id.timeText);
            clockLogo = view.findViewById(R.id.clockLogo);
            notifLogo = view.findViewById(R.id.notif);

        }

    }
}
