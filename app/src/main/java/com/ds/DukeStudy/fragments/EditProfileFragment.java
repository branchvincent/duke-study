package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Displays and retrieves profile information saved in database.

public class EditProfileFragment extends Fragment implements View.OnClickListener {

    // Fields

//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth auth;
    private OnFragmentInteractionListener mListener;
    View EditProfileView;
    Button SubmitProfileButton;
    EditText userNameText;
    EditText userEmailText;
    EditText userMajorText;
    EditText userYearText;

    // Methods

    public EditProfileFragment() {} // Required

    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    //Displays profile information and set listeners for edit button
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EditProfileView =  inflater.inflate(R.layout.fragment_edit_profile, container, false);
        SubmitProfileButton = (Button)EditProfileView.findViewById(R.id.submitProfileButton);
        userNameText =  (EditText)EditProfileView.findViewById(R.id.userNameEdit);
        // userName = userNameText.getText().toString();
        userEmailText = (EditText)EditProfileView.findViewById(R.id.userEmailEdit);
        userMajorText = (EditText)EditProfileView.findViewById(R.id.userMajorEdit);
        userYearText = (EditText)EditProfileView.findViewById(R.id.userYearEdit);

        SubmitProfileButton.setOnClickListener(this);
        return EditProfileView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Upon clicking the submit button, new details are updated in the database, for the user UID
    @Override
    public void onClick(View v) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        //database.child("note").push().setValue(usernameEdit.getText().toString());
        String userName = userNameText.getText().toString();
        String userEmail = userEmailText.getText().toString();
        String userMajor = userMajorText.getText().toString();
        String userYear = userYearText.getText().toString();
        database.child("students").child(auth.getCurrentUser().getUid()).setValue( new Student(userName,userEmail,userMajor,userYear));

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = ProfileFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int tag,String userName);
    }
}