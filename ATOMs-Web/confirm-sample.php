<?php
	require_once('functions.php');
	if(isset($_POST['action'])){
		if($_POST['action'] == "confirm"){
			/*$user = mysql_fetch_array(mysql_query("SELECT * FROM `user` WHERE `web_token` LIKE '".$_POST['web_token']."'"));
			$query = 'SELECT * FROM `transaction` WHERE `user_id` = "'.$user['id'].'" AND `status` = 0 AND `bank` LIKE "'.$_POST['bank'].'" AND `sms_time` = "'.$_POST['time'].'" AND ROUND(`money`, 2) = "'.$_POST['money'].'" LIMIT 0, 1';
			$result = mysql_query($query);
			if(mysql_num_rows($result) > 0){
				$transaction = mysql_fetch_array($result);
				$query = 'SELECT * FROM `order` WHERE `id` = "'.$_POST['orderid'].'" AND `user_id` = "'.$user['id'].'" AND `status` = "0"';
				$result = mysql_query($query);
				if(mysql_num_rows($result) > 0){
					$order = mysql_fetch_array($result);
					if($order['money'] == $_POST['money']){
						mysql_query("UPDATE `android_app`.`transaction` SET `status` = '1' WHERE `transaction`.`id` = '".$transaction['id']."';");
						mysql_query("UPDATE `android_app`.`order` SET `status` = '1', `bank` = '".$transaction['bank']."', `transaction_id` = '".$transaction['id']."', `transfered_at` = '".$transaction['sms_time']."' WHERE `order`.`id` = '".$order['id']."';");
						echo 'Order confirmation successfully!';
					}
					else{
						echo 'Sorry, your money amount does not match with the order!';
					}
				}
				else{
					echo 'Sorry, we cannot find your order!';
				}
			}
			else{
				echo 'Sorry, we cannot find your transaction. Please try again in 30 minutes.';
			}*/
            require_once("../android-connect/confirm_order.php?token=".$_POST['web_token']."&order_id=".$_POST['order_id']."&bank=".$_POST['bank']."&time=".$_POST['time']);
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
    <title>ATOMs | Automatic Transractions and Orders Management System</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <link rel="shortcut icon" href="favicon.png">

    <link rel="stylesheet" href="css/bootstrap.css">
    
    <link rel="stylesheet" href="css/animate.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/slick.css">
    <link rel="stylesheet" href="js/rs-plugin/css/settings.css">

    <link rel="stylesheet" href="css/freeze.css">


    <script type="text/javascript" src="js/modernizr.custom.32033.js"></script>

    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    
</head>

<body>

    <div class="pre-loader">
        <div class="load-con">
            <img src="img/freeze/logo.png" class="animated fadeInDown" alt="">
            <div class="spinner">
              <div class="bounce1"></div>
              <div class="bounce2"></div>
              <div class="bounce3"></div>
            </div>
        </div>
    </div>
   
    <header>
        
        <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
                <div class="container">
                    <!-- Brand and toggle get grouped for better mobile display -->
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                            <span class="fa fa-bars fa-lg"></span>
                        </button>
                        <a class="navbar-brand" href="index.html">
                            <img src="img/freeze/logo.png" alt="" class="logo">
                        </a>
                    </div>

                    <!-- Collect the nav links, forms, and other content for toggling -->
                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

                    </div>
                    <!-- /.navbar-collapse -->
                </div>
                <!-- /.container-->
        </nav>

        
        <!--RevSlider-->
        <div class="tp-banner-container">
            <div class="tp-banner" >
                <ul>
                    <!-- SLIDE  -->
                    <li data-transition="fade" data-slotamount="7" data-masterspeed="1500" >
                        <div class="container">
                            <div class="section-heading scrollpoint inverse sp-effect3">
                                <h1>Transfer Confirmation</h1>
                                <div class="divider"></div>
                                <p><?php if(isset($_SESSION['message'])) { echo $_SESSION['message']; } ?></p>
                            </div>
                            <div class="row inverse">
                                <div class="col-md-12">
                                    <div class="row">
                                        <div class="col-md-2 col-sm-2 scrollpoint sp-effect1"></div>
                                        <div class="col-md-8 col-sm-8 scrollpoint sp-effect1">
                                            <form role="form" action="confirm-sample.php" method="post">
                                                <input type="hidden" id="action" name="action" value="confirm">
                                                <div class="row">
                                                    <div class="col-md-3 col-sm-3 scrollpoint sp-effect1"><h4 style="color: #FFFFFF" class="pull-right">Web Token</h4></div>
                                                    <div class="col-md-9 col-sm-9 scrollpoint sp-effect1">
                                                        <div class="form-group">
                                                            <input type="text" class="form-control" name="web_token" id="web_token" required>
                                                            <h5 style="color: #FFFFFF">Currently have this in testing stage.</h4>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-3 col-sm-3 scrollpoint sp-effect1"><h4 style="color: #FFFFFF" class="pull-right">Order ID</h4></div>
                                                    <div class="col-md-9 col-sm-9 scrollpoint sp-effect1">
                                                        <div class="form-group">
                                                            <input type="text" class="form-control" name="orderid" id="orderid" required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-3 col-sm-3 scrollpoint sp-effect1"><h4 style="color: #FFFFFF" class="pull-right">Transfer Amount</h4></div>
                                                    <div class="col-md-9 col-sm-9 scrollpoint sp-effect1">
                                                        <div class="form-group">
                                                            <input type="text" class="form-control" name="money" id="money" placeholder="XXXXX.XX" required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-3 col-sm-3 scrollpoint sp-effect1"><h4 style="color: #FFFFFF" class="pull-right">Bank</h4></div>
                                                    <div class="col-md-9 col-sm-9 scrollpoint sp-effect1">
                                                        <div class="form-group">
                                                            <select name="bank" id="bank" class="form-control" style="padding: 0px; padding-left: 15px;">
                                                                <option value="-">Please Select...</option>
                                                                <option value="SCB">ธนาคารไทยพาณิชย์</option>
                                                                <option value="KBANK">ธนาคารกสิกรไทย</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-3 col-sm-3 scrollpoint sp-effect1"><h4 style="color: #FFFFFF" class="pull-right">Transfer Time</h4></div>
                                                    <div class="col-md-9 col-sm-9 scrollpoint sp-effect1">
                                                        <div class="form-group">
                                                            <input type="text" class="form-control" name="time" id="time" placeholder="YYYY-MM-DD hh:mm:ss" required>
                                                            <h5 style="color: #FFFFFF">ตัวอย่างเช่น <code>2014-12-25 18:32:24</code> เป็นต้น</h4>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-3 col-sm-3 scrollpoint sp-effect1"></div>
                                                    <div class="col-md-9 col-sm-9 scrollpoint sp-effect1">
                                                        <div class="form-group">
                                                            <h4 style="color: #FFFFFF">By submitting this form, you agreed the ATOMs TOS.</h4>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-3 col-sm-3 scrollpoint sp-effect1"></div>
                                                    <div class="col-md-9 col-sm-9 scrollpoint sp-effect1">
                                                        <button type="submit" class="btn btn-primary btn-lg">Submit</button>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </li>
                    
                </ul>
            </div>
        </div>


    </header>


    <script src="js/jquery-1.11.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/slick.min.js"></script>
    <script src="js/placeholdem.min.js"></script>
    <script src="js/rs-plugin/js/jquery.themepunch.plugins.min.js"></script>
    <script src="js/rs-plugin/js/jquery.themepunch.revolution.min.js"></script>
    <script src="js/waypoints.min.js"></script>
    <script src="js/scripts.js"></script>

    <script>
        $(document).ready(function() {
            appMaster.preLoader();
        });
    </script>

</body>

</html>
