<?php
include 'conn.php';

header('Content-Type: application/json');

$user_id = $_GET['user_id'] ?? 0;

$response = ['success' => false];

$query = mysqli_query($conn, "SELECT nama, email, tanggal_join FROM users WHERE id = $user_id");
if ($row = mysqli_fetch_assoc($query)) {
    $favQuery = mysqli_query($conn, "SELECT COUNT(*) as total FROM favorites WHERE user_id = $user_id");
    $favCount = mysqli_fetch_assoc($favQuery)['total'];

    $response['success'] = true;
    $response['data'] = [
        'nama' => $row['nama'],
        'email' => $row['email'],
        'tanggal_join' => $row['tanggal_join'] ?? '-',
        'total_fav' => $favCount
    ];
}
echo json_encode($response);
