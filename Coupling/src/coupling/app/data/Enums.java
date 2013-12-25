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
		SHOPLIST_OVERVIEW(1) , SHOPLIST(2), CALENDER(3);

		private int value;
		private CategoryType (int value) {
			this.value = value;
		}
		public String getValue() {
			return Integer.toString(value);
		}
	}
	
	public enum ActionType {
		CREATE(1), UPDATE(2), DELETE(3);

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
	}
}
