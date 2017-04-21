package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.DukeStudy.GroupDetailActivity;
import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.NewEventActivity;
import com.ds.DukeStudy.NewGroupActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.misc.EventViewHolder;
import com.ds.DukeStudy.misc.GroupViewHolder;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Event;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Student;
import com.ds.DukeStudy.objects.Util;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//This fragment loads groups lists for a particular course from the database and displays in listview.

public class GroupsListFragment extends Fragment {

    // Fields

    private static final String TAG = "GroupsListFragment";
    private static final String COURSE_KEY_ARG = "courseKey";
    private String courseKey;

    private ListView listView;
    private FirebaseListAdapter<String> listAdapter;
    private Drawable plusIcon, minusIcon;
    private FloatingActionButton newGroupBtn;
    private DatabaseReference groupKeysRef;
    private ValueEventListener groupListener;

    private Student student;
    private DatabaseReference dbRef;
    private FirebaseRecyclerAdapter<Group,GroupViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    // Constructors

    public GroupsListFragment() {}

    public static GroupsListFragment newInstance(String key) {
        GroupsListFragment fragment = new GroupsListFragment();
        Bundle args = new Bundle();
        args.putString(COURSE_KEY_ARG, key);
        fragment.setArguments(args);
        return fragment;
    }

    // Other methods

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_groups_list, container, false);

        // Get arguments
        courseKey = getArguments().getString(COURSE_KEY_ARG);
        if (courseKey == null) {
            throw new IllegalArgumentException("Must pass " + COURSE_KEY_ARG);
        }

        // Get view items
        dbRef = Database.ref.child(Util.GROUP_ROOT).child(courseKey);
        student = ((MainActivity)getActivity()).getStudent();
        mRecycler = (RecyclerView) view.findViewById(R.id.courses_list);
        mRecycler.setHasFixedSize(true);

        // New group button
        newGroupBtn = (FloatingActionButton) view.findViewById(R.id.fab_new_group);
        newGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewGroupActivity.start(getActivity(), courseKey);
            }
        });

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
        Query groupsQuery = dbRef.limitToFirst(100);
//        Query eventsQuery = dbRef.orderByChild("department");
        mAdapter = new FirebaseRecyclerAdapter<Group, GroupViewHolder>(Group.class, R.layout.item_course, GroupViewHolder.class, groupsQuery) {
            @Override
            protected void populateViewHolder(final GroupViewHolder viewHolder, final Group group, final int position) {
                final String key = getRef(position).getKey();

                // Set listener for button
                viewHolder.toggleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        student.toggleAndPut(group);
                        Log.i(TAG, "Toggling " + group.getName() + " at " + group.getKey());
                    }
                });

                // Set listener for view holder
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GroupDetailActivity.start(getActivity(), group.getKey());
                    }
                });

                // Bind view to course
                Boolean member = student.getGroupKeys().contains(group.getKey());
                viewHolder.bindToGroup(group, member);
            }
        };
        mRecycler.setAdapter(mAdapter);
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
//        final View view = inflater.inflate(R.layout.fragment_groups_list, null);
//
//        // Get arguments
//        courseKey = getArguments().getString(COURSE_KEY_ARG);
//        if (courseKey == null) {
//            throw new IllegalArgumentException("Must pass " + COURSE_KEY_ARG);
//        }
//
//        // Get view items
//        listView = (ListView) view.findViewById(R.id.groupList);
//        student = ((MainActivity) getActivity()).getStudent();
//        plusIcon = getResources().getDrawable(R.drawable.ic_menu_addclass);
//        minusIcon = getResources().getDrawable(R.drawable.ic_indeterminate_check_box_black_24dp);
//        groupKeysRef = Database.ref.child("courses").child(courseKey).child("groupKeys");
//
//        // Create adapter to list all groups
//        listAdapter = new FirebaseListAdapter<String>(getActivity(), String.class, R.layout.general_row_view_btn, groupKeysRef) {
//            protected void populateView(final View view, final String groupKey, final int position) {
//
//                // Get view items
//                final DatabaseReference groupRef = Database.ref.child("groups").child(groupKey);
//                final TextView nameText = (TextView) view.findViewById(R.id.firstLine);
//                final TextView countText = (TextView) view.findViewById(R.id.secondLine);
//                final ImageButton toggleBtn = (ImageButton) view.findViewById(R.id.toggleButton);
//
//                // Get group
//                Log.i(TAG, "Populating view for " + groupKey);
//                groupListener = new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.i(TAG, "OnDataChange: groupListener");
//                        final Group group = dataSnapshot.getValue(Group.class);
//
//                        // Set name and count
//                        nameText.setText(group.getName());
//                        countText.setText("Members: " + group.getStudentKeys().size());
//
//                        // Set icon
//                        Boolean isMember = group.getStudentKeys().contains(student.getKey());
//                        toggle(toggleBtn, isMember);
//                        Log.i(TAG, "Group " + group.getName() + ": isMember " + isMember);
//
//                        // Toggle on click
//                        toggleBtn.setOnClickListener(new View.OnClickListener(){
//                            @Override
//                            public void onClick(View view) {
//                                toggle(group);
//                            }
//                        });
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w(TAG, "OnDataChangeCancelled: groupListener", databaseError.toException());
//                    }
//                };
//                groupRef.addValueEventListener(groupListener);
//            }};
//        listView.setAdapter(listAdapter);
//
//        // New post button
//        newGroupBtn = (FloatingActionButton) view.findViewById(R.id.fab_new_group);
//        newGroupBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NewGroupActivity.start(getActivity(), courseKey);
//            }
//        });
//
//        return view;
//    }
//
////    public void updateUi(Student student, Group group, TextView nameText, TextView countText) {
////        nameText.setText(group.getName());
////        countText.setText("Members: " + group.getStudentKeys().size());
////        Boolean isMember = group.getStudentKeys().contains(student.getKey());
////        if (isMember) {
////            toggleBtn.setImageDrawable(minusIcon);
////        } else {
////            toggleBtn.setImageDrawable(plusIcon);
////        }
////    }
//
//    public void toggle(ImageButton button, Boolean isChecked) {
//        if (isChecked) {
//            button.setImageDrawable(minusIcon);
//        } else {
//            button.setImageDrawable(plusIcon);
//        }
//    }
//
//    public void toggle(Group group) {
//        ArrayList<String> groupKeys = student.getGroupKeys();
//        String key = group.getKey();
//        if (groupKeys.contains(key)) {
//            //remove
//            student.removeGroupKey(key);
//            group.removeStudentKey(student.getKey());
//            student.put(); group.put();
//        } else {
//            //add
//            student.addGroupKey(key);
//            group.addStudentKey(student.getKey());
//            student.put(); group.put();
//        }
//    }
//
//    public void onDetach() {
//        super.onDetach();
//    }
}