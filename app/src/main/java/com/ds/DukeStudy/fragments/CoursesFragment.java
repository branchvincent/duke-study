package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Course;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//  Use tab_layout.xml to show three tabs in Groups ???

public class CoursesFragment extends Fragment {

//  Fields

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;
    private DatabaseReference databaseRef;
    private FirebaseListAdapter<String> adapterPost;
    private FirebaseListAdapter<String> adapterGroup;
    private FirebaseListAdapter<String> adapterMember;
    private OnFragmentInteractionListener mListener;
    private String courseID;
    private Boolean isCourse=Boolean.TRUE;
//  Methods

    public CoursesFragment() {} // required

    public static CoursesFragment newInstance(String param1, String param2) {
        CoursesFragment fragment = new CoursesFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //  Inflate the layout for this fragment
        Bundle mybundle=getArguments();
        this.courseID=mybundle.getString("myid");
        View x =  inflater.inflate(R.layout.tab_layout,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
    //  Set adapter for view pager
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager(), this.courseID,this.isCourse));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        //Get the course name and change the title view
        databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference curGroupRef=databaseRef.child("courses").child(this.courseID);
        curGroupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Course curCourse = dataSnapshot.getValue(Course.class);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(curCourse.getDepartment()+curCourse.getCode()+":"+curCourse.getTitle());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return x;
    }


    class MyAdapter extends FragmentPagerAdapter {
            String myid;
            Boolean isCourse;
            public MyAdapter(FragmentManager fm, String myid, Boolean isCourse) {
                super(fm);
                this.myid=myid;
                this.isCourse=isCourse;
            }

            //  Return fragment with respect to Position

            @Override
            public Fragment getItem(int position) {
                Bundle myBundle=new Bundle();
                switch (position) {
                    case 0:
                        PostsFragment pf=new PostsFragment();
                        myBundle.putString("myid", myid);
                        myBundle.putBoolean("isCourse", this.isCourse);
                        pf.setArguments(myBundle);
                        return pf;
                    case 1:
                        GroupsListFragment gf=new GroupsListFragment();
                        myBundle.putString("myid", myid);
                        myBundle.putBoolean("isCourse", this.isCourse);
                        gf.setArguments(myBundle);
                        return gf;
                    case 2:
                        MembersFragment mf=new MembersFragment();
                        myBundle.putString("myid", myid);
                        myBundle.putBoolean("isCourse", this.isCourse);
                        mf.setArguments(myBundle);
                        return mf;
                }
                return null;
            }

        @Override
        public int getCount() {
            return int_items;
        }

    //  Returns title of tab

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Posts";
                case 1: return "Groups";
                case 2: return "Members";
            }
            return null;
        }
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

    /* Interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * See http://developer.android.com/training/basics/fragments/communicating.html
     */
    public interface OnFragmentInteractionListener {
    //  TODO: Update argument type and name
        void onFragmentInteraction(int tag,int view);
    }
}
