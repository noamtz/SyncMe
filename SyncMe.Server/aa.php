<?php

	$query = "INSERT INTO `users`(`regid`, `firstname`, `lastname`, `email`, `created_at`) VALUES ('sdad3424df435-fgh5' , 'avi', 'levy', 'avi@gmail.com', NOW())";
	include 'conn.php';
	$result = mysqli_query($con, $query);
	if(false===$result){
		printf("error: %s\n", mysqli_error($con));
		return false;
	}
	echo mysqli_insert_id($con);
?>