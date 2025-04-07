<?php
$host = "localhost";
$db_name = "afip_mobile";
$username = "root";       
$password = "root";              

try {
    $pdo = new PDO("mysql:host=$host;dbname=$db_name;charset=utf8", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die(json_encode([
        "error" => "Connexion échouée : " . $e->getMessage()
    ]));
}
?>