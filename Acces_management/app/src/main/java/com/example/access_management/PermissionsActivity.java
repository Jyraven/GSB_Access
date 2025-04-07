package com.example.access_management;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.access_management.adapters.PermissionAdapter;
import com.example.access_management.models.Permission;

import java.util.ArrayList;
import java.util.List;

public class PermissionsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Permission> permissionList;
    PermissionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        // 🔙 Ajout de la flèche de retour
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mes permissions");
        }

        recyclerView = findViewById(R.id.permissionsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        permissionList = new ArrayList<>();

        // ➕ Exemple de données (à remplacer par données venant du backend plus tard)
        permissionList.add(new Permission("Laboratoire", "Temporaire", "01/04 - 03/04"));
        permissionList.add(new Permission("Salle des serveurs", "Permanent", "-"));
        permissionList.add(new Permission("Archives", "Temporaire", "05/04 - 06/04"));

        adapter = new PermissionAdapter(permissionList);
        recyclerView.setAdapter(adapter);
    }

    // 🔙 Comportement de la flèche retour
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}