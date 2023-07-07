package com.graduation.project.fernflower;

import com.graduation.project.api.api;
import com.graduation.project.loophole.loophole;
import com.graduation.project.utils.fileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
 * 字符串快速匹配sunday算法
 * sunday与horspool优于strstr、BM、KMP，BM匹配速度相当于KMP的三倍。
 * (1)strstr():c语言的库函数
 * (2)KMP(Knuth-Morris-Pratt)算法
 * (3)BM(Boyer-Moore)算法
 * (4)Horspool算法
 * (5)Sunday算法
 * @author lenovo
 * @date 2019年3月22日
 * description:
 */
public class SundayTest {

    public static void findKeyWord(String path, String word) {
        ArrayList<String> pathList = fileUtils.recursionFileList(path);
        //System.out.println(pathList);
        for (String p : pathList) {
            Anchor anchor = findKeyByPath(p, word);
            if (anchor.getAnchorPointList().size() > 0) {
                System.out.println(
                        "类名：\t" + anchor.getClassName() + "\n" +
                                "路径：\t" + anchor.getPath()
                );
                for (AnchorPoint anchorPoint : anchor.getAnchorPointList()) {
                    System.out.println(
                            "行数：\t" + anchorPoint.getRow() +
                                    "\t\t起点下标：\t" + anchorPoint.getStart() +
                                    "\t\t终点下标：\t" + anchorPoint.getEnd() +
                                    "\t\t关键词：\t" + anchorPoint.getKeyWord() +
                                    "\t\t目标句：\t" + anchorPoint.getLine()
                    );
                }
                System.out.println();
            }
        }
    }


    public static void findKeyWord(String path, String[] word) {
        ArrayList<String> pathList = fileUtils.recursionFileList(path);
        for (String p : pathList) {
            Anchor anchor = findKeyByPath(p, word);
            if (anchor.getAnchorPointList().size() > 0 && anchor.getShowWord() >= word.length) {
                System.out.println(
                        "类名：\t" + anchor.getClassName() + "\n" +
                                "路径：\t" + anchor.getPath()
                );
                for (AnchorPoint anchorPoint : anchor.getAnchorPointList()) {
                    System.out.println(
                            "行数：\t" + anchorPoint.getRow() +
                                    "\t\t起点下标：\t" + anchorPoint.getStart() +
                                    "\t\t终点下标：\t" + anchorPoint.getEnd() +
                                    "\t\t关键词：\t" + anchorPoint.getKeyWord() +
                                    "\t\t目标句：\t" + anchorPoint.getLine()
                    );
                }
                System.out.println();
            }
        }
    }

    public static void findLoophole(String path, String word, loophole loophole) {
        ArrayList<String> pathList = fileUtils.recursionFileList(path);
        //System.out.println(pathList);
        for (String p : pathList) {
            Anchor anchor = findKeyByPath(p, word);
            if (anchor.getAnchorPointList().size() > 0) {
                System.out.println(
                        "\t\t类名：\t" + anchor.getClassName() + "\n" +
                                "\t\t路径：\t" + anchor.getPath()
                );
                for (AnchorPoint anchorPoint : anchor.getAnchorPointList()) {
                    System.out.println(
                            "\t\t行数：\t" + anchorPoint.getRow() +
                                    "\t\t检测方法：\t" + anchorPoint.getKeyWord() +
                                    "\t\t漏洞类型：\t" + loophole.getType() +
                                    "\t\t漏洞分险：\t" + loophole.getRisk() +
                                    "\t\t漏洞语句：\t" + anchorPoint.getLine()
                    );

                }
                System.out.println();
            }
        }
    }

    public static void findManifestLoophole(String path, String word, loophole loophole) {
        ArrayList<String> pathList = fileUtils.recursionFileList(path);
        word = word.replace(" ", "").toLowerCase();
        String[] keyWords = word.split(",");
        for (String p : pathList) {
            Anchor anchor = findKeyByPath(p, keyWords);
            if (anchor.getAnchorPointList().size() > 0 && anchor.getShowWord() >= keyWords.length) {
                System.out.println(
                        "\t\t类名：\t" + anchor.getClassName() + "\n" +
                                "\t\t路径：\t" + anchor.getPath()
                );

                List<AnchorPoint> anchorPointList = anchor.getAnchorPointList();
                if (keyWords.length > 1) {
                    for (int i = 0; i < anchorPointList.size(); ++i)
                        for (int j = anchorPointList.size() - 1; j >= i; --j) {
                            if (!anchorPointList.get(i).getKeyWord().equals(anchorPointList.get(j).getKeyWord()) && anchorPointList.get(i).getRow() == anchorPointList.get(j).getRow())
                                System.out.println(
                                        "\t\t行数：\t" + anchorPointList.get(i).getRow() +
                                                "\t\t检测方法：\t" + anchorPointList.get(i).getKeyWord() +
                                                "\t\t漏洞类型：\t" + loophole.getType() +
                                                "\t\t漏洞分险：\t" + loophole.getRisk() +
                                                "\t\t漏洞语句：\t" + anchorPointList.get(i).getLine()
                                );
                        }
                } else for (AnchorPoint anchorPoint : anchor.getAnchorPointList()) {
                    System.out.println(
                            "\t\t行数：\t" + anchorPoint.getRow() +
                                    "\t\t检测方法：\t" + anchorPoint.getKeyWord() +
                                    "\t\t漏洞类型：\t" + loophole.getType() +
                                    "\t\t漏洞分险：\t" + loophole.getRisk() +
                                    "\t\t漏洞语句：\t" + anchorPoint.getLine()
                    );
                }
                System.out.println();
            }
        }
    }

    public static void findApi(String path, String word, api api) {
        ArrayList<String> pathList = fileUtils.recursionFileList(path);
        //System.out.println(pathList);
        for (String p : pathList) {
            Anchor anchor = findKeyByPath(p, word);
            if (anchor.getAnchorPointList().size() > 0) {
                System.out.println(
                        "\t\t类名：\t" + anchor.getClassName() + "\n" +
                                "\t\t路径：\t" + anchor.getPath()
                );
                for (AnchorPoint anchorPoint : anchor.getAnchorPointList()) {
                    System.out.println(
                            "\t\t行数：\t" + anchorPoint.getRow() +
                                    "\t\t特征函数：\t" + api.getJclass() +
                                    "\t\t检测方法：\t" + anchorPoint.getKeyWord() +
                                    "\t\t功能说明：\t" + api.getDescription() +
                                    "\t\t目标语句：\t" + anchorPoint.getLine()
                    );

                }
                System.out.println();
            }
        }
    }

    public static void findLoophole(String path, String[] word) {
        ArrayList<String> pathList = fileUtils.recursionFileList(path);
        for (String p : pathList) {
            Anchor anchor = findKeyByPath(p, word);
            if (anchor.getAnchorPointList().size() > 0) {
                System.out.println(
                        "\t\t类名：\t" + anchor.getClassName() + "\n" +
                                "\t\t路径：\t" + anchor.getPath()
                );
                for (AnchorPoint anchorPoint : anchor.getAnchorPointList()) {
                    System.out.println(
                            "\t\t行数：\t" + anchorPoint.getRow() +
                                    "\t\t起点下标：\t" + anchorPoint.getStart() +
                                    "\t\t终点下标：\t" + anchorPoint.getEnd() +
                                    "\t\t关键词：\t" + anchorPoint.getKeyWord() +
                                    "\t\t目标句：\t" + anchorPoint.getLine()
                    );
                }
                System.out.println();
            }
        }
    }

    public static Anchor findKeyByPath(String path, String word) {
        List<AnchorPoint> anchorPointList = new ArrayList<>();
        Anchor anchor = new Anchor();
        anchor.setPath(path);
        try {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                anchor.setClassName(file.getName());
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                BufferedReader bre = new BufferedReader(read);
                for (int i = 1; bre.ready(); ++i) {
                    String s = bre.readLine().toLowerCase();
                    String p = word.toLowerCase();
                    anchorPointList = Sunday(s, p, i);
                    //anchor.setShowWord(anchor.getShowWord() + 1);
                    anchor.setAnchorPointList(anchorPointList);
                }
            } else System.out.println("目标文件不存在：" + path);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件读取异常！");
        }
        return anchor;
    }


    public static Anchor findKeyByPath(String path, String[] word) {
        Anchor anchor = new Anchor();
        anchor.setPath(path);
        try {
            File file = new File(path);
            for (String key : word) {
                List<AnchorPoint> anchorPointList = new ArrayList<>();
                if (file.exists() && file.isFile()) {
                    anchor.setClassName(file.getName());
                    InputStreamReader read = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                    BufferedReader bre = new BufferedReader(read);
                    for (int i = 1; bre.ready(); ++i) {
                        String s = bre.readLine().toLowerCase();
                        String p = key.toLowerCase();
                        anchorPointList.addAll(Sunday(s, p, i));
                    }
                } else System.out.println("目标文件不存在：" + path);
                if (anchorPointList.size() > 0) {
                    anchor.setShowWord(anchor.getShowWord() + 1);
                    anchor.setAnchorPointList(anchorPointList);
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
            System.out.println("文件读取异常！");
        }
        return anchor;
    }


    //注意每次都是从后向前
    public static int contains(char[] str, char ch) {
        for (int i = str.length - 1; i >= 0; i--) {
            if (str[i] == ch) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 匹配字符串
     *
     * @param s 目标字符串
     * @param p 需要匹配的字符串
     */

    public static List<AnchorPoint> Sunday(String s, String p, int rows) {
        while (s.startsWith(" ")) s = s.substring(1);
        List<AnchorPoint> anchorPointList = new ArrayList<>();
        char[] sarray = s.toCharArray();
        char[] parray = p.toCharArray();
        int slen = s.length();
        int plen = p.length();
        int i = 0, j = 0;
        while (i <= slen - plen + j) {//这句话控制索引i,j的范围
            if (sarray[i] != parray[j]) {
                //假如主串的sarry[i]与模式串的parray[j]不相等
                if (i == slen - plen + j) {
                    break;
                    //假如主串的sarry[i]与模式串的parray[j]不相等,并且i=slen-plen+j,说明这已经
                    //是在和主串中最后可能相等的字符段比较了,并且不相等,说明后面就再也没有相等的了,所以
                    //跳出循环,结束匹配
                }
                //假如是主串的中间字段与模式串匹配，且结果不匹配
                //则就从模式串的最后面开始,(注意是从后向前)向前遍历,找出模式串的后一位在对应的母串的字符是否在子串中存在
                int pos = contains(parray, sarray[i + plen - j]);
                if (pos == -1) {
                    //表示不存在
                    i = i + plen + 1 - j;
                    j = 0;
                } else {
                    i = i + plen - pos - j;
                    j = 0;
                }
            } else {
                //假如主串的sarry[i]与模式串的parray[j]相等,则继续下面的操作
                if (j == plen - 1) {
                    //判断模式串的索引j是不是已经到达模式串的最后位置，
                    //j==plen-1证明在主串中已经找到一个模式串的位置,
                    //且目前主串尾部的索引为i,主串首部的索引为i-j,打印模式串匹配的第一个位置
                    //System.out.println("行数：" + rows + "\t起始坐标：" + (i - j) + "\t终点坐标：" + i + "\t出现：" + p);
                    AnchorPoint anchorPoint = new AnchorPoint();
                    anchorPoint.setRow(rows);
                    anchorPoint.setStart(i - j);
                    anchorPoint.setKeyWord(p);
                    anchorPoint.setEnd(i);
                    anchorPoint.setLine(s);
                    anchorPointList.add(anchorPoint);

                    //然后主串右移一个位置,再和模式串的首字符比较,从而寻找下一个匹配的位置
                    i = i - j + 1;
                    j = 0;
                } else {
                    //假如模式串的索引j!=plen-1,说明模式串还没有匹配完,则i++,j++继续匹配,
                    i++;
                    j++;
                }
            }
        }

        return anchorPointList;
    }


}