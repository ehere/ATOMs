<?php
require_once("../functions.php");

if(isset($_GET['regenservertoken'])){
	do{
		$server_token = md5(uniqid(mt_rand(), true)).md5(uniqid(mt_rand(), true));
		$result_token = mysql_fetch_array(mysql_query("SELECT COUNT(id) FROM user WHERE server_token = '".$server_token."'"));
	}while ($result_token[0] != 0);

	$query = "UPDATE `android_app`.`user` SET `server_token` = '".$server_token."' WHERE `user`.`id` = '".$_SESSION['user_id']."';";
	mysql_query($query);
	header("Location: token.php");
}

if(isset($_GET['regenwebtoken'])){
	do{
		$web_token = md5(uniqid(mt_rand(), true)).md5(uniqid(mt_rand(), true));
		$result_token = mysql_fetch_array(mysql_query("SELECT COUNT(id) FROM user WHERE web_token = '".$web_token."'"));
	}while ($result_token[0] != 0);

	$query = "UPDATE `android_app`.`user` SET `web_token` = '".$web_token."' WHERE `user`.`id` = '".$_SESSION['user_id']."';";
	mysql_query($query);
	header("Location: token.php");
}

if(isset($_POST['action']) and $_POST['action'] == "editprofile"){
	
	$query = "UPDATE `android_app`.`user` SET `email` = '".$_POST['email']."' WHERE `user`.`id` = '".$_SESSION['user_id']."';";
	mysql_query($query);
	header("Location: profile.php");
}

if(isset($_POST['action']) and $_POST['action'] == "editapiurl"){
	
	$query = "UPDATE `android_app`.`user` SET `url_script` = '".$_POST['url']."' WHERE `user`.`id` = '".$_SESSION['user_id']."';";
	mysql_query($query);
	header("Location: token.php");
}