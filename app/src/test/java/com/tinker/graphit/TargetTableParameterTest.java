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

    }

    @Test
    public void testSetTableName() throws Exception {

    }

    @Test
    public void testSetAxisColumnNumber() throws Exception {

    }

    @Test
    public void testSetDataColumnNumber() throws Exception {

    }
}