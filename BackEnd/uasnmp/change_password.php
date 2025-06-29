<?php
include 'conn.php';
header('Content-Type: application/json');
$response = [];
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = trim($_POST['email'] ?? '');
    $old_password = trim($_POST['old_password'] ?? '');
    $new_password = trim($_POST['new_password'] ?? '');

    if (empty($email) || empty($old_password) || empty($new_password)) {
        $response['status'] = false;
        $response['message'] = 'Semua field wajib diisi';
    } else {
        $stmt = $conn->prepare("SELECT password FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $result = $stmt->get_result();
        $user = $result->fetch_assoc();

        if ($user && password_verify($old_password, $user['password'])) {
            $new_hashed = password_hash($new_password, PASSWORD_DEFAULT);

            $update_stmt = $conn->prepare("UPDATE users SET password = ? WHERE email = ?");
            $update_stmt->bind_param("ss", $new_hashed, $email);
            $success = $update_stmt->execute();

            $response['status'] = $success;
            $response['message'] = $success ? 'Password berhasil diubah' : 'Gagal update password';
        } else {
            $response['status'] = false;
            $response['message'] = 'Password lama salah atau user tidak ditemukan';
        }
    }
} else {
    $response['status'] = false;
    $response['message'] = 'Metode tidak diizinkan';
}

echo json_encode($response);
