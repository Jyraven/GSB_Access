package com.example.access_management.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.access_management.R;
import com.example.access_management.models.Permission;

import java.util.List;

public class PermissionAdapter extends RecyclerView.Adapter<PermissionAdapter.ViewHolder> {

    private List<Permission> permissionList;

    public PermissionAdapter(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_permission, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Permission permission = permissionList.get(position);
        holder.zoneName.setText("Zone : " + permission.getZone());
        holder.permissionType.setText("Type : " + permission.getType());
        holder.dateRange.setText(permission.getDateRange());
    }

    @Override
    public int getItemCount() {
        return permissionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView zoneName, permissionType, dateRange;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            zoneName = itemView.findViewById(R.id.zoneName);
            permissionType = itemView.findViewById(R.id.permissionType);
            dateRange = itemView.findViewById(R.id.dateRange);
        }
    }
}