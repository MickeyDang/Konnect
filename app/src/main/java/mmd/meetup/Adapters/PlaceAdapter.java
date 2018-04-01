package mmd.meetup.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mmd.meetup.Models.MeetingPlace;
import mmd.meetup.R;

/**
 * Created by mickeydang on 2018-04-01.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private List<MeetingPlace> mMeetingPlaces;

    public PlaceAdapter() {
        mMeetingPlaces = new ArrayList<>();
    }

    public void insertMeetingPlace(MeetingPlace mp) {
        mMeetingPlaces.add(mp);
        notifyItemInserted(mMeetingPlaces.size() - 1);
    }

    public List<MeetingPlace> getDataSet() {
        if (mMeetingPlaces == null) {
            mMeetingPlaces = new ArrayList<>();
        }
        return mMeetingPlaces;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.onBind(mMeetingPlaces.get(position));

    }

    @Override
    public int getItemCount() {
        return mMeetingPlaces.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView address;

        ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.nameView);
            address = view.findViewById(R.id.addressView);
        }

        void onBind(MeetingPlace place) {
            name.setText(place.getName());
            address.setText(place.getAddress());
        }
    }
}
