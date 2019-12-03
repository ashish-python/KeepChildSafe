package com;

import com.parentapp.activities.SignInActivity;
import com.parentapp.listeners.BaseListener;
import com.parentapp.utils.JsonFileLoader;
import com.parentapp.utils.NetworkPostRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
public class LoginTest {

    NetworkPostRequest networkPostRequest = Mockito.mock(NetworkPostRequest.class);

    @Before
    public void setup() throws IOException {

    }

    @Test
    public void testLoginPass() {
       
    }
}
