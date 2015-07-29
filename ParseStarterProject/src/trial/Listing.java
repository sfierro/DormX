package trial;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
/*
 * An extension of ParseObject that makes
 * it more convenient to access information
 * about a given Listing
 */
@ParseClassName("Listing")
public class Listing extends ParseObject {

    public Listing() {
        // A default constructor is required.
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser user) {
        put("author", user);
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile file) {

        put("photo", file);
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setPrice(String price) {
        put("price", price);
    }

    public String getPrice() {
        return getString("price");
    }

    public void setLocation(ParseGeoPoint location) {
        put("location", location);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setPos(int pos) {
        put("pos", pos);
    }

    public int getPos() {
        return getInt("pos");
    }

    public void setCategory(String category) {
        put("category", category);
    }

    public String getCategory() {
        return getString("category");
    }

}
