package coupling.app.data;

public class Enums {

	public enum HttpType{
		GET(1), POST(2);

		private int value;
		private HttpType (int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
	
	public enum CategoryType {
	    NOT_DEFINED(0), SHOPLIST_OVERVIEW(1), SHOPLIST(2), CALENDER(3);

		private int value;
		private CategoryType (int value) {
			this.value = value;
		}
		public String getValue() {
			return Integer.toString(value);
		}
		public int value() {
			return value;
		}
		public void setValue(int val){
			value = val;
		}
	}
	
	public enum ActionType {
		 NOT_DEFINED(0), CREATE(1), UPDATE(2), DELETE(3);

		private int value;
		private ActionType (int value) {
			this.value = value;
		}
		public String getValue() {
			return Integer.toString(value);
		}
		public int value(){
			return value;
		}
		public void setValue(int val){
			value = val;
		}
	}
	
	public static ActionType toActionType(int action){
		switch (action) {
		case 1: return ActionType.CREATE;
		case 2: return ActionType.UPDATE;
		case 3: return ActionType.DELETE;
		}
		return ActionType.NOT_DEFINED;
	}
	
	public static CategoryType toCategoryType(int category){
		switch (category) {
		case 1: return CategoryType.SHOPLIST_OVERVIEW;
		case 2: return CategoryType.SHOPLIST;
		case 3: return CategoryType.CALENDER;
		}
		return CategoryType.NOT_DEFINED;
	}
}
