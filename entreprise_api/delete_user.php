<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

require_once "config/db.php";

// Récupérer l'ID de l'utilisateur à supprimer depuis la requête POST
$userId = $_POST['id_utilisateur'] ?? '';

if (empty($userId)) {
    echo json_encode(["error" => "ID utilisateur manquant"]);
    exit;
}

try {
    // Préparer la requête de suppression
    $query = "DELETE FROM UTILISATEUR WHERE id_utilisateur = :id_utilisateur";
    $stmt = $pdo->prepare($query);
    $stmt->bindParam(':id_utilisateur', $userId, PDO::PARAM_INT);
    $stmt->execute();

    if ($stmt->rowCount() > 0) {
        echo json_encode(["success" => "Utilisateur supprimé avec succès"]);
    } else {
        echo json_encode(["error" => "Aucun utilisateur trouvé avec cet ID"]);
    }
} catch (Exception $e) {
    echo json_encode(["error" => "Erreur serveur"]);
}
?>