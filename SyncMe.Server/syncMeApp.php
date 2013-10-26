<?php

include 'sync.php';

$response = array();
$module = new Module();
if(isset($_POST["method"]) && isset($_POST["params"])){
	$method = $_POST["method"];
	//TODO: Becareful with casting if there is nested objects..
	$params = (array) json_decode($_POST["params"]);
	
	
	if(strcmp($method , "register") == 0){	
		$user = json_decode($_POST["params"]);//TODO: Refactor this..
		$module->register($user);
		$response["statusMessage"] = "Registration has succeeded";
	}else if(strcmp($method , "sync") == 0){
	
		$senderEmail = $params["email"];
		$message = $params["message"];
		$res = $module->sync($senderEmail, $message);
		$response["method"] = "sync";
		$response["statusMessage"] = $res;
		
	}else if(strcmp($method , "broadcast") == 0){
	
		//PRIVATE METHOD
		$senderId = $params["senderId"];
		$message = $params["message"];
		$module->broadcast($senderId, $message);
		$response["method"] = "broadcast";
		
	}else if(strcmp($method , "registerFriend") == 0){
	
		$userEmail = $params["email"];
		$friendEmail = $params["friendEmail"];
		$res = $module->registerFriend($userEmail, $friendEmail);
		$response["method"] = "registerFriend";
		$response["statusMessage"] = $res ? "$friendEmail is now connected to $userEmail" : "register $friendEmail to $userEmail has failed";
		
	}else if(strcmp($method , "getUserId") == 0){
	
		//PRIVATE METHOD
		$userEmail = $params["email"];
		$module->getUserId($userEmail);
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