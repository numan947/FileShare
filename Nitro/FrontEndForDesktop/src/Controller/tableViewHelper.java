package Controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

/**
 * Created by numan947 on 10/29/16.
 **/
public class tableViewHelper {
    private StringProperty member1=null;
    private StringProperty member2=null;
    private ObjectProperty<File> member3=null;

    public tableViewHelper(String a, String b,File f){
        this.member1=new SimpleStringProperty(a);
        this.member2=new SimpleStringProperty(b);
        this.member3=new SimpleObjectProperty<>(f);
    }

    public File getMember3() {
        return member3.get();
    }

    public ObjectProperty<File> member3Property() {
        return member3;
    }

    public void setMember3(File member3) {
        this.member3.set(member3);
    }

    public String getMember1() {
        return member1.get();
    }

    public StringProperty member1Property() {
        return member1;
    }

    public void setMember1(String member1) {
        this.member1.set(member1);
    }

    public String getMember2() {
        return member2.get();
    }

    public StringProperty member2Property() {
        return member2;
    }

    public void setMember2(String member2) {
        this.member2.set(member2);
    }
}
