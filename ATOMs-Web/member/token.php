<?php require_once("header.php"); ?>
                                <?php
                                    $token = mysql_fetch_array(mysql_query("SELECT * FROM `user` WHERE `username` LIKE '".$_SESSION['username']."'"));
                                ?>
                                <div class="col-md-8">
                                    <div class="row">
                                        <div class="col-md-2 col-sm-2 scrollpoint sp-effect1"></div>
                                        <div class="col-md-8 col-sm-8 scrollpoint sp-effect1">
                                            <div class="row">
                                                <h4 style="color: #FFFFFF">You can use these tokens to connect your store with ATOMs.</h4>
                                                <hr />
                                                <h4 style="color: #FFFFFF">Server Token: <a href="#" onclick="regenServerToken();" style="color: #FFFFFF"><i class="fa fa-refresh pull-right"></i></a></h4> 
                                                <pre><?php echo $token['server_token']; ?></pre>
                                                <hr />
                                                <h4 style="color: #FFFFFF">Web Token: <a href="#" onclick="regenWebToken();" style="color: #FFFFFF"><i class="fa fa-refresh pull-right"></i></a></h4> 
                                                <pre><?php echo $token['web_token']; ?></pre>
                                                <hr />
                                                <div class="alert alert-danger" role="alert"><strong>FOR YOUR SECURITY PURPOSE, DO NOT LET ANYBODY KNOW THESE TOKENS!</strong></div>
                                            	<hr />
                                                <h5 style="color: #FFFFFF">Click the button below to generate your API code, then you must edit some code to adapt to your site by follow the written instructions to get it works.</h5> 
                                                <a href="template.php?template=api" target="_blank" class="btn btn-primary btn-lg btn-block">Generate API Code</a>
                                                <hr />
                                                <a href="?template=confirm" target="_blank" class="btn btn-primary btn-lg btn-block">Example Transfer Confirmation Form</a>
                                                <hr />
                                            </div>
                                            <div class="row">
                                                <h5 style="color: #FFFFFF">After upload API code to your site, please enter the location you have stored the API code below. (Begin with http:// or https://)</h5>
                                                <form role="form" action="update.php" method="post">
                                                <input type="hidden" id="action" name="action" value="editapiurl">
                                                <div class="well well-lg">
                                                    <div class="row">
                                                        <div class="row">
                                                            <div class="col-md-3 col-sm-3 scrollpoint sp-effect1"><h4 class="pull-right"><strong>API URL</strong></h4></div>
                                                            <div class="col-md-9 col-sm-9 scrollpoint sp-effect1">
                                                            <?php
                                                                $user = mysql_fetch_array(mysql_query("SELECT * FROM `user` WHERE `username` LIKE '".$_SESSION['username']."'"));
                                                            ?>
                                                            <input type="text" class="form-control" name="url" id="url" value="<?php echo $user['url_script']; ?>" required>
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
                                            <!-- here -->
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

        <script type="text/javascript">
		function regenServerToken() {
			var answer = confirm ("Are you sure to regenerate server token?")
			if (answer)
				window.location="update.php?regenservertoken";
		}

		function regenWebToken() {
			var answer = confirm ("Are you sure to regenerate web token?")
			if (answer)
				window.location="update.php?regenwebtoken";
		}
		</script>

<?php require_once("footer.php"); ?>