package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.todoapp.adaptors.BottomSheetDialog;
import com.example.todoapp.adaptors.TodoRecyclerViewAdapter;
import com.example.todoapp.models.Todo;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {


    RecyclerView todoRecyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RealmResults<Todo> todoList;

    TextView noDataTextView;

    BottomAppBar mBoottoAppBar;
    BottomSheetDialog bottomSheetDialog;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoRecyclerView = findViewById(R.id.sepatu_list);
        noDataTextView = findViewById(R.id.noData);
        mBoottoAppBar = findViewById(R.id.bottomAppBar);
        floatingActionButton = findViewById(R.id.bottomFABAppBar);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
//        realm.close();
//        Realm.deleteRealm(realm.getConfiguration());
        todoList = realm.where(Todo.class).findAll();

        noDataCheck();

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PackageInfo.REQUESTED_PERMISSION_NEVER_FOR_LOCATION);

        bottomSheetDialog = new BottomSheetDialog();

        mBoottoAppBar.setNavigationOnClickListener(view -> {

            bottomSheetDialog.show(getSupportFragmentManager(), "Tag");


        });

        floatingActionButton.setOnClickListener(view -> {
            refreshData();
        });


        layoutManager = new LinearLayoutManager(MainActivity.this);
        todoRecyclerView.setLayoutManager(layoutManager);


        adapter = new TodoRecyclerViewAdapter(todoList, MainActivity.this);
        todoRecyclerView.setAdapter(adapter);
//        savedInstanceState.getBoolean("isAdmin");

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(todoRecyclerView);


        todoList.addChangeListener(notes -> {
            noDataCheck();

            adapter.notifyDataSetChanged();
        });
    }

    void noDataCheck() {
        if (todoList.isEmpty()) {
            noDataTextView.setText("No Data");
        } else {
            noDataTextView.setText("");

        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return (getIntent().getExtras().getBoolean("isAdmin") != true) ? 0 : super.getSwipeDirs(recyclerView, viewHolder);
//            return super.getSwipeDirs(recyclerView, viewHolder);
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Todo todoDeleted;
            Todo todoUpdated;

            switch (direction) {
                case ItemTouchHelper.LEFT:

                    todoDeleted = todoList.get(position);

                    Realm realm = Realm.getDefaultInstance();

                    realm.beginTransaction();
                    todoDeleted.deleteFromRealm();
                    realm.commitTransaction();

                    Snackbar.make(todoRecyclerView, "Deleted", Snackbar.LENGTH_LONG).show();

                    adapter.notifyDataSetChanged();

                    break;

                case ItemTouchHelper.RIGHT:

                    todoUpdated = todoList.get(position);

                    Bundle bundle = new Bundle();

                    bundle.putBoolean("isUpdate", true);
                    bundle.putString("id", todoUpdated.getID());

                    Intent intent = new Intent(todoRecyclerView.getContext(), UpdateActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    adapter.notifyDataSetChanged();

                    break;

            }

            noDataCheck();


        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(MainActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.purple_500))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.teal_200))
                    .addSwipeLeftActionIcon(R.drawable.round_delete_24)
                    .addSwipeRightActionIcon(R.drawable.round_edit_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
        }
    };


    public void refreshData() {
        Snackbar.make(todoRecyclerView, "REFRESH", Snackbar.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());

    }
}