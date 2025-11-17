package com.graphhopper.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.github.javafaker.Faker;
import com.graphhopper.util.shapes.GHPoint;

import org.junit.jupiter.api.Test;


public class PointListTest {

    

    @Test
    public void testSetElevation() {
        PointList liste = new PointList(10, true);

        for (int i = 0; i < 5; i++) {
            liste.add(i, i, i);
        }

        liste.setElevation(4, 42);

        assertEquals(42, liste.getEle(4));
    }

    @Test
    public void testSetElevation_withIndexOutOfBound() {
        PointList liste = new PointList(10, true);

        for (int i = 0; i < 5; i++) {
            liste.add(i, i, i);
        }

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            liste.setElevation(5, 42);
        });

    }

    @Test
    public void testSetElevationIn2DPointList() {
        PointList liste = new PointList(10, false);

        for (int i = 0; i < 10; i++) {
            liste.add(i, i);
        }

        assertThrows(IllegalStateException.class, () -> {
            liste.setElevation(2, 42);
        });
    }

    @Test
    public void testClearList() {
        PointList liste = new PointList(10, true);

        for (int i = 0; i < 10; i++) {
            liste.add(i, i, i);
        }

        assertEquals(10, liste.size());

        liste.clear();

        assertTrue(liste.isEmpty());
        assertEquals(0, liste.size());
    }

    @Test
    public void testTrimeToSize() {
        PointList liste = new PointList(10, true);

        for (int i = 0; i < 10; i++) {
            liste.add(i, i, i);
        }

        liste.trimToSize(3);

        assertEquals(3, liste.size());
        for (int i = 0; i<3; i++) {
            assertEquals(i, liste.getLat(i));
        }
    }

    @Test
    public void testTrimToSize_LargerThanOldSize() {
        PointList liste = new PointList(10, true);

        for (int i = 0; i < 10; i++) {
            liste.add(i, i, i);
        }

        assertThrows(IllegalArgumentException.class, () -> {
            liste.trimToSize(42);
        });

    }

    @Test
    public void reverse3DPointList() {
        PointList liste = new PointList(10, true);

        for (int i = 0; i < 10; i++) {
            liste.add(i, i, i);
        }

        liste.reverse();

        int valeur = liste.size();
        for (int i = 0; i < liste.size(); i++) {
            valeur--;
            assertEquals(valeur, liste.getLat(i), 1e-6);
            assertEquals(valeur, liste.getLon(i), 1e-6);
            assertEquals(valeur, liste.getEle(i), 1e-6);
        } 

    }

    @Test
    public void testAddPointListJavaFaker() {
        Faker faker = new Faker();
        PointList sourceList = new PointList(3, true);

        double[] lats = new double[3], lons = new double[3], eles = new double[3];
        for (int i = 0; i < 3; i++) {
            lats[i] = faker.number().randomDouble(6, -90, 90);
            lons[i] = faker.number().randomDouble(6, -180, 180);
            eles[i] = faker.number().randomDouble(2, -100, 1000);
            sourceList.add(lats[i], lons[i], eles[i]);
        }

        PointList targetList = new PointList(3, true);
        targetList.add(sourceList);

        assertEquals(3, targetList.size(), "Target list should have 3 points");
        for (int i = 0; i < 3; i++) {
            assertEquals(lats[i], targetList.getLat(i), 1e-6, "Latitude should match");
            assertEquals(lons[i], targetList.getLon(i), 1e-6, "Longitude should match");
            assertEquals(eles[i], targetList.getEle(i), 1e-2, "Elevation should match");
        }
    }

    @Test
    public void testPointListWithMockedPointAccess() {
        PointAccess point = mock(PointAccess.class);

        when(point.getLat(0)).thenReturn(42.0);
        when(point.getLon(0)).thenReturn(67.0);

        PointList testedList = new PointList(3,false);

        testedList.add(point,0);

        assertEquals(42.0,testedList.getLat(0));
        assertEquals(67.0,testedList.getLon(0));
        
    }
}
