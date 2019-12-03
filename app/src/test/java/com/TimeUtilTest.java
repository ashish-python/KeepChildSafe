package com;

import com.parentapp.utils.TimeUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for methods in {@link TimeUtil} class.
 */
@RunWith(RobolectricTestRunner.class)
public class TimeUtilTest {

    /**
     * Test to convert local time string to utc.
     */
    @Test
    public void testGetFormattedDateTimeUTC1() {
        String date1Str = "2017-07-24T11:30:20.056-05:00";
        String date2Str = "2017-07-24T16:30:20.056Z";
        assertEquals(TimeUtil.getFormattedDateTimeUTC(date1Str).toString(), date2Str);
    }

    /**
     * Test to convert local time long to utc
     */
    @Test
    public void testGetFormattedDatetimeUTC2() {
        String date1Str = "2017-07-24T16:30:20.056Z";
        assertEquals(TimeUtil.getFormattedDateTimeUTC(Long.parseLong("1500913820056")).toString(), date1Str);

    }

    /**
     * Test to convert local time date object to utc
     */
    @Test
    public void testGetFormattedDatetimeUTC3() {
        Date date = new Date();
        date.setTime(1500913820056L);
        String date1Str = "2017-07-24T16:30:20.056Z";
        assertEquals(TimeUtil.getFormattedDateTimeUTC(date).toString(), date1Str);
    }

    }