package com.example.access_management;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.access_management.adapters.UserAdapter;
import com.example.access_management.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<User> userList;
    UserAdapter adapter;

    String role, valeur;
    private final String GET_USERS_URL = "http://10.0.2.2/Github/GSB_Access/entreprise_api/get_users.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Liste des utilisateurs");
        }

        // Extras du QRActivity
        Intent intent = getIntent();
        role = intent.getStringExtra("role");
        valeur = intent.getStringExtra("valeur");

        recyclerView = findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        adapter = new UserAdapter(userList);
        recyclerView.setAdapter(adapter);

        loadUsers();

        FloatingActionButton addUserFab = findViewById(R.id.addUserFab);

        if (!peutCreerUtilisateur(role)) {
            addUserFab.hide();
        }

        addUserFab.setOnClickListener(v -> {
            Intent i = new Intent(UserListActivity.this, EditUserActivity.class);
            i.putExtra("role", role);
            i.putExtra("valeur", valeur);
            startActivityForResult(i, 101);
        });
    }

    private void loadUsers() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, GET_USERS_URL,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        userList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            int id = obj.getInt("id_utilisateur");
                            String nom = obj.getString("nom");
                            String prenom = obj.getString("prenom");
                            String email = obj.getString("login");
                            String groupe = obj.getString("groupe");
                            String role = obj.getString("nom_role");
                            int niveau = obj.getInt("valeur");

                            userList.add(new User(id, nom, prenom, email, groupe, role, niveau));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erreur JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.e("USER_LIST", "Erreur réseau : " + error.toString());
                    Toast.makeText(this, "Erreur réseau", Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("role", role);
                params.put("valeur", valeur);
                return params;
            }
        };

        queue.add(postRequest);
    }

    private boolean peutCreerUtilisateur(String role) {
        return role.equals("Manager") ||
                role.equals("Co direction") ||
                role.equals("PDG") ||
                role.equals("Administrateur");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            loadUsers();
        }
    }
}