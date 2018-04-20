package mmd.meetup.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mmd.meetup.Fragments.MeetingInviteFragment;
import mmd.meetup.Models.User;
import mmd.meetup.R;

public class InvitedUserAdapter extends RecyclerView.Adapter<InvitedUserAdapter.ViewHolder> {

    MeetingInviteFragment.ShareIntentHandler handler;
    private List<User> mUsers;

    public InvitedUserAdapter(MeetingInviteFragment.ShareIntentHandler handler) {
        this.handler = handler;
        mUsers = new ArrayList<>();
    }

    public void insertUser(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    public List<User> getUserList() {
        if (mUsers == null) {
            mUsers = new ArrayList<>();
        }
        return mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invited_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;

        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            email = view.findViewById(R.id.email);
        }

        public void onBind(User user) {
            name.setText(user.getName());
            email.setText(user.getEmail());
        }
    }

}
