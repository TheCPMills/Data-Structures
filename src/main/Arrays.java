package main;

import main.linear.*;
import main.util.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Arrays {
    private Arrays() {}

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> LinearStructure<T> asList(T... a) {
        return new DynamicArray<>(a);
    }

    public static int compare(boolean[] a, boolean[] b) {
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int i = ArraysSupport.mismatch(a, b, Math.min(a.length, b.length));
        if (i >= 0) {
            return Boolean.compare(a[i], b[i]);
        }

        return a.length - b.length;
    }

    public static int compare(boolean[] a, int aFromIndex, int aToIndex, boolean[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int i = ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, Math.min(aLength, bLength));
        if (i >= 0) {
            return Boolean.compare(a[aFromIndex + i], b[bFromIndex + i]);
        }

        return aLength - bLength;
    }

    public static int compare(byte[] a, byte[] b) {
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int i = ArraysSupport.mismatch(a, b, Math.min(a.length, b.length));
        if (i >= 0) {
            return Byte.compare(a[i], b[i]);
        }

        return a.length - b.length;
    }

    public static int compare(byte[] a, int aFromIndex, int aToIndex, byte[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int i = ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, Math.min(aLength, bLength));
        if (i >= 0) {
            return Byte.compare(a[aFromIndex + i], b[bFromIndex + i]);
        }

        return aLength - bLength;
    }

    public static int compare(short[] a, short[] b) {
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int i = ArraysSupport.mismatch(a, b, Math.min(a.length, b.length));
        if (i >= 0) {
            return Short.compare(a[i], b[i]);
        }

        return a.length - b.length;
    }

    public static int compare(short[] a, int aFromIndex, int aToIndex, short[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int i = ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, Math.min(aLength, bLength));
        if (i >= 0) {
            return Short.compare(a[aFromIndex + i], b[bFromIndex + i]);
        }

        return aLength - bLength;
    }

    public static int compare(char[] a, char[] b) {
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int i = ArraysSupport.mismatch(a, b, Math.min(a.length, b.length));
        if (i >= 0) {
            return Character.compare(a[i], b[i]);
        }

        return a.length - b.length;
    }

    public static int compare(char[] a, int aFromIndex, int aToIndex, char[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int i = ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, Math.min(aLength, bLength));
        if (i >= 0) {
            return Character.compare(a[aFromIndex + i], b[bFromIndex + i]);
        }

        return aLength - bLength;
    }

    public static int compare(int[] a, int[] b) {
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int i = ArraysSupport.mismatch(a, b, Math.min(a.length, b.length));
        if (i >= 0) {
            return Integer.compare(a[i], b[i]);
        }

        return a.length - b.length;
    }

    public static int compare(int[] a, int aFromIndex, int aToIndex, int[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int i = ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, Math.min(aLength, bLength));
        if (i >= 0) {
            return Integer.compare(a[aFromIndex + i], b[bFromIndex + i]);
        }

        return aLength - bLength;
    }

    public static int compare(long[] a, long[] b) {
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int i = ArraysSupport.mismatch(a, b, Math.min(a.length, b.length));
        if (i >= 0) {
            return Long.compare(a[i], b[i]);
        }

        return a.length - b.length;
    }

    public static int compare(long[] a, int aFromIndex, int aToIndex, long[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int i = ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, Math.min(aLength, bLength));
        if (i >= 0) {
            return Long.compare(a[aFromIndex + i], b[bFromIndex + i]);
        }

        return aLength - bLength;
    }

    public static int compare(float[] a, float[] b) {
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int i = ArraysSupport.mismatch(a, b, Math.min(a.length, b.length));
        if (i >= 0) {
            return Float.compare(a[i], b[i]);
        }

        return a.length - b.length;
    }

    public static int compare(float[] a, int aFromIndex, int aToIndex, float[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int i = ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, Math.min(aLength, bLength));
        if (i >= 0) {
            return Float.compare(a[aFromIndex + i], b[bFromIndex + i]);
        }

        return aLength - bLength;
    }

    public static int compare(double[] a, double[] b) {
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int i = ArraysSupport.mismatch(a, b, Math.min(a.length, b.length));
        if (i >= 0) {
            return Double.compare(a[i], b[i]);
        }

        return a.length - b.length;
    }

    public static int compare(double[] a, int aFromIndex, int aToIndex, double[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int i = ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, Math.min(aLength, bLength));
        if (i >= 0) {
            return Double.compare(a[aFromIndex + i], b[bFromIndex + i]);
        }

        return aLength - bLength;
    }

    public static <T extends Comparable<? super T>> int compare(T[] a, T[] b) {
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int length = Math.min(a.length, b.length);
        for (int i = 0; i < length; i++) {
            T oa = a[i];
            T ob = b[i];
            if (oa != ob) {
                if (oa == null || ob == null)
                    return oa == null ? -1 : 1;
                int v = oa.compareTo(ob);
                if (v != 0) {
                    return v;
                }
            }
        }

        return a.length - b.length;
    }

    public static <T extends Comparable<? super T>> int compare(T[] a, int aFromIndex, int aToIndex, T[] b,
            int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int length = Math.min(aLength, bLength);
        for (int i = 0; i < length; i++) {
            T oa = a[aFromIndex++];
            T ob = b[bFromIndex++];
            if (oa != ob) {
                if (oa == null || ob == null)
                    return oa == null ? -1 : 1;
                int v = oa.compareTo(ob);
                if (v != 0) {
                    return v;
                }
            }
        }

        return aLength - bLength;
    }

    public static <T> int compare(T[] a, T[] b, Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int length = Math.min(a.length, b.length);
        for (int i = 0; i < length; i++) {
            T oa = a[i];
            T ob = b[i];
            if (oa != ob) {
                int v = cmp.compare(oa, ob);
                if (v != 0) {
                    return v;
                }
            }
        }

        return a.length - b.length;
    }

    public static <T> int compare(T[] a, int aFromIndex, int aToIndex, T[] b, int bFromIndex, int bToIndex,
            Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int length = Math.min(aLength, bLength);
        for (int i = 0; i < length; i++) {
            T oa = a[aFromIndex++];
            T ob = b[bFromIndex++];
            if (oa != ob) {
                int v = cmp.compare(oa, ob);
                if (v != 0) {
                    return v;
                }
            }
        }

        return aLength - bLength;
    }

    public static int compareUnsigned(byte[] a, byte[] b) {
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int i = ArraysSupport.mismatch(a, b, Math.min(a.length, b.length));
        if (i >= 0) {
            return Byte.compareUnsigned(a[i], b[i]);
        }

        return a.length - b.length;
    }

    public static int compareUnsigned(byte[] a, int aFromIndex, int aToIndex, byte[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int i = ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, Math.min(aLength, bLength));
        if (i >= 0) {
            return Byte.compareUnsigned(a[aFromIndex + i], b[bFromIndex + i]);
        }

        return aLength - bLength;
    }

    public static int compareUnsigned(short[] a, short[] b) {
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int i = ArraysSupport.mismatch(a, b, Math.min(a.length, b.length));
        if (i >= 0) {
            return Short.compareUnsigned(a[i], b[i]);
        }

        return a.length - b.length;
    }

    public static int compareUnsigned(short[] a, int aFromIndex, int aToIndex, short[] b, int bFromIndex,
            int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int i = ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, Math.min(aLength, bLength));
        if (i >= 0) {
            return Short.compareUnsigned(a[aFromIndex + i], b[bFromIndex + i]);
        }

        return aLength - bLength;
    }

    public static int compareUnsigned(int[] a, int[] b) {
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int i = ArraysSupport.mismatch(a, b, Math.min(a.length, b.length));
        if (i >= 0) {
            return Integer.compareUnsigned(a[i], b[i]);
        }

        return a.length - b.length;
    }

    public static int compareUnsigned(int[] a, int aFromIndex, int aToIndex, int[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int i = ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, Math.min(aLength, bLength));
        if (i >= 0) {
            return Integer.compareUnsigned(a[aFromIndex + i], b[bFromIndex + i]);
        }

        return aLength - bLength;
    }

    public static int compareUnsigned(long[] a, long[] b) {
        if (a == b)
            return 0;
        if (a == null || b == null)
            return a == null ? -1 : 1;

        int i = ArraysSupport.mismatch(a, b, Math.min(a.length, b.length));
        if (i >= 0) {
            return Long.compareUnsigned(a[i], b[i]);
        }

        return a.length - b.length;
    }

    public static int compareUnsigned(long[] a, int aFromIndex, int aToIndex, long[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        int i = ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, Math.min(aLength, bLength));
        if (i >= 0) {
            return Long.compareUnsigned(a[aFromIndex + i], b[bFromIndex + i]);
        }

        return aLength - bLength;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] copyOf(T[] original, int newLength) {
        return (T[]) copyOf(original, newLength, original.getClass());
    }

    public static <T, U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        @SuppressWarnings("unchecked")
        T[] copy = ((Object) newType == (Object) Object[].class) ? (T[]) new Object[newLength]
                : (T[]) java.lang.reflect.Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    public static byte[] copyOf(byte[] original, int newLength) {
        byte[] copy = new byte[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    public static short[] copyOf(short[] original, int newLength) {
        short[] copy = new short[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    public static int[] copyOf(int[] original, int newLength) {
        int[] copy = new int[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    public static long[] copyOf(long[] original, int newLength) {
        long[] copy = new long[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    public static char[] copyOf(char[] original, int newLength) {
        char[] copy = new char[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    public static float[] copyOf(float[] original, int newLength) {
        float[] copy = new float[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    public static double[] copyOf(double[] original, int newLength) {
        double[] copy = new double[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    public static boolean[] copyOf(boolean[] original, int newLength) {
        boolean[] copy = new boolean[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] copyOfRange(T[] original, int from, int to) {
        return copyOfRange(original, from, to, (Class<? extends T[]>) original.getClass());
    }

    public static <T, U> T[] copyOfRange(U[] original, int from, int to, Class<? extends T[]> newType) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        @SuppressWarnings("unchecked")
        T[] copy = ((Object) newType == (Object) Object[].class) ? (T[]) new Object[newLength]
                : (T[]) java.lang.reflect.Array.newInstance(newType.getComponentType(), newLength);
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    public static short[] copyOfRange(short[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        short[] copy = new short[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    public static int[] copyOfRange(int[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        int[] copy = new int[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    public static long[] copyOfRange(long[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        long[] copy = new long[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    public static char[] copyOfRange(char[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        char[] copy = new char[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    public static float[] copyOfRange(float[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        float[] copy = new float[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    public static double[] copyOfRange(double[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        double[] copy = new double[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    public static boolean[] copyOfRange(boolean[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        boolean[] copy = new boolean[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    public static int deepHashCode(Object a[]) {
        if (a == null)
            return 0;

        int result = 1;

        for (Object element : a) {
            final int elementHash;
            final Class<?> cl;
            if (element == null)
                elementHash = 0;
            else if ((cl = element.getClass().getComponentType()) == null)
                elementHash = element.hashCode();
            else if (element instanceof Object[])
                elementHash = deepHashCode((Object[]) element);
            else
                elementHash = primitiveArrayHashCode(element, cl);

            result = 31 * result + elementHash;
        }

        return result;
    }

    public static boolean deepEquals(Object[] a1, Object[] a2) {
        if (a1 == a2)
            return true;
        if (a1 == null || a2 == null)
            return false;
        int length = a1.length;
        if (a2.length != length)
            return false;

        for (int i = 0; i < length; i++) {
            Object e1 = a1[i];
            Object e2 = a2[i];

            if (e1 == e2)
                continue;
            if (e1 == null)
                return false;

            // Figure out whether the two elements are equal
            boolean eq = deepEquals0(e1, e2);

            if (!eq)
                return false;
        }
        return true;
    }

    public static String deepToString(Object[] a) {
        if (a == null)
            return "null";

        int bufLen = 20 * a.length;
        if (a.length != 0 && bufLen <= 0)
            bufLen = Integer.MAX_VALUE;
        StringBuilder buf = new StringBuilder(bufLen);
        deepToString(a, buf, new HashSet<>());
        return buf.toString();
    }

    public static boolean equals(long[] a, long[] a2) {
        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        return ArraysSupport.mismatch(a, a2, length) < 0;
    }

    public static boolean equals(long[] a, int aFromIndex, int aToIndex, long[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        if (aLength != bLength)
            return false;

        return ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, aLength) < 0;
    }

    public static boolean equals(int[] a, int[] a2) {
        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        return ArraysSupport.mismatch(a, a2, length) < 0;
    }

    public static boolean equals(int[] a, int aFromIndex, int aToIndex, int[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        if (aLength != bLength)
            return false;

        return ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, aLength) < 0;
    }

    public static boolean equals(short[] a, short a2[]) {
        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        return ArraysSupport.mismatch(a, a2, length) < 0;
    }

    public static boolean equals(short[] a, int aFromIndex, int aToIndex, short[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        if (aLength != bLength)
            return false;

        return ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, aLength) < 0;
    }

    public static boolean equals(char[] a, char[] a2) {
        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        return ArraysSupport.mismatch(a, a2, length) < 0;
    }

    public static boolean equals(char[] a, int aFromIndex, int aToIndex, char[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        if (aLength != bLength)
            return false;

        return ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, aLength) < 0;
    }

    public static boolean equals(byte[] a, byte[] a2) {
        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        return ArraysSupport.mismatch(a, a2, length) < 0;
    }

    public static boolean equals(byte[] a, int aFromIndex, int aToIndex, byte[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        if (aLength != bLength)
            return false;

        return ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, aLength) < 0;
    }

    public static boolean equals(boolean[] a, boolean[] a2) {
        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        return ArraysSupport.mismatch(a, a2, length) < 0;
    }

    public static boolean equals(boolean[] a, int aFromIndex, int aToIndex,
                                 boolean[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        if (aLength != bLength)
            return false;

        return ArraysSupport.mismatch(a, aFromIndex,
                                      b, bFromIndex,
                                      aLength) < 0;
    }

    public static boolean equals(double[] a, double[] a2) {
        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        return ArraysSupport.mismatch(a, a2, length) < 0;
    }

    public static boolean equals(double[] a, int aFromIndex, int aToIndex, double[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        if (aLength != bLength)
            return false;

        return ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, aLength) < 0;
    }

    public static boolean equals(float[] a, float[] a2) {
        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        return ArraysSupport.mismatch(a, a2, length) < 0;
    }

    public static boolean equals(float[] a, int aFromIndex, int aToIndex, float[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        if (aLength != bLength)
            return false;

        return ArraysSupport.mismatch(a, aFromIndex, b, bFromIndex, aLength) < 0;
    }

    public static boolean equals(Object[] a, Object[] a2) {
        if (a==a2)
            return true;
        if (a==null || a2==null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        for (int i=0; i<length; i++) {
            if (!Objects.equals(a[i], a2[i]))
                return false;
        }

        return true;
    }

    public static boolean equals(Object[] a, int aFromIndex, int aToIndex, Object[] b, int bFromIndex, int bToIndex) {
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        if (aLength != bLength)
            return false;

        for (int i = 0; i < aLength; i++) {
            if (!Objects.equals(a[aFromIndex++], b[bFromIndex++]))
                return false;
        }

        return true;
    }

    public static <T> boolean equals(T[] a, T[] a2, Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        if (a == a2)
            return true;
        if (a == null || a2 == null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        for (int i = 0; i < length; i++) {
            if (cmp.compare(a[i], a2[i]) != 0)
                return false;
        }

        return true;
    }

    public static <T> boolean equals(T[] a, int aFromIndex, int aToIndex, T[] b, int bFromIndex, int bToIndex,
            Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        if (aLength != bLength)
            return false;

        for (int i = 0; i < aLength; i++) {
            if (cmp.compare(a[aFromIndex++], b[bFromIndex++]) != 0)
                return false;
        }

        return true;
    }

    public static int hashCode(long a[]) {
        if (a == null)
            return 0;

        int result = 1;
        for (long element : a) {
            int elementHash = (int) (element ^ (element >>> 32));
            result = 31 * result + elementHash;
        }

        return result;
    }

    public static int hashCode(int a[]) {
        if (a == null)
            return 0;

        int result = 1;
        for (int element : a)
            result = 31 * result + element;

        return result;
    }

    public static int hashCode(short a[]) {
        if (a == null)
            return 0;

        int result = 1;
        for (short element : a)
            result = 31 * result + element;

        return result;
    }

    public static int hashCode(char a[]) {
        if (a == null)
            return 0;

        int result = 1;
        for (char element : a)
            result = 31 * result + element;

        return result;
    }

    public static int hashCode(byte a[]) {
        if (a == null)
            return 0;

        int result = 1;
        for (byte element : a)
            result = 31 * result + element;

        return result;
    }

    public static int hashCode(boolean a[]) {
        if (a == null)
            return 0;

        int result = 1;
        for (boolean element : a)
            result = 31 * result + (element ? 1231 : 1237);

        return result;
    }

    public static int hashCode(float a[]) {
        if (a == null)
            return 0;

        int result = 1;
        for (float element : a)
            result = 31 * result + Float.floatToIntBits(element);

        return result;
    }

    public static int hashCode(double a[]) {
        if (a == null)
            return 0;

        int result = 1;
        for (double element : a) {
            long bits = Double.doubleToLongBits(element);
            result = 31 * result + (int) (bits ^ (bits >>> 32));
        }
        return result;
    }

    public static int hashCode(Object a[]) {
        if (a == null)
            return 0;

        int result = 1;

        for (Object element : a)
            result = 31 * result + (element == null ? 0 : element.hashCode());

        return result;
    }

    public static <T> Stream<T> stream(T[] array) {
        return stream(array, 0, array.length);
    }

    public static <T> Stream<T> stream(T[] array, int startInclusive, int endExclusive) {
        return StreamSupport.stream(spliterator(array, startInclusive, endExclusive), false);
    }

    public static IntStream stream(int[] array) {
        return stream(array, 0, array.length);
    }

    public static IntStream stream(int[] array, int startInclusive, int endExclusive) {
        return StreamSupport.intStream(spliterator(array, startInclusive, endExclusive), false);
    }

    public static LongStream stream(long[] array) {
        return stream(array, 0, array.length);
    }

    public static LongStream stream(long[] array, int startInclusive, int endExclusive) {
        return StreamSupport.longStream(spliterator(array, startInclusive, endExclusive), false);
    }

    public static DoubleStream stream(double[] array) {
        return stream(array, 0, array.length);
    }

    public static DoubleStream stream(double[] array, int startInclusive, int endExclusive) {
        return StreamSupport.doubleStream(spliterator(array, startInclusive, endExclusive), false);
    }

    public static String toString(long[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0;; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public static String toString(int[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0;; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public static String toString(short[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0;; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public static String toString(char[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0;; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public static String toString(byte[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0;; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public static String toString(boolean[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0;; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public static String toString(float[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0;; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public static String toString(double[] a) {
        if (a == null)
            return "null";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0;; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public static String toString(Object[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0;; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public static String toStringNonNull(Object[] a) {
        return toString(a).replaceAll(", null", "").replaceAll("null", "");
    }

    // helper methods
    static void rangeCheck(int arrayLength, int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        }
        if (fromIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(fromIndex);
        }
        if (toIndex > arrayLength) {
            throw new ArrayIndexOutOfBoundsException(toIndex);
        }
    }

    private static int primitiveArrayHashCode(Object a, Class<?> cl) {
        return
            (cl == byte.class)    ? hashCode((byte[]) a)    :
            (cl == int.class)     ? hashCode((int[]) a)     :
            (cl == long.class)    ? hashCode((long[]) a)    :
            (cl == char.class)    ? hashCode((char[]) a)    :
            (cl == short.class)   ? hashCode((short[]) a)   :
            (cl == boolean.class) ? hashCode((boolean[]) a) :
            (cl == double.class)  ? hashCode((double[]) a)  :
            hashCode((float[]) a);
    }

    private static boolean deepEquals0(Object e1, Object e2) {
        assert e1 != null;
        boolean eq;
        if (e1 instanceof Object[] && e2 instanceof Object[])
            eq = deepEquals((Object[]) e1, (Object[]) e2);
        else if (e1 instanceof byte[] && e2 instanceof byte[])
            eq = equals((byte[]) e1, (byte[]) e2);
        else if (e1 instanceof short[] && e2 instanceof short[])
            eq = equals((short[]) e1, (short[]) e2);
        else if (e1 instanceof int[] && e2 instanceof int[])
            eq = equals((int[]) e1, (int[]) e2);
        else if (e1 instanceof long[] && e2 instanceof long[])
            eq = equals((long[]) e1, (long[]) e2);
        else if (e1 instanceof char[] && e2 instanceof char[])
            eq = equals((char[]) e1, (char[]) e2);
        else if (e1 instanceof float[] && e2 instanceof float[])
            eq = equals((float[]) e1, (float[]) e2);
        else if (e1 instanceof double[] && e2 instanceof double[])
            eq = equals((double[]) e1, (double[]) e2);
        else if (e1 instanceof boolean[] && e2 instanceof boolean[])
            eq = equals((boolean[]) e1, (boolean[]) e2);
        else
            eq = e1.equals(e2);
        return eq;
    }

    private static void deepToString(Object[] a, StringBuilder buf, java.util.Set<Object[]> dejaVu) {
        if (a == null) {
            buf.append("null");
            return;
        }
        int iMax = a.length - 1;
        if (iMax == -1) {
            buf.append("[]");
            return;
        }

        dejaVu.add(a);
        buf.append('[');
        for (int i = 0;; i++) {

            Object element = a[i];
            if (element == null) {
                buf.append("null");
            } else {
                Class<?> eClass = element.getClass();

                if (eClass.isArray()) {
                    if (eClass == byte[].class)
                        buf.append(toString((byte[]) element));
                    else if (eClass == short[].class)
                        buf.append(toString((short[]) element));
                    else if (eClass == int[].class)
                        buf.append(toString((int[]) element));
                    else if (eClass == long[].class)
                        buf.append(toString((long[]) element));
                    else if (eClass == char[].class)
                        buf.append(toString((char[]) element));
                    else if (eClass == float[].class)
                        buf.append(toString((float[]) element));
                    else if (eClass == double[].class)
                        buf.append(toString((double[]) element));
                    else if (eClass == boolean[].class)
                        buf.append(toString((boolean[]) element));
                    else { // element is an array of object references
                        if (dejaVu.contains(element))
                            buf.append("[...]");
                        else
                            deepToString((Object[]) element, buf, dejaVu);
                    }
                } else { // element is non-null and not an array
                    buf.append(element.toString());
                }
            }
            if (i == iMax)
                break;
            buf.append(", ");
        }
        buf.append(']');
        dejaVu.remove(a);
    }

    private static <T> Spliterator<T> spliterator(T[] array, int startInclusive, int endExclusive) {
        return Spliterators.spliterator(array, startInclusive, endExclusive,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    private static Spliterator.OfInt spliterator(int[] array, int startInclusive, int endExclusive) {
        return Spliterators.spliterator(array, startInclusive, endExclusive,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    private static Spliterator.OfLong spliterator(long[] array, int startInclusive, int endExclusive) {
        return Spliterators.spliterator(array, startInclusive, endExclusive,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    private static Spliterator.OfDouble spliterator(double[] array, int startInclusive, int endExclusive) {
        return Spliterators.spliterator(array, startInclusive, endExclusive,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }
}
class DynamicArray<E> extends LinearStructure<E> implements IndexBased<E> {
    private final E[] a;

    DynamicArray(E[] array) {
        a = Objects.requireNonNull(array);
    }

    @Override
    public boolean add(E e) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {}.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public boolean addAll(LinearStructure<E> c) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {}.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public void clear() {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {}.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public DynamicArray<E> clone() {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {}.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    @SuppressWarnings("rawtypes")
    public int compare(Structure o1, Structure o2) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {}.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public boolean equals(Object o) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {}.getClass().getEnclosingMethod()), this.getClass().toString());

    }

    @Override
    public int hashCode() {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {}.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayItr<>(a);
    }

    @Override
    public boolean remove(Object o) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public boolean removeAll(LinearStructure<E> c) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public boolean replaceAll(LinearStructure<E> c, E e) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public boolean retainAll(LinearStructure<E> c) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public Iterator<E> reverseIterator() {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public int size() {
        return a.length;
    }

    @Override
    public void add(int index, E element) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public boolean addAll(int index, Structure<E> c) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public E get(int index) {
        return a[index];
    }

    @Override
    public int indexOf(Object o) {
        E[] a = this.a;
        if (o == null) {
            for (int i = 0; i < a.length; i++)
                if (a[i] == null)
                    return i;
        } else {
            for (int i = 0; i < a.length; i++)
                if (o.equals(a[i]))
                    return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public E remove(int index) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public E set(int index, E element) {
        E oldValue = a[index];
        a[index] = element;
        return oldValue;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(a, a.length, Object[].class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        int size = size();
        if (a.length < size)
            return Arrays.copyOf(this.a, size, (Class<? extends T[]>) a.getClass());
        System.arraycopy(this.a, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }
}
class ArrayItr<E> implements BidirectionalIterator<E> {
    private int cursor;
    private final E[] a;

    ArrayItr(E[] a) {
        this.a = a;
    }

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public boolean hasNext() {
        return cursor < a.length;
    }

    @Override
    public boolean hasPrevious() {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public E next() {
        int i = cursor;
        if (i >= a.length) {
            throw new NoSuchElementException();
        }
        cursor = i + 1;
        return a[i];
    }

    @Override
    public int nextIndex() {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public E previous() {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public int previousIndex() {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }

    @Override
    public void remove() {
        throw new MethodNotApplicableException(Utilities.getMethodName(new Object() {
        }.getClass().getEnclosingMethod()), this.getClass().toString());
    }
}