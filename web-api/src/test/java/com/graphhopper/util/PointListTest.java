package com.graphhopper.util;

import static org.junit.jupiter.api.Assertions.*;

import com.github.javafaker.Faker;
import com.graphhopper.util.shapes.GHPoint;
import com.graphhopper.util.shapes.GHPoint3D;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

import java.util.ArrayList;
import java.util.List;


public class PointListTest {


    @Test
    public void testSetElevation() {
        PointList liste = new PointList(10, true);

        for (int i = 0; i < 10; i++) {
            liste.add(i, i, i);
        }

        liste.setElevation(4, 42);

        assertEquals(42, liste.getEle(4));
    }

    @Test
    public void testSetElevation_withIndexOutOfBound() {
        PointList liste = new PointList(10, true);

        for (int i = 0; i < 10; i++) {
            liste.add(i, i, i);
        }

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            liste.setElevation(42, 42);
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
    }

    @Test
    public void testTrimeToSize() {
        PointList liste = new PointList(10, true);

        for (int i = 0; i < 10; i++) {
            liste.add(i, i, i);
        }

        liste.trimToSize(3);

        assertEquals(3, liste.size());
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

        assertEquals(9, liste.getEle(0));

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
            sourceList.add(new GHPoint3D(lats[i], lons[i], eles[i]));
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
}
