package database.entities;

import android.content.Context;
import com.orm.SugarRecord;

public class FormulaEntities extends SugarRecord<Products>{

	public int mainid;
	public String description;
	public String lcl;
	public String lwl;
	public String target;
	public String lwu;
	public String lcu;
	public String parametercode;
	public int parentproduct;
	public String sd_ucl;

	public FormulaEntities(Context ctx) {
		super(ctx);
	}
	
	public FormulaEntities(Context ctx, int mainid, String description, String lcl, String lwl,
			String target, String lwu, String lcu, String parametercode, int parentproduct, String sd_ucl) {
		super(ctx);
		this.mainid = mainid;
		this.description = description;
		this.lcl = lcl;
		this.lwl = lwl;
		this.target = target;
		this.lwu = lwu;
		this.lcu = lcu;
		this.parametercode = parametercode;
		this.parentproduct = parentproduct;
		this.sd_ucl = sd_ucl;
	}
}