import net.egork.collections.ArrayUtils;
import net.egork.collections.Pair;
import net.egork.collections.set.PersistentSet;
import net.egork.collections.set.PersistentTreapSet;
import net.egork.geometry.GeometryUtils;
import net.egork.geometry.Line;
import net.egork.geometry.Point;
import net.egork.utils.Solver;
import net.egork.utils.io.InputReader;
 
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
public class Forest implements Solver {
	private static final double A = 345244364;
	private static final double B = 452515423;
	private Line scanLine;
 
	public void solve(int testNumber, InputReader in, PrintWriter out) {
		GeometryUtils.epsilon = 1e-11;
		int vertexCount = in.readInt();
		int pointCount = in.readInt();
		final Point[] vertices = new Point[vertexCount];
		Set<Pair<Integer, Integer>> vertexSet = new HashSet<Pair<Integer, Integer>>();
		for (int i = 0; i < vertexCount; i++) {
			int x = in.readInt();
			int y = in.readInt();
			vertexSet.add(Pair.makePair(x, y));
			vertices[i] = new Point(x, y);
		}
		PersistentSet<Line> set = new PersistentTreapSet<Line>(new Comparator<Line>() {
			public int compare(Line o1, Line o2) {
				if (Math.abs(scanLine.intersect(o1).x - scanLine.intersect(o2).x) > GeometryUtils.epsilon)
					return Double.compare(scanLine.intersect(o1).x, scanLine.intersect(o2).x);
				return 0;
			}
		});
		Integer[] order = ArrayUtils.order(vertexCount, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return Double.compare(A * vertices[o2].x + B * vertices[o2].y, A * vertices[o1].x + B * vertices[o1].y);
			}
		});
		List<Double> marks = new ArrayList<Double>(vertexCount);
		marks.add(Double.NEGATIVE_INFINITY);
		set.markState(Double.NEGATIVE_INFINITY);
		for (int i : order) {
			Point current = vertices[i];
			Line vertexLine = new Line(A, B, -A * current.x - B * current.y);
			double mark = vertexLine.c;
			Point last = vertices[(i + 1) % vertexCount];
			Line first = current.line(last);
			double firstMark = new Line(A, B, -A * last.x - B * last.y).c;
			Point next = vertices[(i + vertexCount - 1) % vertexCount];
			Line second = current.line(next);
			double secondMark = new Line(A, B, -A * next.x - B * next.y).c;
			scanLine = new Line(A, B, -A * current.x - B * current.y - 1);
			if (firstMark < mark)
				set.remove(first);
			if (secondMark < mark)
				set.remove(second);
			scanLine = new Line(A, B, -A * current.x - B * current.y + 1);
			if (firstMark > mark)
				set.add(first);
			if (secondMark > mark)
				set.add(second);
			marks.add(mark);
			set.markState(mark);
		}
		for (int it = 0; it < pointCount; it++) {
			int x = in.readInt();
			int y = in.readInt();
			if (vertexSet.contains(Pair.makePair(x, y))) {
				out.println("Border");
				continue;
			}
			Point point = new Point(x, y);
			scanLine = new Line(A, B, -A * point.x - B * point.y);
			int index = Collections.binarySearch(marks, scanLine.c);
			index = -index - 2;
			PersistentSet<Line> current = set.getState(marks.get(index));
			Line perpendicular = scanLine.perpendicular(point);
			int index1 = current.headSet(perpendicular, true).size();
			int index2 = current.headSet(perpendicular, false).size();
			if (index1 != index2)
				out.println("Border");
			else if (index1 % 2 == 0)
				out.println("Outside");
			else
				out.println("Inside");
		}
	}