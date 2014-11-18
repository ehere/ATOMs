<?php
require_once('functions.php');
$response = array();


if(!isset($_POST['token']) or !isset($_POST['id'])){
	$response["order"] = array();
	$response["success"] = 0;
	$response["message"] = "Token or Order ID not found!";
	// echo no users JSON
	echo json_encode($response);
	die();
}


$user = mysql_query("SELECT * FROM user WHERE token = '".$_POST['token']."'");

if(mysql_num_rows($user) == 0){
	$response["order"] = array();
	$response["success"] = 0;
	$response["message"] = "Error! No token found.";
	die();
}

$user = mysql_fetch_array($user);

//Check if it is user's order.
$result = mysql_query("SELECT * FROM `order` WHERE `id` = '".$_POST['id']."'") or die(mysql_error());
if (mysql_num_rows($result) > 0) {
	$result = mysql_fetch_array($result);
	if($result['user_id'] != $user['id']){
		//can't do
		$response["order"] = array();
		$response["success"] = 0;
		$response["message"] = "You don't have permission to edit this order.".$result['user_id']."|".$user['id'];
		// echo no users JSON
		echo json_encode($response);
		die();
	}
} else {
	// no products found
	$response["order"] = array();
	$response["success"] = 0;
	$response["message"] = "Order not found";
	// echo no users JSON
	echo json_encode($response);
	die();
}


if(isset($_POST['mark_as_paid'])){
	//mark unpaid to paid
	mysql_query("UPDATE `android_app`.`order` SET `status` = '1' WHERE `order`.`id` = '".$_POST['id']."'") or die(mysql_error());
	$response["success"] = 1;
	$response["message"] = "The order has mark as paid.";
	try
	{
		$request = file_get_contents($user['url_script']."?atoms_token=".sha1($user['server_token'])."&order_id=".$_POST['id']."&status=1");
		if($request !== "OK")
		{
			$response["message"] = "Update ATOMs database success but fail to update data in your server.";
		}
	}
	catch( Exception $e )
	{
		$response["message"] = "Update ATOMs database success but fail to update data in your server.";
	}
}

if(isset($_POST['mark_as_not_paid'])){
	//mark paid to unpaid

	mysql_query('UPDATE `android_app`.`order` SET `status`=0,`bank`=null,`transaction_id`= null,`transfered_at`="0000-00-00 00:00:00" WHERE `order`.`id` = '.$_POST['id']) or die(mysql_error());
	mysql_query("UPDATE `transaction` SET `status`= 0 WHERE `id` =".$result['transaction_id']." AND `user_id` = ".$user['id']);
	$response["success"] = 1;
	$response["message"] = "The order has mark as not paid.";
	try
	{
		$request = file_get_contents($user['url_script']."?atoms_token=".sha1($user['server_token'])."&order_id=".$_POST['id']."&status=0");
		if($request !== "OK")
		{
			$response["message"] = "Update ATOMs database success but fail to update data in your server.";
		}
	}
	catch( Exception $e )
	{
		$response["message"] = "Update ATOMs database success but fail to update data in your server.";
	}
}

if(isset($_POST['mark_as_shipped'])){
	//mark paid to ship
	mysql_query("UPDATE `android_app`.`order` SET `status` = '2' WHERE `order`.`id` = '".$_POST['id']."'") or die(mysql_error());
	$response["success"] = 1;
	$response["message"] = "The order has mark as shipped.";
	try
	{
		$request = file_get_contents($user['url_script']."?atoms_token=".sha1($user['server_token'])."&order_id=".$_POST['id']."&status=2");
		if($request !== "OK")
		{
			$response["message"] = "Update ATOMs database success but fail to update data in your server.";
		}
	}
	catch( Exception $e )
	{
		$response["message"] = "Update ATOMs database success but fail to update data in your server.";
	}
}

if(isset($_POST['delete'])){
	//mark paid to ship
	mysql_query("DELETE FROM `android_app`.`order` WHERE `order`.`id` = '".$_POST['id']."'") or die(mysql_error());
	$response["success"] = 1;
	$response["message"] = "The order has been deleted.";
}

echo json_encode($response);
?>
