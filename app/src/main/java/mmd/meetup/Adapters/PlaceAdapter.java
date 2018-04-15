package mmd.meetup.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.util.ArrayList;
import java.util.List;

import mmd.meetup.Models.MeetingPlace;
import mmd.meetup.R;
import mmd.meetup.Utils;

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
        holder.onBind(mMeetingPlaces.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mMeetingPlaces.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        MaterialLetterIcon icon;
        TextView name;
        TextView address;
        ImageButton deleteButton;
        int position;

        ViewHolder(View view) {
            super(view);

            icon = view.findViewById(R.id.letter_icon);
            name = view.findViewById(R.id.nameView);
            address = view.findViewById(R.id.addressView);
            deleteButton = view.findViewById(R.id.deleteButton);
        }

        void onBind(MeetingPlace place, int position) {
            this.position = position;

            name.setText(place.getName());
            address.setText(place.getAddress());

            if (place.getName() != null) {
                this.icon.setLetter(String.valueOf(place.getName().charAt(0)));
                this.icon.setShapeColor(Utils.getRandomMaterialColors(place.getName()));
            }

            deleteButton.setOnClickListener(view -> {
                mMeetingPlaces.remove(position);
                notifyItemRemoved(position);
            });
        }
    }
}
