package org.example.Test01;

import java.util.ArrayList;
import java.util.Arrays;


/*
todo
    扩容策略：
    初始容量：10
    扩容倍数：1.5倍 + 1
    扩容公式：int newCapacity = (oldCapacity * 3)/2 + 1
   __________________
    扩容过程：
    检查是否需要扩容（size >= elementData.length）
    计算新容量 = 旧容量 * 1.5 + 1
    创建新数组
    使用System.arraycopy()复制元素
    替换原数组引用
 */
public class Td1 {
    public static void main(String[] args) {
        Td1 td1 = new Td1();
        int grow = td1.grow(10, new int[20]);

        System.out.println(grow);
        ArrayList<String> strings = new ArrayList<>();



    }

    private int grow(int minCapacity,int [] arrays) {

        int[] numbers=arrays;
        int oldCapacity = numbers.length;
        int newCapacity = (oldCapacity * 3)/2 + 1;  // 1.5倍+1
        if (newCapacity < minCapacity)
            newCapacity = minCapacity;
        numbers = Arrays.copyOf(numbers, newCapacity);
        return  numbers.length;

    }
}
