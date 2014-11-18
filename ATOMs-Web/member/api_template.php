<?php
/*
*   require_once this file when you need to add or update order
*   You need to modify all "Edit here" to complete this script
*/

$server_token   = 'replace_server_token_here';
$web_token      = 'replace_web_token_here';

function receive_update($order_id, $status)
{
    /*  Edit this function to update database from data sent by ATOMs server.
    *
    *   order_id is id of order in ATOMs server
    *   status 0 = not paid
    *   status 1 = paid
    *   status 2 = shiped
    */

    // Edit here


    if($success)
        return true;
    else
        return false;

}

function add_order($money, $status, $invoice_url = '')
{
    /*  Call this function when you create order in your server
    *
    *   status 0 = not paid
    *   status 1 = paid
    *   status 2 = shiped
    */

    global $web_token;
    $response = file_get_contents("https://www.diyby.me/android-connect/receive-update.php?token=".$web_token."&order_id=0&status=".$status."&invoice_url=".$invoice_url."&money=".$money);
    echo $response;
    if(is_numeric($response))
    {
        $order_id = $response;
        /* Store return order_id from ATOMs server to your database
        *
        * Edit here
        */
    }
}

function send_update($order_id, $status)
{
    /*  Call this function when you update order status in your server
    *
    *   order_id is id of order in ATOMs server
    */

    global $web_token;
    $response = file_get_contents("http://www.diyby.me/android-connect/receive-update.php?token=".$web_token."&order_id=".$order_id."&status=".$status);
    echo $response;
}

function confirm_order($order_id, $money, $bank, $time)
{
    /*  Call this function when buyer send confirmation when transfered money.
    *
    *   order_id is id of order in ATOMs server if not convert order_id to id in ATOMs server
    */

    // Edit here if order_id is not order_id in ATOMS server

    if (!isset($_COOKIE['attempt']))
    {
        setcookie("attempt", 0, time()+1000 );
    }
    if(isset($_COOKIE['attempt']) and $_COOKIE['attempt'] > 5)
    {
        echo "maximun attempt reach.";
        return false;
    }

    global $web_token;
    $response = file_get_contents("http://www.diyby.me/android-connect/confirm_order.php?token=".$web_token."&order_id=".$order_id."&money=".$money."&bank=".$bank."&time=".$time);
    $response = json_decode($response, true);
    if($response['success'] == 0)  // failed confirm
    {
        setcookie("attempt", $_COOKIE['attempt'] + 1, time()+1000 );
        return false;
    }
    else //confirm success
    {
        /*
           Edit here
           Update order status in your server to paid
        */
        return true;
    }
}

if(!empty($_GET['atoms_token']) and $_GET['atoms_token'] == sha1($server_token))
{
    if(receive_update($_GET['order_id'], $_GET['status']))
        echo 'OK';
    else
        echo 'Failed';
}

?>