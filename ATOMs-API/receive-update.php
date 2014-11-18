<?php
require_once('functions.php');
$response = 'Failed';

if(isset($_GET['token']))
{
	$result = mysql_query('SELECT * FROM `user` WHERE `web_token` ="'.mysql_real_escape_string($_GET['token']).'"');
	if(mysql_num_rows($result) > 0)
	{
		$user = mysql_fetch_array($result);
		if(isset($_GET['order_id']) and $_GET['order_id'] != 0)
		{
			$query = sprintf('SELECT * FROM `order` WHERE `id` = %d AND `user_id` = %d', $_GET['order_id'], $user['id']);
			$result = mysql_query($query);
			if(mysql_num_rows($result) > 0)
			{
				$query = sprintf('UPDATE `order` SET `status`= %d WHERE `id` = %d', $_GET['status'], $_GET['order_id']);
				$result = mysql_query($query);
				if($result)
					$response = 'OK';
			}
		}
		else if(isset($_GET['order_id']) and $_GET['order_id'] == 0)
		{
			if(isset($_GET['money']) and isset($_GET['status']) and isset($_GET['invoice_url']))
			{
				$query = sprintf('INSERT INTO `order`(`user_id`, `money`, `status`, `invoice_url`) VALUES ( %d, %f, %d, "%s" )', $user['id'], $_GET['money'], $_GET['status'], mysql_real_escape_string($_GET['invoice_url']));
				$result = mysql_query($query);
				if($result)
					$response = mysql_insert_id();
			}
		}
	}
}

echo $response;