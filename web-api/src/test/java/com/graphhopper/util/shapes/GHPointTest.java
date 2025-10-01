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
import com.graphhopper.util.shapes.GHPoint;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Karich
 */
public class GHPointTest {
    @Test
    public void testIsValid() {
        GHPoint instance = new GHPoint();
        assertFalse(instance.isValid());
        instance.lat = 1;
        assertFalse(instance.isValid());
        instance.lon = 1;
        assertTrue(instance.isValid());
    }

    @Test
    public void testConstructorAndGetters() {
        // Teste la création d'un GHPoint avec les coordonnées d'Oslo
        GHPoint point = new GHPoint(59.9139, 10.7522);
        assertEquals(59.9139, point.getLat(), 1e-6, "Latitude should be equal to d'Oslo");
        assertEquals(10.7522, point.getLon(), 1e-6, "Longitude should be equal to d'Oslo");
        assertTrue(point.isValid(), "Point should be valid");
    }

    @Test
    public void testFromString() {
        // Teste le parsing d'une chaîne en GHPoint avec les coordonnées d'Oslo
        GHPoint point = GHPoint.fromString("59.9139,10.7522");
        assertEquals(59.9139, point.getLat(), 1e-6, "Lattitude should be parsed correctly");
        assertEquals(10.7522, point.getLon(), 1e-6, "Longitude should be parsed correctly");
        assertThrows(IllegalArgumentException.class, () -> GHPoint.fromString("invalid"),
                "Invalid string should result in exception");
    }

    @Test
    public void testEqualsAndHashCode() {
        // Teste l'égalité et le hashCode entre deux GHPoint avec les coordonnées d'Oslo
        GHPoint point1 = new GHPoint(59.9139, 10.7522);
        GHPoint point2 = new GHPoint(59.9139, 10.7522);
        GHPoint point3 = new GHPoint(51.5074, -0.1278); // Coordonnées de Londres pour contraste
        assertEquals(point1, point2, "Identical points should be equal");
        assertEquals(point1.hashCode(), point2.hashCode(), "hashCodes should be identical for equal points");
        assertNotEquals(point1, point3, "Different points shouldn't be equal");
    }
}
