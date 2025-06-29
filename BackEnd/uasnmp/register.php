<?php
include 'conn.php';
header('Content-Type: application/json');

$response = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $nama = trim($_POST['nama'] ?? '');
    $email = trim($_POST['email'] ?? '');
    $password = trim($_POST['password'] ?? '');
    $check = $conn->prepare("SELECT id FROM users WHERE email = ?");
    $check->bind_param("s", $email);
    $check->execute();
    $check->store_result();

    if ($check->num_rows > 0) {
        $response['status'] = false;
        $response['message'] = 'Email sudah terdaftar';
    } else {
        $hashed = password_hash($password, PASSWORD_DEFAULT);
        $insert = $conn->prepare("INSERT INTO users (nama, email, password, tanggal_join) VALUES (?, ?, ?, CURDATE())");
        $insert->bind_param("sss", $nama, $email, $hashed);

        if ($insert->execute()) {
            $response['status'] = true;
            $response['message'] = 'Registrasi berhasil';
        } else {
            $response['status'] = false;
            $response['message'] = 'Gagal simpan data';
        }
    }
} else {
    $response['status'] = false;
    $response['message'] = 'Metode salah';
}

echo json_encode($response);
