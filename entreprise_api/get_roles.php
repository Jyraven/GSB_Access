<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

require_once "config/db.php";

// Récupérer le rôle et la valeur de l'utilisateur actuel depuis la requête POST
$currentRole = $_POST['currentRole'] ?? '';
$currentValeur = $_POST['currentValeur'] ?? '';

if (empty($currentRole) || empty($currentValeur)) {
    echo json_encode(["error" => "Champs manquants"]);
    exit;
}

try {
    // Préparer la requête pour récupérer tous les rôles
    $query = "SELECT nom_role, niveau_hierarchique FROM ROLE ORDER BY niveau_hierarchique ASC";
    $stmt = $pdo->prepare($query);
    $stmt->execute();
    $roles = $stmt->fetchAll(PDO::FETCH_ASSOC);

    // Filtrer les rôles en fonction du rôle et du niveau de l'utilisateur actuel
    $allowedRoles = [];
    foreach ($roles as $role) {
        switch ($currentRole) {
            case 'Manager':
                if ($role['nom_role'] === 'Salarié') {
                    $allowedRoles[] = $role['nom_role'];
                }
                break;
            case 'Co direction':
                if (in_array($role['nom_role'], ['Salarié', 'Manager'])) {
                    $allowedRoles[] = $role['nom_role'];
                }
                break;
            case 'PDG':
            case 'Administrateur':
                $allowedRoles[] = $role['nom_role'];
                break;
        }
    }

    echo json_encode($allowedRoles);
} catch (Exception $e) {
    echo json_encode(["error" => "Erreur serveur"]);
}
?>