import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.io.File;
import java.util.*;

public class CacheTest {

	public static class Cache {
		final int[] ids;
		final Object[] o;

		public Cache(int[] ids, Object[] o) {
			this.ids = ids.clone();
			Arrays.sort(ids);
			this.o = o;
		}

		public Object get(int id) {
//			if(true)
//				return o[id];
			int pos = Arrays.binarySearch(ids, id);
			return pos >= 0 ? o[pos] : null;
		}
	}

	public static class Cache2 {
		final int[] cnt;
		final int[] a;
		final Object[] o;
		final int mask;

		public Cache2(int[] ids, Object[] o) {
			int n = 1;
			while (n < ids.length)
				n *= 2;
			mask = n - 1;
			cnt = new int[n + 1];
			for (int id : ids)
				++cnt[id & mask];
			for (int i = 1; i < cnt.length; i++)
				cnt[i] += cnt[i - 1];
			a = new int[ids.length];
			this.o = new Object[o.length];
			for (int i = ids.length-1; i >=0; i--) {
				int id = ids[i];
				a[--cnt[id & mask]] = id;
				this.o[cnt[id & mask]] = o[i];
			}
		}

		public Object get(int id) {
			int bucket = id & mask;
			int pos = Arrays.binarySearch(a, cnt[bucket], cnt[bucket + 1], id);
			return pos >= 0 ? o[pos] : null;
		}
	}

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(new File("E:/Dropbox/map/b2.txt"));
		List<Integer> list = new ArrayList<>();
		int cnt = 0;
		while (sc.hasNext()) {
//			list.add(sc.nextInt());
			sc.nextInt();
			list.add(cnt++);
		}
		list.clear();
		int n = 1000000;
		Random rnd = new Random(1);
		cnt = 0;
		for (int i = 0; i < 500000; i++) {
			//list.add(rnd.nextInt(n));
			list.add(cnt++);
			if (rnd.nextInt(10) == 5) ++cnt;
		}

		Collections.sort(list);
		int[] ids = new int[list.size()];
		Object[] o = new Object[n];
		Map<Integer, Object> map = new HashMap<>();
		Int2ObjectMap map2 = new Int2ObjectOpenHashMap();
		for (int i = 0; i < ids.length; i++) {
			ids[i] = list.get(i);
			o[ids[i]] = list.get(i);
			map.put(ids[i], o[ids[i]]);
			map2.put(ids[i], o[ids[i]]);
		}

		Cache cache = new Cache(ids, o);
		Object[] o1 = new Object[n];
		long time = System.currentTimeMillis();
		int cnt1 = 0;
		int steps = 100;
		for (int step = 0; step < steps; step++) {
			for (int i = 0; i < ids.length; i++) {
//				cache.get(ids[i]);
				Object a= o[ids[i]];
				++cnt1;
			}
		}
		System.out.println(System.currentTimeMillis() - time);

		Cache2 cache2 = new Cache2(ids, o);
		time = System.currentTimeMillis();
		int cnt4 = 0;
		for (int step = 0; step < steps; step++) {
			for (int i = 0; i < ids.length; i++) {
				cache2.get(ids[i]);
				++cnt4;
			}
		}
		System.out.println(System.currentTimeMillis() - time);

		time = System.currentTimeMillis();
		int cnt2 = 0;
		for (int step = 0; step < steps; step++) {
			for (int i = 0; i < ids.length; i++) {
//				map.put(i,o[i]);
				Object a = map.get(ids[i]);
				++cnt2;
			}
		}

		System.out.println(System.currentTimeMillis() - time);

		time = System.currentTimeMillis();
		int cnt3 = 0;
		for (int step = 0; step < steps; step++) {
			for (int i = 0; i < ids.length; i++) {
//				map2.put(i,o[i]);
				Object a = map2.get(ids[i]);
				++cnt3;
			}
		}

		System.out.println(System.currentTimeMillis() - time);
		System.out.println(cnt1 + cnt2 + cnt3 + cnt4);
	}
}
