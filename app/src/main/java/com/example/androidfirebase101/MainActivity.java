package com.example.androidfirebase101;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private FirebaseRecyclerAdapter mAdapter;
    private FloatingActionButton fab;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                startActivity(intent);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        ref = database.getReference();

        RecyclerView tasksView = (RecyclerView) findViewById(R.id.recycler_view);

        tasksView.setHasFixedSize(true);
        tasksView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FirebaseRecyclerAdapter<Task, TaskHolder>(Task.class, R.layout.task_item, TaskHolder.class, ref.child(Task.COLLECTION_NAME)) {

            @Override
            public void onViewRecycled(TaskHolder holder) {
                holder.setOnCheckboxClick(null);
                super.onViewRecycled(holder);
            }

            @Override
            public void populateViewHolder(final TaskHolder taskViewHolder, final Task task, int position) {
                taskViewHolder.setName(task.getName());
                taskViewHolder.setDescription(task.getDescription());
                taskViewHolder.setIsDone(task.isDone());
                taskViewHolder.setOnCheckboxClick(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                        final Query query = ref.child(Task.COLLECTION_NAME).orderByChild("id").equalTo(task.getId());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                                String key = nodeDataSnapshot.getKey();
                                String path = "/" + dataSnapshot.getKey() + "/" + key;

                                HashMap<String, Object> result = new HashMap<>();
                                result.put("done", b);
                                ref.child(path).updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                        Snackbar.make(fab, "Saved", Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //
                            }
                        });
                    }
                });
            }
        };
        tasksView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    public static class TaskHolder extends RecyclerView.ViewHolder {
        View mView;

        public TaskHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView field = (TextView) mView.findViewById(R.id.textViewName);
            field.setText(name);
        }

        public void setDescription(String description) {
            TextView field = (TextView) mView.findViewById(R.id.textViewDescription);
            field.setText(description);
        }

        public void setOnCheckboxClick(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
            CheckBox field = (CheckBox) mView.findViewById(R.id.checkBoxIsDone);
            field.setOnCheckedChangeListener(onCheckedChangeListener);
        }

        public void setIsDone(@NonNull Boolean isDone) {
            CheckBox field = (CheckBox) mView.findViewById(R.id.checkBoxIsDone);
            field.setChecked(isDone);
        }
    }
}
