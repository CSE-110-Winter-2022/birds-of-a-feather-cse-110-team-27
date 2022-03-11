package com.example.birdsofafeather;

import android.app.Service;
import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.parsers.WaveParser;

@RunWith(AndroidJUnit4.class)
public class WaveParserTest {

    @Test
    public void waveMessageParsed(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());
        AppDatabase db = AppDatabase.singleton(appContext);
        String message = "a4ca50b6-941b-11ec-b909-0242ac120002,,,,\n" +
                "Bill,,,,\n" +
                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n" +
                "2021,FA,CSE,210,Small\n" +
                "2022,WI,CSE,110,Large\n" +
                "4b295157-ba31-4f9f-8401-5d85d9cf659a,wave,,,";
        Service service = null;


        User ourUser = new User("Emil", "emil@ucsd.edu", "");
        User Bill = new User("Bill", "billiam@ucsd.edu", "");
        ourUser.uuid = "4b295157-ba31-4f9f-8401-5d85d9cf659a";
        Bill.uuid = "a4ca50b6-941b-11ec-b909-0242ac120002";
        long ourUserId = db.userWithCoursesDao().insert(ourUser);
        long billsUserId = db.userWithCoursesDao().insert(Bill);
        assertEquals(Bill.wavedToMe, false);

        WaveParser waveParser = new WaveParser();
        waveParser.parse(appContext, message, service, ourUserId);

        assertEquals(db.userWithCoursesDao().getUser(billsUserId).user.wavedToMe, true);
    }

    @Test
    public void invalidMessagesNotWaveParsed(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());
        AppDatabase db = AppDatabase.singleton(appContext);
        String message = "";
        Service service = null;


        User ourUser = new User("Emil", "emil@ucsd.edu", "");
        User Bill = new User("Bill", "billiam@ucsd.edu", "");
        ourUser.uuid = "4b295157-ba31-4f9f-8401-5d85d9cf659a";
        Bill.uuid = "a4ca50b6-941b-11ec-b909-0242ac120002";
        long ourUserId = db.userWithCoursesDao().insert(ourUser);
        long billsUserId = db.userWithCoursesDao().insert(Bill);
        assertEquals(Bill.wavedToMe, false);

        WaveParser waveParser = new WaveParser();
        waveParser.parse(appContext, message, service, ourUserId);

        assertEquals(db.userWithCoursesDao().getUser(billsUserId).user.wavedToMe, false);
    }

    @Test
    public void multipleWaveMessageParsed(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());
        AppDatabase db = AppDatabase.singleton(appContext);
        String billsWave = "a4ca50b6-941b-11ec-b909-0242ac120002,,,,\n" +
                "Bill,,,,\n" +
                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n" +
                "2021,FA,CSE,210,Small\n" +
                "2022,WI,CSE,110,Large\n" +
                "4b295157-ba31-4f9f-8401-5d85d9cf659a,wave,,,";
        Service service = null;


        User ourUser = new User("Emil", "emil@ucsd.edu", "");
        User Bill = new User("Bill", "billiam@ucsd.edu", "");
        ourUser.uuid = "4b295157-ba31-4f9f-8401-5d85d9cf659a";
        Bill.uuid = "a4ca50b6-941b-11ec-b909-0242ac120002";
        long ourUserId = db.userWithCoursesDao().insert(ourUser);
        long billsUserId = db.userWithCoursesDao().insert(Bill);
        assertEquals(Bill.wavedToMe, false);

        WaveParser waveParser = new WaveParser();
        waveParser.parse(appContext, billsWave, service, ourUserId);

        assertEquals(db.userWithCoursesDao().getUser(billsUserId).user.wavedToMe, true);

        User Will = new User("Will", "billiam@ucsd.edu", "");
        Will.uuid = "a4ca50b6-941b-11ec-b909-0242ac120001";
        String willsWave = "a4ca50b6-941b-11ec-b909-0242ac120001,,,,\n" +
                "Will,,,,\n" +
                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n" +
                "2021,FA,CSE,210,Small\n" +
                "2022,WI,CSE,110,Large\n" +
                "4b295157-ba31-4f9f-8401-5d85d9cf659a,wave,,,";
        long willsUserId = db.userWithCoursesDao().insert(Will);

        assertEquals(Will.wavedToMe, false);

        waveParser.parse(appContext, willsWave, service, ourUserId);

        assertEquals(db.userWithCoursesDao().getUser(willsUserId).user.wavedToMe, true);

    }
}
