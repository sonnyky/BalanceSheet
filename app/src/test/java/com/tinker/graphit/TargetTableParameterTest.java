package com.tinker.graphit;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sonny.kurniawan on 2016/05/03.
 */
public class TargetTableParameterTest {

    private TargetTableParameter testTargetTable;
    private String testURL = "http://yahoo.com";
    private String testUserAccount = "blitzkrieg.burner@gmail.com";
    private String testTableName = "Expenses";
    private String testAxisColumnNumber = "1";
    private String testDataColumnNumber = "3";

    @Before
    public void setUp() throws Exception {
        testTargetTable = new TargetTableParameter();
    }

    @Test
    public void testSetUrl() throws Exception {
        String actual = testTargetTable.setUrl("http://yahoo.com");
        assertNotNull(actual);
        assertEquals(testURL,actual);
    }

    @Test
    public void testSetUserAccount() throws Exception {
        String actual = testTargetTable.setUserAccount("blitzkrieg.burner@gmail.com");
        assertNotNull(actual);
        assertEquals(testUserAccount, actual);
    }

    @Test
    public void testSetTableName() throws Exception {
        String actual = testTargetTable.setTableName("Expenses");
        assertNotNull(actual);
        assertEquals("Table Name not equal",testTableName, actual);
    }

    @Test
    public void testSetAxisColumnNumber() throws Exception {
        String actual = testTargetTable.setAxisColumnNumber("1");
        assertNotNull(actual);
        assertEquals("Axis column number not equal",testAxisColumnNumber, actual);
    }

    @Test
    public void testSetDataColumnNumber() throws Exception {
        String actual = testTargetTable.setDataColumnNumber("3");
        assertNotNull(actual);
        assertEquals("Data column number not equal",testDataColumnNumber, actual);
    }
}