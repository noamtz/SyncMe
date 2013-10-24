<?php

include 'sync.php';

$response = array();
$module = new Module();
if(isset($_POST["method"]) && isset($_POST["params"])){
	$method = $_POST["method"];
	$params = json_decode($_POST["params"]);
	
	
	if(strcmp($method , "register") == 0){	
			$user = $params;
			$module->register($user);
			$response["statusMessage"] = "Registration has succeeded";
	}else if(strcmp($method , "sync") == 0){
	
		$senderEmail = $params["email"];
		$message = json_decode($params["message"]);
		$module->sync($senderEmail, $message);
		$response["method"] = "sync";
		
	}else if(strcmp($method , "broadcast") == 0){
	
		//PRIVATE METHOD
		$senderId = $params["senderId"];
		$message = $params["message"];
		$module->broadcast($senderId, $message);
		$response["method"] = "broadcast";
		
	}else if(strcmp($method , "registerFriend") == 0){
	
		$userEmail = $params["email"];
		$friendEmail = $params["friendEmail"];
		$module- registerFriend($userEmail, $friendEmail);
		$response["method"] = "registerFriend";
		
	}else if(strcmp($method , "getUserId") == 0){
	
		//PRIVATE METHOD
		$userEmail = $params["email"];
		$module- getUserId($userEmail);
		$response["method"] = "getUserId";
		
	}else if(strcmp($method , "saveBroadcast") == 0){
		//PRIVATE METHOD
		$senderId = $params["senderId"];
		$messageId = $params["messageId"];
		$module->saveBroadcast($senderId , $messageId);
		$response["method"] = "saveBroadcast";
		
	}else if(strcmp($method , "saveMessage") == 0){
		
		//PRIVATE METHOD
		$senderId = $params["senderId"];
		$message = $params["message"];
		$module->saveMessage($senderId, $message);
		$response["method"] = "saveMessage";
		
	}else if(strcmp($method , "getReciversRegId") == 0){
		
		//PRIVATE METHOD
		$userId = $params["userId"];
		$module->getReciversRegId($userId);
		$response["method"] = "getReciversRegId";
		
	}else {
		$response["error"] = $method . " : unimplemented method";
	}
} else{
	$response["error"] = "Please choose a method and params"; 
}


echo json_encode($response);

?>