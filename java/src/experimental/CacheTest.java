package experimental;//import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
//import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.io.File;
import java.util.*;

public class CacheTest {

    public static class ArrayCache {
        final Object[] storage;

        public ArrayCache(int[] keys, Object[] values) {
            storage = new Object[keys[keys.length - 1] + 1];
            for (int i = 0; i < keys.length; i++) storage[keys[i]] = values[i];
        }

        public Object get(int key) {
            return key < storage.length ? storage[key] : null;
        }
    }

    public static class BinarySearchCache {
        final int[] keys;
        final Object[] values;

        public BinarySearchCache(int[] keys, Object[] values) {
            this.keys = keys.clone();
            this.values = values;
        }

        public Object get(int key) {
            int pos = Arrays.binarySearch(keys, key);
            return pos >= 0 ? values[pos] : null;
        }
    }

    public static class SortedBucketsCache {
        final int[] cnt;
        final int[] a;
        final Object[] storage;
        final int mask;

        public SortedBucketsCache(int[] keys, Object[] values) {
            int n = 1;
            while (n < keys.length)
                n *= 2;
            mask = n - 1;
            cnt = new int[n + 1];
            for (int id : keys)
                ++cnt[id & mask];
            for (int i = 1; i < cnt.length; i++)
                cnt[i] += cnt[i - 1];
            a = new int[keys.length];
            storage = new Object[values.length];
            for (int i = keys.length - 1; i >= 0; i--) {
                int id = keys[i];
                a[--cnt[id & mask]] = id;
                storage[cnt[id & mask]] = values[i];
            }
        }

        public Object get(int key) {
            int bucket = key & mask;
            int pos = Arrays.binarySearch(a, cnt[bucket], cnt[bucket + 1], key);
            return pos >= 0 ? storage[pos] : null;
        }
    }

    public static void main(String[] args) throws Exception {
        /*
        Scanner sc = new Scanner(new File("D:/Dropbox/map/b.txt"));
        List<Integer> list = new ArrayList<>();
        while (sc.hasNext()) {
            list.add(sc.nextInt());
        }
        for (int i = 0; i + 1 < list.size(); i++) if (list.get(i) >= list.get(i + 1)) throw new RuntimeException();
        final int steps = 1000;
        Random rnd = new Random(1);
        Set<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < 200000; i++) {
            set.add(rnd.nextInt(1000000));
        }
        list.clear();
        list.addAll(set);

        Collections.sort(list);
        List<Integer> list2 = new ArrayList<>(list);
        Collections.shuffle(list2);
        int[] access = new int[list2.size()];
        for (int i = 0; i < access.length; i++) {
            access[i] = list2.get(i);
        }
        int[] ids = new int[list.size()];
        Object[] objects = new Object[ids.length];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = list.get(i);
            objects[i] = list.get(i);
        }

        Map<Integer, Object> map1 = new HashMap<>();
        Int2ObjectMap map2 = new Int2ObjectOpenHashMap();
        ArrayCache arrayCache = new ArrayCache(ids, objects);
        BinarySearchCache binarySearchCache = new BinarySearchCache(ids, objects);
        SortedBucketsCache sortedBucketsCache = new SortedBucketsCache(ids, objects);
        for (int i = 0; i < ids.length; i++) {
            map1.put(ids[i], objects[i]);
            map2.put(ids[i], objects[i]);
        }

        for (int i = 0; i < 1000000; i++) {
            Object o1 = arrayCache.get(i);
            Object o2 = binarySearchCache.get(i);
            Object o3 = sortedBucketsCache.get(i);
            Object o4 = map1.get(i);
            Object o5 = map2.get(i);
            if (o1 != o2 || o1 != o3 || o1 != o4 || o1 != o5)
                throw new RuntimeException();
        }

        long time = System.currentTimeMillis();
        Object[] objects2 = new Object[1000000];
        for (int i = 0; i < objects2.length; i++) objects2[i] = i;
        for (int step = 0; step < steps; step++) {
            for (int i = 0; i < ids.length; i++) {
                Object o = objects2[access[i]];
//                int o = access[i];
            }
        }
        System.out.println("array = " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        for (int step = 0; step < steps; step++) {
            for (int i = 0; i < ids.length; i++) {
                Object o = arrayCache.get(access[i]);
            }
        }
        System.out.println("arrayCache = " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        for (int step = 0; step < steps; step++) {
            for (int i = 0; i < ids.length; i++) {
                Object o = sortedBucketsCache.get(access[i]);
            }
        }
        System.out.println("sortedBucketsCache = " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        for (int step = 0; step < steps; step++) {
            for (int i = 0; i < ids.length; i++) {
                Object o = map2.get(access[i]);
            }
        }
        System.out.println("Int2ObjectOpenHashMap = " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        for (int step = 0; step < steps; step++) {
            for (int i = 0; i < ids.length; i++) {
                Object o = binarySearchCache.get(access[i]);
            }
        }
        System.out.println("binarySearchCache = " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        for (int step = 0; step < steps; step++) {
            for (int i = 0; i < ids.length; i++) {
                Object o = map1.get(access[i]);
            }
        }
        System.out.println("HashMap = " + (System.currentTimeMillis() - time));
        */
    }
}
