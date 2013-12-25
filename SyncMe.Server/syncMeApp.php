<?php

include 'sync.php';

$response = array();
$module = new Module();
if(isset($_POST["method"]) && isset($_POST["params"])){
	$method = $_POST["method"];
	//TODO: Becareful with casting if there is nested objects..
	$params = (array) json_decode($_POST["params"]);

	switch((string)$method){
		case "register":
			$user = json_decode($_POST["params"]);//TODO: Refactor this..
			$module->register($user);
			$response["statusMessage"] = "Registration has succeeded";
		break;
		
		case "sync":
			$senderEmail = $params["email"];
			$message = $params["message"];
			$res = $module->sync($senderEmail, $message);
			$response["method"] = "sync";
			$response["statusMessage"] = $res;
		break;
		
		case "registerFriend":
			$userEmail = $params["email"];
			$friendEmail = $params["friendEmail"];
			$res = $module->registerFriend($userEmail, $friendEmail);
			$response["method"] = "registerFriend";
			if($res)
				$response["statusMessage"] = "$friendEmail is now connected to $userEmail";
			else
				$response["error"] = "Failed to connect $friendEmail to $userEmail";
		break;
		
		case "messageRecieved":
			$senderEmail = $params["email"];
			$messageId =  $params["messageId"];
			$res = $module->messageRecieved($senderEmail, $messageId);
			$response["method"] = "messageRecieved";
		break;
		
		case "getMessage":
			$senderEmail = $params["email"];
			$messageId =  $params["messageId"];
			$res = $module->getMessage($senderEmail, $messageId);
			$response["method"] = "getMessage";
			$response["message"] = json_encode($res);
		break;
		default:
			$response["error"] = "Undefined method"; 
	}
} else{
	$response["error"] = "Please choose a method and params"; 
}


echo json_encode($response);

?>