package com.graphhopper.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class PointListTest {
    
    
    @Test
    public void testSetElevation() {
        PointList liste = new PointList(10,true);

        for (int i = 0; i < 10; i++) {
            liste.add(i,i,i);
        }

        liste.setElevation(4, 42);

        assertEquals(42, liste.getEle(4));
    }

    @Test
    public void testSetElevation_withIndexOutOfBound() {
        PointList liste = new PointList(10,true);

        for (int i = 0; i < 10; i++) {
            liste.add(i,i,i);
        }

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {liste.setElevation(42, 42);});
        
    }

    @Test
    public void testSetElevationIn2DPointList() {
        PointList liste = new PointList(10,false);

        for (int i = 0; i < 10; i++) {
            liste.add(i,i);
        }

        assertThrows(IllegalStateException.class, () -> {liste.setElevation(2, 42);});
    }

    @Test
    public void testClearList() {
        PointList liste = new PointList(10,true);

        for (int i = 0; i < 10; i++) {
            liste.add(i,i,i);
        }

        assertEquals(10, liste.size());

        liste.clear();

        assertTrue(liste.isEmpty());
    }

    @Test
    public void testTrimeToSize() {
        PointList liste = new PointList(10,true);

        for (int i = 0; i < 10; i++) {
            liste.add(i,i,i);
        }

        liste.trimToSize(3);

        assertEquals(3,liste.size());
    }

    @Test
    public void testTrimToSize_LargerThanOldSize() {
        PointList liste = new PointList(10,true);

        for (int i = 0; i < 10; i++) {
            liste.add(i,i,i);
        }

        assertThrows(IllegalArgumentException.class, () -> {
            liste.trimToSize(42);
        });

    }

    @Test
    public void reverse3DPointList() {
        PointList liste = new PointList(10,true);

        for (int i = 0; i < 10; i++) {
            liste.add(i,i,i);
        }

        liste.reverse();

        assertEquals(9, liste.getEle(0));

    }
}
