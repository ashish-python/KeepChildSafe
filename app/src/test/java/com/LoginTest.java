package com;

import com.parentapp.activities.SignInActivity;
import com.parentapp.utils.NetworkPostRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
public class LoginTest {

    NetworkPostRequest networkPostRequest = Mockito.mock(NetworkPostRequest.class);
    SignInActivity signInActivity = Mockito.mock(SignInActivity.class);

    @Before
    public void setup() throws IOException {

    }

    @Test
    public void testLoginPass() {

        /*
        Context context =  signInActivity.getApplicationContext();
        // Let's do a synchronous answer for the callback
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((BaseListener)invocation.getArguments()[0]).callback(context, 200, "success");
                return null;
            }
        }).when(signInActivity).signIn(
                "kat", "kat");

        // Let's call the method under test
        signInActivity.signIn("", "");

        // Verify state and interaction
        Mockito.verify(networkPostRequest, Mockito.times(1)).signIn(
                any(SignInActivity.class));
        Mockito.assertThat(signInActivity.getResult(), is(equalTo(results)));

         */
    }

}
