<?php
include 'conn.php';

header('Content-Type: application/json');

$response = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = $_POST['email'] ?? '';
    $password = $_POST['password'] ?? '';

    $query = mysqli_query($conn, "SELECT * FROM users WHERE email='$email'");
    if ($row = mysqli_fetch_assoc($query)) {
        if (password_verify($password, $row['password'])) {
            $response['status'] = true;
            $response['message'] = 'Login berhasil';
            $response['data'] = $row;
        } else {
            $response['status'] = false;
            $response['message'] = 'Password salah';
        }
    } else {
        $response['status'] = false;
        $response['message'] = 'Email tidak ditemukan';
    }
} else {
    $response['status'] = false;
    $response['message'] = 'Gagal simpan data: ' . mysqli_error($conn);
}

echo json_encode($response);
