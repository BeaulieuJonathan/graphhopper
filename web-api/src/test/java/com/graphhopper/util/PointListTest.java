package com.graphhopper.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class PointListTest {
    

    @Test
    public void testAddPoints() {

    }

    @Test
    public void testEquals() {
        assertEquals(Helper.createPointList(), PointList.EMPTY);
        PointList list1 = Helper.createPointList(38.5, -120.2, 43.252, -126.453, 40.7, -120.95,
                50.3139, 10.612793, 50.04303, 9.497681);
        PointList list2 = Helper.createPointList(38.5, -120.2, 43.252, -126.453, 40.7, -120.95,
                50.3139, 10.612793, 50.04303, 9.497681);
        assertEquals(list1, list2);
    }
    
}
