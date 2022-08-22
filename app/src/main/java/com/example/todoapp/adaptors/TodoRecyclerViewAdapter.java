package com.example.todoapp.adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.models.Todo;

import java.io.ByteArrayInputStream;

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
        ByteArrayInputStream imageStream = new ByteArrayInputStream(todo.getImage());
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

        holder.namaTextView.setText(todo.getNama());
        holder.noPhoneTextView.setText(todo.getPhone());
        holder.imageView.setImageBitmap(bitmap);
        holder.alamatTextView.setText(todo.getAlamat());
        holder.locationTextView.setText(todo.getLocation());
        holder.sexTextView.setText(todo.getSex());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {

        private TextView namaTextView, noPhoneTextView, alamatTextView, locationTextView, sexTextView;
        private ImageView imageView;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);

            namaTextView = itemView.findViewById(R.id.nameTextView);
            noPhoneTextView = itemView.findViewById(R.id.phoneTextVew);
            alamatTextView = itemView.findViewById(R.id.locationTextView);
            locationTextView = itemView.findViewById(R.id.locationDaftarTextView);
            sexTextView = itemView.findViewById(R.id.sexTextView);
            imageView = itemView.findViewById(R.id.photoImagePreview);

        }
    }
}
