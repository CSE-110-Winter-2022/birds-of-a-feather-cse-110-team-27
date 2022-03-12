package com.example.birdsofafeather;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.parsers.NearbyUserParser;
import com.example.birdsofafeather.utils.Constants;

import org.junit.Test;

import java.util.List;

public class NearbyUserParseTest {
    @Test
    public void parseUserTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);

        String Bill = "a4ca50b6-941b-11ec-b909-0242ac120204,,,,\n" +
                "Bill,,,,\n" +
                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n" +
                "2021,FA,CSE,210,Small\n" +
                "2022,WI,CSE,110,Large\n" +
                "2020,FA,MATH,18,Small";

        NearbyUserParser parser = new NearbyUserParser();
        parser.parse(appContext, Bill, null, 2);
        assertTrue(db.userWithCoursesDao().getUserForUUID("a4ca50b6-941b-11ec-b909-0242ac120204").getName().equals("Bill"));
        assertTrue(db.userWithCoursesDao().getUserForUUID("a4ca50b6-941b-11ec-b909-0242ac120204").getProfilePictureUrl().equals("https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0"));
    }

    @Test
    public void parseCoursesTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);

        String Bill = "a4ca50b6-941b-11ec-b909-0242ac120000,,,,\n" +
                "Bill,,,,\n" +
                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n" +
                "2021,FA,CSE,210,Small\n" +
                "2022,WI,CSE,110,Large\n" +
                "2020,FA,MATH,18,Small";

        NearbyUserParser parser = new NearbyUserParser();
        parser.parse(appContext, Bill, null, 2);

        List<Course> course_list = db.userWithCoursesDao().getUserForUUID("a4ca50b6-941b-11ec-b909-0242ac120000").getCourses();

        assertEquals(course_list.get(0).getYear(), 2021);
        assertTrue(course_list.get(0).getQuarter().equals("FALL"));
        assertTrue(course_list.get(0).getDepartment().equals("CSE"));
        assertEquals(course_list.get(0).getCourseNumber(), 210);
        assertEquals(course_list.get(0).getSize(), Constants.sizes[2]);

        assertEquals(course_list.get(1).getYear(), 2022);
        assertTrue(course_list.get(1).getQuarter().equals("WINTER"));
        assertTrue(course_list.get(1).getDepartment().equals("CSE"));
        assertEquals(course_list.get(1).getCourseNumber(), 110);
        assertEquals(course_list.get(1).getSize(), Constants.sizes[4]);

        assertEquals(course_list.get(2).getYear(), 2020);
        assertTrue(course_list.get(2).getQuarter().equals("FALL"));
        assertTrue(course_list.get(2).getDepartment().equals("MATH"));
        assertEquals(course_list.get(2).getCourseNumber(), 18);
        assertEquals(course_list.get(2).getSize(), Constants.sizes[2]);
    }

    @Test
    public void parseMultipleUsers() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);

        String Timmy = "a4ca50b6-941b-11ec-b909-0242ac120004,,,,\n" +
                "Timmy,,,,\n" +
                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n" +
                "2021,FA,CSE,210,Small\n" +
                "2022,WI,CSE,110,Large\n" +
                "2020,FA,MATH,18,Small";

        String Will = "a4ca50b6-941b-11ec-b909-0242ac120005,,,,\n" +
                "Will,,,,\n" +
                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n" +
                "2021,FA,CSE,210,Small\n" +
                "2022,WI,CSE,110,Large";

        NearbyUserParser parser = new NearbyUserParser();
        parser.parse(appContext, Timmy, null, 10);
        parser.parse(appContext, Will, null, 11);

        assertTrue(db.userWithCoursesDao().getUserForUUID("a4ca50b6-941b-11ec-b909-0242ac120004").getName().equals("Timmy"));
        assertTrue(db.userWithCoursesDao().getUserForUUID("a4ca50b6-941b-11ec-b909-0242ac120005").getName().equals("Will"));
    }
}