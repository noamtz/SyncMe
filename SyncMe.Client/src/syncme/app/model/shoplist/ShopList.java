package syncme.app.model.shoplist;

import java.util.List;

public class ShopList {
	
	private long id;
	private ShopListOverview overview;
	//TODO: change to cursor
	private List<Item> items;
	
	
	public ShopListOverview getOverview() {
		return overview;
	}
	public void setOverview(ShopListOverview overview) {
		this.overview = overview;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	
}
