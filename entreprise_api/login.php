<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

require_once "config/db.php";

$login = $_POST['email'] ?? '';
$password = $_POST['password'] ?? '';

if (empty($login) || empty($password)) {
    echo json_encode(["error" => "Champs manquants"]);
    exit;
}

try {
    $stmt = $pdo->prepare("
        SELECT 
            u.id_utilisateur,
            u.nom,
            u.prenom,
            u.login,
            u.mot_de_passe,
            r.nom_role,
            n.niveau_acces,
            n.valeur,
            u.groupe
        FROM UTILISATEUR u
        INNER JOIN ROLE r ON u.id_role = r.id_role
        INNER JOIN NIVEAU_ACCES n ON u.id_niveau_acces = n.id_niveau
        WHERE u.login = ?
    ");
    $stmt->execute([$login]);
    $user = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($user && $password === $user['mot_de_passe']) {
        unset($user['mot_de_passe']);
        echo json_encode($user);
    } else {
        echo json_encode(["error" => "Identifiants incorrects"]);
    }

} catch (Exception $e) {
    echo json_encode(["error" => "Erreur serveur"]);
}
