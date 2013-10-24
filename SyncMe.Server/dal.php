<?php

class DAL{

	function getUsers(){
		return $this->getUser(null);
	}
	
	function getUser($userId){
		include 'conn.php';

		$query = "SELECT users.*
				  FROM users, application 
				  WHERE application.role = 'user' AND application.userEmail = users.email";
		
		if(isset($userId))
			$query = $query . " AND email = '" . $userId . "'";
		
		$result = mysqli_query($con,$query);
									 
		$items = array();
		while($row = mysqli_fetch_object($result))
		{
  			array_push($items, $row);
		}
		return $items;
	}
	
	function getUserRoles($userId){
		include 'conn.php';
		
		$query = sprintf("SELECT idRoles
						  FROM users_has_roles
						  WHERE userEmail='%s'",$userId);
		$result = mysqli_query($con,$query);
		$items = array();
		while($row = mysqli_fetch_object($result))
		{
  			array_push($items, $row);
		}
		
		return $items;
	}

	function updateUsers($data){
		include 'conn.php';

		for($i=0;$i<count($data);$i++){
			$arr = $data[$i];	
			
			//Handle user's roles
			if(array_key_exists("RoleName",$arr)){
				$roles = $arr["RoleName"];
				if(count($roles) > 0 ){
					//Delete all users_has_roles of selected user
					$query_roles = sprintf("DELETE FROM users_has_roles
											WHERE userEmail='%s'",$arr['email']);
					if(!mysqli_query($con,$query_roles)){
						printf("dal.updateUser(delete roles): Error msg: %s\n", $con->error);
					}		
					$roles_values = "";
					//Insert new users roles
					for($i=0;$i<count($roles);$i++){
						$roles_arr = $roles[$i];
						$coma = "";
						if($i < (count($roles)-1))
							$coma = ", ";
						$roles_values = $roles_values . sprintf("('%s','%s')", $arr['email'], $roles_arr['RoleID']) . $coma ;

					}
				
					$query_roles = sprintf("INSERT INTO users_has_roles (userEmail, idRoles)
											VALUES %s",$roles_values);
					
					if(!mysqli_query($con,$query_roles)){
						printf("dal.updateUser(insert roles): Error msg: %s\n", $con->error);
					}	
				}
			}
			
			$userId = $arr["idUsers"];
			$values = sprintf("email='%s',firstname='%s',lastname='%s',phone='%s'", $arr['email'], $arr['firstname'], $arr['lastname'], $arr['phone']);
			
			if($i < (count($data)-1)){
				$values = $values . ", ";
			}

			$query = sprintf("UPDATE `users` 
							  SET %s 
							  WHERE idUsers='%s'",$values,$userId);
			if(!mysqli_query($con,$query)){
				printf("dal.updateUser: Error msg: %s\n", $con->error);
			}
		}
		
	}

	function createUsers($data){
		include 'conn.php';
		
		$result = array();

		for($i=0;$i<count($data);$i++){
			$arr = $data[$i];
			$values = sprintf("('%s','%s','%s','%s')", $arr['email'], $arr['firstname'], $arr['lastname'], $arr['phone']);
			
			$query = sprintf('INSERT INTO users (email, firstname, lastname, phone) 
							  VALUES %s', $values);
							  
			if(mysqli_query($con,$query)){			
				$arr["idUsers"] = $con->insert_id;	
				array_push($result, $arr);
			}
			//Create entry to the system for this user
			$this->createUserAuth($arr['email'], 123456);
		}
		return json_encode($result);
	}
	
	function createUserAuth($email, $password){
		include 'conn.php';
		
		$query = sprintf("INSERT INTO application (password, passwordSalt, role, userEmail)
				  VALUES ('%s','pslt','user','%s')", $password, $email);
				  
		if(!mysqli_query($con,$query)){
			printf("dal.createUserAuth: Error msg: %s\n", $con->error);
		}
	}
	
	function deleteUser($email){
		include 'conn.php';
		
		$query = sprintf("DELETE FROM users
						  WHERE email = '%s'", $email);
				  
		if(!mysqli_query($con,$query)){
			printf("dal.deleteUser: Error msg: %s\n", $con->error);
		}
	}

	function getShifts($from, $to){
		include 'conn.php';
		
		$query = "SELECT *
				  FROM shifts";
		if(isset($from) && isset($to))
			$query = sprintf("%s WHERE start AND end BETWEEN '%s' AND '%s'", $query, $from, $to);
		
		$rs = mysqli_query($con,$query);	
		
		$items = array();
		while($row = mysqli_fetch_object($rs)){
			array_push($items, $row);
		}
		
		return $items;
	}
	
	function updateShifts($data){
		include 'conn.php';
		for($i=0;$i<count($data);$i++){
			$arr = $data[$i];	
	
			$shiftId = $arr["idShifts"];
			$values = sprintf("start='%s',end='%s'", $arr['start'], $arr['end']);
	
			if($i < (count($data)-1)){
				$values = $values . ", ";
			}

			$query = sprintf("UPDATE `shifts` 
							  SET %s 
							  WHERE idShifts='%s'",$values,$shiftId) ;

			if(!mysqli_query($con,$query)){
				printf("dal.updateShifts: Error msg: %s\n", $con->error);
			}

		}		
	}

	function createShifts($data){
		include 'conn.php';
		$result = array();
		for($i=0;$i<count($data);$i++){
			$arr = $data[$i];	
			$query = sprintf("INSERT INTO shifts (start,end) 
							  VALUES ('%s','%s')",$arr['start'], $arr['end']);
			if(mysqli_query($con,$query)){
				$arr['idShifts'] = $con->insert_id;
				array_push($result, $arr);
			}else{
				echo mysqli_error($con);
			}
		}
		return $result;
	}
	
	function getShiftParts($shiftId){
		include 'conn.php';

		$query = sprintf("SELECT idShifts ,  CONCAT(firstname , ' ' , lastname) as uname , type as urole, idUsers , shiftPartId
						  FROM shiftPart, users, roles
						  WHERE idShifts='%s' AND userEmail =  users.email AND roles.idRoles = shiftPart.idRoles", $shiftId);

		$result = mysqli_query($con,$query);
		if(!$result){
			printf("dal.getShiftParts: Error msg: %s\n", $con->error);
		}
		$items = array();
		while($row = mysqli_fetch_object($result))
		{
	  		array_push($items, $row);
		}
		return $items;
	}
	
	function getRoles($userEmail){
		include 'conn.php';
		
		$query = "SELECT  idRoles as RoleID, type as RoleName 
				  FROM roles";
		if(isset($userEmail))
			$query = sprintf("SELECT roles.idRoles as RoleID, Roles.type as RoleName
							  FROM roles, users_has_roles 
							  WHERE userEmail = '%s' AND users_has_roles.idRoles = roles.idRoles", $userEmail);
		
		$result = mysqli_query($con,$query);
		$items = array();
		while($row = mysqli_fetch_object($result))
		{
	  		array_push($items, $row);
		}
		return $items;
	}
	
	function getUsersNames(){
		include 'conn.php';
		
		$query = "SELECT email as idUsers , CONCAT(firstname , ' ' , lastname) as name 
				  FROM `users`, application
				  WHERE application.role = 'user' AND application.userEmail = users.email";
				  
		$result = mysqli_query($con,$query);
		$items = array();
		while($row = mysqli_fetch_object($result))
		{
	  		array_push($items, $row);
		}
		return $items;
	}
	
	function createShiftPart($data){
		include 'conn.php';
		
		$result = array();

		$arr = $data[0];
		$values = sprintf("('%s','%s','%s',NULL)", $arr['idShifts'], $arr['idUsers'], $arr['idRoles']);
		$query = sprintf('INSERT INTO shiftPart (idShifts, userEmail, idRoles, shiftPartId) 
						  VALUES %s', $values);
		if(mysqli_query($con,$query)){			
			$arr["shiftPartId"] = $con->insert_id;	
			array_push($result, $arr);
		}else
			echo "Create shift part in db is failed " . mysqli_error($con) . "   ";
		
		return json_encode($result);
	}
	
	function updateShiftParts($data){
		include 'conn.php';
		$arr = $data[0];	
	
		$shiftId = $arr["idShifts"];
		$values = sprintf("userEmail='%s',idRoles='%s'", $arr['idUsers'], $arr['idRoles']);
	
		$query = sprintf("UPDATE `shiftPart` 
						  SET %s 
						  WHERE idShifts='%s'",$values,$shiftId) ;

		if(!mysqli_query($con,$query)){
			printf("dal.updateShiftParts: Error msg: %s\n", $con->error);
		}		
	}
	
	function deleteShiftPatrs($shiftPartId){
		include 'conn.php';
		
		$query = sprintf("DELETE FROM `shiftPart`  
						  WHERE shiftPartId='%s'",$shiftPartId) ;
		
		if(!mysqli_query($con,$query)){
			printf("dal.deleteShiftPatrs: Error msg: %s\n", $con->error);
		}
	}
	
	function handleUserRequests($createReq, $deleteReq){
		if(isset($createReq)){
			$cols = array("idShifts", "userEmail", "request_time");
			$this->insert_statement("shift_request", $cols, $createReq, null);
		}
		if(isset($deleteReq)){
			$cols = array("idShifts" , "userEmail");
			$this->delete_statement("shift_request", $cols, $deleteReq);
		}
	}
	
	function getUserRequest($email , $from, $to){
		include 'conn.php';
		$query = "SELECT *
				  FROM shift_request
				  WHERE userEmail = '" . $email . "'";
				  
		$result = mysqli_query($con,$query);
		$request = array();
		
		while($row = mysqli_fetch_object($result))
  			array_push($request, $row);
			
		$shifts = $this->getShifts($from, $to);
		
		for($i=0; $i<count($request); $i++){
			for($j=0; $j<count($shifts); $j++){
				if($request[$i]->idShifts == $shifts[$j]->idShifts){
					 $shifts[$j]->part = true;
				}
			}
		}
		return $shifts;
	}
	
	function delete_statement($table, $cols, $values){
		include 'conn.php';
		if(is_array($values)){
			if(is_array($values[0])){
				for($i=0; $i<count($values);$i++){
					$val = $values[$i];
					$values[$i] = "('" . implode("', '",$val) . "')";
				}
				$values = "(" . implode(", ", $values) . ")";
			}
			else
				$values = "('" . implode("', '", $values) . "')";
		}
		if(is_array($cols))
			$cols = "(" . implode(", ", $cols) . ")";
		$query = sprintf("DELETE FROM %s WHERE %s IN %s", $table, $cols, $values);
		
		if(!mysqli_query($con, $query)){
			printf("dal.delete_statement: Error msg: %s\n", $con->error);
			echo $query;
		}
		
	}
	
	function insert_statement($table, $cols, $values , $where){
		include 'conn.php';
		//cheking if we in single insert or multiple
		//by cheking if the first element is an array
		if(is_array($values[0]))
			$query = $this->insertMultipleRows($table, $cols, $values , $where);
		else
			$query = $this->insertSingleRow($table, $cols, $values , $where);
		
		if(!mysqli_query($con,$query)){
			printf("dal.insert_statement: Error msg: %s\n", $con->error);
		}
	}
	
	function insertSingleRow($table, $cols, $values , $where){
		$values = "'" . implode("', '",$values) . "'" ;
		return sprintf("INSERT INTO %s(%s) VALUES (%s)", $table, implode(", ",$cols) , $values);
	}
	
	function insertMultipleRows($table, $cols, $vals , $where){
		
		for($i=0; $i<count($vals); $i++) {
			$value = $vals[$i];
			$vals[$i] = "('" . implode("', '",$value) . "')";
		}
		
		$query = sprintf("INSERT INTO %s(%s) VALUES %s", $table, implode(", ",$cols) , implode(", ",$vals));
		
		if(isset($where))
			$query = $query . " WHERE " . $where;
			
		return $query;
	}
}
?>
