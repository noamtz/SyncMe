/* SHOP LIST  */

CREATE TABLE item (
   id serial
   ,   name varchar(50)
   ,   categoryId Int 
   ,PRIMARY KEY (id)
   , FOREIGN KEY(categoryId) REFERENCES category(id)
);

CREATE TABLE category (
   id serial
   ,   name varchar(50)
   ,PRIMARY KEY (id)
);

CREATE TABLE shopList (
		shopListOverviewId Int
   ,    itemId Int
   ,    quantity Int DEFAULT 1
   ,	done Bit DEFAULT 0
   ,PRIMARY KEY (shopListOverview, itemId)
   ,FOREIGN KEY(itemId) REFERENCES item(id)
   ,FOREIGN KEY(shopListOverviewId) REFERENCES item(shopListOverview)
);

CREATE TABLE shopListOverview (
   id serial
   , title varchar(50)
   , totalItems Int
   , createdAt DATETIME DEFAULT CURRENT_TIMESTAMP
   ,PRIMARY KEY (id)
);

/* CALENDER   */



/* TODO LIST */