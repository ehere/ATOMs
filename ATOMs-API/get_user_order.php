<?php
require_once('functions.php');
$response = array();


$user = mysql_query("SELECT * FROM user WHERE token = '".mysql_real_escape_string($_POST['token'])."'");

if(!isset($_POST['page']) or !is_numeric($_POST['page']))
{
	$response["success"] = 0;
	$response["message"] = "Error! No page found.";
	die();
	echo json_encode($response);
}

if(mysql_num_rows($user) == 0){
	$response["order"] = array();
	$response["success"] = 0;
	$response["message"] = "Error! No token found.";
	echo json_encode($response);
	die();
}

$user = mysql_fetch_array($user);

// get all products from products table
$result = mysql_query("SELECT * FROM `order` WHERE `user_id` = '".$user['id']."' ORDER BY `id` DESC LIMIT ".(($_POST['page']-1)*20).", 20") or die(mysql_error());
// check for empty result
if (mysql_num_rows($result) > 0) {
	// looping through all results
	// products node
	$response["order"] = array();
	$totalpage = mysql_fetch_array(mysql_query("SELECT CEIL(COUNT(`id`)/20) FROM `order` WHERE `user_id` =".$user['id']))[0];
	$response["totalpage"] = $totalpage;
	while ($row = mysql_fetch_array($result)) {
		// temp user array
		$order = array();
		$order["id"] = $row["id"];
		$order["money"] = $row["money"];
		$order["status"] = $row["status"];
		$order["created_at"] = $row["created_at"];
		$order["url"] = $row["invoice_url"];

		// push single product into final response array
		array_push($response["order"], $order);
	}
	// success
	$response["success"] = 1;

	// echoing JSON response
	echo json_encode($response);
} else {
	// no products found
	$response["order"] = array();
	$response["success"] = 0;
	$response["message"] = "No order found";

	// echo no users JSON
	echo json_encode($response);
}
?>
