package com.example.androidfirebase101;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class NewTaskActivity extends AppCompatActivity {
    private EditText mEditTextName;
    private EditText mEditTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        mEditTextName = (EditText) findViewById(R.id.editTextName);
        mEditTextDescription = (EditText) findViewById(R.id.editTextDescription);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference ref = database.getReference();

        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = new Task(UUID.randomUUID().toString(), mEditTextName.getText().toString(), mEditTextDescription.getText().toString(), false);
                ref.child(Task.COLLECTION_NAME).push().setValue(task);
                finish();
            }
        });
        findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
