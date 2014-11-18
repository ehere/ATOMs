<?php
require_once('functions.php');

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['username']) && isset($_POST['password']))
{

	$username = $_POST['username'];
	$password = $_POST['password'];


	$result = mysql_fetch_array(mysql_query("SELECT * FROM user WHERE username = '".$username."'"));

	if(verify($password, $result['password'] )){
		//if($result['token'] == ""){
			//generate new token
			do{
				$token = md5(uniqid(mt_rand(), true)).md5(uniqid(mt_rand(), true));
				$result_token = mysql_fetch_array(mysql_query("SELECT COUNT(id) FROM user WHERE token = '".$token."'"));
			}while ($result_token[0] != 0);

			$query = "UPDATE `android_app`.`user` SET `token` = '".$token."' WHERE `user`.`id` = ".$result['id']."";
			$t_result = mysql_query($query);
			if($t_result){
				//login success and get token
				$response["last_receive"] = $result["last_receive"];
				$response["success"] = 1;
				//$response["message"] = "User successfully logged in.";
				$response["token"] = $token;
			}
			else{
				$response["success"] = 0;
				$response["message"] = "Error! Can't create token.";
			}
		//}
	}
	else{
		$response["success"] = 0;
		$response["message"] = "Error! Wrong username or password.";
	}
}
//if get token
else if (isset($_POST['token']))
{

	$token = $_POST['token'];

	$result = mysql_query("SELECT * FROM user WHERE token = '".$token."'");

	if(mysql_num_rows($result) != 0){
		$row = mysql_fetch_array($result);
		$response["last_receive"] = $row["last_receive"];
		$response["success"] = 1;
		//$response["message"] = "User successfully logged in using token.";
	}
	else{
		$response["success"] = 0;
		$response["message"] = "Error! No token found.";
	}



}
else
{
	// required field is missing
	$response["success"] = 0;
	$response["message"] = "Required field(s) is missing";
}

// echoing JSON response
echo json_encode($response);
?>