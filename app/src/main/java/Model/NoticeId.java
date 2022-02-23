package Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class NoticeId {
    @Exclude
    public String NoticeId;

    public  <T extends  NoticeId> T withId(@NonNull final String Id){
        this.NoticeId = Id;
        return  (T) this;
    }

}