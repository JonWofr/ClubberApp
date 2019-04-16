package de.clubber_stuttgart.clubber.business_logic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;


public class HTTPHelperTest {

    @Test
    public void requestResponseServer() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        HTTPHelper httpHelper = new HTTPHelper();

        //This is actually needed to test a private method
        Method method = HTTPHelper.class.getDeclaredMethod("requestResponseServer", String.class, String.class);
        method.setAccessible(true);

        //These are the default arguments, which get passed when the table is not created yet and the server is requested to send data
        String output = (String) method.invoke(httpHelper, "null", "null");
        assertEquals("{\"events\" : [", output.substring(0, 13));
        assertEquals("]}", output.substring(output.length()-2, output.length()));

        //Tests if values, which exceed the maximum id saved in the online db cause problems
        output = (String) method.invoke(httpHelper, "999999999999999999999", "999999999999999999999");
        assertEquals("{\"events\" : [", output.substring(0, 13));
        assertEquals("]}", output.substring(output.length()-2, output.length()));

        //Tests if values, which are not numeric cause problems
        output = (String) method.invoke(httpHelper, "pisdjflkda", "pisdjflkda");
        assertEquals("{\"events\" : [", output.substring(0, 13));
        assertEquals("]}", output.substring(output.length()-2, output.length()));

        //Tests if float values, cause problems
        output = (String) method.invoke(httpHelper, "5325.23", "5325.23");
        assertEquals("{\"events\" : [", output.substring(0, 13));
        assertEquals("]}", output.substring(output.length()-2, output.length()));
    }
}