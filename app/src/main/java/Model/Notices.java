package Model;

import android.icu.text.CaseMap;

public class Notices extends NoticeId{

    private String Title,Desc;
    private int Status;

    public Notices(String Title, String Desc, int Status){
        this.Title = this.Title;
        this.Desc = this.Desc;
        this.Status = this.Status;
    }

    public Notices(){
    }

    public String getTitle() {
        return Title;
    }

    public String getDesc() {
        return Desc;
    }

    public int getStatus() {
        return Status;
    }
}