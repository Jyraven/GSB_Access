<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

require_once "config/db.php";

// Récupération des données POST
$id = $_POST['id'] ?? null;
$nom = $_POST['nom'] ?? null;
$prenom = $_POST['prenom'] ?? null;
$email = $_POST['email'] ?? null;
$password = $_POST['password'] ?? null;
$role = $_POST['role'] ?? null;
$niveau = $_POST['niveau'] ?? null;
$groupe = $_POST['groupe'] ?? null;

if (!$id || !$nom || !$prenom || !$email || !$role || !$niveau || !$groupe) {
    echo json_encode(["error" => "Champs manquants"]);
    exit;
}

try {
    // Récupérer les ID du rôle et du niveau
    $stmtRole = $pdo->prepare("SELECT id_role FROM ROLE WHERE nom_role = ?");
    $stmtRole->execute([$role]);
    $id_role = $stmtRole->fetchColumn();

    $stmtNiveau = $pdo->prepare("SELECT id_niveau FROM NIVEAU_ACCES WHERE valeur = ?");
    $stmtNiveau->execute([$niveau]);
    $id_niveau = $stmtNiveau->fetchColumn();

    if (!$id_role || !$id_niveau) {
        echo json_encode(["error" => "Rôle ou niveau d'accès invalide"]);
        exit;
    }

    // Mise à jour de l'utilisateur
    if ($password) {
        $stmt = $pdo->prepare("UPDATE UTILISATEUR SET nom = ?, prenom = ?, login = ?, mot_de_passe = ?, id_role = ?, id_niveau_acces = ?, groupe = ? WHERE id_utilisateur = ?");
        $stmt->execute([$nom, $prenom, $email, $password, $id_role, $id_niveau, $groupe, $id]);
    } else {
        $stmt = $pdo->prepare("UPDATE UTILISATEUR SET nom = ?, prenom = ?, login = ?, id_role = ?, id_niveau_acces = ?, groupe = ? WHERE id_utilisateur = ?");
        $stmt->execute([$nom, $prenom, $email, $id_role, $id_niveau, $groupe, $id]);
    }

    echo json_encode(["success" => "Utilisateur mis à jour avec succès"]);

} catch (Exception $e) {
    echo json_encode(["error" => "Erreur serveur"]);
}