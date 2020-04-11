package twitch;

public class User {

    public String getUserbyName(String username) {

        MakeRequest r = new MakeRequest();
        String[] list = r.doRequest("users?login=" + username);

        for (String string : list) {
            if (string.contains("id")) {
                string = string.replace("\"", "");
                string = string.substring(4);
                return string;
            }
        }
        return null;
    }

    public String getUserbyID(String ID) {

        MakeRequest r = new MakeRequest();
        String[] list = r.doRequest("users/" + ID);

        for (String string : list) {
            if (string.contains("display_name")) {
                string = string.replace("\"", "");
                string = string.substring(14);
                return string;
            }
        }
        return null;
    }
}