<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

include 'conn.php';
header('Content-Type: application/json');

$response = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $name = $_POST['name'] ?? '';
    $image_url = $_POST['image_url'] ?? '';
    $short_desc = $_POST['short_desc'] ?? '';
    $long_desc = $_POST['long_desc'] ?? '';
    $category = $_POST['category'] ?? '';

    if ($name && $image_url && $short_desc && $long_desc && $category) {
        $stmt = $conn->prepare("INSERT INTO healing_locations (name, image_url, short_desc, long_desc, category) VALUES (?, ?, ?, ?, ?)");
        $stmt->bind_param("sssss", $name, $image_url, $short_desc, $long_desc, $category);
        $success = $stmt->execute();

        $response['status'] = $success;
        $response['message'] = $success ? 'Lokasi berhasil ditambahkan' : 'Gagal menambahkan data';
    } else {
        $response['status'] = false;
        $response['message'] = 'Semua field wajib diisi';
    }
} else {
    $response['status'] = false;
    $response['message'] = 'Metode tidak diizinkan';
}

echo json_encode($response);
