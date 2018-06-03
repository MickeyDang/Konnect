package mmd.konnect.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import mmd.konnect.Fragments.ShortListFragment.OnListFragmentInteractionListener;
import mmd.konnect.Models.User;
import mmd.konnect.R;

import java.util.ArrayList;
import java.util.List;


public class ShortListAdapter extends RecyclerView.Adapter<ShortListAdapter.ViewHolder> implements FirebaseAdapter<User>{

    private List<User> shortListedUsers;
    private final OnListFragmentInteractionListener mListener;

    public ShortListAdapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
        shortListedUsers = new ArrayList<>();
    }

    @Override
    public void onInsert(User user) {
        shortListedUsers.add(user);
        notifyItemInserted(shortListedUsers.size() - 1);
    }

    @Override
    public void onUpdate(User user) {

    }

    @Override
    public void onFilter(User user) {
        //not handled
    }

    @Override
    public void onDelete(int index) {
        shortListedUsers.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public List<User> getFullList() {
        return null;
    }

    @Override
    public int containsItem(String s) {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shortlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User user = shortListedUsers.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());

        holder.deleteButton.setOnClickListener(view -> {
            onDelete(position);
            //todo delete in Firebase using listener callback
        });
    }

    @Override
    public int getItemCount() {
        return shortListedUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        int pos;
        TextView name;
        TextView email;
        ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            email = view.findViewById(R.id.email);
            deleteButton = view.findViewById(R.id.deleteButton);
        }

    }
}
