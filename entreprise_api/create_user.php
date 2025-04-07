<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

require_once "config/db.php";

// Récupération des données envoyées
$nom = $_POST['nom'] ?? '';
$prenom = $_POST['prenom'] ?? '';
$email = $_POST['email'] ?? '';
$password = $_POST['password'] ?? '';
$role = $_POST['role'] ?? '';
$niveau = $_POST['niveau'] ?? '';
$groupe = $_POST['groupe'] ?? 'Aucun';

// Validation simple
if (empty($nom) || empty($prenom) || empty($email) || empty($password) || empty($role) || empty($niveau)) {
    echo json_encode(["error" => "Champs manquants"]);
    exit;
}

try {
    // Vérifie si l'email existe déjà
    $stmt = $pdo->prepare("SELECT COUNT(*) FROM UTILISATEUR WHERE login = ?");
    $stmt->execute([$email]);
    if ($stmt->fetchColumn() > 0) {
        echo json_encode(["error" => "Email déjà utilisé"]);
        exit;
    }

    // Récupère l'id du rôle
    $stmt = $pdo->prepare("SELECT id_role FROM ROLE WHERE nom_role = ?");
    $stmt->execute([$role]);
    $id_role = $stmt->fetchColumn();

    if (!$id_role) {
        echo json_encode(["error" => "Rôle inconnu"]);
        exit;
    }

    // Insertion
    $stmt = $pdo->prepare("
        INSERT INTO UTILISATEUR (nom, prenom, login, mot_de_passe, qr_code_unique, id_role, id_niveau_acces, groupe)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    ");
    $qr_code = uniqid("QR_");
    $stmt->execute([$nom, $prenom, $email, $password, $qr_code, $id_role, $niveau, $groupe]);

    echo json_encode(["success" => "Utilisateur créé avec succès"]);
} catch (Exception $e) {
    echo json_encode(["error" => "Erreur serveur"]);
}