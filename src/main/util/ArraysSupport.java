package main.util;

import java.lang.ref.*;
import java.lang.reflect.*;
import java.security.*;

public class ArraysSupport {
    static final Unsafe U = Unsafe.getUnsafe();

    private static final boolean BIG_ENDIAN = U.isBigEndian();

    public static final int LOG2_ARRAY_BOOLEAN_INDEX_SCALE = exactLog2(Unsafe.ARRAY_BOOLEAN_INDEX_SCALE);
    public static final int LOG2_ARRAY_BYTE_INDEX_SCALE = exactLog2(Unsafe.ARRAY_BYTE_INDEX_SCALE);
    public static final int LOG2_ARRAY_CHAR_INDEX_SCALE = exactLog2(Unsafe.ARRAY_CHAR_INDEX_SCALE);
    public static final int LOG2_ARRAY_SHORT_INDEX_SCALE = exactLog2(Unsafe.ARRAY_SHORT_INDEX_SCALE);
    public static final int LOG2_ARRAY_INT_INDEX_SCALE = exactLog2(Unsafe.ARRAY_INT_INDEX_SCALE);
    public static final int LOG2_ARRAY_LONG_INDEX_SCALE = exactLog2(Unsafe.ARRAY_LONG_INDEX_SCALE);
    public static final int LOG2_ARRAY_FLOAT_INDEX_SCALE = exactLog2(Unsafe.ARRAY_FLOAT_INDEX_SCALE);
    public static final int LOG2_ARRAY_DOUBLE_INDEX_SCALE = exactLog2(Unsafe.ARRAY_DOUBLE_INDEX_SCALE);

    private static final int LOG2_BYTE_BIT_SIZE = exactLog2(Byte.SIZE);

    private static int exactLog2(int scale) {
        if ((scale & (scale - 1)) != 0)
            throw new Error("data type scale not a power of two");
        return Integer.numberOfTrailingZeros(scale);
    }
private ArraysSupport() {
    }
public static int vectorizedMismatch(Object a, long aOffset, Object b, long bOffset, int length,
        int log2ArrayIndexScale) {
        int log2ValuesPerWidth = LOG2_ARRAY_LONG_INDEX_SCALE - log2ArrayIndexScale;
        int wi = 0;
        for (; wi < length >> log2ValuesPerWidth; wi++) {
            long bi = ((long) wi) << LOG2_ARRAY_LONG_INDEX_SCALE;
            long av = U.getLongUnaligned(a, aOffset + bi);
            long bv = U.getLongUnaligned(b, bOffset + bi);
            if (av != bv) {
                long x = av ^ bv;
                int o = BIG_ENDIAN ? Long.numberOfLeadingZeros(x) >> (LOG2_BYTE_BIT_SIZE + log2ArrayIndexScale)
                        : Long.numberOfTrailingZeros(x) >> (LOG2_BYTE_BIT_SIZE + log2ArrayIndexScale);
                return (wi << log2ValuesPerWidth) + o;
            }
        }
    int tail = length - (wi << log2ValuesPerWidth);

        if (log2ArrayIndexScale < LOG2_ARRAY_INT_INDEX_SCALE) {
            int wordTail = 1 << (LOG2_ARRAY_INT_INDEX_SCALE - log2ArrayIndexScale);
            if (tail >= wordTail) {
                long bi = ((long) wi) << LOG2_ARRAY_LONG_INDEX_SCALE;
                int av = U.getIntUnaligned(a, aOffset + bi);
                int bv = U.getIntUnaligned(b, bOffset + bi);
                if (av != bv) {
                    int x = av ^ bv;
                    int o = BIG_ENDIAN ? Integer.numberOfLeadingZeros(x) >> (LOG2_BYTE_BIT_SIZE + log2ArrayIndexScale)
                            : Integer.numberOfTrailingZeros(x) >> (LOG2_BYTE_BIT_SIZE + log2ArrayIndexScale);
                    return (wi << log2ValuesPerWidth) + o;
                }
                tail -= wordTail;
            }
            return ~tail;
        } else {
            return ~tail;
        }
    }
public static long vectorizedMismatchLargeForBytes(Object a, long aOffset, Object b, long bOffset, long length) {
        long off = 0;
        long remaining = length;
        int i, size;
        boolean lastSubRange = false;
        while (remaining > 7 && !lastSubRange) {
            if (remaining > Integer.MAX_VALUE) {
                size = Integer.MAX_VALUE;
            } else {
                size = (int) remaining;
                lastSubRange = true;
            }
            i = vectorizedMismatch(a, aOffset + off, b, bOffset + off, size, LOG2_ARRAY_BYTE_INDEX_SCALE);
            if (i >= 0)
                return off + i;

            i = size - ~i;
            off += i;
            remaining -= i;
        }
        return ~remaining;
    }
public static int mismatch(boolean[] a, boolean[] b, int length) {
        int i = 0;
        if (length > 7) {
            if (a[0] != b[0])
                return 0;
            i = vectorizedMismatch(a, Unsafe.ARRAY_BOOLEAN_BASE_OFFSET, b, Unsafe.ARRAY_BOOLEAN_BASE_OFFSET, length,
                    LOG2_ARRAY_BOOLEAN_INDEX_SCALE);
            if (i >= 0)
                return i;
            i = length - ~i;
        }
        for (; i < length; i++) {
            if (a[i] != b[i])
                return i;
        }
        return -1;
    }
public static int mismatch(boolean[] a, int aFromIndex, boolean[] b, int bFromIndex, int length) {
        int i = 0;
        if (length > 7) {
            if (a[aFromIndex] != b[bFromIndex])
                return 0;
            int aOffset = Unsafe.ARRAY_BOOLEAN_BASE_OFFSET + aFromIndex;
            int bOffset = Unsafe.ARRAY_BOOLEAN_BASE_OFFSET + bFromIndex;
            i = vectorizedMismatch(a, aOffset, b, bOffset, length, LOG2_ARRAY_BOOLEAN_INDEX_SCALE);
            if (i >= 0)
                return i;
            i = length - ~i;
        }
        for (; i < length; i++) {
            if (a[aFromIndex + i] != b[bFromIndex + i])
                return i;
        }
        return -1;
    }
public static int mismatch(byte[] a, byte[] b, int length) {
        int i = 0;
        if (length > 7) {
            if (a[0] != b[0])
                return 0;
            i = vectorizedMismatch(a, Unsafe.ARRAY_BYTE_BASE_OFFSET, b, Unsafe.ARRAY_BYTE_BASE_OFFSET, length,
                    LOG2_ARRAY_BYTE_INDEX_SCALE);
            if (i >= 0)
                return i;

            i = length - ~i;
        }
    for (; i < length; i++) {
            if (a[i] != b[i])
                return i;
        }
        return -1;
    }
public static int mismatch(byte[] a, int aFromIndex, byte[] b, int bFromIndex, int length) {
        int i = 0;
        if (length > 7) {
            if (a[aFromIndex] != b[bFromIndex])
                return 0;
            int aOffset = Unsafe.ARRAY_BYTE_BASE_OFFSET + aFromIndex;
            int bOffset = Unsafe.ARRAY_BYTE_BASE_OFFSET + bFromIndex;
            i = vectorizedMismatch(a, aOffset, b, bOffset, length, LOG2_ARRAY_BYTE_INDEX_SCALE);
            if (i >= 0)
                return i;
            i = length - ~i;
        }
        for (; i < length; i++) {
            if (a[aFromIndex + i] != b[bFromIndex + i])
                return i;
        }
        return -1;
    }
public static int mismatch(char[] a, char[] b, int length) {
        int i = 0;
        if (length > 3) {
            if (a[0] != b[0])
                return 0;
            i = vectorizedMismatch(a, Unsafe.ARRAY_CHAR_BASE_OFFSET, b, Unsafe.ARRAY_CHAR_BASE_OFFSET, length,
                    LOG2_ARRAY_CHAR_INDEX_SCALE);
            if (i >= 0)
                return i;
            i = length - ~i;
        }
        for (; i < length; i++) {
            if (a[i] != b[i])
                return i;
        }
        return -1;
    }
public static int mismatch(char[] a, int aFromIndex, char[] b, int bFromIndex, int length) {
        int i = 0;
        if (length > 3) {
            if (a[aFromIndex] != b[bFromIndex])
                return 0;
            int aOffset = Unsafe.ARRAY_CHAR_BASE_OFFSET + (aFromIndex << LOG2_ARRAY_CHAR_INDEX_SCALE);
            int bOffset = Unsafe.ARRAY_CHAR_BASE_OFFSET + (bFromIndex << LOG2_ARRAY_CHAR_INDEX_SCALE);
            i = vectorizedMismatch(a, aOffset, b, bOffset, length, LOG2_ARRAY_CHAR_INDEX_SCALE);
            if (i >= 0)
                return i;
            i = length - ~i;
        }
        for (; i < length; i++) {
            if (a[aFromIndex + i] != b[bFromIndex + i])
                return i;
        }
        return -1;
    }
public static int mismatch(short[] a, short[] b, int length) {
        int i = 0;
        if (length > 3) {
            if (a[0] != b[0])
                return 0;
            i = vectorizedMismatch(a, Unsafe.ARRAY_SHORT_BASE_OFFSET, b, Unsafe.ARRAY_SHORT_BASE_OFFSET, length,
                    LOG2_ARRAY_SHORT_INDEX_SCALE);
            if (i >= 0)
                return i;
            i = length - ~i;
        }
        for (; i < length; i++) {
            if (a[i] != b[i])
                return i;
        }
        return -1;
    }
public static int mismatch(short[] a, int aFromIndex, short[] b, int bFromIndex, int length) {
        int i = 0;
        if (length > 3) {
            if (a[aFromIndex] != b[bFromIndex])
                return 0;
            int aOffset = Unsafe.ARRAY_SHORT_BASE_OFFSET + (aFromIndex << LOG2_ARRAY_SHORT_INDEX_SCALE);
            int bOffset = Unsafe.ARRAY_SHORT_BASE_OFFSET + (bFromIndex << LOG2_ARRAY_SHORT_INDEX_SCALE);
            i = vectorizedMismatch(a, aOffset, b, bOffset, length, LOG2_ARRAY_SHORT_INDEX_SCALE);
            if (i >= 0)
                return i;
            i = length - ~i;
        }
        for (; i < length; i++) {
            if (a[aFromIndex + i] != b[bFromIndex + i])
                return i;
        }
        return -1;
    }
public static int mismatch(int[] a, int[] b, int length) {
        int i = 0;
        if (length > 1) {
            if (a[0] != b[0])
                return 0;
            i = vectorizedMismatch(a, Unsafe.ARRAY_INT_BASE_OFFSET, b, Unsafe.ARRAY_INT_BASE_OFFSET, length,
                    LOG2_ARRAY_INT_INDEX_SCALE);
            if (i >= 0)
                return i;
            i = length - ~i;
        }
        for (; i < length; i++) {
            if (a[i] != b[i])
                return i;
        }
        return -1;
    }
public static int mismatch(int[] a, int aFromIndex, int[] b, int bFromIndex, int length) {
        int i = 0;
        if (length > 1) {
            if (a[aFromIndex] != b[bFromIndex])
                return 0;
            int aOffset = Unsafe.ARRAY_INT_BASE_OFFSET + (aFromIndex << LOG2_ARRAY_INT_INDEX_SCALE);
            int bOffset = Unsafe.ARRAY_INT_BASE_OFFSET + (bFromIndex << LOG2_ARRAY_INT_INDEX_SCALE);
            i = vectorizedMismatch(a, aOffset, b, bOffset, length, LOG2_ARRAY_INT_INDEX_SCALE);
            if (i >= 0)
                return i;
            i = length - ~i;
        }
        for (; i < length; i++) {
            if (a[aFromIndex + i] != b[bFromIndex + i])
                return i;
        }
        return -1;
    }
public static int mismatch(float[] a, float[] b, int length) {
        return mismatch(a, 0, b, 0, length);
    }
public static int mismatch(float[] a, int aFromIndex, float[] b, int bFromIndex, int length) {
        int i = 0;
        if (length > 1) {
            if (Float.floatToRawIntBits(a[aFromIndex]) == Float.floatToRawIntBits(b[bFromIndex])) {
                int aOffset = Unsafe.ARRAY_FLOAT_BASE_OFFSET + (aFromIndex << LOG2_ARRAY_FLOAT_INDEX_SCALE);
                int bOffset = Unsafe.ARRAY_FLOAT_BASE_OFFSET + (bFromIndex << LOG2_ARRAY_FLOAT_INDEX_SCALE);
                i = vectorizedMismatch(a, aOffset, b, bOffset, length, LOG2_ARRAY_FLOAT_INDEX_SCALE);
            }
            // Mismatched
            if (i >= 0) {
                if (!Float.isNaN(a[aFromIndex + i]) || !Float.isNaN(b[bFromIndex + i]))
                    return i;

                i++;
            } else {
                i = length - ~i;
            }
        }
        for (; i < length; i++) {
            if (Float.floatToIntBits(a[aFromIndex + i]) != Float.floatToIntBits(b[bFromIndex + i]))
                return i;
        }
        return -1;
    }
public static int mismatch(long[] a, long[] b, int length) {
        if (length == 0) {
            return -1;
        }
        if (a[0] != b[0])
            return 0;
        int i = vectorizedMismatch(a, Unsafe.ARRAY_LONG_BASE_OFFSET, b, Unsafe.ARRAY_LONG_BASE_OFFSET, length,
                LOG2_ARRAY_LONG_INDEX_SCALE);
        return i >= 0 ? i : -1;
    }
public static int mismatch(long[] a, int aFromIndex, long[] b, int bFromIndex, int length) {
        if (length == 0) {
            return -1;
        }
        if (a[aFromIndex] != b[bFromIndex])
            return 0;
        int aOffset = Unsafe.ARRAY_LONG_BASE_OFFSET + (aFromIndex << LOG2_ARRAY_LONG_INDEX_SCALE);
        int bOffset = Unsafe.ARRAY_LONG_BASE_OFFSET + (bFromIndex << LOG2_ARRAY_LONG_INDEX_SCALE);
        int i = vectorizedMismatch(a, aOffset, b, bOffset, length, LOG2_ARRAY_LONG_INDEX_SCALE);
        return i >= 0 ? i : -1;
    }
public static int mismatch(double[] a, double[] b, int length) {
        return mismatch(a, 0, b, 0, length);
    }
public static int mismatch(double[] a, int aFromIndex, double[] b, int bFromIndex, int length) {
        if (length == 0) {
            return -1;
        }
        int i = 0;
        if (Double.doubleToRawLongBits(a[aFromIndex]) == Double.doubleToRawLongBits(b[bFromIndex])) {
            int aOffset = Unsafe.ARRAY_DOUBLE_BASE_OFFSET + (aFromIndex << LOG2_ARRAY_DOUBLE_INDEX_SCALE);
            int bOffset = Unsafe.ARRAY_DOUBLE_BASE_OFFSET + (bFromIndex << LOG2_ARRAY_DOUBLE_INDEX_SCALE);
            i = vectorizedMismatch(a, aOffset, b, bOffset, length, LOG2_ARRAY_DOUBLE_INDEX_SCALE);
        }
        if (i >= 0) {
            // Check if mismatch is not associated with two NaN values
            if (!Double.isNaN(a[aFromIndex + i]) || !Double.isNaN(b[bFromIndex + i]))
                return i;

            // Mismatch on two different NaN values that are normalized to match
            // Fall back to slow mechanism
            // ISSUE: Consider looping over vectorizedMismatch adjusting ranges
            // However, requires that returned value be relative to input ranges
            i++;
            for (; i < length; i++) {
                if (Double.doubleToLongBits(a[aFromIndex + i]) != Double.doubleToLongBits(b[bFromIndex + i]))
                    return i;
            }
        }
    return -1;
    }
public static final int MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

    public static int newLength(int oldLength, int minGrowth, int prefGrowth) {
        int newLength = Math.max(minGrowth, prefGrowth) + oldLength;
        if (newLength - MAX_ARRAY_LENGTH <= 0) {
            return newLength;
        }
        return hugeLength(oldLength, minGrowth);
    }
private static int hugeLength(int oldLength, int minGrowth) {
        int minLength = oldLength + minGrowth;
        if (minLength < 0) { // overflow
            throw new OutOfMemoryError("Required array length too large");
        }
        if (minLength <= MAX_ARRAY_LENGTH) {
            return MAX_ARRAY_LENGTH;
        }
        return Integer.MAX_VALUE;
    }
}
final class Unsafe {

    private static native void registerNatives();
    static {
        registerNatives();
    }
private Unsafe() {}
private static final Unsafe theUnsafe = new Unsafe();

    /**
     * Provides the caller with the capability of performing unsafe
     * operations.
     *
     * <p>The returned {@code Unsafe} object should be carefully guarded
     * by the caller, since it can be used to read and write data at arbitrary
     * memory addresses.  It must never be passed to untrusted code.
     *
     * <p>Most methods in this class are very low-level, and correspond to a
     * small number of hardware instructions (on typical machines).  Compilers
     * are encouraged to optimize these methods accordingly.
     *
     * <p>Here is a suggested idiom for using unsafe operations:
     *
     * <pre> {@code
     * class MyTrustedClass {
     *   private static final Unsafe unsafe = Unsafe.getUnsafe();
     *   ...
     *   private long myCountAddress = ...;
     *   public int getCount() { return unsafe.getByte(myCountAddress); }
     * }}</pre>
     *
     * (It may assist compilers to make the local variable {@code final}.)
     */
    public static Unsafe getUnsafe() {
        return theUnsafe;
    }
/// peek and poke operations
    /// (compilers should optimize these to memory ops)

    // These work on object fields in the Java heap.
    // They will not work on elements of packed arrays.

    /**
     * Fetches a value from a given Java variable.
     * More specifically, fetches a field or array element within the given
     * object {@code o} at the given offset, or (if {@code o} is null)
     * from the memory address whose numerical value is the given offset.
     * <p>
     * The results are undefined unless one of the following cases is true:
     * <ul>
     * <li>The offset was obtained from {@link #objectFieldOffset} on
     * the {@link java.lang.reflect.Field} of some Java field and the object
     * referred to by {@code o} is of a class compatible with that
     * field's class.
     *
     * <li>The offset and object reference {@code o} (either null or
     * non-null) were both obtained via {@link #staticFieldOffset}
     * and {@link #staticFieldBase} (respectively) from the
     * reflective {@link Field} representation of some Java field.
     *
     * <li>The object referred to by {@code o} is an array, and the offset
     * is an integer of the form {@code B+N*S}, where {@code N} is
     * a valid index into the array, and {@code B} and {@code S} are
     * the values obtained by {@link #arrayBaseOffset} and {@link
     * #arrayIndexScale} (respectively) from the array's class.  The value
     * referred to is the {@code N}<em>th</em> element of the array.
     *
     * </ul>
     * <p>
     * If one of the above cases is true, the call references a specific Java
     * variable (field or array element).  However, the results are undefined
     * if that variable is not in fact of the type returned by this method.
     * <p>
     * This method refers to a variable by means of two parameters, and so
     * it provides (in effect) a <em>double-register</em> addressing mode
     * for Java variables.  When the object reference is null, this method
     * uses its offset as an absolute address.  This is similar in operation
     * to methods such as {@link #getInt(long)}, which provide (in effect) a
     * <em>single-register</em> addressing mode for non-Java variables.
     * However, because Java variables may have a different layout in memory
     * from non-Java variables, programmers should not assume that these
     * two addressing modes are ever equivalent.  Also, programmers should
     * remember that offsets from the double-register addressing mode cannot
     * be portably confused with longs used in the single-register addressing
     * mode.
     *
     * @param o Java heap object in which the variable resides, if any, else
     *        null
     * @param offset indication of where the variable resides in a Java heap
     *        object, if any, else a memory address locating the variable
     *        statically
     * @return the value fetched from the indicated Java variable
     * @throws RuntimeException No defined exceptions are thrown, not even
     *         {@link NullPointerException}
     */
    
    public native int getInt(Object o, long offset);

    /**
     * Stores a value into a given Java variable.
     * <p>
     * The first two parameters are interpreted exactly as with
     * {@link #getInt(Object, long)} to refer to a specific
     * Java variable (field or array element).  The given value
     * is stored into that variable.
     * <p>
     * The variable must be of the same type as the method
     * parameter {@code x}.
     *
     * @param o Java heap object in which the variable resides, if any, else
     *        null
     * @param offset indication of where the variable resides in a Java heap
     *        object, if any, else a memory address locating the variable
     *        statically
     * @param x the value to store into the indicated Java variable
     * @throws RuntimeException No defined exceptions are thrown, not even
     *         {@link NullPointerException}
     */
    
    public native void putInt(Object o, long offset, int x);

    /**
     * Fetches a reference value from a given Java variable.
     * @see #getInt(Object, long)
     */
    
    public native Object getReference(Object o, long offset);

    /**
     * Stores a reference value into a given Java variable.
     * <p>
     * Unless the reference {@code x} being stored is either null
     * or matches the field type, the results are undefined.
     * If the reference {@code o} is non-null, card marks or
     * other store barriers for that object (if the VM requires them)
     * are updated.
     * @see #putInt(Object, long, int)
     */
    
    public native void putReference(Object o, long offset, Object x);

    /** @see #getInt(Object, long) */
    
    public native boolean getBoolean(Object o, long offset);

    /** @see #putInt(Object, long, int) */
    
    public native void    putBoolean(Object o, long offset, boolean x);

    /** @see #getInt(Object, long) */
    
    public native byte    getByte(Object o, long offset);

    /** @see #putInt(Object, long, int) */
    
    public native void    putByte(Object o, long offset, byte x);

    /** @see #getInt(Object, long) */
    
    public native short   getShort(Object o, long offset);

    /** @see #putInt(Object, long, int) */
    
    public native void    putShort(Object o, long offset, short x);

    /** @see #getInt(Object, long) */
    
    public native char    getChar(Object o, long offset);

    /** @see #putInt(Object, long, int) */
    
    public native void    putChar(Object o, long offset, char x);

    /** @see #getInt(Object, long) */
    
    public native long    getLong(Object o, long offset);

    /** @see #putInt(Object, long, int) */
    
    public native void    putLong(Object o, long offset, long x);

    /** @see #getInt(Object, long) */
    
    public native float   getFloat(Object o, long offset);

    /** @see #putInt(Object, long, int) */
    
    public native void    putFloat(Object o, long offset, float x);

    /** @see #getInt(Object, long) */
    
    public native double  getDouble(Object o, long offset);

    /** @see #putInt(Object, long, int) */
    
    public native void    putDouble(Object o, long offset, double x);

    /**
     * Fetches a native pointer from a given memory address.  If the address is
     * zero, or does not point into a block obtained from {@link
     * #allocateMemory}, the results are undefined.
     *
     * <p>If the native pointer is less than 64 bits wide, it is extended as
     * an unsigned number to a Java long.  The pointer may be indexed by any
     * given byte offset, simply by adding that offset (as a simple integer) to
     * the long representing the pointer.  The number of bytes actually read
     * from the target address may be determined by consulting {@link
     * #addressSize}.
     *
     * @see #allocateMemory
     * @see #getInt(Object, long)
     */
    
    public long getAddress(Object o, long offset) {
        if (ADDRESS_SIZE == 4) {
            return Integer.toUnsignedLong(getInt(o, offset));
        } else {
            return getLong(o, offset);
        }
    }
/**
     * Stores a native pointer into a given memory address.  If the address is
     * zero, or does not point into a block obtained from {@link
     * #allocateMemory}, the results are undefined.
     *
     * <p>The number of bytes actually written at the target address may be
     * determined by consulting {@link #addressSize}.
     *
     * @see #allocateMemory
     * @see #putInt(Object, long, int)
     */
    
    public void putAddress(Object o, long offset, long x) {
        if (ADDRESS_SIZE == 4) {
            putInt(o, offset, (int)x);
        } else {
            putLong(o, offset, x);
        }
    }
// These read VM internal data.

    /**
     * Fetches an uncompressed reference value from a given native variable
     * ignoring the VM's compressed references mode.
     *
     * @param address a memory address locating the variable
     * @return the value fetched from the indicated native variable
     */
    public native Object getUncompressedObject(long address);

    // These work on values in the C heap.

    /**
     * Fetches a value from a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #allocateMemory
     */
    
    public byte getByte(long address) {
        return getByte(null, address);
    }
/**
     * Stores a value into a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #getByte(long)
     */
    
    public void putByte(long address, byte x) {
        putByte(null, address, x);
    }
/** @see #getByte(long) */
    
    public short getShort(long address) {
        return getShort(null, address);
    }
/** @see #putByte(long, byte) */
    
    public void putShort(long address, short x) {
        putShort(null, address, x);
    }
/** @see #getByte(long) */
    
    public char getChar(long address) {
        return getChar(null, address);
    }
/** @see #putByte(long, byte) */
    
    public void putChar(long address, char x) {
        putChar(null, address, x);
    }
/** @see #getByte(long) */
    
    public int getInt(long address) {
        return getInt(null, address);
    }
/** @see #putByte(long, byte) */
    
    public void putInt(long address, int x) {
        putInt(null, address, x);
    }
/** @see #getByte(long) */
    
    public long getLong(long address) {
        return getLong(null, address);
    }
/** @see #putByte(long, byte) */
    
    public void putLong(long address, long x) {
        putLong(null, address, x);
    }
/** @see #getByte(long) */
    
    public float getFloat(long address) {
        return getFloat(null, address);
    }
/** @see #putByte(long, byte) */
    
    public void putFloat(long address, float x) {
        putFloat(null, address, x);
    }
/** @see #getByte(long) */
    
    public double getDouble(long address) {
        return getDouble(null, address);
    }
/** @see #putByte(long, byte) */
    
    public void putDouble(long address, double x) {
        putDouble(null, address, x);
    }
/** @see #getAddress(Object, long) */
    
    public long getAddress(long address) {
        return getAddress(null, address);
    }
/** @see #putAddress(Object, long, long) */
    
    public void putAddress(long address, long x) {
        putAddress(null, address, x);
    }



    /// helper methods for validating various types of objects/values

    /**
     * Create an exception reflecting that some of the input was invalid
     *
     * <em>Note:</em> It is the responsibility of the caller to make
     * sure arguments are checked before the methods are called. While
     * some rudimentary checks are performed on the input, the checks
     * are best effort and when performance is an overriding priority,
     * as when methods of this class are optimized by the runtime
     * compiler, some or all checks (if any) may be elided. Hence, the
     * caller must not rely on the checks and corresponding
     * exceptions!
     *
     * @return an exception object
     */
    private RuntimeException invalidInput() {
        return new IllegalArgumentException();
    }
/**
     * Check if a value is 32-bit clean (32 MSB are all zero)
     *
     * @param value the 64-bit value to check
     *
     * @return true if the value is 32-bit clean
     */
    private boolean is32BitClean(long value) {
        return value >>> 32 == 0;
    }
/**
     * Check the validity of a size (the equivalent of a size_t)
     *
     * @throws RuntimeException if the size is invalid
     *         (<em>Note:</em> after optimization, invalid inputs may
     *         go undetected, which will lead to unpredictable
     *         behavior)
     */
    private void checkSize(long size) {
        if (ADDRESS_SIZE == 4) {
            // Note: this will also check for negative sizes
            if (!is32BitClean(size)) {
                throw invalidInput();
            }
        } else if (size < 0) {
            throw invalidInput();
        }
    }
/**
     * Check the validity of a native address (the equivalent of void*)
     *
     * @throws RuntimeException if the address is invalid
     *         (<em>Note:</em> after optimization, invalid inputs may
     *         go undetected, which will lead to unpredictable
     *         behavior)
     */
    private void checkNativeAddress(long address) {
        if (ADDRESS_SIZE == 4) {
            // Accept both zero and sign extended pointers. A valid
            // pointer will, after the +1 below, either have produced
            // the value 0x0 or 0x1. Masking off the low bit allows
            // for testing against 0.
            if ((((address >> 32) + 1) & ~1) != 0) {
                throw invalidInput();
            }
        }
    }
/**
     * Check the validity of an offset, relative to a base object
     *
     * @param o the base object
     * @param offset the offset to check
     *
     * @throws RuntimeException if the size is invalid
     *         (<em>Note:</em> after optimization, invalid inputs may
     *         go undetected, which will lead to unpredictable
     *         behavior)
     */
    private void checkOffset(Object o, long offset) {
        if (ADDRESS_SIZE == 4) {
            // Note: this will also check for negative offsets
            if (!is32BitClean(offset)) {
                throw invalidInput();
            }
        } else if (offset < 0) {
            throw invalidInput();
        }
    }

    private void checkPointer(Object o, long offset) {
        if (o == null) {
            checkNativeAddress(offset);
        } else {
            checkOffset(o, offset);
        }
    }

    private void checkPrimitiveArray(Class<?> c) {
        Class<?> componentType = c.getComponentType();
        if (componentType == null || !componentType.isPrimitive()) {
            throw invalidInput();
        }
    }

    private void checkPrimitivePointer(Object o, long offset) {
        checkPointer(o, offset);

        if (o != null) {
            checkPrimitiveArray(o.getClass());
        }
    }

    private long alignToHeapWordSize(long bytes) {
        if (bytes >= 0) {
            return (bytes + ADDRESS_SIZE - 1) & ~(ADDRESS_SIZE - 1);
        } else {
            throw invalidInput();
        }
    }

    public long allocateMemory(long bytes) {
        bytes = alignToHeapWordSize(bytes);

        allocateMemoryChecks(bytes);

        if (bytes == 0) {
            return 0;
        }
    long p = allocateMemory0(bytes);
        if (p == 0) {
            throw new OutOfMemoryError("Unable to allocate " + bytes + " bytes");
        }
    return p;
    }
    
    private void allocateMemoryChecks(long bytes) {
        checkSize(bytes);
    }

    public long reallocateMemory(long address, long bytes) {
        bytes = alignToHeapWordSize(bytes);

        reallocateMemoryChecks(address, bytes);

        if (bytes == 0) {
            freeMemory(address);
            return 0;
        }
        long p = (address == 0) ? allocateMemory0(bytes) : reallocateMemory0(address, bytes);
        if (p == 0) {
            throw new OutOfMemoryError("Unable to allocate " + bytes + " bytes");
        }
        return p;
    }

    private void reallocateMemoryChecks(long address, long bytes) {
        checkPointer(null, address);
        checkSize(bytes);
    }

    public void setMemory(Object o, long offset, long bytes, byte value) {
        setMemoryChecks(o, offset, bytes, value);

        if (bytes == 0) {
            return;
        }
        setMemory0(o, offset, bytes, value);
    }

    public void setMemory(long address, long bytes, byte value) {
        setMemory(null, address, bytes, value);
    }

    private void setMemoryChecks(Object o, long offset, long bytes, byte value) {
        checkPrimitivePointer(o, offset);
        checkSize(bytes);
    }

    public void copyMemory(Object srcBase, long srcOffset,
                           Object destBase, long destOffset,
                           long bytes) {
        copyMemoryChecks(srcBase, srcOffset, destBase, destOffset, bytes);

        if (bytes == 0) {
            return;
        }
        copyMemory0(srcBase, srcOffset, destBase, destOffset, bytes);
    }

    public void copyMemory(long srcAddress, long destAddress, long bytes) {
        copyMemory(null, srcAddress, null, destAddress, bytes);
    }

    private void copyMemoryChecks(Object srcBase, long srcOffset,
                                  Object destBase, long destOffset,
                                  long bytes) {
        checkSize(bytes);
        checkPrimitivePointer(srcBase, srcOffset);
        checkPrimitivePointer(destBase, destOffset);
    }

    public void copySwapMemory(Object srcBase, long srcOffset,
                               Object destBase, long destOffset,
                               long bytes, long elemSize) {
        copySwapMemoryChecks(srcBase, srcOffset, destBase, destOffset, bytes, elemSize);

        if (bytes == 0) {
            return;
        }
        copySwapMemory0(srcBase, srcOffset, destBase, destOffset, bytes, elemSize);
    }

    private void copySwapMemoryChecks(Object srcBase, long srcOffset,
                                        Object destBase, long destOffset,
                                        long bytes, long elemSize) {
            checkSize(bytes);

            if (elemSize != 2 && elemSize != 4 && elemSize != 8) {
                throw invalidInput();
            }
            if (bytes % elemSize != 0) {
                throw invalidInput();
            }
        checkPrimitivePointer(srcBase, srcOffset);
        checkPrimitivePointer(destBase, destOffset);
    }

    public void copySwapMemory(long srcAddress, long destAddress, long bytes, long elemSize) {
        copySwapMemory(null, srcAddress, null, destAddress, bytes, elemSize);
    }

    public void freeMemory(long address) {
        freeMemoryChecks(address);

        if (address == 0) {
            return;
        }
    freeMemory0(address);
    }

    private void freeMemoryChecks(long address) {
        checkPointer(null, address);
    }

    public void writebackMemory(long address, long length) {
        checkWritebackEnabled();
        checkWritebackMemory(address, length);

        writebackPreSync0();

        long line = dataCacheLineAlignDown(address);
        long end = address + length;
        while (line < end) {
            writeback0(line);
            line += dataCacheLineFlushSize();
        }
        writebackPostSync0();
    }

    private void checkWritebackMemory(long address, long length) {
        checkNativeAddress(address);
        checkSize(length);
    }

    private void checkWritebackEnabled() {
        if (!isWritebackEnabled()) {
            throw new RuntimeException("writebackMemory not enabled!");
        }
    }

    private native void writeback0(long address);

    private native void writebackPreSync0();

    private native void writebackPostSync0();

    public static final int INVALID_FIELD_OFFSET = -1;

    public long objectFieldOffset(Field f) {
        if (f == null) {
            throw new NullPointerException();
        }
        return objectFieldOffset0(f);
    }

    public long objectFieldOffset(Class<?> c, String name) {
        if (c == null || name == null) {
            throw new NullPointerException();
        }
        return objectFieldOffset1(c, name);
    }

    public long staticFieldOffset(Field f) {
        if (f == null) {
            throw new NullPointerException();
        }
        return staticFieldOffset0(f);
    }

    public Object staticFieldBase(Field f) {
        if (f == null) {
            throw new NullPointerException();
        }
         return staticFieldBase0(f);
    }

    public boolean shouldBeInitialized(Class<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
      return shouldBeInitialized0(c);
    }

    public void ensureClassInitialized(Class<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
     ensureClassInitialized0(c);
    }

    public int arrayBaseOffset(Class<?> arrayClass) {
        if (arrayClass == null) {
            throw new NullPointerException();
        }
        return arrayBaseOffset0(arrayClass);
    }

    public static final int ARRAY_BOOLEAN_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(boolean[].class);

    public static final int ARRAY_BYTE_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(byte[].class);

    public static final int ARRAY_SHORT_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(short[].class);

    public static final int ARRAY_CHAR_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(char[].class);

    public static final int ARRAY_INT_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(int[].class);

    public static final int ARRAY_LONG_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(long[].class);

    public static final int ARRAY_FLOAT_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(float[].class);

    public static final int ARRAY_DOUBLE_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(double[].class);

    public static final int ARRAY_OBJECT_BASE_OFFSET
            = theUnsafe.arrayBaseOffset(Object[].class);

    public int arrayIndexScale(Class<?> arrayClass) {
        if (arrayClass == null) {
            throw new NullPointerException();
        }
        return arrayIndexScale0(arrayClass);
    }

    public static final int ARRAY_BOOLEAN_INDEX_SCALE
            = theUnsafe.arrayIndexScale(boolean[].class);

    public static final int ARRAY_BYTE_INDEX_SCALE
            = theUnsafe.arrayIndexScale(byte[].class);

    public static final int ARRAY_SHORT_INDEX_SCALE
            = theUnsafe.arrayIndexScale(short[].class);

    public static final int ARRAY_CHAR_INDEX_SCALE
            = theUnsafe.arrayIndexScale(char[].class);

    public static final int ARRAY_INT_INDEX_SCALE
            = theUnsafe.arrayIndexScale(int[].class);

    public static final int ARRAY_LONG_INDEX_SCALE
            = theUnsafe.arrayIndexScale(long[].class);

    /** The value of {@code arrayIndexScale(float[].class)} */
    public static final int ARRAY_FLOAT_INDEX_SCALE
            = theUnsafe.arrayIndexScale(float[].class);

    /** The value of {@code arrayIndexScale(double[].class)} */
    public static final int ARRAY_DOUBLE_INDEX_SCALE
            = theUnsafe.arrayIndexScale(double[].class);

    /** The value of {@code arrayIndexScale(Object[].class)} */
    public static final int ARRAY_OBJECT_INDEX_SCALE
            = theUnsafe.arrayIndexScale(Object[].class);

    public int addressSize() {
        return ADDRESS_SIZE;
    }

    public static final int ADDRESS_SIZE = UnsafeConstants.ADDRESS_SIZE0;

    public int pageSize() { return UnsafeConstants.PAGE_SIZE; }

    public int dataCacheLineFlushSize() { return UnsafeConstants.DATA_CACHE_LINE_FLUSH_SIZE; }

    public long dataCacheLineAlignDown(long address) {
        return (address & ~(UnsafeConstants.DATA_CACHE_LINE_FLUSH_SIZE - 1));
    }
    public static boolean isWritebackEnabled() { return UnsafeConstants.DATA_CACHE_LINE_FLUSH_SIZE != 0; }

    public Class<?> defineClass(String name, byte[] b, int off, int len,
                                ClassLoader loader,
                                ProtectionDomain protectionDomain) {
        if (b == null) {
            throw new NullPointerException();
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return defineClass0(name, b, off, len, loader, protectionDomain);
    }

    public native Class<?> defineClass0(String name, byte[] b, int off, int len,
                                        ClassLoader loader,
                                        ProtectionDomain protectionDomain);

    public Class<?> defineAnonymousClass(Class<?> hostClass, byte[] data, Object[] cpPatches) {
        if (hostClass == null || data == null) {
            throw new NullPointerException();
        }
        if (hostClass.isArray() || hostClass.isPrimitive()) {
            throw new IllegalArgumentException();
        }
    return defineAnonymousClass0(hostClass, data, cpPatches);
    }
    
    public native Object allocateInstance(Class<?> cls)
        throws InstantiationException;

    public Object allocateUninitializedArray(Class<?> componentType, int length) {
       if (componentType == null) {
           throw new IllegalArgumentException("Component type is null");
       }
       if (!componentType.isPrimitive()) {
           throw new IllegalArgumentException("Component type is not primitive");
       }
       if (length < 0) {
           throw new IllegalArgumentException("Negative length");
       }
       return allocateUninitializedArray0(componentType, length);
    }

    private Object allocateUninitializedArray0(Class<?> componentType, int length) {
       if (componentType == byte.class)    return new byte[length];
       if (componentType == boolean.class) return new boolean[length];
       if (componentType == short.class)   return new short[length];
       if (componentType == char.class)    return new char[length];
       if (componentType == int.class)     return new int[length];
       if (componentType == float.class)   return new float[length];
       if (componentType == long.class)    return new long[length];
       if (componentType == double.class)  return new double[length];
       return null;
    }

    public native void throwException(Throwable ee);
    
    public final native boolean compareAndSetReference(Object o, long offset,
                                                       Object expected,
                                                       Object x);

    
    public final native Object compareAndExchangeReference(Object o, long offset,
                                                           Object expected,
                                                           Object x);

    
    public final Object compareAndExchangeReferenceAcquire(Object o, long offset,
                                                           Object expected,
                                                           Object x) {
        return compareAndExchangeReference(o, offset, expected, x);
    }

    public final Object compareAndExchangeReferenceRelease(Object o, long offset,
                                                           Object expected,
                                                           Object x) {
        return compareAndExchangeReference(o, offset, expected, x);
    }

    public final boolean weakCompareAndSetReferencePlain(Object o, long offset,
                                                         Object expected,
                                                         Object x) {
        return compareAndSetReference(o, offset, expected, x);
    }

    public final boolean weakCompareAndSetReferenceAcquire(Object o, long offset,
                                                           Object expected,
                                                           Object x) {
        return compareAndSetReference(o, offset, expected, x);
    }

    public final boolean weakCompareAndSetReferenceRelease(Object o, long offset,
                                                           Object expected,
                                                           Object x) {
        return compareAndSetReference(o, offset, expected, x);
    }

    public final boolean weakCompareAndSetReference(Object o, long offset,
                                                    Object expected,
                                                    Object x) {
        return compareAndSetReference(o, offset, expected, x);
    }
    
    public final native boolean compareAndSetInt(Object o, long offset,
                                                 int expected,
                                                 int x);

    
    public final native int compareAndExchangeInt(Object o, long offset,
                                                  int expected,
                                                  int x);

    
    public final int compareAndExchangeIntAcquire(Object o, long offset,
                                                         int expected,
                                                         int x) {
        return compareAndExchangeInt(o, offset, expected, x);
    }

    public final int compareAndExchangeIntRelease(Object o, long offset,
                                                         int expected,
                                                         int x) {
        return compareAndExchangeInt(o, offset, expected, x);
    }

    public final boolean weakCompareAndSetIntPlain(Object o, long offset,
                                                   int expected,
                                                   int x) {
        return compareAndSetInt(o, offset, expected, x);
    }

    public final boolean weakCompareAndSetIntAcquire(Object o, long offset,
                                                     int expected,
                                                     int x) {
        return compareAndSetInt(o, offset, expected, x);
    }

    public final boolean weakCompareAndSetIntRelease(Object o, long offset,
                                                     int expected,
                                                     int x) {
        return compareAndSetInt(o, offset, expected, x);
    }

    public final boolean weakCompareAndSetInt(Object o, long offset,
                                              int expected,
                                              int x) {
        return compareAndSetInt(o, offset, expected, x);
    }

    public final byte compareAndExchangeByte(Object o, long offset,
                                             byte expected,
                                             byte x) {
        long wordOffset = offset & ~3;
        int shift = (int) (offset & 3) << 3;
        if ( UnsafeConstants.BIG_ENDIAN) {
            shift = 24 - shift;
        }
        int mask           = 0xFF << shift;
        int maskedExpected = (expected & 0xFF) << shift;
        int maskedX        = (x & 0xFF) << shift;
        int fullWord;
        do {
            fullWord = getIntVolatile(o, wordOffset);
            if ((fullWord & mask) != maskedExpected)
                return (byte) ((fullWord & mask) >> shift);
        } while (!weakCompareAndSetInt(o, wordOffset,
                                                fullWord, (fullWord & ~mask) | maskedX));
        return expected;
    }

    public final boolean compareAndSetByte(Object o, long offset,
                                           byte expected,
                                           byte x) {
        return compareAndExchangeByte(o, offset, expected, x) == expected;
    }

    public final boolean weakCompareAndSetByte(Object o, long offset,
                                               byte expected,
                                               byte x) {
        return compareAndSetByte(o, offset, expected, x);
    }
    
    public final boolean weakCompareAndSetByteAcquire(Object o, long offset,
                                                      byte expected,
                                                      byte x) {
        return weakCompareAndSetByte(o, offset, expected, x);
    }
    
    public final boolean weakCompareAndSetByteRelease(Object o, long offset,
                                                      byte expected,
                                                      byte x) {
        return weakCompareAndSetByte(o, offset, expected, x);
    }
    
    public final boolean weakCompareAndSetBytePlain(Object o, long offset,
                                                    byte expected,
                                                    byte x) {
        return weakCompareAndSetByte(o, offset, expected, x);
    }
    
    public final byte compareAndExchangeByteAcquire(Object o, long offset,
                                                    byte expected,
                                                    byte x) {
        return compareAndExchangeByte(o, offset, expected, x);
    }
    
    public final byte compareAndExchangeByteRelease(Object o, long offset,
                                                    byte expected,
                                                    byte x) {
        return compareAndExchangeByte(o, offset, expected, x);
    }
    
    public final short compareAndExchangeShort(Object o, long offset,
                                               short expected,
                                               short x) {
        if ((offset & 3) == 3) {
            throw new IllegalArgumentException("Update spans the word, not supported");
        }
        long wordOffset = offset & ~3;
        int shift = (int) (offset & 3) << 3;
        if (UnsafeConstants.BIG_ENDIAN) {
            shift = 16 - shift;
        }
        int mask           = 0xFFFF << shift;
        int maskedExpected = (expected & 0xFFFF) << shift;
        int maskedX        = (x & 0xFFFF) << shift;
        int fullWord;
        do {
            fullWord = getIntVolatile(o, wordOffset);
            if ((fullWord & mask) != maskedExpected) {
                return (short) ((fullWord & mask) >> shift);
            }
        } while (!weakCompareAndSetInt(o, wordOffset,
                                                fullWord, (fullWord & ~mask) | maskedX));
        return expected;
    }
    
    public final boolean compareAndSetShort(Object o, long offset,
                                            short expected,
                                            short x) {
        return compareAndExchangeShort(o, offset, expected, x) == expected;
    }
    
    public final boolean weakCompareAndSetShort(Object o, long offset,
                                                short expected,
                                                short x) {
        return compareAndSetShort(o, offset, expected, x);
    }
    
    public final boolean weakCompareAndSetShortAcquire(Object o, long offset,
                                                       short expected,
                                                       short x) {
        return weakCompareAndSetShort(o, offset, expected, x);
    }
public final boolean weakCompareAndSetShortRelease(Object o, long offset,
                                                       short expected,
                                                       short x) {
        return weakCompareAndSetShort(o, offset, expected, x);
    }
    
    public final boolean weakCompareAndSetShortPlain(Object o, long offset,
                                                     short expected,
                                                     short x) {
        return weakCompareAndSetShort(o, offset, expected, x);
    }
    
    public final short compareAndExchangeShortAcquire(Object o, long offset,
                                                     short expected,
                                                     short x) {
        return compareAndExchangeShort(o, offset, expected, x);
    }
    
    public final short compareAndExchangeShortRelease(Object o, long offset,
                                                    short expected,
                                                    short x) {
        return compareAndExchangeShort(o, offset, expected, x);
    }
    
    private char s2c(short s) {
        return (char) s;
    }
    
    private short c2s(char s) {
        return (short) s;
    }
public final boolean compareAndSetChar(Object o, long offset,
                                           char expected,
                                           char x) {
        return compareAndSetShort(o, offset, c2s(expected), c2s(x));
    }
    
    public final char compareAndExchangeChar(Object o, long offset,
                                             char expected,
                                             char x) {
        return s2c(compareAndExchangeShort(o, offset, c2s(expected), c2s(x)));
    }
    
    public final char compareAndExchangeCharAcquire(Object o, long offset,
                                            char expected,
                                            char x) {
        return s2c(compareAndExchangeShortAcquire(o, offset, c2s(expected), c2s(x)));
    }
    
    public final char compareAndExchangeCharRelease(Object o, long offset,
                                            char expected,
                                            char x) {
        return s2c(compareAndExchangeShortRelease(o, offset, c2s(expected), c2s(x)));
    }
    
    public final boolean weakCompareAndSetChar(Object o, long offset,
                                               char expected,
                                               char x) {
        return weakCompareAndSetShort(o, offset, c2s(expected), c2s(x));
    }
    
    public final boolean weakCompareAndSetCharAcquire(Object o, long offset,
                                                      char expected,
                                                      char x) {
        return weakCompareAndSetShortAcquire(o, offset, c2s(expected), c2s(x));
    }
    
    public final boolean weakCompareAndSetCharRelease(Object o, long offset,
                                                      char expected,
                                                      char x) {
        return weakCompareAndSetShortRelease(o, offset, c2s(expected), c2s(x));
    }
    
    public final boolean weakCompareAndSetCharPlain(Object o, long offset,
                                                    char expected,
                                                    char x) {
        return weakCompareAndSetShortPlain(o, offset, c2s(expected), c2s(x));
    }
private boolean byte2bool(byte b) {
        return b != 0;
    }
    
    private byte bool2byte(boolean b) {
        return b ? (byte)1 : (byte)0;
    }
    
    public final boolean compareAndSetBoolean(Object o, long offset,
                                              boolean expected,
                                              boolean x) {
        return compareAndSetByte(o, offset, bool2byte(expected), bool2byte(x));
    }
public final boolean compareAndExchangeBoolean(Object o, long offset,
                                                   boolean expected,
                                                   boolean x) {
        return byte2bool(compareAndExchangeByte(o, offset, bool2byte(expected), bool2byte(x)));
    }
    
    public final boolean compareAndExchangeBooleanAcquire(Object o, long offset,
                                                    boolean expected,
                                                    boolean x) {
        return byte2bool(compareAndExchangeByteAcquire(o, offset, bool2byte(expected), bool2byte(x)));
    }
    
    public final boolean compareAndExchangeBooleanRelease(Object o, long offset,
                                                       boolean expected,
                                                       boolean x) {
        return byte2bool(compareAndExchangeByteRelease(o, offset, bool2byte(expected), bool2byte(x)));
    }
public final boolean weakCompareAndSetBoolean(Object o, long offset,
                                                  boolean expected,
                                                  boolean x) {
        return weakCompareAndSetByte(o, offset, bool2byte(expected), bool2byte(x));
    }
    
    public final boolean weakCompareAndSetBooleanAcquire(Object o, long offset,
                                                         boolean expected,
                                                         boolean x) {
        return weakCompareAndSetByteAcquire(o, offset, bool2byte(expected), bool2byte(x));
    }
public final boolean weakCompareAndSetBooleanRelease(Object o, long offset,
                                                         boolean expected,
                                                         boolean x) {
        return weakCompareAndSetByteRelease(o, offset, bool2byte(expected), bool2byte(x));
    }
    
    public final boolean weakCompareAndSetBooleanPlain(Object o, long offset,
                                                       boolean expected,
                                                       boolean x) {
        return weakCompareAndSetBytePlain(o, offset, bool2byte(expected), bool2byte(x));
    }
public final boolean compareAndSetFloat(Object o, long offset,
                                            float expected,
                                            float x) {
        return compareAndSetInt(o, offset,
                                 Float.floatToRawIntBits(expected),
                                 Float.floatToRawIntBits(x));
    }
public final float compareAndExchangeFloat(Object o, long offset,
                                               float expected,
                                               float x) {
        int w = compareAndExchangeInt(o, offset,
                                      Float.floatToRawIntBits(expected),
                                      Float.floatToRawIntBits(x));
        return Float.intBitsToFloat(w);
    }
    
    public final float compareAndExchangeFloatAcquire(Object o, long offset,
                                                  float expected,
                                                  float x) {
        int w = compareAndExchangeIntAcquire(o, offset,
                                             Float.floatToRawIntBits(expected),
                                             Float.floatToRawIntBits(x));
        return Float.intBitsToFloat(w);
    }
    
    public final float compareAndExchangeFloatRelease(Object o, long offset,
                                                  float expected,
                                                  float x) {
        int w = compareAndExchangeIntRelease(o, offset,
                                             Float.floatToRawIntBits(expected),
                                             Float.floatToRawIntBits(x));
        return Float.intBitsToFloat(w);
    }
    
    public final boolean weakCompareAndSetFloatPlain(Object o, long offset,
                                                     float expected,
                                                     float x) {
        return weakCompareAndSetIntPlain(o, offset,
                                     Float.floatToRawIntBits(expected),
                                     Float.floatToRawIntBits(x));
    }
    
    public final boolean weakCompareAndSetFloatAcquire(Object o, long offset,
                                                       float expected,
                                                       float x) {
        return weakCompareAndSetIntAcquire(o, offset,
                                            Float.floatToRawIntBits(expected),
                                            Float.floatToRawIntBits(x));
    }
    
    public final boolean weakCompareAndSetFloatRelease(Object o, long offset,
                                                       float expected,
                                                       float x) {
        return weakCompareAndSetIntRelease(o, offset,
                                            Float.floatToRawIntBits(expected),
                                            Float.floatToRawIntBits(x));
    }
    
    public final boolean weakCompareAndSetFloat(Object o, long offset,
                                                float expected,
                                                float x) {
        return weakCompareAndSetInt(o, offset,
                                             Float.floatToRawIntBits(expected),
                                             Float.floatToRawIntBits(x));
    }
public final boolean compareAndSetDouble(Object o, long offset,
                                             double expected,
                                             double x) {
        return compareAndSetLong(o, offset,
                                 Double.doubleToRawLongBits(expected),
                                 Double.doubleToRawLongBits(x));
    }
public final double compareAndExchangeDouble(Object o, long offset,
                                                 double expected,
                                                 double x) {
        long w = compareAndExchangeLong(o, offset,
                                        Double.doubleToRawLongBits(expected),
                                        Double.doubleToRawLongBits(x));
        return Double.longBitsToDouble(w);
    }
public final double compareAndExchangeDoubleAcquire(Object o, long offset,
                                                        double expected,
                                                        double x) {
        long w = compareAndExchangeLongAcquire(o, offset,
                                               Double.doubleToRawLongBits(expected),
                                               Double.doubleToRawLongBits(x));
        return Double.longBitsToDouble(w);
    }
    
    public final double compareAndExchangeDoubleRelease(Object o, long offset,
                                                        double expected,
                                                        double x) {
        long w = compareAndExchangeLongRelease(o, offset,
                                               Double.doubleToRawLongBits(expected),
                                               Double.doubleToRawLongBits(x));
        return Double.longBitsToDouble(w);
    }
public final boolean weakCompareAndSetDoublePlain(Object o, long offset,
                                                      double expected,
                                                      double x) {
        return weakCompareAndSetLongPlain(o, offset,
                                     Double.doubleToRawLongBits(expected),
                                     Double.doubleToRawLongBits(x));
    }
    
    public final boolean weakCompareAndSetDoubleAcquire(Object o, long offset,
                                                        double expected,
                                                        double x) {
        return weakCompareAndSetLongAcquire(o, offset,
                                             Double.doubleToRawLongBits(expected),
                                             Double.doubleToRawLongBits(x));
    }
    
    public final boolean weakCompareAndSetDoubleRelease(Object o, long offset,
                                                        double expected,
                                                        double x) {
        return weakCompareAndSetLongRelease(o, offset,
                                             Double.doubleToRawLongBits(expected),
                                             Double.doubleToRawLongBits(x));
    }
    
    public final boolean weakCompareAndSetDouble(Object o, long offset,
                                                 double expected,
                                                 double x) {
        return weakCompareAndSetLong(o, offset,
                                              Double.doubleToRawLongBits(expected),
                                              Double.doubleToRawLongBits(x));
    }
    
    public final native boolean compareAndSetLong(Object o, long offset,
                                                  long expected,
                                                  long x);

    
    public final native long compareAndExchangeLong(Object o, long offset,
                                                    long expected,
                                                    long x);

    public final long compareAndExchangeLongAcquire(Object o, long offset,
                                                           long expected,
                                                           long x) {
        return compareAndExchangeLong(o, offset, expected, x);
    }
    
    public final long compareAndExchangeLongRelease(Object o, long offset,
                                                           long expected,
                                                           long x) {
        return compareAndExchangeLong(o, offset, expected, x);
    }
    
    public final boolean weakCompareAndSetLongPlain(Object o, long offset,
                                                    long expected,
                                                    long x) {
        return compareAndSetLong(o, offset, expected, x);
    }
public final boolean weakCompareAndSetLongAcquire(Object o, long offset,
                                                      long expected,
                                                      long x) {
        return compareAndSetLong(o, offset, expected, x);
    }
    
    public final boolean weakCompareAndSetLongRelease(Object o, long offset,
                                                      long expected,
                                                      long x) {
        return compareAndSetLong(o, offset, expected, x);
    }
    
    public final boolean weakCompareAndSetLong(Object o, long offset,
                                               long expected,
                                               long x) {
        return compareAndSetLong(o, offset, expected, x);
    }
    
    public native Object getReferenceVolatile(Object o, long offset);

    public native void putReferenceVolatile(Object o, long offset, Object x);
    
    public native int     getIntVolatile(Object o, long offset);
    
    public native void    putIntVolatile(Object o, long offset, int x);
    
    public native boolean getBooleanVolatile(Object o, long offset);
    
    public native void    putBooleanVolatile(Object o, long offset, boolean x);
    
    public native byte    getByteVolatile(Object o, long offset);
    
    public native void    putByteVolatile(Object o, long offset, byte x);
    
    public native short   getShortVolatile(Object o, long offset);
    
    public native void    putShortVolatile(Object o, long offset, short x);
    
    public native char    getCharVolatile(Object o, long offset);
    
    public native void    putCharVolatile(Object o, long offset, char x);
    
    public native long    getLongVolatile(Object o, long offset);
    
    public native void    putLongVolatile(Object o, long offset, long x);
    
    public native float   getFloatVolatile(Object o, long offset);
    
    public native void    putFloatVolatile(Object o, long offset, float x);
    
    public native double  getDoubleVolatile(Object o, long offset);
    
    public native void    putDoubleVolatile(Object o, long offset, double x);
    
    public final Object getReferenceAcquire(Object o, long offset) {
        return getReferenceVolatile(o, offset);
    }
    
    public final boolean getBooleanAcquire(Object o, long offset) {
        return getBooleanVolatile(o, offset);
    }
    
    public final byte getByteAcquire(Object o, long offset) {
        return getByteVolatile(o, offset);
    }
    
    public final short getShortAcquire(Object o, long offset) {
        return getShortVolatile(o, offset);
    }
    
    public final char getCharAcquire(Object o, long offset) {
        return getCharVolatile(o, offset);
    }
    
    public final int getIntAcquire(Object o, long offset) {
        return getIntVolatile(o, offset);
    }
    
    public final float getFloatAcquire(Object o, long offset) {
        return getFloatVolatile(o, offset);
    }
    
    public final long getLongAcquire(Object o, long offset) {
        return getLongVolatile(o, offset);
    }
    
    public final double getDoubleAcquire(Object o, long offset) {
        return getDoubleVolatile(o, offset);
    }
public final void putReferenceRelease(Object o, long offset, Object x) {
        putReferenceVolatile(o, offset, x);
    }
    
    public final void putBooleanRelease(Object o, long offset, boolean x) {
        putBooleanVolatile(o, offset, x);
    }
    
    public final void putByteRelease(Object o, long offset, byte x) {
        putByteVolatile(o, offset, x);
    }
    
    public final void putShortRelease(Object o, long offset, short x) {
        putShortVolatile(o, offset, x);
    }
    
    public final void putCharRelease(Object o, long offset, char x) {
        putCharVolatile(o, offset, x);
    }
    
    public final void putIntRelease(Object o, long offset, int x) {
        putIntVolatile(o, offset, x);
    }
    
    public final void putFloatRelease(Object o, long offset, float x) {
        putFloatVolatile(o, offset, x);
    }
    
    public final void putLongRelease(Object o, long offset, long x) {
        putLongVolatile(o, offset, x);
    }
    
    public final void putDoubleRelease(Object o, long offset, double x) {
        putDoubleVolatile(o, offset, x);
    }
    
    public final Object getReferenceOpaque(Object o, long offset) {
        return getReferenceVolatile(o, offset);
    }
    
    public final boolean getBooleanOpaque(Object o, long offset) {
        return getBooleanVolatile(o, offset);
    }
    
    public final byte getByteOpaque(Object o, long offset) {
        return getByteVolatile(o, offset);
    }
    
    public final short getShortOpaque(Object o, long offset) {
        return getShortVolatile(o, offset);
    }
    
    public final char getCharOpaque(Object o, long offset) {
        return getCharVolatile(o, offset);
    }
    
    public final int getIntOpaque(Object o, long offset) {
        return getIntVolatile(o, offset);
    }
    
    public final float getFloatOpaque(Object o, long offset) {
        return getFloatVolatile(o, offset);
    }
    
    public final long getLongOpaque(Object o, long offset) {
        return getLongVolatile(o, offset);
    }
    
    public final double getDoubleOpaque(Object o, long offset) {
        return getDoubleVolatile(o, offset);
    }
    
    public final void putReferenceOpaque(Object o, long offset, Object x) {
        putReferenceVolatile(o, offset, x);
    }
    
    public final void putBooleanOpaque(Object o, long offset, boolean x) {
        putBooleanVolatile(o, offset, x);
    }
    
    public final void putByteOpaque(Object o, long offset, byte x) {
        putByteVolatile(o, offset, x);
    }
    
    public final void putShortOpaque(Object o, long offset, short x) {
        putShortVolatile(o, offset, x);
    }
    
    public final void putCharOpaque(Object o, long offset, char x) {
        putCharVolatile(o, offset, x);
    }
    
    public final void putIntOpaque(Object o, long offset, int x) {
        putIntVolatile(o, offset, x);
    }
    
    public final void putFloatOpaque(Object o, long offset, float x) {
        putFloatVolatile(o, offset, x);
    }
    
    public final void putLongOpaque(Object o, long offset, long x) {
        putLongVolatile(o, offset, x);
    }
    
    public final void putDoubleOpaque(Object o, long offset, double x) {
        putDoubleVolatile(o, offset, x);
    }
public native void unpark(Object thread);

    public native void park(boolean isAbsolute, long time);

    public int getLoadAverage(double[] loadavg, int nelems) {
        if (nelems < 0 || nelems > 3 || nelems > loadavg.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    return getLoadAverage0(loadavg, nelems);
    }
public final int getAndAddInt(Object o, long offset, int delta) {
        int v;
        do {
            v = getIntVolatile(o, offset);
        } while (!weakCompareAndSetInt(o, offset, v, v + delta));
        return v;
    }
    
    public final int getAndAddIntRelease(Object o, long offset, int delta) {
        int v;
        do {
            v = getInt(o, offset);
        } while (!weakCompareAndSetIntRelease(o, offset, v, v + delta));
        return v;
    }
    
    public final int getAndAddIntAcquire(Object o, long offset, int delta) {
        int v;
        do {
            v = getIntAcquire(o, offset);
        } while (!weakCompareAndSetIntAcquire(o, offset, v, v + delta));
        return v;
    }
public final long getAndAddLong(Object o, long offset, long delta) {
        long v;
        do {
            v = getLongVolatile(o, offset);
        } while (!weakCompareAndSetLong(o, offset, v, v + delta));
        return v;
    }
    
    public final long getAndAddLongRelease(Object o, long offset, long delta) {
        long v;
        do {
            v = getLong(o, offset);
        } while (!weakCompareAndSetLongRelease(o, offset, v, v + delta));
        return v;
    }
    
    public final long getAndAddLongAcquire(Object o, long offset, long delta) {
        long v;
        do {
            v = getLongAcquire(o, offset);
        } while (!weakCompareAndSetLongAcquire(o, offset, v, v + delta));
        return v;
    }
    
    public final byte getAndAddByte(Object o, long offset, byte delta) {
        byte v;
        do {
            v = getByteVolatile(o, offset);
        } while (!weakCompareAndSetByte(o, offset, v, (byte) (v + delta)));
        return v;
    }
    
    public final byte getAndAddByteRelease(Object o, long offset, byte delta) {
        byte v;
        do {
            v = getByte(o, offset);
        } while (!weakCompareAndSetByteRelease(o, offset, v, (byte) (v + delta)));
        return v;
    }
    
    public final byte getAndAddByteAcquire(Object o, long offset, byte delta) {
        byte v;
        do {
            v = getByteAcquire(o, offset);
        } while (!weakCompareAndSetByteAcquire(o, offset, v, (byte) (v + delta)));
        return v;
    }
    
    public final short getAndAddShort(Object o, long offset, short delta) {
        short v;
        do {
            v = getShortVolatile(o, offset);
        } while (!weakCompareAndSetShort(o, offset, v, (short) (v + delta)));
        return v;
    }
    
    public final short getAndAddShortRelease(Object o, long offset, short delta) {
        short v;
        do {
            v = getShort(o, offset);
        } while (!weakCompareAndSetShortRelease(o, offset, v, (short) (v + delta)));
        return v;
    }
    
    public final short getAndAddShortAcquire(Object o, long offset, short delta) {
        short v;
        do {
            v = getShortAcquire(o, offset);
        } while (!weakCompareAndSetShortAcquire(o, offset, v, (short) (v + delta)));
        return v;
    }
    
    public final char getAndAddChar(Object o, long offset, char delta) {
        return (char) getAndAddShort(o, offset, (short) delta);
    }
    
    public final char getAndAddCharRelease(Object o, long offset, char delta) {
        return (char) getAndAddShortRelease(o, offset, (short) delta);
    }
    
    public final char getAndAddCharAcquire(Object o, long offset, char delta) {
        return (char) getAndAddShortAcquire(o, offset, (short) delta);
    }
public final float getAndAddFloat(Object o, long offset, float delta) {
        int expectedBits;
        float v;
        do {
            expectedBits = getIntVolatile(o, offset);
            v = Float.intBitsToFloat(expectedBits);
        } while (!weakCompareAndSetInt(o, offset,
                                                expectedBits, Float.floatToRawIntBits(v + delta)));
        return v;
    }
public final float getAndAddFloatRelease(Object o, long offset, float delta) {
        int expectedBits;
        float v;
        do {
            expectedBits = getInt(o, offset);
            v = Float.intBitsToFloat(expectedBits);
        } while (!weakCompareAndSetIntRelease(o, offset,
                                               expectedBits, Float.floatToRawIntBits(v + delta)));
        return v;
    }
public final float getAndAddFloatAcquire(Object o, long offset, float delta) {
        int expectedBits;
        float v;
        do {
            expectedBits = getIntAcquire(o, offset);
            v = Float.intBitsToFloat(expectedBits);
        } while (!weakCompareAndSetIntAcquire(o, offset,
                                               expectedBits, Float.floatToRawIntBits(v + delta)));
        return v;
    }
    
    public final double getAndAddDouble(Object o, long offset, double delta) {
        long expectedBits;
        double v;
        do {
            expectedBits = getLongVolatile(o, offset);
            v = Double.longBitsToDouble(expectedBits);
        } while (!weakCompareAndSetLong(o, offset,
                                                 expectedBits, Double.doubleToRawLongBits(v + delta)));
        return v;
    }
    
    public final double getAndAddDoubleRelease(Object o, long offset, double delta) {
        long expectedBits;
        double v;
        do {
            expectedBits = getLong(o, offset);
            v = Double.longBitsToDouble(expectedBits);
        } while (!weakCompareAndSetLongRelease(o, offset,
                                                expectedBits, Double.doubleToRawLongBits(v + delta)));
        return v;
    }
    
    public final double getAndAddDoubleAcquire(Object o, long offset, double delta) {
        long expectedBits;
        double v;
        do {
            expectedBits = getLongAcquire(o, offset);
            v = Double.longBitsToDouble(expectedBits);
        } while (!weakCompareAndSetLongAcquire(o, offset,
                                                expectedBits, Double.doubleToRawLongBits(v + delta)));
        return v;
    }
public final int getAndSetInt(Object o, long offset, int newValue) {
        int v;
        do {
            v = getIntVolatile(o, offset);
        } while (!weakCompareAndSetInt(o, offset, v, newValue));
        return v;
    }
public final int getAndSetIntRelease(Object o, long offset, int newValue) {
        int v;
        do {
            v = getInt(o, offset);
        } while (!weakCompareAndSetIntRelease(o, offset, v, newValue));
        return v;
    }
public final int getAndSetIntAcquire(Object o, long offset, int newValue) {
        int v;
        do {
            v = getIntAcquire(o, offset);
        } while (!weakCompareAndSetIntAcquire(o, offset, v, newValue));
        return v;
    }
public final long getAndSetLong(Object o, long offset, long newValue) {
        long v;
        do {
            v = getLongVolatile(o, offset);
        } while (!weakCompareAndSetLong(o, offset, v, newValue));
        return v;
    }
public final long getAndSetLongRelease(Object o, long offset, long newValue) {
        long v;
        do {
            v = getLong(o, offset);
        } while (!weakCompareAndSetLongRelease(o, offset, v, newValue));
        return v;
    }
    
    public final long getAndSetLongAcquire(Object o, long offset, long newValue) {
        long v;
        do {
            v = getLongAcquire(o, offset);
        } while (!weakCompareAndSetLongAcquire(o, offset, v, newValue));
        return v;
    }
    
    public final Object getAndSetReference(Object o, long offset, Object newValue) {
        Object v;
        do {
            v = getReferenceVolatile(o, offset);
        } while (!weakCompareAndSetReference(o, offset, v, newValue));
        return v;
    }
public final Object getAndSetReferenceRelease(Object o, long offset, Object newValue) {
        Object v;
        do {
            v = getReference(o, offset);
        } while (!weakCompareAndSetReferenceRelease(o, offset, v, newValue));
        return v;
    }
public final Object getAndSetReferenceAcquire(Object o, long offset, Object newValue) {
        Object v;
        do {
            v = getReferenceAcquire(o, offset);
        } while (!weakCompareAndSetReferenceAcquire(o, offset, v, newValue));
        return v;
    }
public final byte getAndSetByte(Object o, long offset, byte newValue) {
        byte v;
        do {
            v = getByteVolatile(o, offset);
        } while (!weakCompareAndSetByte(o, offset, v, newValue));
        return v;
    }
public final byte getAndSetByteRelease(Object o, long offset, byte newValue) {
        byte v;
        do {
            v = getByte(o, offset);
        } while (!weakCompareAndSetByteRelease(o, offset, v, newValue));
        return v;
    }
public final byte getAndSetByteAcquire(Object o, long offset, byte newValue) {
        byte v;
        do {
            v = getByteAcquire(o, offset);
        } while (!weakCompareAndSetByteAcquire(o, offset, v, newValue));
        return v;
    }
public final boolean getAndSetBoolean(Object o, long offset, boolean newValue) {
        return byte2bool(getAndSetByte(o, offset, bool2byte(newValue)));
    }
public final boolean getAndSetBooleanRelease(Object o, long offset, boolean newValue) {
        return byte2bool(getAndSetByteRelease(o, offset, bool2byte(newValue)));
    }
public final boolean getAndSetBooleanAcquire(Object o, long offset, boolean newValue) {
        return byte2bool(getAndSetByteAcquire(o, offset, bool2byte(newValue)));
    }
public final short getAndSetShort(Object o, long offset, short newValue) {
        short v;
        do {
            v = getShortVolatile(o, offset);
        } while (!weakCompareAndSetShort(o, offset, v, newValue));
        return v;
    }
public final short getAndSetShortRelease(Object o, long offset, short newValue) {
        short v;
        do {
            v = getShort(o, offset);
        } while (!weakCompareAndSetShortRelease(o, offset, v, newValue));
        return v;
    }
public final short getAndSetShortAcquire(Object o, long offset, short newValue) {
        short v;
        do {
            v = getShortAcquire(o, offset);
        } while (!weakCompareAndSetShortAcquire(o, offset, v, newValue));
        return v;
    }
public final char getAndSetChar(Object o, long offset, char newValue) {
        return s2c(getAndSetShort(o, offset, c2s(newValue)));
    }
public final char getAndSetCharRelease(Object o, long offset, char newValue) {
        return s2c(getAndSetShortRelease(o, offset, c2s(newValue)));
    }
public final char getAndSetCharAcquire(Object o, long offset, char newValue) {
        return s2c(getAndSetShortAcquire(o, offset, c2s(newValue)));
    }
public final float getAndSetFloat(Object o, long offset, float newValue) {
        int v = getAndSetInt(o, offset, Float.floatToRawIntBits(newValue));
        return Float.intBitsToFloat(v);
    }
public final float getAndSetFloatRelease(Object o, long offset, float newValue) {
        int v = getAndSetIntRelease(o, offset, Float.floatToRawIntBits(newValue));
        return Float.intBitsToFloat(v);
    }
public final float getAndSetFloatAcquire(Object o, long offset, float newValue) {
        int v = getAndSetIntAcquire(o, offset, Float.floatToRawIntBits(newValue));
        return Float.intBitsToFloat(v);
    }
public final double getAndSetDouble(Object o, long offset, double newValue) {
        long v = getAndSetLong(o, offset, Double.doubleToRawLongBits(newValue));
        return Double.longBitsToDouble(v);
    }
public final double getAndSetDoubleRelease(Object o, long offset, double newValue) {
        long v = getAndSetLongRelease(o, offset, Double.doubleToRawLongBits(newValue));
        return Double.longBitsToDouble(v);
    }
public final double getAndSetDoubleAcquire(Object o, long offset, double newValue) {
        long v = getAndSetLongAcquire(o, offset, Double.doubleToRawLongBits(newValue));
        return Double.longBitsToDouble(v);
    }
public final boolean getAndBitwiseOrBoolean(Object o, long offset, boolean mask) {
        return byte2bool(getAndBitwiseOrByte(o, offset, bool2byte(mask)));
    }
public final boolean getAndBitwiseOrBooleanRelease(Object o, long offset, boolean mask) {
        return byte2bool(getAndBitwiseOrByteRelease(o, offset, bool2byte(mask)));
    }
public final boolean getAndBitwiseOrBooleanAcquire(Object o, long offset, boolean mask) {
        return byte2bool(getAndBitwiseOrByteAcquire(o, offset, bool2byte(mask)));
    }
public final boolean getAndBitwiseAndBoolean(Object o, long offset, boolean mask) {
        return byte2bool(getAndBitwiseAndByte(o, offset, bool2byte(mask)));
    }
public final boolean getAndBitwiseAndBooleanRelease(Object o, long offset, boolean mask) {
        return byte2bool(getAndBitwiseAndByteRelease(o, offset, bool2byte(mask)));
    }
public final boolean getAndBitwiseAndBooleanAcquire(Object o, long offset, boolean mask) {
        return byte2bool(getAndBitwiseAndByteAcquire(o, offset, bool2byte(mask)));
    }
    
    public final boolean getAndBitwiseXorBoolean(Object o, long offset, boolean mask) {
        return byte2bool(getAndBitwiseXorByte(o, offset, bool2byte(mask)));
    }
    
    public final boolean getAndBitwiseXorBooleanRelease(Object o, long offset, boolean mask) {
        return byte2bool(getAndBitwiseXorByteRelease(o, offset, bool2byte(mask)));
    }
    
    public final boolean getAndBitwiseXorBooleanAcquire(Object o, long offset, boolean mask) {
        return byte2bool(getAndBitwiseXorByteAcquire(o, offset, bool2byte(mask)));
    }
    
    public final byte getAndBitwiseOrByte(Object o, long offset, byte mask) {
        byte current;
        do {
            current = getByteVolatile(o, offset);
        } while (!weakCompareAndSetByte(o, offset,
                                                  current, (byte) (current | mask)));
        return current;
    }
    
    public final byte getAndBitwiseOrByteRelease(Object o, long offset, byte mask) {
        byte current;
        do {
            current = getByte(o, offset);
        } while (!weakCompareAndSetByteRelease(o, offset,
                                                 current, (byte) (current | mask)));
        return current;
    }
    
    public final byte getAndBitwiseOrByteAcquire(Object o, long offset, byte mask) {
        byte current;
        do {
            current = getByte(o, offset);
        } while (!weakCompareAndSetByteAcquire(o, offset,
                                                 current, (byte) (current | mask)));
        return current;
    }
    
    public final byte getAndBitwiseAndByte(Object o, long offset, byte mask) {
        byte current;
        do {
            current = getByteVolatile(o, offset);
        } while (!weakCompareAndSetByte(o, offset,
                                                  current, (byte) (current & mask)));
        return current;
    }
public final byte getAndBitwiseAndByteRelease(Object o, long offset, byte mask) {
        byte current;
        do {
            current = getByte(o, offset);
        } while (!weakCompareAndSetByteRelease(o, offset,
                                                 current, (byte) (current & mask)));
        return current;
    }
    
    public final byte getAndBitwiseAndByteAcquire(Object o, long offset, byte mask) {
        byte current;
        do {
            current = getByte(o, offset);
        } while (!weakCompareAndSetByteAcquire(o, offset,
                                                 current, (byte) (current & mask)));
        return current;
    }
public final byte getAndBitwiseXorByte(Object o, long offset, byte mask) {
        byte current;
        do {
            current = getByteVolatile(o, offset);
        } while (!weakCompareAndSetByte(o, offset,
                                                  current, (byte) (current ^ mask)));
        return current;
    }
    
    public final byte getAndBitwiseXorByteRelease(Object o, long offset, byte mask) {
        byte current;
        do {
            current = getByte(o, offset);
        } while (!weakCompareAndSetByteRelease(o, offset,
                                                 current, (byte) (current ^ mask)));
        return current;
    }
public final byte getAndBitwiseXorByteAcquire(Object o, long offset, byte mask) {
        byte current;
        do {
            current = getByte(o, offset);
        } while (!weakCompareAndSetByteAcquire(o, offset,
                                                 current, (byte) (current ^ mask)));
        return current;
    }
public final char getAndBitwiseOrChar(Object o, long offset, char mask) {
        return s2c(getAndBitwiseOrShort(o, offset, c2s(mask)));
    }
public final char getAndBitwiseOrCharRelease(Object o, long offset, char mask) {
        return s2c(getAndBitwiseOrShortRelease(o, offset, c2s(mask)));
    }
public final char getAndBitwiseOrCharAcquire(Object o, long offset, char mask) {
        return s2c(getAndBitwiseOrShortAcquire(o, offset, c2s(mask)));
    }
public final char getAndBitwiseAndChar(Object o, long offset, char mask) {
        return s2c(getAndBitwiseAndShort(o, offset, c2s(mask)));
    }
public final char getAndBitwiseAndCharRelease(Object o, long offset, char mask) {
        return s2c(getAndBitwiseAndShortRelease(o, offset, c2s(mask)));
    }
  
    public final char getAndBitwiseAndCharAcquire(Object o, long offset, char mask) {
        return s2c(getAndBitwiseAndShortAcquire(o, offset, c2s(mask)));
    }
    
    public final char getAndBitwiseXorChar(Object o, long offset, char mask) {
        return s2c(getAndBitwiseXorShort(o, offset, c2s(mask)));
    }
public final char getAndBitwiseXorCharRelease(Object o, long offset, char mask) {
        return s2c(getAndBitwiseXorShortRelease(o, offset, c2s(mask)));
    }
    
    public final char getAndBitwiseXorCharAcquire(Object o, long offset, char mask) {
        return s2c(getAndBitwiseXorShortAcquire(o, offset, c2s(mask)));
    }
public final short getAndBitwiseOrShort(Object o, long offset, short mask) {
        short current;
        do {
            current = getShortVolatile(o, offset);
        } while (!weakCompareAndSetShort(o, offset,
                                                current, (short) (current | mask)));
        return current;
    }
public final short getAndBitwiseOrShortRelease(Object o, long offset, short mask) {
        short current;
        do {
            current = getShort(o, offset);
        } while (!weakCompareAndSetShortRelease(o, offset,
                                               current, (short) (current | mask)));
        return current;
    }
public final short getAndBitwiseOrShortAcquire(Object o, long offset, short mask) {
        short current;
        do {
            current = getShort(o, offset);
        } while (!weakCompareAndSetShortAcquire(o, offset,
                                               current, (short) (current | mask)));
        return current;
    }
    
    public final short getAndBitwiseAndShort(Object o, long offset, short mask) {
        short current;
        do {
            current = getShortVolatile(o, offset);
        } while (!weakCompareAndSetShort(o, offset,
                                                current, (short) (current & mask)));
        return current;
    }
    
    public final short getAndBitwiseAndShortRelease(Object o, long offset, short mask) {
        short current;
        do {
            current = getShort(o, offset);
        } while (!weakCompareAndSetShortRelease(o, offset,
                                               current, (short) (current & mask)));
        return current;
    }
    
    public final short getAndBitwiseAndShortAcquire(Object o, long offset, short mask) {
        short current;
        do {
            current = getShort(o, offset);
        } while (!weakCompareAndSetShortAcquire(o, offset,
                                               current, (short) (current & mask)));
        return current;
    }
public final short getAndBitwiseXorShort(Object o, long offset, short mask) {
        short current;
        do {
            current = getShortVolatile(o, offset);
        } while (!weakCompareAndSetShort(o, offset,
                                                current, (short) (current ^ mask)));
        return current;
    }
public final short getAndBitwiseXorShortRelease(Object o, long offset, short mask) {
        short current;
        do {
            current = getShort(o, offset);
        } while (!weakCompareAndSetShortRelease(o, offset,
                                               current, (short) (current ^ mask)));
        return current;
    }
public final short getAndBitwiseXorShortAcquire(Object o, long offset, short mask) {
        short current;
        do {
            current = getShort(o, offset);
        } while (!weakCompareAndSetShortAcquire(o, offset,
                                               current, (short) (current ^ mask)));
        return current;
    }
    
    public final int getAndBitwiseOrInt(Object o, long offset, int mask) {
        int current;
        do {
            current = getIntVolatile(o, offset);
        } while (!weakCompareAndSetInt(o, offset,
                                                current, current | mask));
        return current;
    }
    
    public final int getAndBitwiseOrIntRelease(Object o, long offset, int mask) {
        int current;
        do {
            current = getInt(o, offset);
        } while (!weakCompareAndSetIntRelease(o, offset,
                                               current, current | mask));
        return current;
    }
public final int getAndBitwiseOrIntAcquire(Object o, long offset, int mask) {
        int current;
        do {
            current = getInt(o, offset);
        } while (!weakCompareAndSetIntAcquire(o, offset,
                                               current, current | mask));
        return current;
    }
public final int getAndBitwiseAndInt(Object o, long offset, int mask) {
        int current;
        do {
            current = getIntVolatile(o, offset);
        } while (!weakCompareAndSetInt(o, offset,
                                                current, current & mask));
        return current;
    }
public final int getAndBitwiseAndIntRelease(Object o, long offset, int mask) {
        int current;
        do {
            current = getInt(o, offset);
        } while (!weakCompareAndSetIntRelease(o, offset,
                                               current, current & mask));
        return current;
    }
    
    public final int getAndBitwiseAndIntAcquire(Object o, long offset, int mask) {
        int current;
        do {
            current = getInt(o, offset);
        } while (!weakCompareAndSetIntAcquire(o, offset,
                                               current, current & mask));
        return current;
    }
    
    public final int getAndBitwiseXorInt(Object o, long offset, int mask) {
        int current;
        do {
            current = getIntVolatile(o, offset);
        } while (!weakCompareAndSetInt(o, offset,
                                                current, current ^ mask));
        return current;
    }
    
    public final int getAndBitwiseXorIntRelease(Object o, long offset, int mask) {
        int current;
        do {
            current = getInt(o, offset);
        } while (!weakCompareAndSetIntRelease(o, offset,
                                               current, current ^ mask));
        return current;
    }
    
    public final int getAndBitwiseXorIntAcquire(Object o, long offset, int mask) {
        int current;
        do {
            current = getInt(o, offset);
        } while (!weakCompareAndSetIntAcquire(o, offset,
                                               current, current ^ mask));
        return current;
    }
    
    public final long getAndBitwiseOrLong(Object o, long offset, long mask) {
        long current;
        do {
            current = getLongVolatile(o, offset);
        } while (!weakCompareAndSetLong(o, offset,
                                                current, current | mask));
        return current;
    }
    
    public final long getAndBitwiseOrLongRelease(Object o, long offset, long mask) {
        long current;
        do {
            current = getLong(o, offset);
        } while (!weakCompareAndSetLongRelease(o, offset,
                                               current, current | mask));
        return current;
    }
    
    public final long getAndBitwiseOrLongAcquire(Object o, long offset, long mask) {
        long current;
        do {
            current = getLong(o, offset);
        } while (!weakCompareAndSetLongAcquire(o, offset,
                                               current, current | mask));
        return current;
    }
    
    public final long getAndBitwiseAndLong(Object o, long offset, long mask) {
        long current;
        do {
            current = getLongVolatile(o, offset);
        } while (!weakCompareAndSetLong(o, offset,
                                                current, current & mask));
        return current;
    }
    
    public final long getAndBitwiseAndLongRelease(Object o, long offset, long mask) {
        long current;
        do {
            current = getLong(o, offset);
        } while (!weakCompareAndSetLongRelease(o, offset,
                                               current, current & mask));
        return current;
    }
    
    public final long getAndBitwiseAndLongAcquire(Object o, long offset, long mask) {
        long current;
        do {
            // Plain read, the value is a hint, the acquire CAS does the work
            current = getLong(o, offset);
        } while (!weakCompareAndSetLongAcquire(o, offset,
                                               current, current & mask));
        return current;
    }
    
    public final long getAndBitwiseXorLong(Object o, long offset, long mask) {
        long current;
        do {
            current = getLongVolatile(o, offset);
        } while (!weakCompareAndSetLong(o, offset,
                                                current, current ^ mask));
        return current;
    }
    
    public final long getAndBitwiseXorLongRelease(Object o, long offset, long mask) {
        long current;
        do {
            current = getLong(o, offset);
        } while (!weakCompareAndSetLongRelease(o, offset,
                                               current, current ^ mask));
        return current;
    }
    
    public final long getAndBitwiseXorLongAcquire(Object o, long offset, long mask) {
        long current;
        do {
            current = getLong(o, offset);
        } while (!weakCompareAndSetLongAcquire(o, offset,
                                               current, current ^ mask));
        return current;
    }
    
    public native void loadFence();
    
    public native void storeFence();
    
    public native void fullFence();

    public final void loadLoadFence() {
        loadFence();
    }
public final void storeStoreFence() {
        storeFence();
    }
private static void throwIllegalAccessError() {
        throw new IllegalAccessError();
    }
private static void throwNoSuchMethodError() {
        throw new NoSuchMethodError();
    }
public final boolean isBigEndian() { return  UnsafeConstants.BIG_ENDIAN; }
public final boolean unalignedAccess() { return UnsafeConstants.UNALIGNED_ACCESS; }
public final long getLongUnaligned(Object o, long offset) {
        if ((offset & 7) == 0) {
            return getLong(o, offset);
        } else if ((offset & 3) == 0) {
            return makeLong(getInt(o, offset),
                            getInt(o, offset + 4));
        } else if ((offset & 1) == 0) {
            return makeLong(getShort(o, offset),
                            getShort(o, offset + 2),
                            getShort(o, offset + 4),
                            getShort(o, offset + 6));
        } else {
            return makeLong(getByte(o, offset),
                            getByte(o, offset + 1),
                            getByte(o, offset + 2),
                            getByte(o, offset + 3),
                            getByte(o, offset + 4),
                            getByte(o, offset + 5),
                            getByte(o, offset + 6),
                            getByte(o, offset + 7));
        }
    }
public final long getLongUnaligned(Object o, long offset, boolean bigEndian) {
        return convEndian(bigEndian, getLongUnaligned(o, offset));
    }
    
    public final int getIntUnaligned(Object o, long offset) {
        if ((offset & 3) == 0) {
            return getInt(o, offset);
        } else if ((offset & 1) == 0) {
            return makeInt(getShort(o, offset),
                           getShort(o, offset + 2));
        } else {
            return makeInt(getByte(o, offset),
                           getByte(o, offset + 1),
                           getByte(o, offset + 2),
                           getByte(o, offset + 3));
        }
    }
public final int getIntUnaligned(Object o, long offset, boolean bigEndian) {
        return convEndian(bigEndian, getIntUnaligned(o, offset));
    }
    
    public final short getShortUnaligned(Object o, long offset) {
        if ((offset & 1) == 0) {
            return getShort(o, offset);
        } else {
            return makeShort(getByte(o, offset),
                             getByte(o, offset + 1));
        }
    }
public final short getShortUnaligned(Object o, long offset, boolean bigEndian) {
        return convEndian(bigEndian, getShortUnaligned(o, offset));
    }
    
    public final char getCharUnaligned(Object o, long offset) {
        if ((offset & 1) == 0) {
            return getChar(o, offset);
        } else {
            return (char)makeShort(getByte(o, offset),
                                   getByte(o, offset + 1));
        }
    }
public final char getCharUnaligned(Object o, long offset, boolean bigEndian) {
        return convEndian(bigEndian, getCharUnaligned(o, offset));
    }
public final void putLongUnaligned(Object o, long offset, long x) {
        if ((offset & 7) == 0) {
            putLong(o, offset, x);
        } else if ((offset & 3) == 0) {
            putLongParts(o, offset,
                         (int)(x >> 0),
                         (int)(x >>> 32));
        } else if ((offset & 1) == 0) {
            putLongParts(o, offset,
                         (short)(x >>> 0),
                         (short)(x >>> 16),
                         (short)(x >>> 32),
                         (short)(x >>> 48));
        } else {
            putLongParts(o, offset,
                         (byte)(x >>> 0),
                         (byte)(x >>> 8),
                         (byte)(x >>> 16),
                         (byte)(x >>> 24),
                         (byte)(x >>> 32),
                         (byte)(x >>> 40),
                         (byte)(x >>> 48),
                         (byte)(x >>> 56));
        }
    }
public final void putLongUnaligned(Object o, long offset, long x, boolean bigEndian) {
        putLongUnaligned(o, offset, convEndian(bigEndian, x));
    }
    
    public final void putIntUnaligned(Object o, long offset, int x) {
        if ((offset & 3) == 0) {
            putInt(o, offset, x);
        } else if ((offset & 1) == 0) {
            putIntParts(o, offset,
                        (short)(x >> 0),
                        (short)(x >>> 16));
        } else {
            putIntParts(o, offset,
                        (byte)(x >>> 0),
                        (byte)(x >>> 8),
                        (byte)(x >>> 16),
                        (byte)(x >>> 24));
        }
    }
public final void putIntUnaligned(Object o, long offset, int x, boolean bigEndian) {
        putIntUnaligned(o, offset, convEndian(bigEndian, x));
    }
    
    public final void putShortUnaligned(Object o, long offset, short x) {
        if ((offset & 1) == 0) {
            putShort(o, offset, x);
        } else {
            putShortParts(o, offset,
                          (byte)(x >>> 0),
                          (byte)(x >>> 8));
        }
    }
public final void putShortUnaligned(Object o, long offset, short x, boolean bigEndian) {
        putShortUnaligned(o, offset, convEndian(bigEndian, x));
    }
    
    public final void putCharUnaligned(Object o, long offset, char x) {
        putShortUnaligned(o, offset, (short)x);
    }
    public final void putCharUnaligned(Object o, long offset, char x, boolean bigEndian) {
        putCharUnaligned(o, offset, convEndian(bigEndian, x));
    }
private static int pickPos(int top, int pos) { return  UnsafeConstants.BIG_ENDIAN ? top - pos : pos; }
private static long makeLong(byte i0, byte i1, byte i2, byte i3, byte i4, byte i5, byte i6, byte i7) {
        return ((toUnsignedLong(i0) << pickPos(56, 0))
              | (toUnsignedLong(i1) << pickPos(56, 8))
              | (toUnsignedLong(i2) << pickPos(56, 16))
              | (toUnsignedLong(i3) << pickPos(56, 24))
              | (toUnsignedLong(i4) << pickPos(56, 32))
              | (toUnsignedLong(i5) << pickPos(56, 40))
              | (toUnsignedLong(i6) << pickPos(56, 48))
              | (toUnsignedLong(i7) << pickPos(56, 56)));
    }
    private static long makeLong(short i0, short i1, short i2, short i3) {
        return ((toUnsignedLong(i0) << pickPos(48, 0))
              | (toUnsignedLong(i1) << pickPos(48, 16))
              | (toUnsignedLong(i2) << pickPos(48, 32))
              | (toUnsignedLong(i3) << pickPos(48, 48)));
    }
    private static long makeLong(int i0, int i1) {
        return (toUnsignedLong(i0) << pickPos(32, 0))
             | (toUnsignedLong(i1) << pickPos(32, 32));
    }
    private static int makeInt(short i0, short i1) {
        return (toUnsignedInt(i0) << pickPos(16, 0))
             | (toUnsignedInt(i1) << pickPos(16, 16));
    }
    private static int makeInt(byte i0, byte i1, byte i2, byte i3) {
        return ((toUnsignedInt(i0) << pickPos(24, 0))
              | (toUnsignedInt(i1) << pickPos(24, 8))
              | (toUnsignedInt(i2) << pickPos(24, 16))
              | (toUnsignedInt(i3) << pickPos(24, 24)));
    }
    private static short makeShort(byte i0, byte i1) {
        return (short)((toUnsignedInt(i0) << pickPos(8, 0))
                     | (toUnsignedInt(i1) << pickPos(8, 8)));
    }
private static byte  pick(byte  le, byte  be) { return  UnsafeConstants.BIG_ENDIAN ? be : le; }
    private static short pick(short le, short be) { return  UnsafeConstants.BIG_ENDIAN ? be : le; }
    private static int   pick(int   le, int   be) { return  UnsafeConstants.BIG_ENDIAN ? be : le; }
private void putLongParts(Object o, long offset, byte i0, byte i1, byte i2, byte i3, byte i4, byte i5, byte i6, byte i7) {
        putByte(o, offset + 0, pick(i0, i7));
        putByte(o, offset + 1, pick(i1, i6));
        putByte(o, offset + 2, pick(i2, i5));
        putByte(o, offset + 3, pick(i3, i4));
        putByte(o, offset + 4, pick(i4, i3));
        putByte(o, offset + 5, pick(i5, i2));
        putByte(o, offset + 6, pick(i6, i1));
        putByte(o, offset + 7, pick(i7, i0));
    }
    private void putLongParts(Object o, long offset, short i0, short i1, short i2, short i3) {
        putShort(o, offset + 0, pick(i0, i3));
        putShort(o, offset + 2, pick(i1, i2));
        putShort(o, offset + 4, pick(i2, i1));
        putShort(o, offset + 6, pick(i3, i0));
    }
    private void putLongParts(Object o, long offset, int i0, int i1) {
        putInt(o, offset + 0, pick(i0, i1));
        putInt(o, offset + 4, pick(i1, i0));
    }
    private void putIntParts(Object o, long offset, short i0, short i1) {
        putShort(o, offset + 0, pick(i0, i1));
        putShort(o, offset + 2, pick(i1, i0));
    }
    private void putIntParts(Object o, long offset, byte i0, byte i1, byte i2, byte i3) {
        putByte(o, offset + 0, pick(i0, i3));
        putByte(o, offset + 1, pick(i1, i2));
        putByte(o, offset + 2, pick(i2, i1));
        putByte(o, offset + 3, pick(i3, i0));
    }
    private void putShortParts(Object o, long offset, byte i0, byte i1) {
        putByte(o, offset + 0, pick(i0, i1));
        putByte(o, offset + 1, pick(i1, i0));
    }
private static int toUnsignedInt(byte n)    { return n & 0xff; }
    private static int toUnsignedInt(short n)   { return n & 0xffff; }
    private static long toUnsignedLong(byte n)  { return n & 0xffl; }
    private static long toUnsignedLong(short n) { return n & 0xffffl; }
    private static long toUnsignedLong(int n)   { return n & 0xffffffffl; }
private static char convEndian(boolean big, char n)   { return big ==  UnsafeConstants.BIG_ENDIAN ? n : Character.reverseBytes(n); }
    private static short convEndian(boolean big, short n) { return big ==  UnsafeConstants.BIG_ENDIAN ? n : Short.reverseBytes(n)    ; }
    private static int convEndian(boolean big, int n)     { return big ==  UnsafeConstants.BIG_ENDIAN ? n : Integer.reverseBytes(n)  ; }
    private static long convEndian(boolean big, long n)   { return big ==  UnsafeConstants.BIG_ENDIAN ? n : Long.reverseBytes(n)     ; }



    private native long allocateMemory0(long bytes);
    private native long reallocateMemory0(long address, long bytes);
    private native void freeMemory0(long address);
    private native void setMemory0(Object o, long offset, long bytes, byte value);
    
    private native void copyMemory0(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes);
    private native void copySwapMemory0(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes, long elemSize);
    private native long objectFieldOffset0(Field f);
    private native long objectFieldOffset1(Class<?> c, String name);
    private native long staticFieldOffset0(Field f);
    private native Object staticFieldBase0(Field f);
    private native boolean shouldBeInitialized0(Class<?> c);
    private native void ensureClassInitialized0(Class<?> c);
    private native int arrayBaseOffset0(Class<?> arrayClass);
    private native int arrayIndexScale0(Class<?> arrayClass);
    private native Class<?> defineAnonymousClass0(Class<?> hostClass, byte[] data, Object[] cpPatches);
    private native int getLoadAverage0(double[] loadavg, int nelems);


    /**
     * Invokes the given direct byte buffer's cleaner, if any.
     *
     * @param directBuffer a direct byte buffer
     * @throws NullPointerException     if {@code directBuffer} is null
     * @throws IllegalArgumentException if {@code directBuffer} is non-direct,
     *                                  or is a {@link java.nio.Buffer#slice slice}, or is a
     *                                  {@link java.nio.Buffer#duplicate duplicate}
     */
    public void invokeCleaner(java.nio.ByteBuffer directBuffer) {
        if (!directBuffer.isDirect())
            throw new IllegalArgumentException("buffer is non-direct");

        DirectBuffer db = (DirectBuffer) directBuffer;
        if (db.attachment() != null)
            throw new IllegalArgumentException("duplicate or slice");

        Cleaner cleaner = db.cleaner();
        if (cleaner != null) {
            cleaner.clean();
        }
    }
// The following deprecated methods are used by JSR 166.

    @Deprecated(since="12", forRemoval=true)
    public final Object getObject(Object o, long offset) {
        return getReference(o, offset);
    }
    @Deprecated(since="12", forRemoval=true)
    public final Object getObjectVolatile(Object o, long offset) {
        return getReferenceVolatile(o, offset);
    }
    @Deprecated(since="12", forRemoval=true)
    public final Object getObjectAcquire(Object o, long offset) {
        return getReferenceAcquire(o, offset);
    }
    @Deprecated(since="12", forRemoval=true)
    public final Object getObjectOpaque(Object o, long offset) {
        return getReferenceOpaque(o, offset);
    }


    @Deprecated(since="12", forRemoval=true)
    public final void putObject(Object o, long offset, Object x) {
        putReference(o, offset, x);
    }
    @Deprecated(since="12", forRemoval=true)
    public final void putObjectVolatile(Object o, long offset, Object x) {
        putReferenceVolatile(o, offset, x);
    }
    @Deprecated(since="12", forRemoval=true)
    public final void putObjectOpaque(Object o, long offset, Object x) {
        putReferenceOpaque(o, offset, x);
    }
    @Deprecated(since="12", forRemoval=true)
    public final void putObjectRelease(Object o, long offset, Object x) {
        putReferenceRelease(o, offset, x);
    }


    @Deprecated(since="12", forRemoval=true)
    public final Object getAndSetObject(Object o, long offset, Object newValue) {
        return getAndSetReference(o, offset, newValue);
    }
    @Deprecated(since="12", forRemoval=true)
    public final Object getAndSetObjectAcquire(Object o, long offset, Object newValue) {
        return getAndSetReferenceAcquire(o, offset, newValue);
    }
    @Deprecated(since="12", forRemoval=true)
    public final Object getAndSetObjectRelease(Object o, long offset, Object newValue) {
        return getAndSetReferenceRelease(o, offset, newValue);
    }


    @Deprecated(since="12", forRemoval=true)
    public final boolean compareAndSetObject(Object o, long offset, Object expected, Object x) {
        return compareAndSetReference(o, offset, expected, x);
    }
    @Deprecated(since="12", forRemoval=true)
    public final Object compareAndExchangeObject(Object o, long offset, Object expected, Object x) {
        return compareAndExchangeReference(o, offset, expected, x);
    }
    @Deprecated(since="12", forRemoval=true)
    public final Object compareAndExchangeObjectAcquire(Object o, long offset, Object expected, Object x) {
        return compareAndExchangeReferenceAcquire(o, offset, expected, x);
    }
    @Deprecated(since="12", forRemoval=true)
    public final Object compareAndExchangeObjectRelease(Object o, long offset, Object expected, Object x) {
        return compareAndExchangeReferenceRelease(o, offset, expected, x);
    }
@Deprecated(since="12", forRemoval=true)
    public final boolean weakCompareAndSetObject(Object o, long offset, Object expected, Object x) {
        return weakCompareAndSetReference(o, offset, expected, x);
    }
    @Deprecated(since="12", forRemoval=true)
    public final boolean weakCompareAndSetObjectAcquire(Object o, long offset, Object expected, Object x) {
        return weakCompareAndSetReferenceAcquire(o, offset, expected, x);
    }
    @Deprecated(since="12", forRemoval=true)
    public final boolean weakCompareAndSetObjectPlain(Object o, long offset, Object expected, Object x) {
        return weakCompareAndSetReferencePlain(o, offset, expected, x);
    }
    @Deprecated(since="12", forRemoval=true)
    public final boolean weakCompareAndSetObjectRelease(Object o, long offset, Object expected, Object x) {
        return weakCompareAndSetReferenceRelease(o, offset, expected, x);
    }
}
final class UnsafeConstants {

    private UnsafeConstants() {
    }

    static final int ADDRESS_SIZE0;
    static final int PAGE_SIZE;
    static final boolean  BIG_ENDIAN;
    static final boolean UNALIGNED_ACCESS;
    static final int DATA_CACHE_LINE_FLUSH_SIZE;

    static {
        ADDRESS_SIZE0 = 0;
        PAGE_SIZE = 0;
        BIG_ENDIAN = false;
        UNALIGNED_ACCESS = false;
        DATA_CACHE_LINE_FLUSH_SIZE = 0;
    }
}
interface DirectBuffer {
    public long address();
    public Object attachment();
    public Cleaner cleaner();
}
class Cleaner extends PhantomReference<Object> {
    private static final ReferenceQueue<Object> dummyQueue = new ReferenceQueue<>();
    private static Cleaner first = null;
    private Cleaner next = null, prev = null;

    private static synchronized Cleaner add(Cleaner cl) {
        if (first != null) {
            cl.next = first;
            first.prev = cl;
        }
        first = cl;
        return cl;
    }

    private static synchronized boolean remove(Cleaner cl) {
        if (cl.next == cl)
            return false;

        if (first == cl) {
            if (cl.next != null)
                first = cl.next;
            else
                first = cl.prev;
        }
        if (cl.next != null)
            cl.next.prev = cl.prev;
        if (cl.prev != null)
            cl.prev.next = cl.next;

        cl.next = cl;
        cl.prev = cl;
        return true;

    }

    private final Runnable thunk;

    private Cleaner(Object referent, Runnable thunk) {
        super(referent, dummyQueue);
        this.thunk = thunk;
    }

    public static Cleaner create(Object ob, Runnable thunk) {
        if (thunk == null)
            return null;
        return add(new Cleaner(ob, thunk));
    }

    public void clean() {
        if (!remove(this))
            return;
        try {
            thunk.run();
        } catch (final Throwable x) {
            AccessController.doPrivileged(new PrivilegedAction<>() {
                public Void run() {
                    if (System.err != null)
                        new Error("Cleaner terminated abnormally", x).printStackTrace();
                    System.exit(1);
                    return null;
                }
            });
        }
    }
}