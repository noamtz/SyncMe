package coupling.app.BL;

import java.util.HashMap;
import java.util.Map;

import coupling.app.Utils;



public class BLFactory {

	private static BLFactory blFactory;
	
	private Map<Long, BLShopList> shopLists;
	private BLShopListOverview blShopListOverview;
	private BLGroceryList blGroceryList;
	
	private BLFactory(){
		shopLists = new HashMap<Long, BLShopList>();
	}
	
	public static BLFactory getInstance(){
		if(blFactory == null)
			blFactory = new BLFactory();
		return blFactory;
	}
	
	public BLShopList getShopList(Long listId){
		BLShopList blShopList = shopLists.get(listId);
		if(blShopList == null && listId != null){
			Utils.Log("BLFactory", "ListId: " + listId);
			blShopList = new BLShopList(listId);
			shopLists.put(listId, blShopList);
		}
		return blShopList;
	}
	
	public BLShopListOverview getShopListOverview(){
		if(blShopListOverview == null)
			blShopListOverview = new BLShopListOverview();
		return blShopListOverview;
	}
	
	public BLGroceryList getGroceryList(){
		if (blGroceryList == null)
			blGroceryList = new BLGroceryList();
		return blGroceryList;
	}
	
}
