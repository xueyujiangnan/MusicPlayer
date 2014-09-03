package juyee.com.cy.frame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Administrator on 2014/9/3.
 */
public class DialogDemo extends JDialog {
    JTextArea field;
    Container c;
    String sValue= "\n本软件基于java media framework构建，同时我们"
            +"\n正处于基础java学习阶段，所以功能还不是非常完"
            +"\n善，难登大雅之堂。"
            +"\n\n\t功能简介："
            +"\n本软件的播放清单文件保存在同目录下的"+"\""+"list.ini"
            +"\""+"文件"+"\n下，如果系统不存在此文件则第一次打开的时候软件"
            +"\n会自动建立。本软件能打开单个音乐文件或整个音乐"
            +"\n目录,清单支持右键操作，当在列表中读取到不支持的"
            +"\n文件时，此软件会自动把那个文件从清单清除，并另"
            +"\n外播放一首歌。不过此软件有个不足之处，就是当你更"
            +"\n改了清单的时候，要正常退出，即先关闭播放器，然后"
            +"\n再关闭DOS窗口，因为我是在关闭播放器的时候保存清单"
            +"\n文件的。不过你运行我编译出来的jar文件就没有此问题"
            +"\n如果你使用中遇到任何问题，请通知我们,谢谢你的支持";

    DialogDemo(Frame frame,String title)
    {
        super(frame,title);

        field=new JTextArea();
        field.setText(sValue);
        field.setEditable(false);
        c=getContentPane();
        c.setLayout(new BorderLayout());
        c.add(field,BorderLayout.CENTER);//默认为BorderLayout布局
        setLocation(80,250);
        setSize(300,350);

        setResizable(false);
//setVisible(true);
    }
}
