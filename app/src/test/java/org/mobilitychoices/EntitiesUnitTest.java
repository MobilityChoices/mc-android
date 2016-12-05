package org.mobilitychoices;

import org.json.JSONObject;
import org.junit.Test;
import org.mobilitychoices.entities.Location;
import org.mobilitychoices.entities.Token;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class EntitiesUnitTest {

    @Test
    public void token_fromJson() throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", "TestToken");
        System.out.println(jsonObject);
        assertEquals("TestToken", jsonObject.getString("token"));
        Token token = new Token();
        token.fromJSON(jsonObject);
        assertEquals("TestToken", token.getToken());
    }

    @Test
    public void token_toJson() throws Exception{
        Token token = new Token();
        token.setToken("TestToken");

        JSONObject jsonObject = token.toJSON();
        assertEquals(true, jsonObject != null);
        assertEquals("TestToken", jsonObject.getString("token"));
    }

    @Test
    public void location_toJSON() throws Exception{
        Location location = new Location(52.0, 141.0, 0.0, 0.0, 1477402488);
        JSONObject jsonObject = location.toJSON();
        assertEquals(true, jsonObject != null);
        assertEquals(true, jsonObject.getDouble("latitude") == 52.0);
        assertEquals(true, jsonObject.getDouble("longitude") == 141.0);
        assertEquals(true, jsonObject.getDouble("altitude") == 0.0);
        assertEquals(true, jsonObject.getDouble("speed") == 0.0);
        assertEquals(true, jsonObject.getDouble("time") == 1477402488);
    }

}