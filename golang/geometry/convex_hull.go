package geometry

import "sort"

// Point represents a 2D point
type Point struct {
	X, Y int64
}

func cross(o, a, b Point) int64 {
	return (a.X-o.X)*(b.Y-o.Y) - (a.Y-o.Y)*(b.X-o.X)
}

// ConvexHull computes the convex hull of a set of 2D points
// Returns points in counter-clockwise order
func ConvexHull(points []Point) []Point {
	n := len(points)
	if n < 2 {
		return points
	}
	sort.Slice(points, func(i, j int) bool {
		if points[i].X != points[j].X {
			return points[i].X < points[j].X
		}
		return points[i].Y < points[j].Y
	})

	hull := make([]Point, 0, 2*n)
	// Lower hull
	for _, p := range points {
		for len(hull) >= 2 && cross(hull[len(hull)-2], hull[len(hull)-1], p) <= 0 {
			hull = hull[:len(hull)-1]
		}
		hull = append(hull, p)
	}
	// Upper hull
	lower := len(hull) + 1
	for i := n - 2; i >= 0; i-- {
		p := points[i]
		for len(hull) >= lower && cross(hull[len(hull)-2], hull[len(hull)-1], p) <= 0 {
			hull = hull[:len(hull)-1]
		}
		hull = append(hull, p)
	}
	return hull[:len(hull)-1]
}
