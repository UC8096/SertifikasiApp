package com.example.todoapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.models.Todo;
import java.util.List;

import io.realm.RealmResults;

public class TodoRecyclerViewAdapter extends RecyclerView.Adapter<TodoRecyclerViewAdapter.TodoViewHolder> {
    private RealmResults<Todo> todoList;
    private Context context;


    public TodoRecyclerViewAdapter(RealmResults<Todo> todoList, Context ctx) {
        this.todoList = todoList;
        this.context = ctx;

    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);

        return new TodoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {

        Todo todo = todoList.get(position);

        holder.titleTextView.setText(todo.getTitle());
        holder.descriptionTextView.setText(todo.getDescription());
        holder.locationTextView.setText(todo.getLocation());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView locationTextView;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextVew);
            locationTextView = itemView.findViewById(R.id.locationTextView);
        }
    }
}
