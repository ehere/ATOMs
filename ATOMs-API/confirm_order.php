<?php
require_once('functions.php');
$response = array();

$response["success"] = 1;
$response["message"] = "Nothing change!";

if(!isset($_GET['token']) or !isset($_GET['order_id']) or !is_numeric($_GET['order_id']) or !isset($_GET['money']) or !isset($_GET['bank']) or !isset($_GET['time'])){
	$response["success"] = 0;
	$response["message"] = "Error! Incompleted data.";
	// echo no users JSON
	echo json_encode($response);
	die();
}


$user = mysql_query("SELECT * FROM user WHERE web_token = '".$_GET['token']."'");

if(mysql_num_rows($user) == 0){
	$response["success"] = 0;
	$response["message"] = "Error! No token found.";
	echo json_encode($response);
	die();
}

$user = mysql_fetch_array($user);

$query = 'SELECT * FROM `transaction` WHERE `user_id` = "'.$user['id'].'" AND `status` = 0 AND `bank` LIKE "'.$_GET['bank'].'" AND  `sms_time` >= ("'.$_GET['time'].'" - INTERVAL 1 MINUTE) AND  `sms_time` <= ("'.$_GET['time'].'" + INTERVAL 10 MINUTE) AND ROUND(`money`, 2) = "'.$_GET['money'].'" LIMIT 0, 1';
$result = mysql_query($query);
if(mysql_num_rows($result) > 0){
	$transaction = mysql_fetch_array($result);
	$query = 'SELECT * FROM `order` WHERE `id` = "'.$_GET['order_id'].'" AND `user_id` = "'.$user['id'].'" AND `status` = "0"';
	$result = mysql_query($query);
	if(mysql_num_rows($result) > 0){
		$order = mysql_fetch_array($result);
		if($order['money'] == $_GET['money']){
			mysql_query("UPDATE `android_app`.`transaction` SET `status` = '1' WHERE `transaction`.`id` = '".$transaction['id']."' LIMIT 1;");
			mysql_query("UPDATE `android_app`.`order` SET `status` = '1', `bank` = '".$transaction['bank']."', `transaction_id` = '".$transaction['id']."', `transfered_at` = '".$transaction['sms_time']."' WHERE `order`.`id` = '".$order['id']."';");
			$response["success"] = 1;
			$response["message"] = "Success! Order confirmation completed.";
		}
		else{
			$response["success"] = 0;
			$response["message"] = "Error! Amount transfered does not match.";
		}
	}
	else{
		$response["success"] = 0;
		$response["message"] = "Error! We cannot find your order.";
	}
}
else{
	$response["success"] = 0;
	$response["message"] = "Error! Sorry, we cannot find your transaction. Please try again in 30 minutes. || ".$query;
}

echo json_encode($response);
?>
