package de.smoodi.projectchip.util;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MinecraftUtil {

    /**
     * Returns a JSON object based on the API request made. Can be null if failed.
     * @param url
     * @return
     */
    private static JSONObject getJSONObject(String url)
    {
        JSONObject obj;

        try
        {
            String c = Unirest.get(url).asString().getBody();
            if (c == null) return null;
            obj = (JSONObject) new JSONParser().parse(c);
            String err = (String) (obj.get("error"));
            if (err != null)
            {
                return null;
                /*
                switch (err)
                {
                    case "IllegalArgumentException":
                        throw new IllegalArgumentException((String) obj.get("errorMessage"));
                    default:
                        throw new RuntimeException(err);
                }
                 */
            }
        }
        catch (ParseException | UnirestException e)
        {
            throw new RuntimeException(e);
        }

        return obj;
    }

    /**
     * Returns the current UUID of a Minecraft Username. Can be null.
     * @param username
     * @return
     */
    public static String getUUIDOfUsername(String username)
    {
        JSONObject obj = getJSONObject("https://api.mojang.com/users/profiles/minecraft/" + username);
        if(obj != null) return (String) obj.get("id");
        else return null;
    }

    /**
     * Returns the UUID of a username back at a given moment in time. (E.g. when the name was later reclaimed by someone else). Can be null.
     * @param username
     * @param timestamp
     * @return
     */
    public static String getUUIDOfUsername(String username, String timestamp)
    {
        JSONObject obj = getJSONObject("https://api.mojang.com/users/profiles/minecraft/" + username + "?at=" + timestamp);
        if(obj != null) return (String) obj.get("id");
        else return null;
    }

    /**
     * Returns the player name based on the uuid.
     * @param uuid
     * @return
     */
    public static String getPlayerName(String uuid) {
        JSONObject obj = getJSONObject("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
        if(obj != null) return (String) obj.get("name");
        else return "undefined";
    }

}
