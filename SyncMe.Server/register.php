<?php
 /* This file receives requests from android device and stores the user in the database.*/

 
/**
 * Registering a user device
 * Store reg id in users table
 */
 
function executeQuery($query){
		include 'conn.php';
		$result = mysqli_query($con, $query);
		if(false===$result){
			printf("error: %s\n", mysqli_error($con));
			return false;
		}
		return  $result;
}
 
 function storeUser($regInfo){
	include 'conn.php';
	print_r($regInfo);
	$regId = $regInfo->regid;
	$firstname = $regInfo->firstname;
	$lastname = $regInfo->lastname;
	$email = $regInfo->email;
	
	$query = "INSERT INTO `users` (`regid`, `firstname`, `lastname`, `email`, `created_at`) 
						VALUES ('$regId', '$firstname', '$lastname', '$email', NOW())";
						
	
	if(executeQuery($query) == false){
		echo json_encode(array("error" => "failed to register user in the database"));
	}
 }
 
 $regInfo = json_decode($_POST['registeration']);//check isset
 storeUser($regInfo);
 
 //return error json.
?>