package mmd.meetup.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mmd.meetup.Models.TimeOption;
import mmd.meetup.R;

/**
 * Created by mickeydang on 2018-03-31.
 */

public class TimeOptionAdapter extends RecyclerView.Adapter<TimeOptionAdapter.ViewHolder> {

    private List<TimeOption> timeOptions;

    public TimeOptionAdapter() {
        timeOptions = new ArrayList<>();
    }

    public void updateAdapter(TimeOption to) {
        timeOptions.add(to);
        notifyItemInserted(timeOptions.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timeoption, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TimeOption to = timeOptions.get(position);

        holder.date.setText(to.date);
        holder.time.setText(to.startTime);

    }

    @Override
    public int getItemCount() {
        return timeOptions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView time;
        Button vote;

        public ViewHolder(View v) {
            super(v);

            date = v.findViewById(R.id.dateView);
            time = v.findViewById(R.id.timeView);
            vote = v.findViewById(R.id.voteButton);
        }

    }

}
