package org.example.Test01;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * JDK 1.7版本 ArrayList扩容机制演示
 * 特点：
 * 1. 初始容量立即分配为10
 * 2. 扩容公式：(oldCapacity * 3)/2 + 1
 * 3. 使用除法运算
 */
public class Td2_jdk7 {


    /**
     * 模拟JDK 1.7的ArrayList实现
     */
    static class ArrayList17<E> {
        private static final int DEFAULT_CAPACITY = 10;
        private Object[] elementData;
        private int size;

        // JDK 1.7构造函数：立即分配默认容量
        public ArrayList17() {
            this.elementData = new Object[DEFAULT_CAPACITY];
            this.size = 0;
        }

        public ArrayList17(int initialCapacity) {
            if (initialCapacity < 0) {
                throw new IllegalArgumentException("初始容量不能为负数: " + initialCapacity);
            }
            this.elementData = new Object[initialCapacity];
            this.size = 0;
        }

        public boolean add(E e) {
            ensureCapacity(size + 1);
            elementData[size++] = e;
            return true;
        }

        // JDK 1.7扩容逻辑
        private void ensureCapacity(int minCapacity) {
            if (minCapacity > elementData.length) {
                grow(minCapacity);
            }
        }

        // JDK 1.7扩容核心算法
        private void grow(int minCapacity) {
            int oldCapacity = elementData.length;
            // JDK 1.7扩容公式：(oldCapacity * 3)/2 + 1
            int newCapacity = (oldCapacity * 3) / 2 + 1;

            System.out.printf("JDK 1.7扩容: %d -> %d (公式: (%d * 3)/2 + 1 = %d)\n",
                    oldCapacity, newCapacity, oldCapacity, newCapacity);

            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }

            // 创建新数组并复制数据
            elementData = Arrays.copyOf(elementData, newCapacity);
        }

        public int size() {
            return size;
        }

        public int capacity() {
            return elementData.length;
        }

        @Override
        public String toString() {
            return String.format("ArrayList17[size=%d, capacity=%d]", size, capacity());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== JDK 1.7 ArrayList扩容演示 ===\n");

        ArrayList17<Integer> list = new ArrayList17<>();
        System.out.println("初始状态: " + list);

        // 添加元素，观察扩容过程
        System.out.println("\n添加元素过程:");
        for (int i = 0; i < 20; i++) {
            int oldCapacity = list.capacity();
            list.add(i);
            int newCapacity = list.capacity();

            if (oldCapacity != newCapacity) {
                System.out.printf("添加第%d个元素时发生扩容!\n", i + 1);
            }

            System.out.printf("添加元素%d: %s\n", i, list);
        }

        // 演示扩容序列
        System.out.println("\n=== 扩容序列演示 ===");
        demonstrateCapacityGrowth();
    }

    private static void demonstrateCapacityGrowth() {
        System.out.println("JDK 1.7扩容序列 (公式: (old * 3)/2 + 1):");

        int capacity = 10;  // 初始容量
        System.out.printf("初始容量: %d\n", capacity);

        for (int i = 0; i < 5; i++) {
            int newCapacity = (capacity * 3) / 2 + 1;
            System.out.printf("第%d次扩容: %d -> %d\n", i + 1, capacity, newCapacity);
            capacity = newCapacity;
        }

        // 对比实际ArrayList行为（如果在JDK 1.7环境下）
        System.out.println("\n验证实际ArrayList行为:");
        ArrayList<Integer> realList = new ArrayList<>();
        try {
            Field elementDataField = ArrayList.class.getDeclaredField("elementData");
            elementDataField.setAccessible(true);

            System.out.printf("真实ArrayList初始容量: %d\n",
                    ((Object[])elementDataField.get(realList)).length);

        } catch (Exception e) {
            System.out.println("无法访问ArrayList内部字段 (可能不是JDK 1.7)");
        }
    }
}




/**
 * JDK 1.8+版本 ArrayList扩容机制演示
 * 特点：
 * 1. 延迟初始化（懒加载）
 * 2. 扩容公式：oldCapacity + (oldCapacity >> 1)
 * 3. 使用位运算优化性能
 * 4. 更精确的边界检查
 */
class Td_jdk8{

    /**
     * 模拟JDK 1.8+的ArrayList实现
     */
    static class ArrayList18<E> {
        private static final int DEFAULT_CAPACITY = 10;
        private static final Object[] EMPTY_ELEMENTDATA = {};
        private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
        private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

        private Object[] elementData;
        private int size;

        // JDK 1.8+构造函数：延迟初始化
        public ArrayList18() {
            this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
            this.size = 0;
        }

        public ArrayList18(int initialCapacity) {
            if (initialCapacity > 0) {
                this.elementData = new Object[initialCapacity];
            } else if (initialCapacity == 0) {
                this.elementData = EMPTY_ELEMENTDATA;
            } else {
                throw new IllegalArgumentException("初始容量不能为负数: " + initialCapacity);
            }
            this.size = 0;
        }

        public boolean add(E e) {
            ensureCapacityInternal(size + 1);
            elementData[size++] = e;
            return true;
        }

        private void ensureCapacityInternal(int minCapacity) {
            // 延迟初始化逻辑
            if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
                minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
                System.out.println("延迟初始化: 分配默认容量 " + DEFAULT_CAPACITY);
            }

            ensureExplicitCapacity(minCapacity);
        }

        private void ensureExplicitCapacity(int minCapacity) {
            if (minCapacity - elementData.length > 0) {
                grow(minCapacity);
            }
        }

        // JDK 1.8+扩容核心算法
        private void grow(int minCapacity) {
            int oldCapacity = elementData.length;
            // JDK 1.8+扩容公式：oldCapacity + (oldCapacity >> 1) = oldCapacity * 1.5
            int newCapacity = oldCapacity + (oldCapacity >> 1);

            System.out.printf("JDK 1.8+扩容: %d -> %d (公式: %d + (%d >> 1) = %d + %d = %d)\n",
                    oldCapacity, newCapacity, oldCapacity, oldCapacity,
                    oldCapacity, (oldCapacity >> 1), newCapacity);

            if (newCapacity - minCapacity < 0) {
                newCapacity = minCapacity;
                System.out.println("调整为最小需求容量: " + newCapacity);
            }

            if (newCapacity - MAX_ARRAY_SIZE > 0) {
                newCapacity = hugeCapacity(minCapacity);
                System.out.println("处理超大容量: " + newCapacity);
            }

            // 创建新数组并复制数据
            elementData = Arrays.copyOf(elementData, newCapacity);
        }

        private static int hugeCapacity(int minCapacity) {
            if (minCapacity < 0) {
                throw new OutOfMemoryError();
            }
            return (minCapacity > MAX_ARRAY_SIZE) ?
                    Integer.MAX_VALUE : MAX_ARRAY_SIZE;
        }

        public int size() {
            return size;
        }

        public int capacity() {
            return elementData.length;
        }

        public boolean isLazyInitialized() {
            return elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
        }

        @Override
        public String toString() {
            if (isLazyInitialized()) {
                return String.format("ArrayList18[size=%d, capacity=未初始化(懒加载)]", size);
            }
            return String.format("ArrayList18[size=%d, capacity=%d]", size, capacity());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== JDK 1.8+ ArrayList扩容演示 ===\n");

        // 演示延迟初始化
        demonstrateLazyInitialization();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 演示扩容过程
        demonstrateGrowthProcess();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 演示位运算优化
        demonstrateBitShiftOptimization();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 对比真实ArrayList
        compareWithRealArrayList();
    }

    private static void demonstrateLazyInitialization() {
        System.out.println("=== 延迟初始化演示 ===");
        ArrayList18<Integer> list = new ArrayList18<>();
        System.out.println("创建ArrayList后: " + list);

        // 添加第一个元素触发初始化
        list.add(1);
        System.out.println("添加第一个元素后: " + list);
    }

    private static void demonstrateGrowthProcess() {
        System.out.println("=== 扩容过程演示 ===");
        ArrayList18<Integer> list = new ArrayList18<>();

        // 添加元素观察扩容
        for (int i = 0; i < 20; i++) {
            boolean wasLazy = list.isLazyInitialized();
            int oldCapacity = wasLazy ? 0 : list.capacity();

            list.add(i);

            int newCapacity = list.capacity();

            if (wasLazy || oldCapacity != newCapacity) {
                System.out.printf("添加第%d个元素时发生扩容/初始化!\n", i + 1);
            }

            if (i < 12 || i % 5 == 0) {  // 只显示前面几次和间隔显示
                System.out.printf("添加元素%d: %s\n", i, list);
            }
        }
    }

    private static void demonstrateBitShiftOptimization() {
        System.out.println("=== 位运算优化演示 ===");
        System.out.println("JDK 1.8+扩容公式对比:");
        System.out.println("数学公式: newCapacity = oldCapacity * 1.5");
        System.out.println("位运算: newCapacity = oldCapacity + (oldCapacity >> 1)");
        System.out.println();

        int[] testCapacities = {10, 15, 22, 33, 49, 73, 109};

        for (int oldCap : testCapacities) {
            int mathResult = (int)(oldCap * 1.5);
            int bitResult = oldCap + (oldCap >> 1);

            System.out.printf("容量%d: 数学计算=%d, 位运算=%d, 右移值=%d\n",
                    oldCap, mathResult, bitResult, (oldCap >> 1));
        }
    }

    private static void compareWithRealArrayList() {
        System.out.println("=== 与真实ArrayList对比 ===");
        ArrayList<Integer> realList = new ArrayList<>();

        try {
            Field elementDataField = ArrayList.class.getDeclaredField("elementData");
            elementDataField.setAccessible(true);

            System.out.printf("真实ArrayList初始状态: 容量=%d\n",
                    ((Object[])elementDataField.get(realList)).length);

            // 添加一个元素观察变化
            realList.add(1);
            System.out.printf("添加一个元素后: 容量=%d\n",
                    ((Object[])elementDataField.get(realList)).length);

            // 触发几次扩容
            for (int i = 2; i <= 16; i++) {
                int oldCap = ((Object[])elementDataField.get(realList)).length;
                realList.add(i);
                int newCap = ((Object[])elementDataField.get(realList)).length;

                if (oldCap != newCap) {
                    System.out.printf("真实ArrayList扩容: %d -> %d (添加第%d个元素时)\n",
                            oldCap, newCap, i);
                }
            }

        } catch (Exception e) {
            System.out.println("无法访问ArrayList内部字段: " + e.getMessage());
            System.out.println("这是正常的，因为反射访问受到限制");
        }

        // 展示扩容序列
        System.out.println("\n理论扩容序列:");
        int capacity = 10;
        for (int i = 0; i < 5; i++) {
            int newCapacity = capacity + (capacity >> 1);
            System.out.printf("第%d次扩容: %d -> %d\n", i + 1, capacity, newCapacity);
            capacity = newCapacity;
        }
    }
}