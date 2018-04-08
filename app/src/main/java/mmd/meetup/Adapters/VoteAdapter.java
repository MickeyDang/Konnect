package mmd.meetup.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mmd.meetup.Fragments.VoteListFragment.OnListFragmentInteractionListener;
import mmd.meetup.Models.DataWrapper;
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
    private final OnListFragmentInteractionListener mListener;

    public VoteAdapter(List<DataWrapper> list, OnListFragmentInteractionListener listener, int index) {
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
        //todo change views!
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vote_divider, parent, false);

        switch (viewType) {
            case TYPE_DIVIDER:
                return new Divider(view);
            case TYPE_PLACE:
                return new PlaceViewHolder(view);
            case TYPE_TIME:
                return new TimeViewHolder(view);
            default:
                return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return mOptions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
        }
    }

    class TimeViewHolder extends ViewHolder {
        TimeViewHolder(View v) {
            super(v);
        }
    }

    class PlaceViewHolder extends ViewHolder {
        PlaceViewHolder(View v) {
            super(v);
        }
    }

    class Divider extends ViewHolder {
        Divider(View v) {
            super(v);
        }
    }
}
