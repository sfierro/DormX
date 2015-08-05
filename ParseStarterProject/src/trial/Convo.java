package trial;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Convo")
public class Convo extends ParseObject {

    public ParseUser getBuyer() {
        return getParseUser("buyer");
    }

    public void setBuyer(ParseUser user) {
        put("buyer", user);
    }

    public ParseUser getSeller() { return getParseUser("seller"); }

    public void setSeller(ParseUser seller) { put("seller",seller);}

    public void setID(String ID) {put("ID",ID);}

    public String getID() {return getString("ID");}

}