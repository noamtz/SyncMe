<?php

function saveInDb($messageContent, $from, $to){
	$type = $messageContent["type"];
	$data = $messageContent["data"];
	if(array_key_exists("messageId",$messageContent))
		$messageId = $messageContent["messageId"];
		
	//UPDATE
	if(isset($messageId)){
		$query = "UPDATE messages SET (data='$data') WHERE id = '$messageId' ";
	}
	//CREATE
	else{
		$query = "INSERT INTO messages (from,to,type,data) VALUES ('$from','$to','$type','$data') ";
	}
	//execute
}

function getUserRegId($userId){
	include 'conn.php';
	$query = "SELECT regid FROM users WHERE email = '$userId'"; 
	//execute;
	return  '';
}

function sync($messageContent, $from, $to){
	
	saveInDb($messageContent, $from, $to);

	$regIdReciver = array(getUserRegId($to));//for now return only one
	include_once './GCM.php';
     
    $gcm = new GCM();
 
    $message = array("from" => $from, "to" => $to, "content" => $message );
 
    $result = $gcm->send_notification($regIdReciver, $message);
	
}


$message = json_decode($_POST["message"]);
$from = $message['from'];
$to = $message['to'];
$messageContent = json_decode($message['content']);

sync($messageContent, $from, $to);

//return an error json.

?>