<?php require_once("header.php"); ?>
                                <style>
                                form .form-control {
                                    color: #555555;
                                }
                                </style>
                                <?php
                                    $user = mysql_fetch_array(mysql_query("SELECT * FROM `user` WHERE `username` LIKE '".$_SESSION['username']."'"));
                                ?>
                                <div class="col-md-8">
                                    <div class="row">
                                        <div class="col-md-2 col-sm-2 scrollpoint sp-effect1"></div>
                                        <div class="col-md-8 col-sm-8 scrollpoint sp-effect1">
                                            <form role="form" action="update.php" method="post">
                                            <input type="hidden" id="action" name="action" value="editprofile">
                                            <div class="well well-lg">
                                            <div class="row">
                                                <div class="row">
                                                    <div class="col-md-3 col-sm-3 scrollpoint sp-effect1"><h4 class="pull-right"><strong>Username</strong></h4></div>
                                                    <div class="col-md-9 col-sm-9 scrollpoint sp-effect1">
                                                        <h4><?php echo $user['username']; ?></h4>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-3 col-sm-3 scrollpoint sp-effect1"><h4 class="pull-right"><strong>Name</strong></h4></div>
                                                    <div class="col-md-9 col-sm-9 scrollpoint sp-effect1">
                                                        <div class="form-group">
                                                            <h4><?php echo $user['prefix'].''.$user['firstname'].' '.$user['lastname']; ?></h4>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-3 col-sm-3 scrollpoint sp-effect1"><h4 class="pull-right"><strong>Email</strong></h4></div>
                                                    <div class="col-md-9 col-sm-9 scrollpoint sp-effect1">
                                                    <input type="text" class="form-control" name="email" id="email" value="<?php echo $user['email']; ?>" required>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-3 col-sm-3 scrollpoint sp-effect1"></div>
                                                    <div class="col-md-9 col-sm-9 scrollpoint sp-effect1">
                                                        <br />
                                                        <button type="submit" class="btn btn-primary btn-lg" style="width: 100%;">Save</button>
                                                    </div>
                                                </div>
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

<?php require_once("footer.php"); ?>