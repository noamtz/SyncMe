<?php
class Module{
	function executeQuery($query){
		include 'conn.php';
		$result = mysqli_query($con, $query);
		if(false===$result){
			printf("error: %s\n", mysqli_error($con));
			return false;
		}
		return  $result;
	}

	function prepareMessage($message ){
		$message = json_decode($_POST["message"]);
		
		$from = $message->from;
		$to = $message->to;
		$messageContentObj = json_decode($message->content);

		$type = $messageContentObj->type;
		$data = json_encode($messageContentObj->data);
		$messageContent = array('from'=>$from, 'to'=>$to, 'type'=>$type, 'data'=>$data);
		if(property_exists($messageContentObj, 'messageId')){
			$messageId = json_encode($messageContentObj->messageId);
			$messageContent["messageId"] = $messageId;
		}
		
		return $messageContent;
	}
	
	function saveInDb($messageContent){
		$type = $messageContent["type"];
		$data = $messageContent["data"];
		if(array_key_exists("messageId",$messageContent))
			$messageId = $messageContent["messageId"];

		$from = $messageContent['from'];
		$to = $messageContent['to'];	
			
		//UPDATE
		if(isset($messageId)){
			$query = "UPDATE `messages` SET `data`='$data' WHERE id = '$messageId' ";
		}
		//CREATE
		else{
			$query = "INSERT INTO `messages` (`from`,`to`,`type`,`data`) VALUES ('$from','$to','$type','$data') ";
		}
		//execute
		return $this->executeQuery($query);
	}


	function getUserRegId($userId){
		include 'conn.php';
		$query = "SELECT `regid` FROM `users` WHERE `email` = '$userId'"; 

		//execute;
		$result = $this->executeQuery($query);
        $items = array();
        while($row = mysqli_fetch_object($result))
        {
            array_push($items, $row);
        }
        return $items;
	}

	
	function sync($message){
		$messageContent = $this->prepareMessage(message);
		$from = $messageContent['from'];
		$to = $messageContent['to'];
		
		saveInDb($messageContent);

		$regIdReciver = array(getUserRegId($to));//for now return only one
		include_once './GCM.php';
		 
		$gcm = new GCM();
	 
		$message = array("from" => $from, "to" => $to, "content" => $message );
	 
		$result = $gcm->send_notification($regIdReciver, $message);
		
	}

}
$response = array();
$module = new Module();
if(isset($_POST["method"])){
	$method = $_POST["method"];
	if(strcmp($method , "register") == 0){	
		//echo $module->getUserRegId("noam@gmail.com")[0]->regid;
		$response["method"] = "register";
	} else if(strcmp($method , "sync") == 0){
		$message = $_POST["message"];	
		$messageContent = $module->prepareMessage($message);
		//$res = $module->saveInDb($messageContent);
		$response["method"] = "sync";
		
	} else {
		$response["error"] = $method . " : unimplemented method";
		
	}
} else{
	$response["error"] = "Please choose a method"; 
}


echo json_encode($response);



?>