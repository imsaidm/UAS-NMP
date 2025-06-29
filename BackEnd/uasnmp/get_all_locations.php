<?php
include 'conn.php';
header('Content-Type: application/json');

$query = mysqli_query($conn, "SELECT * FROM healing_locations");
$data = [];

while ($row = mysqli_fetch_assoc($query)) {
    $data[] = $row;
}

echo json_encode([
    "status" => true,
    "data" => $data
]);
