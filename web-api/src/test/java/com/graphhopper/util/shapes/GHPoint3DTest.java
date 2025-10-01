/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.util.shapes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Peter Karich
 */
public class GHPoint3DTest {
    @Test
    public void testEquals() {
        GHPoint3D point1 = new GHPoint3D(1, 2, Double.NaN);
        GHPoint3D point2 = new GHPoint3D(1, 2, Double.NaN);
        assertEquals(point1, point2);

        point1 = new GHPoint3D(1, 2, 0);
        point2 = new GHPoint3D(1, 2, 1);
        assertNotEquals(point1, point2);

        point1 = new GHPoint3D(1, 2, 0);
        point2 = new GHPoint3D(1, 2.1, 0);
        assertNotEquals(point1, point2);

        point1 = new GHPoint3D(1, 2.1, 0);
        point2 = new GHPoint3D(1, 2.1, 0);
        assertEquals(point1, point2);
    }

    @Test
    public void testConstructorAndGetters() {
        // Teste la création d'un GHPoint3D avec les coordonnées d'Oslo
        GHPoint3D point = new GHPoint3D(59.9139, 10.7522, 100.0);
        assertEquals(59.9139, point.getLat(), 1e-6, "Latitude should be equal to Oslo, Norway");
        assertEquals(10.7522, point.getLon(), 1e-6, "Longitude should be equal to Oslo, Norway");
        assertEquals(100.0, point.getEle(), 1e-6, "Elevation should be equal");
        assertTrue(point.isValid(), "Point should be valid");
    }

    @Test
    public void testEqualsWithNaNElevation_butMoreOfIt() {
        // Teste l'égalité avec une élévation NaN avec les coordonnées d'Oslo
        GHPoint3D point1 = new GHPoint3D(59.9139, 10.7522, Double.NaN);
        GHPoint3D point2 = new GHPoint3D(59.9139, 10.7522, Double.NaN);
        assertEquals(point1, point2, "Points with elevation NaN should be equal if  lat and lon identical ");
        assertEquals(point1.hashCode(), point2.hashCode(), "hashCodes should be identicals");
    }

    @Test
    public void testToGeoJson() {
        // Teste la conversion en GeoJSON avec les coordonnées d'Oslo
        GHPoint3D point = new GHPoint3D(59.9139, 10.7522, 100.0);
        Double[] geoJson = point.toGeoJson();
        assertArrayEquals(new Double[]{10.7522, 59.9139, 100.0}, geoJson,
                "GeoJSON should include lon, lat, ele");
    }
}
