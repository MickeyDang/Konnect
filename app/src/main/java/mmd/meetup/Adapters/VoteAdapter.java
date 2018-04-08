package mmd.meetup.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mmd.meetup.Fragments.VoteListFragment;
import mmd.meetup.Models.DataWrapper;
import mmd.meetup.Models.MeetingPlace;
import mmd.meetup.Models.TimeOption;
import mmd.meetup.R;

import java.util.ArrayList;
import java.util.List;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    final private int TYPE_DIVIDER = 10;
    final private int TYPE_PLACE = 11;
    final private int TYPE_TIME = 12;

    //determines creation of views
    //index 0 is divider, index [index] is divider, < index is places, > index is times;
    private int indexMiddleDivider;

    private List<DataWrapper> mOptions;
    private final VoteListFragment.OnVoteSelectedListener mListener;

    public VoteAdapter(List<DataWrapper> list, VoteListFragment.OnVoteSelectedListener listener, int index) {
        mListener = listener;
        mOptions = list;
        indexMiddleDivider = index;
    }

    public List<DataWrapper> getFullList() {
        if (mOptions == null) {
            mOptions = new ArrayList<>();
        }
        return mOptions;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == indexMiddleDivider) {
            return TYPE_DIVIDER;
        } else if (position < indexMiddleDivider){
            return TYPE_PLACE;
        } else if (position > indexMiddleDivider) {
            return TYPE_TIME;
        } else {
            Log.e(LOG_TAG, "calling super.getItemViewType(). This is unhandled and should not fire");
            return super.getItemViewType(position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_DIVIDER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_vote_divider, parent, false);
                return new Divider(view);
            case TYPE_PLACE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_vote_place, parent, false);
                return new PlaceViewHolder(view);
            case TYPE_TIME:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_vote_time, parent, false);
                return new TimeViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_vote_divider, parent, false);
                return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.onBind(mOptions.get(position));
    }

    @Override
    public int getItemCount() {
        return mOptions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
        }

        public void onBind(DataWrapper wrap) {

        }
    }

    class TimeViewHolder extends ViewHolder {

        TextView date;
        TextView time;

        TimeViewHolder(View v) {
            super(v);

            date = v.findViewById(R.id.date);
            time = v.findViewById(R.id.time);
        }

        @Override
        public void onBind(DataWrapper wrap) {
            super.onBind(wrap);

            TimeOption to = (TimeOption) wrap.getValue();
            date.setText(to.getDate());
            StringBuilder builder = new StringBuilder();
            builder.append(to.getStartTime()).append(" to ").append(to.getEndTime());
            time.setText(builder);
        }
    }

    class PlaceViewHolder extends ViewHolder {

        TextView title;
        TextView address;

        PlaceViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.title);
            address = v.findViewById(R.id.address);
        }

        @Override
        public void onBind(DataWrapper wrap) {
            super.onBind(wrap);

            MeetingPlace mp = (MeetingPlace) wrap.getValue();

            //ensures it fits on one line
            if (mp.getName().length() > 30) {
                title.setText(mp.getName().substring(0, 30).concat("..."));
            } else {
                title.setText(mp.getName());
            }

            address.setText(mp.getAddress());
        }
    }

    class Divider extends ViewHolder {
        TextView title;

        Divider(View v) {
            super(v);
            title = v.findViewById(R.id.title);
        }

        @Override
        public void onBind(DataWrapper wrap) {
            String s = (String) wrap.getValue();
            title.setText(s);
        }
    }
}
