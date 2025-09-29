package com.graphhopper.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class PointListTest {
    
    /* Ce test a pour objectif de vérifier qu'un objet PointList vide
     * Est équivalent à PointList.EMPTY qu'elle soit 2D ou 3D.
     */
    @Test
    public void test_EmptyList() {
        PointList Empty2DPointList = new PointList(10,false);
        PointList Empty3DPointList = new PointList(10,true);

        assertEquals(Empty2DPointList,PointList.EMPTY);

        assertEquals(Empty3DPointList,PointList.EMPTY);
    }
    

    /* Ce test a pour objectif de vérifier l'ajout de points dans la liste */
    @Test
    public void test_addition() {
        PointList Liste = new PointList();

        Liste.add(1, 1);
        Liste.add(2, 2);
        Liste.add(3, 3);
        Liste.add(4, 4);
        Liste.add(5, 5);

        // ajout de points par une list
        PointList ajouterPoints = new PointList();
        ajouterPoints.add(8,8);
        ajouterPoints.add(9,99);
        ajouterPoints.add(10,10);

        Liste.add(ajouterPoints);

        assertEquals(8,Liste.size());
        assertEquals(9, Liste.getLat(6));
        assertEquals(99, Liste.getLon(6));
    }

}
