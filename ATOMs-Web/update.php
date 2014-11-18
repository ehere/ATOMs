<?php
require_once("functions.php");
var_dump($_POST);
if(isset($_POST['action'])){
	if($_POST['action'] == "register"){
		//do register
		$tocheck = array(
			"0" => "username",
			"1" => "password",
			"2" => "title",
			"3" => "firstname",
			"4" => "lastname",
			"5" => "email",
		);
		foreach ($tocheck as $key => $field) {
			if(!isset($_POST[$field]) or $_POST[$field] == ""){
				$_SESSION['message'] = "Please fill your ".$field.".";
				header("Location: register.php");
				die();
			}
		}
		if($_POST['title'] == "other"){
			if(!isset($_POST['title_other']) or $_POST['title_other'] == ""){
				$_SESSION['message'] = "Please fill your title.";
				//var_dump($_SESSION);
				header("Location: register.php");
				die();
			}
			$title = $_POST['title_other'];
		}
		else{
			$title = $_POST['title'];
		}

		$checkusername = mysql_fetch_array(mysql_query("SELECT COUNT(id) FROM user WHERE username = '".$_POST['username']."'"));
		if($checkusername[0] != 0){
			$_SESSION['message'] = "This username has been taken.";
			//var_dump($_SESSION);
			header("Location: register.php");
			die();
		}

		$checkname = mysql_fetch_array(mysql_query("SELECT COUNT(id) FROM user WHERE firstname = '".$_POST['firstname']."' AND lastname = '".$_POST['lastname']."'"));
		if($checkname[0] != 0){
			$_SESSION['message'] = "This name has already registered.";
			//var_dump($_SESSION);
			header("Location: register.php");
			die();
		}

		$checkemail = mysql_fetch_array(mysql_query("SELECT COUNT(id) FROM user WHERE email = '".$_POST['email']."'"));
		if($checkemail[0] != 0){
			$_SESSION['message'] = "This email has already registered.";
			//var_dump($_SESSION);
			header("Location: register.php");
			die();
		}

		$_POST['password'] = generateHash($_POST['password']);

		do{
			$token = md5(uniqid(mt_rand(), true)).md5(uniqid(mt_rand(), true));
			$result_token = mysql_fetch_array(mysql_query("SELECT COUNT(id) FROM user WHERE token = '".$token."'"));
		}while ($result_token[0] != 0);


		do{
			$server_token = md5(uniqid(mt_rand(), true)).md5(uniqid(mt_rand(), true));
			$result_token = mysql_fetch_array(mysql_query("SELECT COUNT(id) FROM user WHERE server_token = '".$server_token."'"));
		}while ($result_token[0] != 0);

		do{
			$web_token = md5(uniqid(mt_rand(), true)).md5(uniqid(mt_rand(), true));
			$result_token = mysql_fetch_array(mysql_query("SELECT COUNT(id) FROM user WHERE web_token = '".$web_token."'"));
		}while ($result_token[0] != 0);

		$query = "INSERT INTO `android_app`.`user` (`id`, `username`, `password`, `prefix`, `firstname`, `lastname`, `email`, `url_script`, `image`, `state`, `role`, `token`, `server_token`, `web_token`) VALUES (NULL, 
'".$_POST['username']."',
'".$_POST['password']."',
'".$title."',
'".$_POST['firstname']."',
'".$_POST['lastname']."',
'".$_POST['email']."',
'',
NULL, 
'1', '0',
'".$token."',
'".$server_token."',
'".$web_token."')";
		mysql_query($query);
		//var_dump($query);
		$_SESSION['web_token'] = $web_token;
		$_SESSION['server_token'] = $server_token;
		header("Location: registered.php");
	}
}