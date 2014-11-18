<?php 
if(isset($_GET['template'])){
    require_once("../functions.php");
    $token = mysql_fetch_array(mysql_query("SELECT * FROM `user` WHERE `username` LIKE '".$_SESSION['username']."'"));
    $file = file_get_contents('./'.$_GET['template'].'_template.php', FILE_USE_INCLUDE_PATH);
    $file = str_replace("replace_server_token_here", $token['server_token'], $file);
    $file = str_replace("replace_web_token_here", $token['web_token'], $file);
    echo '<pre>'.htmlspecialchars($file).'</pre>';
    die();
}

require_once("header.php"); 

?>
                                <?php
                                    $token = mysql_fetch_array(mysql_query("SELECT * FROM `user` WHERE `username` LIKE '".$_SESSION['username']."'"));
                                ?>
                                <div class="col-md-8">
                                    <div class="row">
                                        <div class="col-md-2 col-sm-2 scrollpoint sp-effect1"></div>
                                        <div class="col-md-8 col-sm-8 scrollpoint sp-effect1">
                                            <div class="row">
                                                <h4 style="color: #FFFFFF">You can use following template to connect your store with ATOMs.</h4>
                                                <hr />
                                                <a href="?template=api" target="_blank" class="btn btn-primary btn-lg btn-block">API Code</a>
                                                <hr />
                                                <a href="?template=confirm" target="_blank" class="btn btn-primary btn-lg btn-block">Example Transfer Confirmation Form</a>
                                                <hr />
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