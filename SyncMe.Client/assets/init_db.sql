/* SHOP LIST  */

CREATE TABLE category (
   id serail
   ,   name varchar(50)
   ,PRIMARY KEY (id)
);

CREATE TABLE item (
   id serail
   ,   name varchar(50)
   ,   categoryId Int 
   ,PRIMARY KEY (id)
   , FOREIGN KEY(categoryId) REFERENCES category(id)
);

CREATE TABLE shopListOverview (
   id serail
   , title varchar(50)
   , totalItems Int
   , createdAt DATETIME DEFAULT CURRENT_TIMESTAMP
   ,PRIMARY KEY (id)
);

CREATE TABLE shopList (
	shopListOverviewId Int
   ,    itemId Int
   ,    quantity Integer DEFAULT 1
   ,	done Bit DEFAULT 0
   ,PRIMARY KEY (shopListOverviewId , itemId)
   ,FOREIGN KEY(itemId) REFERENCES item(id)
   ,FOREIGN KEY(shopListOverviewId) REFERENCES shopListOverview(id)
);

CREATE VIEW v_shopList AS 
SELECT shopList.*, item.name as itemName, category.id as categoryId, category.name as categoryName
FROM shopList
INNER JOIN item
ON shopList.itemId=item.id
INNER JOIN category
ON item.categoryId=category.id;


/* CALENDER   */



/* TODO LIST */