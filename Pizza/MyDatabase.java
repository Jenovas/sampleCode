package kupon.dominion.pizza;

import java.util.ArrayList;
import java.util.TreeMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase extends SQLiteOpenHelper {

	static final String dbName = "myDB";
	static final String tCoupons = "coupons";
	static final String fCouponId = "id";
	static final String fCouponName = "name";
	static final String fCouponDecription = "description";
	static final String fCouponURL = "image_url";
	static final String fCouponStart = "date_start";
	static final String fCouponEnd = "date_End";
	static final String fCouponPrice = "price";
	static final String fCouponActive = "active";
	static final String fCouponVisible = "visible";
	
	static final String tNot = "notifications";
	static final String fNotId = "id";
	static final String fNotTitle = "title";
	static final String fNotMessage = "message";
	static final String fNotDate = "date";
	
	static final String tOption = "options";
	static final String fOptionId = "id";
	static final String fOptionName = "name";
	static final String fOptionEnabled = "enabled";

	public MyDatabase(Context context) {
		// THE VALUE OF 1 ON THE NEXT LINE REPRESENTS THE VERSION NUMBER OF THE DATABASE
		// IN THE FUTURE IF YOU MAKE CHANGES TO THE DATABASE, YOU NEED TO INCREMENT THIS NUMBER
		// DOING SO WILL CAUSE THE METHOD onUpgrade() TO AUTOMATICALLY GET TRIGGERED
		super(context, dbName, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// ESTABLISH NEW DATABASE TABLES IF THEY DON'T ALREADY EXIST IN THE DATABASE
		db.execSQL("CREATE TABLE IF NOT EXISTS "+tCoupons+" (" +
				fCouponId + " TEXT , " +
				fCouponName + " TEXT , " +
				fCouponDecription + " TEXT , " +
				fCouponURL + " TEXT , " +
				fCouponStart + " TEXT , " +
				fCouponEnd + " TEXT , " +
				fCouponPrice + " TEXT , " +
				fCouponActive + " TEXT , " +
				fCouponVisible + " TEXT" +
				/*fWho + " TEXT, " +*/
				")");

		db.execSQL("CREATE TABLE IF NOT EXISTS "+tNot+" (" +
				fNotId + " INTEGER PRIMARY KEY , " +
				fNotTitle + " TEXT , "   +
				fNotMessage + " TEXT , "   +
				fNotDate + " INTEGER "   +
				")");   
		
		db.execSQL("CREATE TABLE IF NOT EXISTS "+tOption+" (" +
				fOptionId + " INTEGER PRIMARY KEY , " +
				fOptionName + " TEXT , "   +
				fOptionEnabled + " INTEGER "   +
				")"); 
		
		ContentValues cv1 = new ContentValues();
		cv1.put(fOptionId, 1);
		cv1.put(fOptionName, "Enable Notifications");
		cv1.put(fOptionEnabled, 1);
		db.insert(tOption, null, cv1);
		/*             
		 * 
		 * MORE ADVANCED EXAMPLES OF USAGE
		 *
                db.execSQL("CREATE TRIGGER fk_empdept_deptid " +
                                " BEFORE INSERT "+
                                " ON "+employeeTable+                          
                                " FOR EACH ROW BEGIN"+
                                " SELECT CASE WHEN ((SELECT "+colDeptID+" FROM "+deptTable+" WHERE "+colDeptID+"=new."+colDept+" ) IS NULL)"+
                                " THEN RAISE (ABORT,'Foreign Key Violation') END;"+
                                "  END;");

                db.execSQL("CREATE VIEW "+viewEmps+
                                " AS SELECT "+employeeTable+"."+colID+" AS _id,"+
                                " "+employeeTable+"."+colName+","+
                                " "+employeeTable+"."+colAge+","+
                                " "+deptTable+"."+colDeptName+""+
                                " FROM "+employeeTable+" JOIN "+deptTable+
                                " ON "+employeeTable+"."+colDept+" ="+deptTable+"."+colDeptID
                                );
		 */                             
	}

	public void DeleteKuponTable()
	{
		SQLiteDatabase myDB = this.getWritableDatabase();
		myDB.execSQL("DELETE FROM "+tCoupons);
	}
	
	public void AddKupon(String ID, String NAME, String DESCRIPTION, String URL, String START, String END, String PRICE, String ACTIVE)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		// Alarm 1
		cv.put(fCouponId, ID);
		cv.put(fCouponName, NAME);
		cv.put(fCouponDecription, DESCRIPTION);
		cv.put(fCouponURL, URL);
		cv.put(fCouponStart, START);
		cv.put(fCouponEnd, END);
		cv.put(fCouponPrice, PRICE);
		cv.put(fCouponActive, ACTIVE);
		cv.put(fCouponVisible, ""+1);
		db.insert(tCoupons, null, cv);
		
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// THIS METHOD DELETES THE EXISTING TABLE AND THEN CALLS THE METHOD onCreate() AGAIN TO RECREATE A NEW TABLE
		// THIS SERVES TO ESSENTIALLY RESET THE DATABASE
		// INSTEAD YOU COULD MODIFY THE EXISTING TABLES BY ADDING/REMOVING COLUMNS/ROWS/VALUES THEN NO EXISTING DATA WOULD BE LOST
		db.execSQL("DROP TABLE IF EXISTS "+tCoupons);
		db.execSQL("DROP TABLE IF EXISTS "+tNot);
		onCreate(db);
	}

	public void setTicketUnactive(String iD)
	{
		SQLiteDatabase myDB = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(fCouponVisible, ""+0);
		myDB.update(tCoupons, cv, fCouponId+"=?", new String []{String.valueOf(iD)});
	}
	
	public int getNotEnabled()
	{
		SQLiteDatabase myDB = this.getReadableDatabase();
		String[] mySearch = new String[]{String.valueOf(1)};
		Cursor myCursor = myDB.rawQuery("SELECT "+ fOptionEnabled + " FROM " + tOption +" WHERE " + fOptionId +"=?",mySearch);
		myCursor.moveToFirst();
		int index = myCursor.getColumnIndex(fOptionEnabled);
		int myAnswer = myCursor.getInt(index);
		myCursor.close();
		return myAnswer;
	} 
	
	public void setNotEnabled(int Enabled)
	{
		SQLiteDatabase myDB = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(fOptionEnabled, Enabled);
		myDB.update(tOption, cv, fOptionId+"=?", new String []{String.valueOf(1)});
	}
	
	public ArrayList<TreeMap<String, String>> getCouponList()
	{	
		int i = 0;
		ArrayList<TreeMap<String, String>> mylist = new ArrayList<TreeMap<String, String>>();
		SQLiteDatabase myDB = this.getReadableDatabase();
		Cursor myCursor = myDB.rawQuery("SELECT "+ "*" + " FROM " + tCoupons, null);
		if (myCursor.moveToFirst())
		{
			do
			{
				int index;
				
				index =  myCursor.getColumnIndex(fCouponId);
				String ID = myCursor.getString(index);
				
				index =  myCursor.getColumnIndex(fCouponName);
				String NAME = myCursor.getString(index);
				
				index =  myCursor.getColumnIndex(fCouponDecription);
				String DESCRIPTION = myCursor.getString(index);
				
				index =  myCursor.getColumnIndex(fCouponURL);
				String URL = myCursor.getString(index);
				
				index =  myCursor.getColumnIndex(fCouponStart);
				String START = myCursor.getString(index);
				
				index =  myCursor.getColumnIndex(fCouponEnd);
				String END = myCursor.getString(index);
				
				index =  myCursor.getColumnIndex(fCouponPrice);
				String PRICE = myCursor.getString(index);
				
				index =  myCursor.getColumnIndex(fCouponActive);
				String ACTIVE = myCursor.getString(index);
				
				index =  myCursor.getColumnIndex(fCouponVisible);
				String VISIBLE = myCursor.getString(index);
				
				TreeMap<String, String> sortedMap = new TreeMap<String, String>();
				sortedMap.put("GUID", ""+i);
				sortedMap.put("ID", ID);
				sortedMap.put("NAME", NAME);
				sortedMap.put("DESCRIPTION", DESCRIPTION);
				sortedMap.put("URL", URL);
				sortedMap.put("START", START);
				sortedMap.put("END", END);
				sortedMap.put("PRICE", PRICE);
				sortedMap.put("ACTIVE", ACTIVE);
				sortedMap.put("VISIBLE", VISIBLE);
				mylist.add(sortedMap);
				i++;
			} while (myCursor.moveToNext());
		}
		myCursor.close();
		return mylist;
	}
}