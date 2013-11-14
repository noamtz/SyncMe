

CREATE TABLE category (
   _id INTEGER PRIMARY KEY AUTOINCREMENT
   ,   name varchar(50)
);

 CREATE TABLE item (
   _id INTEGER PRIMARY KEY AUTOINCREMENT
   ,   name varchar(50)
   ,   categoryId Int 
   , FOREIGN KEY(categoryId) REFERENCES category(_id)
);

 CREATE TABLE shopListOverview (
   _id INTEGER PRIMARY KEY AUTOINCREMENT
   , title varchar(50)
   , totalItems Int DEFAULT 0
   , createdAt DATETIME DEFAULT CURRENT_TIMESTAMP
);

 CREATE TABLE shopList (
	shopListOverviewId Int
   ,    itemId Int
   ,    quantity Integer DEFAULT 1
   ,	done Bit DEFAULT 0
   ,PRIMARY KEY (shopListOverviewId , itemId)
   ,FOREIGN KEY(itemId) REFERENCES item(_id)
   ,FOREIGN KEY(shopListOverviewId) REFERENCES shopListOverview(_id)
);

 CREATE VIEW v_shopList AS 
 SELECT shopList.*, item.name as itemName, category._id as categoryId, category.name as categoryName 
 FROM shopList 
 INNER JOIN item 
 ON shopList.itemId=item._id 
 INNER JOIN category 
 ON item.categoryId=category._id; 

