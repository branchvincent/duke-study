package com.ds.DukeStudy.misc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Event;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GroupViewHolder extends RecyclerView.ViewHolder {

    // Fields

    private static final String TAG = "GroupViewHolder";
    public TextView titleView, bodyView;
    public ToggleButton toggleBtn;

    // Methods

    public GroupViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.course_title);
        bodyView = (TextView) itemView.findViewById(R.id.course_body);
        toggleBtn = (ToggleButton) itemView.findViewById(R.id.toggle_btn);
    }

    public void bindToGroup(Group group, Boolean checked) {
        Log.i(TAG, "Binding group");
        titleView.setText(group.getName());
        bodyView.setText("Members: " + group.getStudentKeys().size());
        toggleBtn.setChecked(checked);
    }
}
