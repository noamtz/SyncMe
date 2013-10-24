<?php
class Module{
	// DATABASE OPERATIONS
	function executeQuery($query){	
		include 'conn.php';
		$result = mysqli_query($con, $query);
		if(false===$result){
			printf("error: %s\n", mysqli_error($con));
			return false;
		}
		return  $result;
	}

	function getQueryResult($query){
		$result = $this->executeQuery($query);
		$items = array();
        while($row = mysqli_fetch_object($result))
            array_push($items, $row);
        return $items;
	}
	
	function insertQuery($query){
		include 'conn.php';
		$result = mysqli_query($con, $query);
		if(false===$result){
			printf("error: %s\n", mysqli_error($con));
			return false;
		}
		return mysqli_insert_id($con);
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


	// LOGIC MIXED OPERATIONS
	
	function register($user){
		$regId = $user->regId;
		$firstname = $user->firstname;
		$lastname = $user->lastname;
		$email = $user->email;
		
		$query = "INSERT INTO `users` (`regid`, `firstname`, `lastname`, `email`, `created_at`) 
							VALUES ('$regId', '$firstname', '$lastname', '$email', NOW())";
							
		$this->executeQuery($query);
	}
	
	function getReciversRegId($userId){
		include 'conn.php';
		$query = "SELECT `regid`
				  FROM `users`
				  INNER JOIN `user_friends`
				  ON `users`.`id`=`user_friends`.`user`
				  WHERE `user_friends`.`friend` = 1;"; 

		return $this->getQueryResult($query);
        
	}
	
	function broadcast($senderId, $message){
		include_once './GCM.php';
		
		$regIdRecivers = $this->getReciversRegId($senderId);
		$gcm = new GCM();
		//handle errors.
		foreach($regIdRecivers as $reciever)
			$result = $gcm->send_notification($regIdReciver, $message);
	}
	
	function createMessage($senderId, $type, $data){
		$query = "INSERT INTO `messages` (`type`, `data`, `userId`) VALUES ('$type', '$data', $senderId)";
		
		return insertQuery($query);
	}
	
	function updateMessage($messageId, $data){
		$query = "UPDATE `messages` SET `data`='$data' WHERE id = '$messageId' ";
		
		return executeQuery($query);
	}
	
	function saveMessage($senderId, $message){
		$type = $message['type'];
		$data = json_encode($message['data']);
		
		if(array_key_exists("messageId",$message)){
			$messageId = $message["messageId"];
			$this->updateMessage($messageId, $data);
		}
		else	
			$messageId = $this->createMessage($senderId, $type, $data);
		return $messageId;
	}
	
	function saveBroadcast($senderId , $messageId){
		$query = "SELECT * FROM user_friends WHERE `user` = $senderId";
		
		$recievers = $this->getQueryResult($query);
		//Prepare multiplte values.
		$values = "";
		foreach ($recievers as $value)
			$values = $values . "(". implode(", ",$value). " , $messageId),";
		$values=eregi_replace(',$', '', $values);
		
		$query = "INSERT INTO `broadcast_messages` (`sender`,`reciever`,`messageId`) VALUES $values ";
		$this->executeQuery($query);
	}
	
	function registerFriend($userEmail, $friendEmail){
		$userId = getUserId($userEmail);
		$friendId = getUserId($friendEmail);
		
		$query = "INSERT INTO `user_friends` (`user`,`friend`) VALUES ($userId , $friendId), ($friendId , $userId)";
		$this->executeQuery($query);
	}
	
	function getUserId($userEmail){
		$query = "SELECT * FROM `users` WHERE email = $userEmail";
		$result = $this->executeQuery($query);
		return mysqli_fetch_object($result)['id'];
	}
	
	function sync($senderEmail, $message){
		$senderId = $this->getUserId($senderEmail);
		$messageId = $this->saveMessage($senderId, $message);
		$this->saveBroadcast($senderId , $messageId);
		$this->broadcast($senderId, $message);
	}
	

}
?>