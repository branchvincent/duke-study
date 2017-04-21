package com.ds.DukeStudy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.misc.GroupViewHolder;
import com.ds.DukeStudy.misc.StudentViewHolder;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Student;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MembersListFragment extends Fragment {

    // Fields

    private static final String TAG = "MembersListFragment";
    private static final String PATH_ARG = "dbPath";

    private String dbPath;
    private FirebaseListAdapter<String> listAdapter;
    private ListView listView;

    private Student student;
    private DatabaseReference dbRef;
    private FirebaseRecyclerAdapter<Student,StudentViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    // Constructors

    public MembersListFragment() {}

    public static MembersListFragment newInstance(String dpPath) {
        MembersListFragment fragment = new MembersListFragment();
        Bundle args = new Bundle();
        args.putString(PATH_ARG, dpPath);
        fragment.setArguments(args);
        return fragment;
    }

    // Other methods

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_members_list, null);

        // Get arguments
        dbPath = getArguments().getString(PATH_ARG);
        if (dbPath == null) {
            throw new IllegalArgumentException("Must pass " + PATH_ARG);
        }

        // Get view items
        Log.i(TAG, "Getting students from " + dbPath);
        dbRef = Database.ref.child(dbPath);
        student = ((MainActivity)getActivity()).getStudent();
        mRecycler = (RecyclerView) view.findViewById(R.id.students_list);
        mRecycler.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Log.i(TAG, "Creating group list from " + dbPath);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
//        mManager.setReverseLayout(true);
//        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Log.i(TAG, "Querying students from " + dbPath);
        Query studentsQuery = dbRef.limitToFirst(100);
//        Query eventsQuery = dbRef.orderByChild("department");
//        mAdapter = new FirebaseRecyclerAdapter<Student,StudentViewHolder>(Student.class, R.layout.item_course, StudentViewHolder.class, studentsQuery) {
//            @Override
//            protected void populateViewHolder(final StudentViewHolder viewHolder, final Student student, final int position) {
//                final String key = getRef(position).getKey();
//
//                // Set listener for view holder
////                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
//////                        Context context = getActivity().getApplicationContext();
//////                        getActivity().getSupportFragmentManager().beginTransaction()
//////                                .replace(R.id.flContent, new ViewProfileFragment().newInstance(studentKey), null)
//////                                .addToBackStack(null)
//////                                .commit();
////                        }
////                });
//
//                // Bind view to student
//                viewHolder.bindToStudent(student);
//            }
//        };
//        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        View view = inflater.inflate(R.layout.fragment_members_list, null);
//
//        // Get arguments
//        dbPath = getArguments().getString(PATH_ARG);
//        if (dbPath == null) {
//            throw new IllegalArgumentException("Must pass " + PATH_ARG);
//        }
//
//        // Get view items
//        listView = (ListView) view.findViewById(R.id.memberList);
//        DatabaseReference studentKeysRef = Database.ref.child(dbPath).child("studentKeys");
//
//        // Create adapter to list all members
//        listAdapter = new FirebaseListAdapter<String>(getActivity(), String.class, R.layout.general_row_view, studentKeysRef) {
//            @Override
//            protected void populateView(final View view, final String studentKey,final int position) {
//
//                // Get view items
//                final TextView memberName = (TextView) view.findViewById(R.id.firstLine);
//                final TextView memberEmail = (TextView) view.findViewById(R.id.secondLine);
//
//                // Get each student
//                Database.ref.child("students").child(studentKey).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.i(TAG, "OnDataChange: studentListener");
//                        final Student student = dataSnapshot.getValue(Student.class);
//
//                        // Set name and email
//                        memberName.setText(student.getName());
//                        memberEmail.setText(student.getEmail());
//
//                        // Send to profile on click
//                        view.setOnClickListener(new View.OnClickListener(){
//                            @Override
//                            public void onClick(View view) {
//                                Context context = getActivity().getApplicationContext();
//                                getActivity().getSupportFragmentManager().beginTransaction()
//                                        .replace(R.id.flContent, new ViewProfileFragment().newInstance(studentKey), null)
//                                        .addToBackStack(null)
//                                        .commit();
//                            }
//                        });
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w(TAG, "OnDataChangeCancelled: studentListener", databaseError.toException());
//                    }
//                });
//            }
//        };
//        listView.setAdapter(listAdapter);
//        return view;
//    }
}
