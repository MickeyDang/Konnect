package mmd.konnect.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import mmd.konnect.Adapters.ShortListAdapter;
import mmd.konnect.Firebase.FirebaseClient;
import mmd.konnect.Firebase.FirebaseDB;
import mmd.konnect.Models.User;
import mmd.konnect.R;


public class ShortListFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private ShortListAdapter mAdapter;
    private FloatingActionButton addShortlist;

    public ShortListFragment() {
    }

    @SuppressWarnings("unused")
    public static ShortListFragment newInstance() {
        ShortListFragment fragment = new ShortListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shortlist, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.list);
        addShortlist = view.findViewById(R.id.addShortlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ShortListAdapter(mListener);
        mRecyclerView.setAdapter(mAdapter);

        List<String> shortListUids = FirebaseClient.getInstance().getUser().getShortlist();
        startListeningForUsers(shortListUids);

        addShortlist.setOnClickListener(v -> {
            addByEmail();
        });

    }

    private void addByName() {
        final String BTN_MESSAGE = "ADD";
        final String TITLE = "Add By Name";

        makeAlert(BTN_MESSAGE, TITLE, addedUser -> {
            User currentUser = FirebaseClient.getInstance().getUser();
            boolean addSuccess = currentUser.addToShortList(addedUser.getName());
            if (addSuccess) {
                FirebaseClient.getInstance().updateShortlist(currentUser);
                mAdapter.onInsert(addedUser);
            }

        }, false);
    }

    private void searchByName() {
        //todo
    }

    private void addByEmail() {
        final String BTN_MESSAGE = "ADD";
        final String TITLE = "Add By Name";

        makeAlert(BTN_MESSAGE, TITLE, addedUser -> {
            User currentUser = FirebaseClient.getInstance().getUser();
            boolean addSuccess = currentUser.addToShortList(addedUser.getId());
            if (addSuccess) {
                FirebaseClient.getInstance().updateShortlist(currentUser);
                mAdapter.onInsert(addedUser);
            }

        }, true);
    }

    private void searchByEmail(String email, FirebaseClient.Callback<User> callback) {
        FirebaseClient.getInstance().findUserByEmail(email, callback);
    }

    private void makeAlert(String btnMessage, String title, FirebaseClient.Callback<User> callback, boolean searchByEmail) {
        final View v = View.inflate(getContext(), R.layout.view_find_user_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(v)
                .setTitle(title)
                .setPositiveButton(btnMessage, (DialogInterface dialogInterface, int i) -> {
                    EditText editText = v.findViewById(R.id.idField);

                    if (searchByEmail)
                        searchByEmail(editText.getText().toString(), callback);
                    else
                        searchByName();

                });
        builder.show();
    }

    private void startListeningForUsers(List<String> list) {
        for (String s : list) {
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseDB.Users.path)
                    .child(s)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            mAdapter.onInsert(user);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
    }
}
