<?php
require_once('functions.php');
$response = array();

//$_POST['token'] = "0c6bb54d09727a1cadde9cfa13cd2841eee6dae2915e748c3aae677bdd03bf3c";
$user = mysql_query("SELECT * FROM user WHERE token = '".$_POST['token']."'");

if(!isset($_POST['page']) or !is_numeric($_POST['page']))
{
	$response["success"] = 0;
	$response["message"] = "Error! No page found.";
	die();
	echo json_encode($response);
}

$user = mysql_fetch_array($user);

// get all products from products table
$result = mysql_query("SELECT * FROM `transaction` WHERE `user_id` = '".$user['id']."' ORDER BY `id` DESC LIMIT ".(($_POST['page']-1)*20).", 20") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
	// looping through all results
	// products node
	$response["transaction"] = array();
	$totalpage = mysql_fetch_array(mysql_query("SELECT CEIL(COUNT(`id`)/20) FROM `transaction` WHERE `user_id` =".$user['id']))[0];
	$response["totalpage"] = $totalpage;

	while ($row = mysql_fetch_array($result)) {
		// temp user array
		$transaction = array();
		$transaction["id"] = $row["id"];
		$transaction["bank"] = $row["bank"];
		$transaction["money"] = $row["money"];
		$transaction["status"] = $row["status"];
		$transaction["created_at"] = $row["created_at"];

		// push single product into final response array
		array_push($response["transaction"], $transaction);
	}
	// success
	$response["success"] = 1;

	// echoing JSON response
	echo json_encode($response);
} else {
	// no products found
	$response["transaction"] = array();
	$response["success"] = 0;
	$response["message"] = "No transaction found";

	// echo no users JSON
	echo json_encode($response);
}
?>
