<?php
require_once('functions.php');
$response = array();

$response["success"] = 1;
$response["message"] = "Nothing change!";

if(!isset($_POST['token']) or !isset($_POST['id']) or !is_numeric($_POST['id'])){
	$response["transaction"] = array();
	$response["success"] = 0;
	$response["message"] = "Token or transaction ID not found!";
	// echo no users JSON
	echo json_encode($response);
	die();
}


$user = mysql_query("SELECT * FROM user WHERE token = '".$_POST['token']."'");

if(mysql_num_rows($user) == 0){
	$response["transaction"] = array();
	$response["success"] = 0;
	$response["message"] = "Error! No token found.";
	die();
}

$user = mysql_fetch_array($user);

//Check if it is user's transaction.
$result = mysql_query("SELECT * FROM `transaction` WHERE `id` = '".$_POST['id']."'") or die(mysql_error());
if (mysql_num_rows($result) > 0) {
	$result = mysql_fetch_array($result);
	if($result['user_id'] != $user['id']){
		//can't do
		$response["transaction"] = array();
		$response["success"] = 0;
		$response["message"] = "You don't have permission to edit this transaction.".$result['user_id']."|".$user['id'];
		// echo no users JSON
		echo json_encode($response);
		die();
	}
} else {
	// no products found
	$response["transaction"] = array();
	$response["success"] = 0;
	$response["message"] = "transaction not found";
	// echo no users JSON
	echo json_encode($response);
	die();
}


if(isset($_POST['mark_as_used'])){
	//mark unpaid to paid
	mysql_query("UPDATE `android_app`.`transaction` SET `status` = '1' WHERE `transaction`.`id` = '".$_POST['id']."'") or die(mysql_error());
	$response["success"] = 1;
	$response["message"] = "The transaction has mark as Used.";
}

if(isset($_POST['mark_as_unused'])){
	//mark paid to unpaid
	mysql_query("UPDATE `android_app`.`transaction` SET `status` = '0' WHERE `transaction`.`id` = '".$_POST['id']."'") or die(mysql_error());
	mysql_query('UPDATE `android_app`.`order` SET `status`=0,`bank`=null,`transaction_id`= null,`transfered_at`="0000-00-00 00:00:00" WHERE `transaction_id` = '.$_POST['id']." AND `user_id` = ".$user['id']);
	$response["success"] = 1;
	$response["message"] = "The transaction has mark as Unused.";
}


if(isset($_POST['delete'])){
	//mark paid to ship
	if(mysql_query("DELETE FROM `transaction` WHERE `id` = '".$_POST['id']."'"))
	{
		$response["success"] = 1;
		$response["message"] = "The transaction has been deleted.";
	}
	else
	{
		$response["success"] = 0;
		$response["message"] = "Something wrong.";
	}
}

echo json_encode($response);
?>
