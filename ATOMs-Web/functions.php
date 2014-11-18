<?php
session_start();
error_reporting(E_ALL);
ini_set("display_errors", 1);
// checks if any user is logged in
function loggedin() {
	return isset($_SESSION['username']);
}

function isadmin() {
	if(!loggedin())
	{
		return false;
	}
	$query="SELECT role FROM users WHERE username='".$_SESSION['username']."'";
	$result = mysql_query($query);
	$row = mysql_fetch_array($result);
	if($row['role'] == 1){
		return true;
	}
	else{
		return false;
	}
}
include('dbinfo.php');
$mysqlconnect = mysql_connect($host,$user,$password);
mysql_select_db($database) or die('Error connecting to database.');
mysql_query("SET NAMES UTF8");
mysql_query("SET time_zone = '+7:00'");
date_default_timezone_set('Asia/Bangkok');

// connects to the database
function connectdb() {
	global $host, $user,$password,$mysqlconnect,$database ;
	if(!mysql_ping ($mysqlconnect))
	{
		mysql_close($mysqlconnect);
		$mysqlconnect = mysql_connect($host,$user,$password);
		mysql_select_db($database) or die('Error connecting to database.');
		mysql_query("SET NAMES UTF8");
		mysql_query("SET time_zone = '+7:00'");
	}
}

// generates a random alpha numeric sequence. Used to generate salt
function randomAlphaNum($length){
	$rangeMin = pow(36, $length-1);
	$rangeMax = pow(36, $length)-1;
	$base10Rand = mt_rand($rangeMin, $rangeMax);
	$newRand = base_convert($base10Rand, 10, 36);
	return $newRand;
}

// converts text to an uniform only '\n' newline break
function treat($text) {
	$s1 = str_replace("\n\r", "\n", $text);
	return str_replace("\r", "", $s1);
}

function get_ip() {
	 $ipaddress = '';
	 if (isset($_SERVER['HTTP_CLIENT_IP']))
		 $ipaddress = $_SERVER['HTTP_CLIENT_IP'];
	 else if(isset($_SERVER['HTTP_X_FORWARDED_FOR']))
		 $ipaddress = $_SERVER['HTTP_X_FORWARDED_FOR'];
	 else if(isset($_SERVER['HTTP_X_FORWARDED']))
		 $ipaddress = $_SERVER['HTTP_X_FORWARDED'];
	 else if(isset($_SERVER['HTTP_FORWARDED_FOR']))
		 $ipaddress = $_SERVER['HTTP_FORWARDED_FOR'];
	 else if(isset($_SERVER['HTTP_FORWARDED']))
		 $ipaddress = $_SERVER['HTTP_FORWARDED'];
	 else if(isset($_SERVER['REMOTE_ADDR']))
		 $ipaddress = $_SERVER['REMOTE_ADDR'];
	 else
		 $ipaddress = 'UNKNOWN';

	 return $ipaddress;
}
function thai_date($time){
  $time = strtotime($time);
	//$thai_day_arr=array("อาทิตย์","จันทร์","อังคาร","พุธ","พฤหัสบดี","ศุกร์","เสาร์");
  $thai_month_arr=array(
	"0"=>"",
	"1"=>"ม.ค.",
	"2"=>"ก.พ.",
	"3"=>"มี.ค.",
	"4"=>"ม.ย.",
	"5"=>"พ.ค.",
	"6"=>"มิ.ย.",
	"7"=>"ก.ค.",
	"8"=>"ส.ค.",
	"9"=>"ก.ย.",
	"10"=>"ต.ค.",
	"11"=>"พ.ย.",
	"12"=>"ธ.ค."
  );
	//$thai_date_return="วัน".$thai_day_arr[date("w",$time)];
	$thai_date_return='';
	$thai_date_return.= /*"ที่ ".*/date("j",$time).' ';
	$thai_date_return.=/*" เดือน".*/$thai_month_arr[date("n",$time)].' ';
	$thai_date_return.=/* " พ.ศ.".*/(date("Y",$time)+543).' ';
	$thai_date_return.= "  ".date("H:i",$time)." น.";
	return $thai_date_return;
}

function microtime_float()
{
	list($usec, $sec) = explode(" ", microtime());
	return ((float)$usec + (float)$sec);
}
function isMobile()
{
	// Check the server headers to see if they're mobile friendly
	if(isset($_SERVER["HTTP_X_WAP_PROFILE"]))
	{
		return true;
	}

	// If the http_accept header supports wap then it's a mobile too
	if(preg_match("/wap\.|\.wap/i",$_SERVER["HTTP_ACCEPT"]))
	{
		return true;
	}

	// Still no luck? Let's have a look at the user agent on the browser. If it contains
	// any of the following, it's probably a mobile device. Kappow!
	if(isset($_SERVER["HTTP_USER_AGENT"]))
	{
		$user_agents = array("midp", "j2me", "avantg", "docomo", "novarra", "palmos","ipad","iphone","android" ,"palmsource", "240x320", "opwv", "chtml", "pda", "windows\ ce", "mmp\/", "blackberry", "mib\/", "symbian", "wireless", "nokia", "hand", "mobi", "phone", "cdm", "up\.b", "audio", "SIE\-", "SEC\-", "samsung", "HTC", "mot\-", "mitsu", "sagem", "sony", "alcatel", "lg", "erics", "vx", "NEC", "philips", "mmm", "xx", "panasonic", "sharp", "wap", "sch", "rover", "pocket", "benq", "java", "pt", "pg", "vox", "amoi", "bird", "compal", "kg", "voda", "sany", "kdd", "dbt", "sendo", "sgh", "gradi", "jb", "\d\d\di", "moto");
		foreach($user_agents as $user_string)
		{
			if(preg_match("/".$user_string."/i",$_SERVER["HTTP_USER_AGENT"]))
			{
				return true;
			}
		}
	}

	// None of the above? Then it's probably not a mobile device.
	return false;
}

function generateHash($password) {
	if (defined("CRYPT_BLOWFISH") && CRYPT_BLOWFISH) {
		$salt = '$2y$11$' . substr(md5(uniqid(rand(), true)), 0, 22);
		return crypt($password, $salt);
	}
}
function verify($password, $hashedPassword) {
	return crypt($password, $hashedPassword) == $hashedPassword;
}