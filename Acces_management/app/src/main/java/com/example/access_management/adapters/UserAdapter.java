package com.example.access_management.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.access_management.EditUserActivity;
import com.example.access_management.R;
import com.example.access_management.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList;
    private Context context;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nomPrenom.setText(user.getPrenom() + " " + user.getNom());
        holder.userRole.setText("Rôle : " + user.getRole());

        holder.editUserButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditUserActivity.class);
            intent.putExtra("mode", "edit");
            intent.putExtra("id", String.valueOf(user.getId()));
            intent.putExtra("nom", user.getNom());
            intent.putExtra("prenom", user.getPrenom());
            intent.putExtra("email", user.getEmail());
            intent.putExtra("groupe", user.getGroupe());
            intent.putExtra("role", user.getRole());
            intent.putExtra("niveau", String.valueOf(user.getNiveau()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomPrenom, userRole;
        Button editUserButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomPrenom = itemView.findViewById(R.id.nomPrenom);
            userRole = itemView.findViewById(R.id.userRole);
            editUserButton = itemView.findViewById(R.id.editUserButton);
        }
    }
}
