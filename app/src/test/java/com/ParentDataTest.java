package com;

import com.parentapp.utils.JsonFileLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


@RunWith(RobolectricTestRunner.class)
public class ParentDataTest {
    private String sampleGeofencesData;

    @Before
    public void setup() throws IOException {
        sampleGeofencesData = JsonFileLoader.loadJsonFile("parent_data.json", this.getClass().getClassLoader());
    }

    @Test
    public void testParentData() {
        try {
            JSONObject jsonObject = new JSONObject(sampleGeofencesData);
            assertTrue(jsonObject.has("children"));
            JSONArray childrenArray = jsonObject.getJSONArray("children");
            JSONObject firstChildObject = childrenArray.getJSONObject(0);
            assertTrue(firstChildObject.has("registeredGeofences"));
            JSONObject firstChildGeofencesObject = firstChildObject.getJSONObject("registeredGeofences");
            assertEquals(firstChildGeofencesObject.getString("geofenceId"), "5db8d8c1065e01661c60b88g");
            assertEquals(firstChildGeofencesObject.getDouble("lat"), 40.742965);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}