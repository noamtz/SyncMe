<?php
 /* This file receives requests from android device and stores the user in the database.*/

 
/**
 * Registering a user device
 * Store reg id in users table
 */
 
 
 $regInfo = json_decode($_POST['registeration']);//check isset
 
 
 
 function storeUser($regInfo){
	include 'conn.php';
	
	$redId = $regInfo['regId'];
	$firstname = $regInfo['firstname'];
	$lastname = $regInfo['lastname'];
	$email = $regInfo['email'];
	
	$query = "INSERT INTO users (regid, firstname, lastname, email, created_at) 
						VALUES ('$regId', '$firstname', '$lastname', '$email', NOW())";
						
	
	
	//execute
	
 }
 
 storeUser($regInfo);
 
 //return error json.
?>