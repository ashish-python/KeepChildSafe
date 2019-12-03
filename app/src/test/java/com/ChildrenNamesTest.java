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

@RunWith(RobolectricTestRunner.class)
public class ChildrenNamesTest {
    private String childrenNames;

    @Before
    public void setup() throws IOException {
        childrenNames = JsonFileLoader.loadJsonFile("child_data.json", this.getClass().getClassLoader());
    }

    @Test
    public void testChildData() {
        try {
            JSONArray jsonArray = new JSONArray(childrenNames);
            JSONObject firstChildObject = jsonArray.getJSONObject(0);
            JSONObject secondChildObject = jsonArray.getJSONObject(1);
            assertEquals(firstChildObject.getString("id"), "5db8d8c1065e01661c60b88g");
            assertEquals(firstChildObject.getString("name"), "Cynthia");
            assertEquals(secondChildObject.getString("id"), "5db8d8c1065e01661c60b88f");
            assertEquals(secondChildObject.getString("name"), "Maya");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}