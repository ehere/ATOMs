<?php
require_once('functions.php');

?>
<form name="input" action="sms_receiver.php" method="post">
id: <input type="text" name="id"><br>
<!-- user_id: <input type="text" name="user_id"><br> -->
token: <input type="text" name="token"><br>
sender: <input type="text" name="sender"><br>
message: <input type="text" name="message"><br>
time: <input type="text" name="time" value="<?php echo date('Y-m-d H:i:s'); ?>">
<input type="submit" value="Submit">
</form>