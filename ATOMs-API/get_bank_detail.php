<?php
require_once('functions.php');
$result = mysql_query("SELECT * FROM `bank_number`");
while($row = mysql_fetch_array($result))
{
	$response[$row["name"]] = $row["number"];
}
echo json_encode($response);
?>