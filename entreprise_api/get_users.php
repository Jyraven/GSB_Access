<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

require_once "config/db.php";

$role = $_POST['role'] ?? '';
$valeur = $_POST['valeur'] ?? '';

if (empty($role) || empty($valeur)) {
    echo json_encode(["error" => "Champs manquants"]);
    exit;
}

try {
    if ($role === 'Salarié') {
        echo json_encode([]);
        exit;
    }

    // 🛠️ Requête avec groupe ajouté
    $query = "
        SELECT 
            u.id_utilisateur,
            u.nom,
            u.prenom,
            u.login,
            u.groupe,
            r.nom_role,
            n.valeur
        FROM UTILISATEUR u
        INNER JOIN ROLE r ON u.id_role = r.id_role
        INNER JOIN NIVEAU_ACCES n ON u.id_niveau_acces = n.id_niveau
    ";

    if ($role === 'Manager') {
        $query .= " WHERE r.nom_role IN ('Salarié', 'Manager')";
    } else if ($role === 'Co direction') {
        $query .= " WHERE n.valeur < ?";
    } else if ($role === 'PDG') {
        $query .= " WHERE r.nom_role != 'Administrateur'";
    } else if ($role === 'Administrateur') {
        $query .= " WHERE n.valeur <= ?";
    }

    $query .= " ORDER BY r.niveau_hierarchique ASC, u.nom ASC";

    $stmt = $pdo->prepare($query);

    if (in_array($role, ['Co direction', 'Administrateur'])) {
        $stmt->execute([$valeur]);
    } else {
        $stmt->execute();
    }

    $users = $stmt->fetchAll(PDO::FETCH_ASSOC);
    echo json_encode($users);

} catch (Exception $e) {
    echo json_encode(["error" => "Erreur serveur"]);
}