package syncme.app.model.shoplist;

import java.util.List;

import syncme.app.data.DAL;

public class ShopList {

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

	public String getTitle(){
		return overview.getTitle();
	}

	public void setTitle(String title){
		overview.setTitle(title);
	}

	public void addItem(Item item){
		if(item.getId() == -1)
			item.setId(DAL.getInstance().getItems().create(item.toDB()));
		if(DAL.getInstance().getShopList().createItem(overview.getId(), item))
			items.add(item);
	}

	public void deleteItem(Item item){
		if(DAL.getInstance().getShopList().deleteItem(overview.getId(), item.getId()))
			items.remove(item);
	}

}
