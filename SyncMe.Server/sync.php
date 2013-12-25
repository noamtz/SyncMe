<?php

class Module{

	/* DATABASE OPERATIONS */
	
	function executeQuery($query){	
		include 'conn.php';
		$result = mysqli_query($con, $query);
		if(false===$result){
			echo $query; 
			printf("*executeQuery* error: %s\n", mysqli_error($con));
			
			return false;
		}
		return  $result;
	}

	function getQueryResult($query){
		$result = $this->executeQuery($query);
		$items = array();
		if($result != false && mysqli_num_rows($result) > 0){
			while($row = mysqli_fetch_object($result))
				array_push($items, $row);
		}
        return $items;
	}
	
	function insertQuery($query){
		include 'conn.php';
		$result = mysqli_query($con, $query);
		if(false===$result){
		
			printf("*insertQuery* error: %s\n", mysqli_error($con));
			echo $query;
			return false;
		}
		return mysqli_insert_id($con);
	}
	
	/* PUBLIC METHODS */
	
	function register($user){
		$regId = $user->regId;
		$firstname = $user->firstname;
		$lastname = $user->lastname;
		$email = $user->email;
		
		$query = "INSERT INTO `users` (`regid`, `firstname`, `lastname`, `email`, `created_at`) 
							VALUES ('$regId', '$firstname', '$lastname', '$email', NOW())";
							
		$this->executeQuery($query);
	}
	
	function registerFriend($userEmail, $friendEmail){
		$userId = $this->getUserId($userEmail);
		$friendId = $this->getUserId($friendEmail);
		
		$query = "INSERT INTO `user_friends` (`user`,`friend`) VALUES ($userId , $friendId), ($friendId , $userId)";
		return $this->executeQuery($query) != false;
	}
	
	function sync($senderEmail, $message){
		$senderId = $this->getUserId($senderEmail);
		$messageId = $this->saveMessage($senderId, $message);
		//$this->saveBroadcast($senderId , $messageId);
		return $this->broadcast($senderId, $message);
	}
	
	//Create permission to this method
	function getData($messageId){
		$query = "SELECT * FROM `messages` WHERE id = '$messageId'";
		$result = executeQuery($query);
		return $result;
	}
	/* PRIVATE METHODS */
	
		
	function broadcast($senderId, $message){
		include_once './GCM.php';
		
		$regIdRecivers = $this->getReciversRegId($senderId);
		$gcm = new GCM();
		//handle errors.
		$recievers = array();
		foreach($regIdRecivers as $reciever){
			array_push($recievers, $reciever->regid);
		}

		return $gcm->send_notification($recievers, $message);
	}
	
	function getReciversRegId($userId){
		include 'conn.php';
		$query = "SELECT `regid`
				  FROM `users`
				  INNER JOIN `user_friends`
				  ON `users`.`id`=`user_friends`.`user`
				  WHERE `user_friends`.`friend` = 1"; 

		return $this->getQueryResult($query);
        
	}

	function createMessage($senderId, $type, $action, $data){
		$query = "INSERT INTO `messages` (`type` ,`action` , `data`, `userId`) VALUES ('$type' ,'$action' , '$data', $senderId)";
		
		return $this->insertQuery($query);
	}
	
	function updateMessage($messageId, $data){
		$query = "UPDATE `messages` SET `data`='$data' WHERE id = '$messageId' ";
		
		return executeQuery($query);
	}
	
	function saveMessage($senderId, $message){
		$type = $message->type;
		$action = $message->action;
		$data = json_encode($message->data);

		$messageId = $this->createMessage($senderId, $type, $action, $data);
		return $messageId;
	}
	
	function messageRecieved($senderId, $messageId){
		$query = "DELETE FROM `messages` 
			      LEFT JOIN `users` ON `messages`.`userId` = `users`.`id` 
				  WHERE `users`.`email` = $senderId AND `messages`.`id` = $messageId ";
				  
		return executeQuery($query);
	}
	
	function getMessage($senderEmail, $messageId){
		$query = "SELECT `messages`.* FROM `messages` 
			      LEFT JOIN `users` ON `messages`.`userId` = `users`.`id` 
				  WHERE `users`.`email` = $senderId AND `messages`.`id` = $messageId ";
		$result = array();
		if(mysqli_num_rows($result) > 0)
			 push_array(mysqli_fetch_object($result));

		return result;
	}
	
	function saveBroadcast($senderId , $messageId){
		$query = "SELECT friend FROM user_friends WHERE `user` = $senderId";
		
		$recievers = $this->getQueryResult($query);
		//Prepare multiplte values.
		$values = "";
		if(count($recievers) > 0){
			foreach ($recievers as $value)
				$values = $values . "($senderId ,$value->friend,$messageId),";
			$values=rtrim ($values,",");
			$query = "INSERT INTO `broadcast_messages` (`sender`,`reciever`,`messageId`) VALUES $values ";
			return $this->executeQuery($query);
		}
		return false;
	}
	
	function getUserId($userEmail){
		$query = "SELECT * FROM `users` WHERE `email` = '$userEmail'";
		$result = $this->executeQuery($query);
		if(mysqli_num_rows($result) > 0)
			return mysqli_fetch_object($result)->id;
		return "No user id found";
	}

	function getMessages(){
		$query = "SELECT CONCAT(`users`.`firstname` ,' ', `users`.`lastname` )as Name,`messages`.`id` as MessageId , `messages`.`type`,`messages`.`data`,`messages`.`userId`
			      FROM `messages`
				  LEFT JOIN `users`
				  ON `messages`.`userId` = `users`.`id`";
		$result = $this->executeQuery($query);
		$messages = array();
		while($row = mysqli_fetch_object($result)){
			array_push($messages, $row);
		}
		return $messages;
	}
	
}
?>