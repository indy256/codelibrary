package test.geometry;

import org.junit.jupiter.api.Test; 
import static org.junit.jupiter.api.Assertions.assertEquals;
import geometry.CircleOperations.Circle;
import geometry.CircleOperations;
import geometry.CircleOperations.Point;

public class CircleOperationTest {
	
	@Test
	void testConcentricCirclesDifferentRadius() {
		Circle c1 = new Circle(4, 5, 1);
		Circle c2 = new Circle(4, 5, 9);
		Point[] res = CircleOperations.circleCircleIntersection(c1, c2);
		assertEquals(res.length, 0);
	}
	
	@Test
	void testConcentricCirclesSameRadius() {
		Circle c1 = new Circle(4, 5, 9);
		Circle c2 = new Circle(4, 5, 9);
		Point[] res = CircleOperations.circleCircleIntersection(c1, c2);
		assertEquals(res, null);
	}
	
	@Test
	void testCirclesNotIntersect() {
		Circle c1 = new Circle(0, 0, 1);
		Circle c2 = new Circle(0, 5, 1);
		Point[] res = CircleOperations.circleCircleIntersection(c1, c2);
		assertEquals(res.length, 0);
	}
	
	@Test
	void testCircleIntersectAtOnePoint() {
		Circle c1 = new Circle(0, 0, 4); 
		Circle c2 = new Circle(5, 0, 1);
		Point[] res = CircleOperations.circleCircleIntersection(c1, c2);
		assertEquals(res.length, 1);
	}
	
	@Test
	void testCircleIntersectAtTwoPoint() {
		Circle c1 = new Circle(1, 2, 3);
		Circle c2 = new Circle(2, 3, 2);
		Point[] res = CircleOperations.circleCircleIntersection(c1, c2);
		assertEquals(res.length, 2);
	}
	
	@Test
	void testCircleInsideAnotherNotIntersect() {
		Circle c1 = new Circle(4, 4, 16);
		Circle c2 = new Circle(2, 2, 2);
		Point[] res = CircleOperations.circleCircleIntersection(c1, c2);
		assertEquals(res.length, 0);
	}
}
