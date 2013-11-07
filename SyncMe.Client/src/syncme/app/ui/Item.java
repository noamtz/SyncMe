package syncme.app.ui;

import java.io.Serializable;

public class Item implements Serializable{

	private static final long serialVersionUID = -3165905995770621828L;
	
	private String item;
	private int count;
	private boolean isChecked;
	
	public Item(String item ,int count)
	{
		this.setItem(item);
		this.setCount(count);
		this.setIsChecked(false);
	}
	
	public boolean getIsChecked()
	{
		return this.isChecked;
	}
	
	public void setIsChecked(boolean isChecked)
	{
		this.isChecked = isChecked;
	}
	
	public int getCount()
	{
		return this.count;
	}
	
	public void setCount(int count)
	{
		this.count = count;
	}
	
	public String getItem()
	{
		return this.item;
	}
	
	public void setItem(String item)
	{
		this.item = item;
	}

}
