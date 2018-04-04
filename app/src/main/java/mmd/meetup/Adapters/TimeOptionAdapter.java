package mmd.meetup.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;

import mmd.meetup.Models.TimeOption;
import mmd.meetup.R;
import mmd.meetup.Utils;

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

    public ArrayList<TimeOption> getTimeOptions () {
        //lazy loading
        if (timeOptions == null) {
            timeOptions = new ArrayList<>();
        }
        return (ArrayList<TimeOption>) timeOptions;
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

        holder.date.setText(to.getDate());
        holder.time.setText(to.getStartTime());

        holder.icon.setLetter(String.valueOf(position));
        holder.icon.setShapeColor(Utils.getRandomMaterialColors(String.valueOf(to.getDate())));

    }

    @Override
    public int getItemCount() {
        return timeOptions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        MaterialLetterIcon icon;
        TextView date;
        TextView time;
        ImageButton deleteButton;

        public ViewHolder(View v) {
            super(v);

            icon = v.findViewById(R.id.letter_icon);
            date = v.findViewById(R.id.dateView);
            time = v.findViewById(R.id.timeView);
            deleteButton = v.findViewById(R.id.deleteButton);
        }

    }

}
