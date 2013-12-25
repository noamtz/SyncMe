

<html>
<head>

<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<div class="container" style="margin-top:15px;">
		 <?php
			include 'sync.php';
			$module = new Module();
		?>
	<div class="row" style="margin:15px;">
		<h2 style="color:#909090;">Coupling Dash Board</h2>
	</div>
	<div class="row">
		<h3>Send Message:</h3>
		<form class="form-inline" role="form">
			  <div class="form-group">
				<label class="sr-only" for="exampleInputEmail2">Email address</label>
				<input type="email" class="form-control" id="exampleInputEmail2" placeholder="Enter email">
			  </div>
			  <div class="form-group">
				<label class="sr-only" for="exampleInputPassword2">Password</label>
				<input type="password" class="form-control" id="exampleInputPassword2" placeholder="Password">
			  </div>
			  <div class="checkbox">
				<label>
				  <input type="checkbox"> Remember me
				</label>
			  </div>
			  <button type="submit" class="btn btn-default">Sign in</button>
		</form>
	</div>
	<hr>
	<div class="row">
		<table class="table table-hover">
			 <thead>
				<th>Sender Name</th>
				<th>MessageId</th>
				<th>Type</th>
				<th>Data</th>
				<th>UserId</th>
			 </thead>
			 <tbody>
				 <?php
					$messages = $module->getMessages();
					foreach ($messages as $obj) {
					?>
						<tr>	
						<td><?php echo $obj->Name;?></td>
						<td><?php echo $obj->MessageId;?></td>
						<td><?php echo $obj->type;?></td>
						<td><?php echo $obj->data;?></td>	
						<td><?php echo $obj->userId;?></td>
						</tr>
					<?php } ?> 
			 </tbody>
		</table>
	</div>
	<hr>
	<div class="row">
		<table class="table table-hover">
			 <thead>
				<th>Sender Name</th>
				<th>MessageId</th>
				<th>Type</th>
				<th>Data</th>
				<th>UserId</th>
			 </thead>
			 <tbody>
				 <?php
					$messages = $module->getMessages();
					foreach ($messages as $obj) {
					?>
						<tr>	
						<td><?php echo $obj->Name;?></td>
						<td><?php echo $obj->MessageId;?></td>
						<td><?php echo $obj->type;?></td>
						<td><?php echo $obj->data;?></td>	
						<td><?php echo $obj->userId;?></td>
						</tr>
					<?php } ?> 
			 </tbody>
		</table>
	</div>

</div>
<script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>

<script>
	function sendMessage(){
		
	}
</script>

</body>
</html>
