package ch.so.agi.h2.functions;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

public class RemoveInnerRings {
    public static Geometry removeInnerRings(Geometry geom) {
        if (geom == null) {
            return null;
        }
        return removeInnerRings(geom, 0.0);
    }
    
    public static Geometry removeInnerRings(Geometry geom, Double tolerance) {
        if (geom == null) {
            return null;
        }
        
        if (geom instanceof MultiPolygon) {
            List<Polygon> cleanPolygons = new ArrayList<Polygon>();
            for (int i=0; i<geom.getNumGeometries(); i++) {
                Polygon polygon = (Polygon) geom.getGeometryN(i);
                Polygon cleanPolygon = removeInnerRings(polygon, tolerance);
                cleanPolygons.add(cleanPolygon);
            }
            return geom.getFactory().createMultiPolygon(cleanPolygons.toArray(new Polygon[0]));
        } else if (geom instanceof Polygon) {
            Polygon polygon = (Polygon) geom;
            return removeInnerRings(polygon, tolerance);
        } else {
            return geom;
        }
    }
        
    private static Polygon removeInnerRings(Polygon polygon, Double tolerance) {
        List<LinearRing> holes = new ArrayList<>();
        for (int j = 0; j < polygon.getNumInteriorRing(); j++) {
            LinearRing ring = (LinearRing) polygon.getInteriorRingN(j);
            
//            MinimumDiameter md = new MinimumDiameter(polygon);
//            System.out.println(md.getLength());
//            System.out.println(md.getDiameter());
//            System.out.println(md.getSupportingSegment());
            
            if (ring.getFactory().createPolygon(ring).getArea() >= tolerance) {
                holes.add(ring);
            } else {
                System.out.println(ring.getFactory().createPolygon(ring).getArea());
            }
        }
        return polygon.getFactory().createPolygon((LinearRing)polygon.getExteriorRing(), holes.toArray(new LinearRing[0]));
    }
}
