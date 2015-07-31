package trial;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {

    public ParseUser getAuthor() {
        return getParseUser("Author");
    }

    public String getBody() {
        return getString("body");
    }

    public void setAuthor(ParseUser user) {
        put("Author", user);
    }

    public void setBody(String body) {
        put("body", body);
    }
}