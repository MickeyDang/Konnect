package mmd.konnect.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import mmd.konnect.Firebase.FirebaseClient;
import mmd.konnect.R;


public class SettingFragment extends Fragment implements BackPressFragment{

    SettingInteractionListener mListener;

    EditText nameField;
    String originalName;
    String currName;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        originalName = FirebaseClient.getInstance().getUser().getName();
        nameField = view.findViewById(R.id.nameField);
        nameField.setHint(originalName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingInteractionListener) {
            mListener = (SettingInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SettingInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onBackPress() {
        currName = nameField.getText().toString();
        if (!currName.equals(originalName) && !currName.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.nav_button_save))
                    .setMessage(getString(R.string.prompt_save))
                    .setPositiveButton(getString(R.string.nav_button_save), ((dialogInterface, i) -> {
                        FirebaseClient.getInstance().changeUserName(currName);
                        originalName = currName;
                    }));

            builder.show();
        }
    }

    public interface SettingInteractionListener{
    }
}
