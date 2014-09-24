package database.entities;

import android.content.Context;
import com.orm.SugarRecord;

public class ApkData extends SugarRecord<ApkData> {

    public String serveradres;
    public String bagadres;
    public String formulapicadres;
    public String xlsadres;
    public String formulaspecadres;
    public String login;
    public String password;
    
    public ApkData(Context ctx) {
        super(ctx);
    }

    public ApkData(Context ctx, String serveradres, String bagadres,
            String formulapicadres, String xlsadres, String formulaspecadres,
            String login, String password) {
        super(ctx);
        this.serveradres = serveradres;
        this.bagadres = bagadres;
        this.formulapicadres = formulapicadres;
        this.xlsadres = xlsadres;
        this.formulaspecadres = formulaspecadres;
        this.login = login;
        this.password = password;
    }
}
