<?php require_once("../functions.php"); ?>

<?php
    if(loggedin()){
        header("Location: index.php");
        die();
    }
    if(isset($_POST['action'])){
        if($_POST['action'] == "login"){
            //login
            $user = mysql_query("SELECT * FROM `user` WHERE `username` LIKE '".$_POST['username']."'");
            if(mysql_num_rows($user) == 0){
                header("Location: login.php");
                die();
            }
            $user = mysql_fetch_array($user);
            if(verify($_POST['password'], $user['password'])){
                $_SESSION['username'] = $user['username'];
                $_SESSION['user_id'] = $user['id'];
                header("Location: index.php");
                die();
            }
        }
        header("Location: login.php");
        die();
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
    <title>ATOMs Login | Automatic Transractions and Orders Management System</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <link rel="shortcut icon" href="favicon.png">

    <link rel="stylesheet" href="../css/bootstrap.css">
    
    <link rel="stylesheet" href="../css/animate.css">
    <link rel="stylesheet" href="../css/font-awesome.min.css">
    <link rel="stylesheet" href="../css/slick.css">
    <link rel="stylesheet" href="../js/rs-plugin/css/settings.css">

    <link rel="stylesheet" href="../css/freeze.css">


    <script type="text/javascript" src="../js/modernizr.custom.32033.js"></script>

    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    
</head>

<body style="background: linear-gradient(-45deg, #66cdcc 0%, #336799 100%) top right no-repeat !important; background-attachment:fixed !important; ">

    <div class="pre-loader">
        <div class="load-con">
            <img src="../img/freeze/logo.png" class="animated fadeInDown" alt="">
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
                        <a class="navbar-brand" href="/">
                            <img src="../img/freeze/logo.png" alt="" class="logo">
                        </a>
                    </div>

                    
                </div>
                <!-- /.container-->
        </nav>
    </header>
        
        <!--RevSlider-->

        <div style="height:100%;">
            <div>
                <ul>
                    <!-- SLIDE  -->
                    <li data-transition="fade" data-slotamount="7" data-masterspeed="1500" >
                        <div class="container">
                            <div class="section-heading scrollpoint inverse sp-effect3">
                                <h1>Login</h1>
                                <div class="divider"></div>
                                <p><?php if(isset($_SESSION['message'])) { echo $_SESSION['message']; } ?></p>
                            </div>
                            <div class="row inverse">
                                <div class="col-md-4"></div>
                                <div class="col-md-4">
                                    <div class="row">
                                        <form role="form" action="login.php" method="post">
                                        <input type="hidden" id="action" name="action" value="login">
                                        <h4 style="color: #FFFFFF">Username</h4>
                                        <input type="text" class="form-control" name="username" id="username" placeholder="Your Username" required>
                                        <br />
                                        <h4 style="color: #FFFFFF">Password</h4>
                                        <input type="password" class="form-control" name="password" id="password" placeholder="Your Password" required>
                                        <br />
                                        <button type="submit" class="btn btn-primary btn-lg" style="width: 100%;">Login</button>
                                        </form>
                                    </div>
                                </div>
                                <div class="col-md-4"></div>
                            </div>
                        </div>

                    </li>
                    
                </ul>
            </div>
        </div>

    <script src="../js/jquery-1.11.1.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="../js/slick.min.js"></script>
    <script src="../js/placeholdem.min.js"></script>
    <script src="../js/rs-plugin/js/jquery.themepunch.plugins.min.js"></script>
    <script src="../js/rs-plugin/js/jquery.themepunch.revolution.min.js"></script>
    <script src="../js/waypoints.min.js"></script>
    <script src="../js/scripts.js"></script>

    <script>
        $(document).ready(function() {
            appMaster.preLoader();
        });
        $(function(){
          $('body').css({ height: $(window).innerHeight() });
          $(window).resize(function(){
            $('body').css({ height: $(window).innerHeight() });
          });
        });
    </script>


</body>

</html>