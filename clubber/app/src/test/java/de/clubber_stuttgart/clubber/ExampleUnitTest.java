package de.clubber_stuttgart.clubber;

import android.app.Activity;
import android.content.Intent;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void downloadServiceQueueIdTester(){
        assertEquals(0.0, DownloadServiceJson.getQueueId(),0);



    }
}