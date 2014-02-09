
CREATE TABLE IF NOT EXISTS  `a1733779_coupldb`.`users` (
	 `id` BIGINT NOT NULL AUTO_INCREMENT ,
	 `regid` VARCHAR( 250 ) NOT NULL ,
	 `firstname` VARCHAR( 45 ) NULL ,
	 `lastname` VARCHAR( 45 ) NULL ,
	 `email` VARCHAR( 200 ) NULL ,
	 `created_at` TIMESTAMP NULL ,
	PRIMARY KEY (  `id` )
);

CREATE TABLE  `a1733779_coupldb`.`messages` (
	 `id` BIGINT NOT NULL AUTO_INCREMENT ,
	 `type` TINYINT NULL ,
	 `action` TINYINT NULL ,
	 `data` TEXT NULL ,
	 `userId` BIGINT NOT NULL ,
	PRIMARY KEY (  `id` ,  `userId` ) ,
	INDEX  `fk_messages_users_idx` (  `userId` ASC ) ,
	CONSTRAINT  `fk_messages_users` FOREIGN KEY (  `userId` ) REFERENCES  `a1733779_coupldb`.`users` (
	`id`
	) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE  `a1733779_coupldb`.`user_friends` (
`user` BIGINT NOT NULL ,
`friend` BIGINT NOT NULL ,
`is_active` BOOL NOT NULL DEFAULT  '1',
PRIMARY KEY (  `user` ,  `friend` )
) ENGINE = MYISAM;


ALTER TABLE  `user_friends` ADD CONSTRAINT fk_user_friends_users_from FOREIGN KEY (  `user` ) REFERENCES  `a1733779_coupldb`.`users` (
`id`
);

ALTER TABLE  `user_friends` ADD CONSTRAINT fk_user_friends_users_from FOREIGN KEY (  `friend` ) REFERENCES  `a1733779_coupldb`.`users` (
`id`
);