package com.example.demo.controller;

import java.util.Scanner;
import org.springframework.util.StringUtils;

/**
 * 测试类
 * @author fangjy
 * @date 2022-03-15 16:08
 **/
public class FjyTest {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        String help = "请输入" + tip + "：";
        System.out.println(help);
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (!StringUtils.isEmpty(ipt)) {
                return ipt;
            }
        }
        return "请输入正确的值";
    }

    public static void main(String[] args) {
        try {
            String value = scanner("分表键值");
            String result = String.valueOf(Math.abs((long) value.hashCode()) % 8);
            System.out.println("在第"+result+"张表");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
