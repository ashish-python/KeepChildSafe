package com;

import android.util.Log;

import com.parentapp.utils.JsonFileLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import static junit.framework.TestCase.assertTrue;


@RunWith(RobolectricTestRunner.class)
public class ParentDataTest {
    private String sampleGeofencesData;

    @Before
    public void setup() throws IOException {
        sampleGeofencesData = JsonFileLoader.loadJsonFile("parent_data.json", this.getClass().getClassLoader());
    }

    @Test
    public void testX() {
        try {
            JSONObject jsonObject = new JSONObject(sampleGeofencesData);
            assertTrue(jsonObject.has("cycles"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}