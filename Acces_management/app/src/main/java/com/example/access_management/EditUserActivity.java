package com.example.access_management;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {

    EditText nomEditText, prenomEditText, emailEditText, passwordEditText, groupeEditText;
    Spinner roleSpinner, niveauSpinner;
    Button validateButton;

    String currentRole;
    int currentValeur;
    List<String> allowedRoles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Créer un utilisateur");
        }

        currentRole = getIntent().getStringExtra("role");
        currentValeur = getIntent().hasExtra("valeur") ? Integer.parseInt(getIntent().getStringExtra("valeur")) : 7;

        nomEditText = findViewById(R.id.nomEditText);
        prenomEditText = findViewById(R.id.prenomEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        groupeEditText = findViewById(R.id.groupeEditText);
        roleSpinner = findViewById(R.id.roleSpinner);
        niveauSpinner = findViewById(R.id.niveauSpinner);
        validateButton = findViewById(R.id.validateButton);

        fetchRoles();

        // Pré-remplir les champs texte si en mode "edit"
        String mode = getIntent().getStringExtra("mode");
        if ("edit".equals(mode)) {
            nomEditText.setText(getIntent().getStringExtra("nom"));
            prenomEditText.setText(getIntent().getStringExtra("prenom"));
            emailEditText.setText(getIntent().getStringExtra("email"));
            groupeEditText.setText(getIntent().getStringExtra("groupe"));
        }

        validateButton.setOnClickListener(v -> {
            String nom = nomEditText.getText().toString();
            String prenom = prenomEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String mdp = passwordEditText.getText().toString();
            String groupe = groupeEditText.getText().toString();

            if (roleSpinner.getSelectedItem() == null || niveauSpinner.getSelectedItem() == null) {
                Toast.makeText(this, "Rôle ou niveau non sélectionné", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedRole = roleSpinner.getSelectedItem().toString();
            String selectedNiveau = niveauSpinner.getSelectedItem().toString();

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || selectedNiveau.isEmpty() || groupe.isEmpty()) {
                Toast.makeText(this, "Remplis tous les champs obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            int niveau = Integer.parseInt(selectedNiveau);
            if (!currentRole.equals("PDG") && !currentRole.equals("Administrateur") && niveau >= currentValeur) {
                Toast.makeText(this, "Niveau d’accès trop élevé", Toast.LENGTH_LONG).show();
                return;
            }

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "edit".equals(mode)
                    ? "http://10.0.2.2/Github/GSB_Access/entreprise_api/update_user.php"
                    : "http://10.0.2.2/Github/GSB_Access/entreprise_api/create_user.php";

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.has("success")) {
                                Toast.makeText(this, json.getString("success"), Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(this, json.getString("error"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Erreur JSON", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        Toast.makeText(this, "Erreur réseau", Toast.LENGTH_SHORT).show();
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("nom", nomEditText.getText().toString());
                    params.put("prenom", prenomEditText.getText().toString());
                    params.put("email", emailEditText.getText().toString());
                    params.put("groupe", groupeEditText.getText().toString());
                    params.put("role", roleSpinner.getSelectedItem().toString());
                    params.put("niveau", niveauSpinner.getSelectedItem().toString());

                    String mdp = passwordEditText.getText().toString();
                    if (mdp.isEmpty() && !"edit".equals(mode)) {
                        Toast.makeText(EditUserActivity.this, "Le mot de passe est obligatoire pour créer un utilisateur", Toast.LENGTH_SHORT).show();
                        return null;
                    } else if (!mdp.isEmpty()) {
                        params.put("password", mdp);
                    }

                    if ("edit".equals(mode)) {
                        params.put("id_utilisateur", getIntent().getStringExtra("id"));
                    }

                    return params;
                }
            };
            queue.add(postRequest);
        });
    }

    private void fetchRoles() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2/Github/GSB_Access/entreprise_api/get_roles.php";

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        if (response.trim().startsWith("{")) {
                            try {
                                JSONObject errorJson = new JSONObject(response);
                                if (errorJson.has("error")) {
                                    Toast.makeText(EditUserActivity.this, errorJson.getString("error"), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        JSONArray roles = new JSONArray(response);

                        allowedRoles.clear();
                        List<String> niveauxPossibles = new ArrayList<>();
                        for (int i = 1; i < currentValeur; i++) {
                            niveauxPossibles.add(String.valueOf(i));
                        }
                        if (currentValeur == 7) niveauxPossibles.add("7");

                        for (int i = 0; i < roles.length(); i++) {
                            JSONObject obj = roles.getJSONObject(i);
                            String nomRole = obj.getString("nom_role");
                            int valeur = obj.getInt("valeur");

                            if (currentRole.equals("Administrateur") || currentRole.equals("PDG") || valeur < currentValeur) {
                                allowedRoles.add(nomRole);
                            } else if (currentRole.equals("Manager") && nomRole.equals("Salarié")) {
                                allowedRoles.add(nomRole);
                            } else if (currentRole.equals("Codirection") && (nomRole.equals("Salarié") || nomRole.equals("Manager"))) {
                                allowedRoles.add(nomRole);
                            }
                        }

                        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allowedRoles);
                        roleSpinner.setAdapter(roleAdapter);

                        ArrayAdapter<String> niveauAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, niveauxPossibles);
                        niveauSpinner.setAdapter(niveauAdapter);

                        if ("edit".equals(getIntent().getStringExtra("mode"))) {
                            String roleUtilisateur = getIntent().getStringExtra("role");
                            String niveauUtilisateur = getIntent().getStringExtra("valeur");

                            roleSpinner.post(() -> {
                                for (int i = 0; i < allowedRoles.size(); i++) {
                                    if (allowedRoles.get(i).equals(roleUtilisateur)) {
                                        roleSpinner.setSelection(i);
                                        break;
                                    }
                                }
                            });

                            niveauSpinner.post(() -> {
                                for (int i = 0; i < niveauSpinner.getCount(); i++) {
                                    if (niveauSpinner.getItemAtPosition(i).toString().equals(niveauUtilisateur)) {
                                        niveauSpinner.setSelection(i);
                                        break;
                                    }
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erreur JSON rôle", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Erreur réseau rôle", Toast.LENGTH_SHORT).show();
                });

        queue.add(getRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
