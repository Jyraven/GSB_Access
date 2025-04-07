package com.example.access_management;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        // Flèche retour + titre
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("QR Code");
        }

        // Données reçues du Login
        Intent intent = getIntent();
        String nom = intent.getStringExtra("nom");
        String prenom = intent.getStringExtra("prenom");
        String role = intent.getStringExtra("role");
        String niveau = intent.getStringExtra("niveau");
        String valeur = intent.getStringExtra("valeur"); // ✅ valeur d'accès numérique
        String groupe = intent.getStringExtra("groupe");

        // Log pour debug
        Log.d("DEBUG_QR", "valeur envoyée à UserListActivity = " + valeur);

        // 👤 Affichage des infos
        TextView userNameText = findViewById(R.id.userNameText);
        TextView groupeText = findViewById(R.id.groupeText);
        userNameText.setText(prenom + " " + nom);
        groupeText.setText("Groupe : " + groupe);

        // Génération du QR code
        String qrData = "Nom:" + nom + ";Prénom:" + prenom + ";Rôle:" + role + ";Groupe:" + groupe;

        ImageView qrImageView = findViewById(R.id.qrImageView);
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrData, BarcodeFormat.QR_CODE, 400, 400);
            qrImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // Bouton vers les autorisations de base
        Button permissionsButton = findViewById(R.id.permissionsButton);
        permissionsButton.setOnClickListener(v -> {
            Intent i = new Intent(QRActivity.this, PermissionsActivity.class);
            startActivity(i);
        });

        // Bouton Liste utilisateurs (visible uniquement si pas Salarié)
        Button usersButton = findViewById(R.id.usersButton);
        usersButton.setOnClickListener(v -> {
            if (role.equals("Salarié")) {
                Toast.makeText(this, "Accès interdit pour les Salariés", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(QRActivity.this, UserListActivity.class);
                i.putExtra("role", role);
                i.putExtra("valeur", valeur); // ✅ bonne valeur envoyée
                startActivity(i);
            }
        });

        // Déconnexion
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            Intent i = new Intent(QRActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}