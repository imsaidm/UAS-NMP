<?php
include 'conn.php';

$action = $_REQUEST['action'] ?? '';
$user_id = $_REQUEST['user_id'] ?? 0;
$healing_id = $_REQUEST['healing_id'] ?? 0;
$response = ['success' => false];

if ($action === 'check') {
    $query = mysqli_query($conn, "SELECT * FROM favorites WHERE user_id=$user_id AND healing_id=$healing_id");
    $response['favorited'] = mysqli_num_rows($query) > 0;
    echo json_encode($response);
    exit;
}

if ($action === 'add') {
    $check = mysqli_query($conn, "SELECT * FROM favorites WHERE user_id=$user_id AND healing_id=$healing_id");
    if (mysqli_num_rows($check) == 0) {
        $insert = mysqli_query($conn, "INSERT INTO favorites (user_id, healing_id) VALUES ($user_id, $healing_id)");
        $response['success'] = $insert ? true : false;
    } else {
        $response['success'] = true;
    }
    echo json_encode($response);
    exit;
}

if ($action === 'remove') {
    $delete = mysqli_query($conn, "DELETE FROM favorites WHERE user_id=$user_id AND healing_id=$healing_id");
    $response['success'] = $delete ? true : false;
    echo json_encode($response);
    exit;
}
if ($action === 'list') {
    $data = [];
    $query = mysqli_query($conn, "
        SELECT hl.*
        FROM favorites f
        JOIN healing_locations hl ON f.healing_id = hl.id
        WHERE f.user_id = $user_id
    ");

    while ($row = mysqli_fetch_assoc($query)) {
        $data[] = $row;
    }

    echo json_encode([
        'success' => true,
        'data' => $data
    ]);
    exit;
}
?>
