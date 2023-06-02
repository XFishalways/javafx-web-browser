package com.browser.bookmarks;

import com.browser.Application.Main;
import javafx.application.Application;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Add {
    public void Add(String title, String type, String url, String tip) {
        int num = 0;
        List<List<String>> arrList = new ArrayList<>();
        try {
            File file = new File("C:\\Users\\Cyan\\Desktop\\jiwang.txt");
            if (!file.exists()) {
                System.out.println("文件未找到");
                System.exit(0);
            }
            Scanner s = new Scanner(file);
            num = s.nextInt();
            for (int i = 0; i < num; i++) {
                arrList.add(new ArrayList<>());
                arrList.get(i).add(" " + s.next());
                arrList.get(i).add(" " + s.next());
                arrList.get(i).add(" " + s.next());
                arrList.get(i).add(" " + s.next());
            }
            s.close();
        } catch (FileNotFoundException e) {

        }
        num = num + 1;
        arrList.add(new ArrayList<>());
        arrList.get(num - 1).add(" " + title);
        arrList.get(num - 1).add(" " + type);
        arrList.get(num - 1).add(" " + url);
        arrList.get(num - 1).add(" " + tip);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\Cyan\\Desktop\\jiwang.txt"));
            bufferedWriter.write(String.valueOf(num));
            for (List<String> strings : arrList) {
                for (String string : strings) {
                    bufferedWriter.write(string);
                }
            }
            bufferedWriter.close();
        } catch (IOException e) {

        }
    }
}