<?php
function transaction_process($sms_id)
{
	$response = array();
	//Fetch Bank number
	$bank 		= array();
	$result 	= mysql_query("SELECT * FROM `bank_number`");
	while($row = mysql_fetch_array($result))
	{
		$bank[$row['name']] = $row['number'];
	}

	//Validate id
	if(empty($sms_id) or !is_numeric($sms_id))
	{
		$response["success"] = 0;
		$response["message"] = "Invalid ID";
		return $response;
	}

	//find duplicate
	$result = mysql_query(sprintf("SELECT * FROM `transaction` WHERE `sms_id` = %d AND `created_at` >= (NOW() - INTERVAL 5 MINUTE)", $sms_id));
	if(mysql_num_rows($result) > 0 )
	{
		$response["success"] = 0;
		$response["message"] = "Duplicate ID";
		return $response;
	}

	//Fetch SMS
	$result = mysql_query("SELECT * FROM `sms_income` WHERE `id` = ".$sms_id);
	if(mysql_num_rows($result) == 0 )
	{
		$response["success"] = 0;
		$response["message"] = "ID not found";
		return $response;
	}

	while($row = mysql_fetch_array($result))
	{
		$process = array();
		//KBank Validator
		if($row["sender"] == $bank["KBANK"])
		{
			$message = $row["message"];
			if (strpos($message,"เงินเข้า") !== false)
			{
				$temp = explode(" ", $message);
				$date = explode("/",$temp[0]);
				$date = date("Y")."-".$date[1]."-".$date[0];
				$process['sms_id'] 			= $row["id"];
				$process['bank'] 			= array_search($row["sender"], $bank);
				$process['time'] 			= $date." ".$temp[1].":00";
				$process['sms_time'] 		= $row["received_at"];
				$process['account_number'] 	= $temp[3];
				$process['money'] 			= str_replace(",", "", $temp[5]);

				// Validate Data
				$valid = true;
				if 	(!(	strpos($process['account_number'],"X") !== false and
						(	strpos($process['account_number'],"0") !== false or
							strpos($process['account_number'],"1") !== false or
							strpos($process['account_number'],"2") !== false or
							strpos($process['account_number'],"3") !== false or
							strpos($process['account_number'],"4") !== false or
							strpos($process['account_number'],"5") !== false or
							strpos($process['account_number'],"6") !== false or
							strpos($process['account_number'],"7") !== false or
							strpos($process['account_number'],"8") !== false or
							strpos($process['account_number'],"9") !== false
						)
					))
				{
					$valid = false;
				}
				if($process['time'] != date('Y-m-d H:i:s',strtotime($process['time'])))
				{
					$valid = false;
				}
				if(!is_numeric($process['money']))
				{
					$valid = false;
				}
				if($valid)
				{
					$query = sprintf('INSERT INTO `transaction`(`user_id`, `bank`, `sms_time`, `account_number`, `money`, `sms_id`) VALUES ("%s","%s","%s","%s","%s", %d)', $row["user_id"], $process['bank'], $process['time'], $process['account_number'], $process['money'], $process['sms_id']);
					mysql_query($query);
					$row = mysql_fetch_array(mysql_query('SELECT * FROM `transaction` WHERE `sms_id` = '.$process['sms_id']));
					$response["success"] = 1;
					$response["message"] = "Transaction Complete";
					$response["bank"] = $process['bank'];
					$response["transaction_id"] = $row['id'];
					$response["time"] = $process['time'];
					$response["money"] = $process['money'];
					return $response;
				}
				else
				{
					$response["success"] = 0;
					$response["message"] = "Invalid Transaction";
					return $response;
				}
			}
			else
			{
				$response["success"] = 0;
				$response["message"] = "Invalid Message";
				return $response;
			}
		}
		//SCB Validator
		else if($row["sender"] == "027777777")
		{
			$message = $row["message"];
			if (strpos($message,"เงินเข้า") !== false and strpos($message,"รายการเงินเข้า") === false)
			{
				$query = sprintf('SELECT count(*) FROM `sms_income` WHERE `message` = "%s"', $message);
				$count = mysql_fetch_array(mysql_query($query));
				$count = $count[0];

				if($count == 1)
				{
					$temp = explode(" ", $message);

					$process['sms_id'] 			= $row["id"];
					$process['bank'] 			= array_search($row["sender"], $bank);
					$process['time'] 			= $row["received_at"];
					$process['sms_time'] 		= $row["received_at"];
					$process['account_number'] 	= str_replace("ผ่าน", "", $temp[2]);
					$process['channel']			= $temp[3];
					$process['money'] 			= str_replace(",", "", str_replace("บเข้าบ/ช", "",$temp[1]));

					// Validate Data
					$valid = true;
					if 	(!(	strpos($process['account_number'],"x") !== false and
							(	strpos($process['account_number'],"0") !== false or
								strpos($process['account_number'],"1") !== false or
								strpos($process['account_number'],"2") !== false or
								strpos($process['account_number'],"3") !== false or
								strpos($process['account_number'],"4") !== false or
								strpos($process['account_number'],"5") !== false or
								strpos($process['account_number'],"6") !== false or
								strpos($process['account_number'],"7") !== false or
								strpos($process['account_number'],"8") !== false or
								strpos($process['account_number'],"9") !== false
							)
						))
					{
						$valid = false;
					}
					if($process['time'] != date('Y-m-d H:i:s',strtotime($process['time'])))
					{
						$valid = false;
					}
					if(!is_numeric($process['money']))
					{
						$valid = false;
					}
					if($valid)
					{
						$query = sprintf('INSERT INTO `transaction`(`user_id`, `bank`, `sms_time`, `account_number`, `money`, `sms_id`, `channel`) VALUES ("%s","%s","%s","%s","%s", %d, "%s")', $row["user_id"], $process['bank'], $process['time'], $process['account_number'], $process['money'], $process['sms_id'], $process['channel']);
						mysql_query($query);
						$row = mysql_fetch_array(mysql_query('SELECT * FROM `transaction` WHERE `sms_id` = '.$process['sms_id']));
						$response["success"] = 1;
						$response["message"] = "Transaction Complete";
						$response["bank"] = $process['bank'];
						$response["transaction_id"] = $row['id'];
						$response["time"] = $process['time'];
						$response["money"] = $process['money'];
						return $response;
					}
					else
					{
						$response["success"] = 0;
						$response["message"] = "Invalid Transaction";
						return $response;
					}

				}
				else
				{
					$response["success"] = 0;
					$response["message"] = "Duplicate Message";
					return $response;
				}
			}
			else
			{
				$response["success"] = 0;
				$response["message"] = "Invalid Message";
				return $response;
			}
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "Invalid Bank";
			return $response;
		}
	}

}
?>