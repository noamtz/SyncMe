package syncme.app.data;

import syncme.app.App;

public class DAL {

	static DBHandler dbHandler;
	
	static DAL dal;
	
	private DataSource shopListOverView , items, categories;
	private DataSourceShopList shopList;
	
	private DAL(){
		dbHandler = new DBHandler(App.getAppCtx());
	}
	
	public static DAL getInstance(){
		if(dal == null)
			dal = new DAL();
		return dal;
	}
	
	//TODO: change table name and add another custom classes
	public DataSource getShopListOverview(){
		if(shopListOverView == null)
			shopListOverView = new DataSource(dbHandler,DBConstants.TABLE_SHOPLIST_OVERVIEW);
		return shopListOverView;
	}
	
	public DataSource getItems(){
		if(items == null)
			items = new DataSource(dbHandler,DBConstants.TABLE_ITEM);
		return items;
	}
	
	public DataSource getCategories(){
		if(categories == null)
			categories = new DataSource(dbHandler,DBConstants.TABLE_CATEGORY);
		return categories;
	}
	
	public DataSourceShopList getShopList(){
		if(shopList == null)
			shopList = new DataSourceShopList(dbHandler);
		return shopList;
	}
}
