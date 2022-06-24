package com.github.terralian.csharp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Comparator;
import java.util.List;

/**
 * Some method for csharp
 * 
 * @author terra.lian
 * @since 2021-8-24
 */
public class LangUtil {

    /**
     * Removes a range of elements from the List
     * 
     * @param list Target list
     * @param index The zero-based starting index of the range of elements to remove.
     * @param count The number of elements to remove.
     * @throws IllegalArgumentException index is less than 0. or count is less than 0.
     * @throws IndexOutOfBoundsException index and count do not denote a valid range of elements in the List.
     * @see <a href="https://docs.microsoft.com/en-us/dotnet/api/system.collections.arraylist.removerange?view=net-5.0">docs</a>
     */
    public static void listRemoveRange(List<?> list, int index, int count) {
        if (index < 0 || count < 0) {
            throw new IllegalArgumentException("index is less than 0 or count is less than 0.");
        }
        list.subList(index, index + count).clear();
    }

    /**
     * Sets a range of elements in an array to the default value of each element type.
     * 
     * @param array he array whose elements need to be cleared.
     * @param index The starting index of the range of elements to clear.
     * @param length The number of elements to clear.
     * @see <a href="https://docs.microsoft.com/en-us/dotnet/api/system.array.clear?view=net-5.0">docs</a>
     */
    public static void arrayClear(double[] array, int index, int length) {
        int end = index + length;
        for (; index < end; index++) {
            array[index] = 0d;
        }
    }

    /**
     * Default sort comparator for csharp List
     * 
     * @param <E> the type of objects that may be compared by this comparator
     */
    public static <E extends Comparable<E>> Comparator<E> defaultSort() {
        return (a, b) -> a.compareTo(b);
    }

    /**
     * 将二进制数组转long
     * 
     * @param input 输入的数组
     * @param littleEndian 是否为小端序，默认为大端序
     */
    public static long toLong(byte[] input, boolean littleEndian) {
        ByteBuffer buffer = ByteBuffer.wrap(input, 0, 8);
        if (littleEndian) {
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        return buffer.getLong();
    }

    private LangUtil() {}
}
