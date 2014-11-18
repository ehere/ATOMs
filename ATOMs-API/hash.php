<?php
function generateHash($password) {
	if (defined("CRYPT_BLOWFISH") && CRYPT_BLOWFISH) {
		$salt = '$2y$11$' . substr(md5(uniqid(rand(), true)), 0, 22);
		return crypt($password, $salt);
	}
}
function verify($password, $hashedPassword) {
	return crypt($password, $hashedPassword) == $hashedPassword;
}
$password = generateHash("test");
echo $password;
var_dump(verify("test", $password ));
?>