package test.geometry;

import org.junit.jupiter.api.Test; 
import static org.junit.jupiter.api.Assertions.assertEquals;
import geometry.CircleOperations.Circle;
import geometry.CircleOperations;
import geometry.CircleOperations.Point;
import geometry.CircleOperations.Line;

public class TestCircleLineIntersection {
	
	@Test
	void testLineIntersectCircle() {
		Line l = new Line(1, -1, -2);
		Circle c = new Circle(2, 2, 2);
		Point[] res = CircleOperations.circleLineIntersection(c, l);
		assertEquals(res.length, 2);
	}
	
	@Test
	void testLineIntersectCircleAtOnePoint() {
		Line l = new Line(0, 1, 0);
		Circle c = new Circle(2, 2, 2);
		Point[] res = CircleOperations.circleLineIntersection(c, l);
		assertEquals(res.length, 1);
	}
	
	@Test
	void testLineNotIntersectCircle() {
		Line l = new Line(0, 1, 10);
		Circle c = new Circle(2, 2, 2);
		Point[] res = CircleOperations.circleLineIntersection(c, l);
		assertEquals(res.length, 0);
	}

}
