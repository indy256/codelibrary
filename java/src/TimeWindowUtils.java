import java.util.*;

public class TimeWindowUtils {

	public static class TimeWindow {
		final int startTime;
		final int stopTime;
		final int serviceTime;

		public TimeWindow(int startTime, int stopTime, int serviceTime) {
			this.startTime = startTime;
			this.stopTime = stopTime;
			this.serviceTime = serviceTime;
		}
	}

	public static TimeWindow[] unionMin(TimeWindow[] timeWindows) {
		int n = timeWindows.length;
		PriorityQueue<TimeWindow> q = new PriorityQueue<>((a, b) -> Integer.compare(a.startTime, b.startTime));
		Collections.addAll(q, timeWindows);
		TimeWindow[] res = new TimeWindow[2 * n - 1];
		int cnt = 0;
		while (!q.isEmpty()) {
			TimeWindow w2 = q.poll();
			if (w2.startTime >= w2.stopTime)
				continue;
			while (cnt > 0 && w2.startTime + w2.serviceTime < res[cnt - 1].startTime + res[cnt - 1].serviceTime) {
				if (w2.stopTime < res[cnt - 1].stopTime) {
					q.add(new TimeWindow(w2.stopTime, res[cnt - 1].stopTime, res[cnt - 1].serviceTime));
				}
				--cnt;
			}
			if (cnt == 0) {
				res[cnt++] = w2;
			} else {
				TimeWindow w1 = res[cnt - 1];
				if (w1.serviceTime < w2.serviceTime) {
					if (w1.stopTime < w2.stopTime) {
						if (w1.stopTime <= w2.startTime) {
							res[cnt++] = w2;

						} else {
							q.add(new TimeWindow(w1.stopTime, w2.stopTime, w2.serviceTime));
						}
					}
				} else if (w1.serviceTime > w2.serviceTime) {
					int p = w2.startTime - (w1.serviceTime - w2.serviceTime);
					if (p >= w1.stopTime) {
						res[cnt++] = w2;
					} else {
						if (p == w1.startTime) {
							--cnt;
						} else {
							res[cnt - 1] = new TimeWindow(w1.startTime, p, w1.serviceTime);
						}
						res[cnt++] = w2;
						if (w1.stopTime > w2.stopTime) {
							q.add(new TimeWindow(w2.stopTime, w1.stopTime, w1.serviceTime));
						}
					}
				} else {
					if (w1.stopTime <= w2.startTime) {
						res[cnt++] = w2;
					} else {
						res[cnt - 1] = new TimeWindow(w1.startTime, Math.max(w1.stopTime, w2.stopTime), w1.serviceTime);
					}
				}
			}
		}
		return Arrays.copyOf(res, cnt);
	}

	public static TimeWindow[] intersectionMaxSchedules(TimeWindow[][] schedules) {
		TimeWindow[] res = schedules[0];
		for (int i = 1; i < schedules.length; i++) {
			res = intersectionMaxSchedules(res, schedules[i]);
		}
		return res;
	}

	public static TimeWindow[] intersectionMaxSchedules(TimeWindow[] schedule1, TimeWindow[] schedule2) {
		TimeWindow[] res = new TimeWindow[schedule1.length + schedule2.length - 1];
		int cnt = 0;
		for (int i = 0, j = 0; i < schedule1.length && j < schedule2.length; ) {
			final TimeWindow w1 = schedule1[i];
			final TimeWindow w2 = schedule2[j];
			int a = Math.max(i > 0 ? schedule1[i - 1].stopTime : 0, j > 0 ? schedule2[j - 1].stopTime : 0);
			int b = Math.min(w1.stopTime, w2.stopTime);
			int y1 = w1.startTime + w1.serviceTime;
			int y2 = w2.startTime + w2.serviceTime;
			if (between(y2, y1, w1.stopTime + w1.serviceTime) && between(y2 - w1.serviceTime, a, Math.min(w2.startTime, b))) {
				res[cnt++] = new TimeWindow(y2 - w1.serviceTime, b, w1.serviceTime);
			} else if (between(y1, y2, w2.stopTime + w2.serviceTime) && between(y1 - w2.serviceTime, a, Math.min(w1.startTime, b))) {
				res[cnt++] = new TimeWindow(y1 - w2.serviceTime, b, w2.serviceTime);
			} else {
				TimeWindow w = (between(a, w1.startTime, w1.stopTime) ? a : w1.startTime) + w1.serviceTime >
						(between(a, w2.startTime, w2.stopTime) ? a : w2.startTime) + w2.serviceTime ? w1 : w2;
				int x = Math.max(a, w.startTime);
				int y = Math.min(b, w.stopTime);
				res[cnt++] = x <= y ? new TimeWindow(x, y, w.serviceTime) : new TimeWindow(b, b, w.serviceTime + w.startTime - b);
			}
			if (w1.stopTime <= w2.stopTime) {
				++i;
			}
			if (w2.stopTime <= w1.stopTime) {
				++j;
			}
		}
		return Arrays.copyOf(res, cnt);
	}

	static boolean between(int p, int a, int b) {
		return p >= a & p <= b;
	}

	public static TimeWindow[] composeSchedules(TimeWindow[][] schedules) {
		TimeWindow[] res = schedules[0];
		for (int i = 1; i < schedules.length; i++) {
			res = composeSchedules(res, schedules[i]);
		}
		return res;
	}

	public static TimeWindow[] composeSchedules(TimeWindow[] schedule1, TimeWindow[] schedule2) {
		TimeWindow[] res = new TimeWindow[schedule1.length + schedule2.length - 1];
		int cnt = 0;
		for (int i = 0, j = 0; i < schedule1.length && j < schedule2.length; ) {
			final TimeWindow w1 = schedule1[i];
			final TimeWindow w2 = schedule2[j];
			int earliestArrivalTo2 = w1.startTime + w1.serviceTime;
			int latestArrivalTo2 = w1.stopTime + w1.serviceTime;
			if (earliestArrivalTo2 >= w2.stopTime) {
				++j;
				continue;
			}
			final TimeWindow w;
			if (latestArrivalTo2 <= w2.startTime) {
				w = new TimeWindow(w1.stopTime, w1.stopTime, w2.startTime - w1.stopTime + w2.serviceTime);
			} else {
				w = new TimeWindow(
						Math.max(earliestArrivalTo2, w2.startTime) - w1.serviceTime,
						Math.min(latestArrivalTo2, w2.stopTime) - w1.serviceTime,
						w1.serviceTime + w2.serviceTime);
			}
			if (cnt > 0 && res[cnt - 1].startTime + res[cnt - 1].serviceTime == w.startTime + w.serviceTime) {
				res[cnt - 1] = w;
			} else {
				res[cnt++] = w;
			}
			if (latestArrivalTo2 < w2.stopTime) {
				++i;
			} else {
				++j;
			}
		}
		return Arrays.copyOf(res, cnt);
	}

	// random test
	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		Random rnd = new Random(1);

		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(20) + 1;
			TimeWindow[] timeWindows = new TimeWindow[n];
			int range = 3600 * 24;
			for (int i = 0; i < n; i++) {
				int b = rnd.nextInt(range) + 1;
				int a = rnd.nextInt(b);
				int serviceTime = rnd.nextInt(3600);
				timeWindows[i] = new TimeWindow(a, b, serviceTime);
			}
			TimeWindow[] unionMin = unionMin(timeWindows);
			for (int i = 0; i <= range; i++) {
				Integer res1 = eval(unionMin, i);
				Integer res2 = evalUnionMin(timeWindows, i);
				if (!Objects.equals(res1, res2))
					throw new RuntimeException();
			}
		}

		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(5) + 1;
			TimeWindow[][] schedules = new TimeWindow[n][];
			int range = 3600 * 24;
			for (int i = 0; i < n; i++) {
				int cnt = rnd.nextInt(10) + 1;
				TimeWindow[] timeWindows = new TimeWindow[cnt];
				for (int j = 0; j < cnt; j++) {
					int b = rnd.nextInt(range) + 1;
					int a = rnd.nextInt(b);
					int serviceTime = rnd.nextInt(3600);
					timeWindows[j] = new TimeWindow(a, b, serviceTime);
				}
				schedules[i] = unionMin(timeWindows);
			}
			TimeWindow[] composition = composeSchedules(schedules);
			for (int x = 0; x <= range; x++) {
				Integer res1 = eval(composition, x);
				Integer res2 = evalCompose(schedules, x);
				if (!Objects.equals(res1, res2))
					throw new RuntimeException();
			}
		}

		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(10) + 1;
			TimeWindow[][] schedules = new TimeWindow[n][];
			int range = 3600 * 24;
			for (int i = 0; i < n; i++) {
				int cnt = rnd.nextInt(10) + 1;
				TimeWindow[] timeWindows = new TimeWindow[cnt];
				for (int j = 0; j < cnt; j++) {
					int b = rnd.nextInt(range) + 1;
					int a = rnd.nextInt(b);
					int serviceTime = rnd.nextInt(3600);
					timeWindows[j] = new TimeWindow(a, b, serviceTime);
				}
				schedules[i] = unionMin(timeWindows);
			}
			TimeWindow[] intersectionMax = intersectionMaxSchedules(schedules);
			for (int x = 0; x <= range; x++) {
				Integer res1 = eval(intersectionMax, x);
				Integer res2 = evalIntersectionMax(schedules, x);
				if (!Objects.equals(res1, res2))
					throw new RuntimeException();
			}
		}
		System.out.println(System.currentTimeMillis() - time);
	}

	static Integer evalUnionMin(TimeWindow[] timeWindows, int x) {
		int res = Integer.MAX_VALUE;
		for (TimeWindow w : timeWindows) {
			Integer cur = eval(new TimeWindow[]{w}, x);
			if (cur != null)
				res = Math.min(res, cur);
		}
		return res == Integer.MAX_VALUE ? null : res;
	}

	static Integer evalCompose(TimeWindow[][] timeWindows, int x) {
		Integer cur = x;
		for (TimeWindow[] windows : timeWindows) {
			cur = eval(windows, cur);
			if (cur == null)
				return null;
		}
		return cur;
	}

	static Integer evalIntersectionMax(TimeWindow[][] timeWindows, int x) {
		int res = Integer.MIN_VALUE;
		for (TimeWindow[] windows : timeWindows) {
			Integer cur = eval(windows, x);
			if (cur == null)
				return null;
			res = Math.max(res, cur);
		}
		return res;
	}

	static Integer eval(TimeWindow[] timeWindows, int x) {
		for (TimeWindow w : timeWindows)
			if (w.startTime <= x && x < w.stopTime)
				return x + w.serviceTime;
			else if (x < w.startTime)
				return w.startTime + w.serviceTime;
		return null;
	}
}
