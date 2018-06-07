package mmd.konnect.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import mmd.konnect.Fragments.VoteListFragment;
import mmd.konnect.Models.DataWrapper;
import mmd.konnect.Models.MeetingPlace;
import mmd.konnect.Models.TimeOption;
import mmd.konnect.R;

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

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == indexMiddleDivider) {
            return TYPE_DIVIDER;
        } else if (position < indexMiddleDivider) {
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
                return new Divider(view);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (holder instanceof SelectableViewHolder) {
            ((SelectableViewHolder) holder).onBind(mOptions.get(position), position);
        } else if (holder instanceof Divider) {
            ((Divider) holder).onBind(mOptions.get(position), position);
        }

    }

    @Override
    public int getItemCount() {
        return mOptions.size();
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
        }

        abstract public void onBind(DataWrapper wrap, int pos);
    }

    abstract class SelectableViewHolder extends ViewHolder {
        ToggleButton mToggleButton;
        View v;
        DataWrapper wrap;
        boolean isSelected;
        int position;

        SelectableViewHolder(View v) {
            super(v);
            this.v = v;
            mToggleButton = v.findViewById(R.id.toggleButton);
            isSelected = false;
        }

        @Override
        public void onBind(DataWrapper wrap, int position) {
            this.position = position;
            this.wrap = wrap;

            v.setOnClickListener(view -> {
                isSelected = !isSelected;
                mToggleButton.setChecked(!mToggleButton.isChecked());
                toggleMode();
            });

        }

        abstract void toggleMode();
    }

    class TimeViewHolder extends SelectableViewHolder {

        TextView date;
        TextView time;

        TimeViewHolder(View v) {
            super(v);

            date = v.findViewById(R.id.date);
            time = v.findViewById(R.id.time);
        }

        @Override
        public void onBind(DataWrapper wrap, int position) {
            super.onBind(wrap, position);

            TimeOption to = (TimeOption) wrap.getValue();
            date.setText(to.getDate());
            StringBuilder builder = new StringBuilder();
            builder.append(to.getStartTime()).append(" to ").append(to.getEndTime());
            time.setText(builder);
        }

        @Override
        protected void toggleMode() {
            if (isSelected) {
                mListener.onTimeAdded((TimeOption) wrap.getValue(), position - indexMiddleDivider - 1);
            } else {
                mListener.onTimeRemoved((TimeOption) wrap.getValue(), position - indexMiddleDivider - 1);
            }
        }
    }

    class PlaceViewHolder extends SelectableViewHolder {

        TextView title;
        TextView address;

        PlaceViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.title);
            address = v.findViewById(R.id.address);
        }

        @Override
        public void onBind(DataWrapper wrap, int position) {
            super.onBind(wrap, position);

            MeetingPlace mp = (MeetingPlace) wrap.getValue();

            //ensures it fits on one line
            String formattedText;

            formattedText = mp.getName().length() > 30 ? mp.getName().substring(0, 30).concat("...") : mp.getName();
            title.setText(formattedText);

            formattedText = mp.getAddress().length() > 40 ? mp.getAddress().substring(0, 40).concat("...") : mp.getAddress();
            address.setText(formattedText);
        }

        @Override
        protected void toggleMode() {
            if (isSelected) {
                mListener.onPlaceAdded((MeetingPlace) wrap.getValue(), position - 1);
            } else {
                mListener.onPlaceRemoved((MeetingPlace) wrap.getValue(), position - 1);
            }
        }
    }

    class Divider extends ViewHolder {
        TextView title;
        View dividerLine;
        int position;

        Divider(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            dividerLine = v.findViewById(R.id.view);
        }

        @Override
        public void onBind(DataWrapper wrap, int position) {
            this.position = position;
            String s = (String) wrap.getValue();
            title.setText(s);

            if (position == 0) {
                dividerLine.setVisibility(View.INVISIBLE);
            }
        }
    }
}
