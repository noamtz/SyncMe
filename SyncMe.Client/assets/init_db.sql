CREATE TABLE item(
   id serial
   ,   name varchar(50)
   ,   categoryId Int 
   ,PRIMARY KEY (id)
   , FOREIGN KEY(categoryId) REFERENCES category(id)
);

CREATE TABLE category(
   id serial
   ,   name varchar(50)
   ,PRIMARY KEY (id)
);

CREATE TABLE shopListitems(
	shopListId Int
   ,    itemId Int
   ,    quantity Int DEFAULT 1
   ,	done Bit DEFAULT 0
   ,PRIMARY KEY (shopListId, itemId)
   ,FOREIGN KEY(itemId) REFERENCES item(id)
   ,FOREIGN KEY(shopListId) REFERENCES item(shopList)
);

CREATE TABLE shopList(
   id serial
   ,PRIMARY KEY (id)
);