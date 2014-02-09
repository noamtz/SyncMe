package coupling.app.models;

import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import coupling.app.Ids;
import static coupling.app.data.Constants.*;

public class ShopListOverView extends BaseModel{

	private String title;
	
	public ShopListOverView(Cursor c){
		ids = new Ids(c);
		title =  c.getString(c.getColumnIndexOrThrow(TITLE));
		isMine = c.getInt(c.getColumnIndexOrThrow(IS_MINE)) == 1;	
		isLocked = c.getInt(c.getColumnIndexOrThrow(IS_LOCKED)) == 1;	
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public ContentValues toDb() {
		ContentValues values = super.toDb();
		if(title != null)
			values.put(TITLE, title);
		return values;
	}

	@Override
	public Map<String, Object> toNetwork() {
		Map<String,Object> data = super.toNetwork();
		if(title != null)
			data.put(IS_DONE, title);
		return data;
	}
}
