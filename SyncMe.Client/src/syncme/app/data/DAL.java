package syncme.app.data;

import syncme.app.App;

public class DAL {

	static DBHandler dbHandler;
	
	static DAL dal;
	
	DataSource shopList , item, category;
	
	private DAL(){
		dbHandler = new DBHandler(App.getAppCtx());
	}
	
	public static DAL getInstance(){
		if(dal == null)
			dal = new DAL();
		return dal;
	}
	
	//TODO: change table name and add another custom classes
	public DataSource getShopList(){
		if(shopList == null)
			shopList = new DataSource(dbHandler,DBConstants.TABLE_SHOP_LIST);
		return shopList;
	}
}
