package com.example.access_management;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton;

    private final String LOGIN_URL = "http://10.0.2.2/Github/GSB_Access/entreprise_api/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);

                        if (json.has("error")) {
                            Toast.makeText(this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
                        } else {

                            //  Récupère les infos
                            String nom = json.getString("nom");
                            String prenom = json.getString("prenom");
                            String role = json.getString("nom_role");
                            String niveau = json.getString("niveau_acces"); // libellé texte
                            String groupe = json.getString("groupe");
                            String valeur = json.getString("valeur");        // valeur numérique d’accès

                            android.util.Log.d("DEBUG_LOGIN", "valeur reçue = " + valeur);


                            //  Envoie les données à QRActivity
                            Intent intent = new Intent(this, QRActivity.class);
                            intent.putExtra("nom", nom);
                            intent.putExtra("prenom", prenom);
                            intent.putExtra("role", role);
                            intent.putExtra("niveau", niveau);
                            intent.putExtra("valeur", valeur);
                            intent.putExtra("groupe", groupe);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erreur JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Erreur réseau : " + error.toString(), Toast.LENGTH_LONG).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        queue.add(postRequest);
    }
}