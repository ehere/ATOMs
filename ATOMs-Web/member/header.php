<?php require_once("../functions.php"); ?>
<?php 
    if(!loggedin()){
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
    <title>ATOMs CP | Automatic Transractions and Orders Management System</title>
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
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                            <span class="fa fa-bars fa-lg"></span>
                        </button>
                        <a class="navbar-brand" href="../">
                            <img src="../img/freeze/logo.png" alt="" class="logo">
                        </a>
                    </div>

                    <!-- Collect the nav links, forms, and other content for toggling -->
                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

                        <ul class="nav navbar-nav navbar-right">
                            <li><a href="index.php">Home</a>
                            </li>
                            <li><a href="profile.php">Your Profile</a>
                            </li>
                            <li><a href="token.php">Your Token</a>
                            </li>
                            <li><a class="getApp" href="../#getApp">get app</a>
                            </li>
                            <li><a href="logout.php">Logout</a>
                            </li>
                        </ul>
                    </div>
                    <!-- /.navbar-collapse -->
                </div>
                <!-- /.container-->
        </nav>
    </header>
        
        <!--RevSlider-->

        <style>
        .nav-pills > li.active > a,
        .nav-pills > li.active > a:hover,
        .nav-pills > li.active > a:focus {
          color: #ffffff;
          background-color: #0e76bc;
        }

        .nav-pills > li > a,
        .nav-pills > li > a:hover,
        .nav-pills > li > a:focus {
          color: #ffffff;
          background-color: transparent;
        }
        .revslider-initialised {
        }

        </style>

        <div style="height:100%;">
            <div>
                <ul>
                    <!-- SLIDE  -->
                    <li data-transition="fade" data-slotamount="7" data-masterspeed="1500" >
                        <div class="container">
                            <div class="section-heading scrollpoint inverse sp-effect3">
                                <h1></h1>
                                <div class=""></div>
                                <p><?php if(isset($_SESSION['message'])) { echo $_SESSION['message']; } ?></p>
                            </div>
                            <div class="row inverse">
                                <div class="col-md-2">
                                </div>