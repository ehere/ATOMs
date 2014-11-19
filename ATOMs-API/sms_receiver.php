<?php
require_once('functions.php');
if (!isset($_POST['token']))
{
	// token field is missing
	$response["success"] = 0;
	$response["message"] = "token is missing";
	// echoing JSON response
	echo json_encode($response);
	exit();
}

$token = $_POST['token'];
$result = mysql_query("SELECT * FROM user WHERE token = '".$token."'");
//search token
if(mysql_num_rows($result) == 0)
{
	$response["success"] = 0;
	$response["message"] = "Error! No token found.";
	echo json_encode($response);
	exit();
}

$result 		= mysql_fetch_array($result);
$user_id 		= $result['id'];
$sender 		= $_POST['sender'];
$receiver 		= "";
$received_at 	= $_POST['time'];
$message 		= $_POST['message'];
//$receiver 	= $_POST['receiver'];

$date 		= date("Y-m-d H:i:s");
$query 		= sprintf('UPDATE `user` SET `last_receive`="%s",`last_auth`="%s" WHERE `id` = %d', $date, $date, $user_id);
mysql_query($query);


$query 		= sprintf('INSERT INTO `sms_income`(`user_id`, `sender`, `receiver`, `message`, `received_at`) VALUES ("%d","%s","%s","%s","%s")',$user_id,addslashes($sender),addslashes($receiver),addslashes($message),$received_at);
$result 	= mysql_query($query);

// check if row inserted or not
if ($result)
{
	// successfully inserted into database
	$response["success"] = 1;
	$response["message"] = "SMS successfully receive.";
	echo json_encode($response);

	//Process transaction
	require_once('transaction_processor.php');
	$query 		= sprintf('SELECT * FROM `sms_income` WHERE `user_id` = %d AND `sender` = "%s" AND `receiver` = "%s" AND `message` = "%s" AND `received_at` = "%s"',$user_id,addslashes($sender),addslashes($receiver),addslashes($message),$received_at);
	$row = mysql_fetch_array(mysql_query($query));
	$process = transaction_process($row['id']);
	if($process['success'] == 1)
	{
		$query 	= sprintf('SELECT * FROM `order` WHERE `user_id` = %d AND (`transfered_at` >= ("%s" - INTERVAL 1 MINUTE ) AND `transfered_at` <= ("%s" + INTERVAL 1 MINUTE ) AND ABS(%f-`money`) <= 0.00001 AND `status` = 0 AND transaction_id IS NULL AND `bank`= "%s") LIMIT 1', $user_id, $process['time'], $process['time'], $process['money'], $process['bank']);
		$result = mysql_query($query);
		if(mysql_num_rows($result) > 0)
		{
			$order = mysql_fetch_array($result);
			$query 	= sprintf('UPDATE `order` SET `status`= 1 ,`transaction_id`= %d WHERE `id` = %d', $process['transaction_id'], $order['id']);
			mysql_query($query);
			mysql_query('UPDATE `transaction` SET `status`=1 WHERE `id` = '.$process['transaction_id']);

		}

	}
}
else
{
	// failed to insert row
	$response["success"] = 0;
	$response["message"] = "Oops! An error occurred.";
	echo json_encode($response);
}

?>
