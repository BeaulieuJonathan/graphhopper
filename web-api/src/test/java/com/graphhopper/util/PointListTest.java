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
    public void testWithJavaFaker() {
        Faker faker = new Faker();

        PointList pointList2D = new PointList(5, false);
        PointList pointList3D = new PointList(5, true);
        List<GHPoint> generatedPoints = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            double lat = faker.number().randomDouble(6, -90, 90);
            double lon = faker.number().randomDouble(6, -180, 180);
            double ele = faker.number().randomDouble(2, -100, 1000);
            generatedPoints.add(new GHPoint3D(lat, lon, ele));
            pointList2D.add(new GHPoint(lat, lon));
            pointList3D.add(new GHPoint3D(lat, lon, ele));
        }
        LineString lineString3D = pointList3D.toLineString(true);
        assertEquals(pointList3D.size(), lineString3D.getCoordinates().length, "LineString should have the same number of coordinates");
        for (int i = 0; i < pointList3D.size(); i++) {
            Coordinate coor = lineString3D.getCoordinateN(i);
            assertEquals(Helper.round6(pointList3D.getLon(i)), coor.x, 1e-6, "LineString x should have the same longitude");
            assertEquals(Helper.round6(pointList3D.getLat(i)), coor.y, 1e-6, "LineString y should  have the same latitude");
            assertEquals(Helper.round2(pointList3D.getEle(i)), coor.z, 1e-2, "LineString z should have the same elevation");
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
