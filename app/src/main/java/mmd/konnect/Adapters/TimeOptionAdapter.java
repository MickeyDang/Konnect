package mmd.konnect.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;

import mmd.konnect.Models.TimeOption;
import mmd.konnect.R;
import mmd.konnect.Utils;

/**
 * Created by mickeydang on 2018-03-31.
 */

public class TimeOptionAdapter extends RecyclerView.Adapter<TimeOptionAdapter.ViewHolder> {

    private List<TimeOption> timeOptions;
    private ViewHolderUpdater mViewHolderUpdater;

    public TimeOptionAdapter(ViewHolderUpdater updater) {
        timeOptions = new ArrayList<>();
        mViewHolderUpdater = updater;
    }

    public void updateAdapter(TimeOption to) {
        timeOptions.add(to);
        notifyItemInserted(timeOptions.size() - 1);
    }

    public ArrayList<TimeOption> getTimeOptions() {
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
        holder.onBind(timeOptions.get(position), position);
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
        int position;

        public ViewHolder(View v) {
            super(v);

            icon = v.findViewById(R.id.letter_icon);
            date = v.findViewById(R.id.dateView);
            time = v.findViewById(R.id.timeView);
            deleteButton = v.findViewById(R.id.deleteButton);
        }

        public void onBind(TimeOption to, int position) {
            this.position = position;
            date.setText(to.getDate());
            time.setText(to.getStartTime());

            icon.setLetter(String.valueOf(position));
            icon.setShapeColor(Utils.getHashedMaterialColor(String.valueOf(to.getDate())));

            deleteButton.setOnClickListener(view -> {
                timeOptions.remove(position);
                notifyItemRemoved(position);

                //update position of other visible ViewHolders displayed in material icon
                mViewHolderUpdater.updateIconPositionNumbers();
            });
        }
    }
}
