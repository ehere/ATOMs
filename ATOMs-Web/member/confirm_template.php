<?php
    //Edit your API Code location here.
    require_once( YOUR_API_CODE_LOCATION );
    if(isset($_POST['action'])){
        if($_POST['action'] == "confirm"){
            confirm_order($_POST['order_id'], $_POST['money'], $_POST['bank'], $_POST['time']);
            //You can do whatever you want below here, like update your database.
        }
    }
?>

<!doctype html>
<!--[if lt IE 7]><html lang="en" class="no-js ie6"><![endif]-->
<!--[if IE 7]><html lang="en" class="no-js ie7"><![endif]-->
<!--[if IE 8]><html lang="en" class="no-js ie8"><![endif]-->
<!--[if gt IE 8]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->

<head>
    <meta charset="UTF-8">
    <title>ATOMs | Sample Money Transfer Confirmation Form</title>
</head>

<body>
   
    <form role="form" action="confirm-sample.php" method="post">
    <input type="hidden" id="action" name="action" value="confirm">

    Order ID <input type="text" name="order_id" id="order_id" required><br />

    Transfer Amount <input type="text" name="money" id="money" placeholder="XXXXX.XX" required><br />

    Bank 
    <select name="bank" id="bank">
        <option value="-">Please Select...</option>
        <option value="SCB">ธนาคารไทยพาณิชย์</option>
        <option value="KBANK">ธนาคารกสิกรไทย</option>
    </select>
    <br />

    Transfer Time <input type="text" name="time" id="time" placeholder="YYYY-MM-DD hh:mm:ss" required><br />
    ตัวอย่างเช่น <code>2014-12-25 18:32:24</code> เป็นต้น<br />

    <button type="submit">Submit</button>

</body>

</html>
