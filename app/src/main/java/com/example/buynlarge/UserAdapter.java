package com.example.buynlarge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder>{
    private List<User> users = new ArrayList<>();

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User currentUser = users.get(position);
        holder.usernameTextView.setText(currentUser.getUsername());
        holder.passwordTextView.setText(currentUser.getPassword());
        holder.userIdTextView.setText(String.valueOf(currentUser.getUserId()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users){
        this.users = users;
        notifyDataSetChanged();
    }

    class UserHolder extends RecyclerView.ViewHolder{
        private TextView userIdTextView;
        private TextView usernameTextView;
        private TextView passwordTextView;

        public UserHolder(View itemView){
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userId_textView);
            usernameTextView = itemView.findViewById(R.id.username_textView);
            passwordTextView = itemView.findViewById(R.id.password_textView);

        }
    }
}
