package Model;

import android.icu.text.CaseMap;

public class Notices extends NoticeId{

    private String Title,Desc;
    private int Status, Count;

    public Notices(String Title, String Desc, int Status, int Count){
        this.Title = this.Title;
        this.Desc = this.Desc;
        this.Status = this.Status;
        this.Count = this.Count;
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

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}