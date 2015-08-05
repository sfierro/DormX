package trial;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {

    public ParseUser getAuthor() {
        return getParseUser("Author");
    }

    public ParseUser getSeller() {return getParseUser("seller");}

    public String getBody() {
        return getString("body");
    }

    public void setBody(String body) {
        put("body", body);
    }

    public void setID(String ID) {put("ID",ID);}

    public String getID() {return getString("ID");}

}