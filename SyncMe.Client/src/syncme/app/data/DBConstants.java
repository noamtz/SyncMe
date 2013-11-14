package syncme.app.data;

public class DBConstants {
	
	public static final String DATABASE_NAME = "syncme.client.db";
	public static final int DATABASE_VERSION = 2;
	
	public static final String TABLE_SHOP_LIST = "shopList";
	public static final String VIEW_SHOP_LIST = "v_shopList";
	public static final String TABLE_ITEM = "item";
	public static final String TABLE_CATEGORY = "category";
	public static final String TABLE_SHOPLIST_OVERVIEW = "shopListOverview";
	//TODO: fill script file path.
	//init db script file path
	public static final String INIT_DB_SCRIPT_PATH = "init_db.sql";
	
	
	/* SHOP LIST OVERVIEW */
	public static final String SHOPLIST_OVERVIEW_ID = "_id";
	public static final String SHOPLIST_OVERVIEW_TITLE = "title";
	public static final String SHOPLIST_OVERVIEW_TOTALITEMS = "totalItems";
	//TODO: check if 'created_at' automatic created in db
	public static final String SHOPLIST_OVERVIEW_CREATED_AT = "createdAt"; 
}