<?php
header("Location: profile.php");
die();
require_once("header.php");
?>
                                <div class="col-md-8">
                                    <div class="row">
                                        <div class="col-md-2 col-sm-2 scrollpoint sp-effect1"></div>
                                        <div class="col-md-8 col-sm-8 scrollpoint sp-effect1">
                                            <div class="row">
                                                <h3 style="color: #FFFFFF">Hi, Welcome <?php echo $_SESSION['username']; ?> to ATOMs Control Panel.</h3>
                                                <h3 style="color: #FFFFFF">Select what you want to do from menu.</h3>
                                            </div>
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