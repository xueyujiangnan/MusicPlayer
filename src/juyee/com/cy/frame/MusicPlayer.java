package juyee.com.cy.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by Administrator on 2014/9/2.
 */
public class MusicPlayer extends JFrame implements ActionListener,Runnable{
    private JMenuBar          bar;//菜单条
    private JMenu             fileMenu,choiceMenu,aboutMenu;
    private JMenuItem         openItem,openDirItem,closeItem,about,infor;
    private JCheckBoxMenuItem onTop;
    private boolean           top=false,loop;//设定窗口是否在最前面
    private Player            player;//Play是个实现Controller的接口
    private File              file,listFile;//利用File类结合JFileChooser进行文件打开操作,后则与list.ini有关
    private Container         c;
    //private UIManager.LookAndFeelInfo[] look;
    private String            title,listIniAddress;//标题
    private FileDialog        fd;
    private JPanel            panel,panelSouth;
    private Icon              icon; //开始进入的时候要显示的图标，它为抽象类，不能自己创建
    private JLabel            label,listB;//用来显示图标

    private JList             list;//播放清单
    private JScrollPane       scroll;//使播放清单具有滚动功能
    private ListValues        listWriteFile;//用于向文件中读取对象
    private ObjectInputStream input;//对象输入流
    private ObjectOutputStream output;//对象输出流

    private JPopupMenu        popupMenu;//鼠标右键弹出菜单
    private JMenuItem         del,delAll,reName;      //弹出菜单显示的菜单项,包括删除,全部删除和重命名

    private Vector fileName,dirName,numList;
    private String            files,dir;
    private int               index;//曲目指针
    private Properties prop;//获得系统属性
    private int               indexForDel;//标志要删除的列表项目的索引
    private ButtonGroup       buttonGroup;//控制按钮组
    private JRadioButtonMenuItem[]    buttonValues;
    private String[]          content={"随机播放","顺序播放","单曲循环"};

    private DialogDemo        dialog1;
    //private JDialogTest       dialog2;//用于显示播放清单
    public MusicPlayer(){
        super("java音频播放器1.1版");//窗口标题
        c=getContentPane();
        c.setLayout(new BorderLayout());
        //c.setBackground(new Color(40,40,95));

        fileName=new Vector(1);
        dirName=new Vector(1);
        numList=new Vector(1);//构造三个容器用于支持播放清单
        //vectorToString=new String[];
        //prop=new Properties(System.getProperties());
        //listIniAddress=prop.getProperty("user.dir")+"\\list.ini";
        //listFile=new File(listIniAddress);//本来这些代码用来取的系统属性，后来
        //发现根本就不用这么麻烦
        listFile=new File("list.ini");//直接存于此目录
        Thread readToList=new Thread(this);//注意编线程程序的时候要注意运行的时候含有的变量亿定义或者初始化，
        //这就要求线程要等上述所说的情况下再运行，否则很容易发生错误或则异常

        list=new JList();
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setSelectionForeground(new Color(0,150,150));
        list.setVisibleRowCount(10);
        list.setFixedCellHeight(12);
        list.setFixedCellWidth(250);
        list.setFont(new Font("Serif",Font.PLAIN,12));
        list.setBackground(new Color(40,40,95));
        list.setForeground(new Color(0,128,255));
        //list.setOpaque(false);
        list.setToolTipText("点右键显示更多功能");//创建播放清单并设置各个属性
        list.addMouseListener(new MouseAdapter()
                              {
                                  public void mouseClicked(MouseEvent e)
                                  {
                                      if (e.getClickCount() == 2) //判断是否双击
                                      {
                                          index = list.locationToIndex(e.getPoint());//将鼠标坐标转化成list中的选项指针
                                          createPlayer2();
                                          //System.out.println("Double clicked on Item " + index);，此是测试的时候加的
                                      }
                                  }
                                  /* public void mousePressed(MouseEvent e)
                                   {
                                    checkMenu(e);//自定义函数，判断是否是右键，来决定是否显示菜单
                                   }*/
                                  public void mouseReleased(MouseEvent e)
                                  {
                                      checkMenu(e);//与上面的一样，判断是否鼠标右键
                                  }

                              }
        );
        //listB=new JLabel(new ImageIcon("qingdan.gif"),SwingConstants.CENTER);
        scroll=new JScrollPane(list);//用于存放播放列表
        //dialog2=new JDialogTest(MediaPlayer.this,"播放清单",scroll);
        //dialog2.setVisible(true);

        readToList.start();//启动先程，加载播放列表
        try
        {
            Thread.sleep(10);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
          /*look=UIManager.getInstalledLookAndFeels();
  try
  {
   UIManager.setLookAndFeel(look[2].getClassName());
   SwingUtilities.updateComponentTreeUI(this);
  }
  catch(Exception e)
  {
   e.printStackTrace();
  }*///与下面的代码实现相同的功能,但执行速度要慢，原因:明显转了个大弯

  /*try
  {
   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  }
  catch(Exception e)
  {
   e.printStackTrace();
  } *///此段代码使执行速度大大降低


        bar=new JMenuBar();
        setJMenuBar(bar);//此两行创建菜单栏并放到此窗口程序
        //bar.setBackground(new Color(48,91,183));
        fileMenu=new JMenu("文件");
        bar.add(fileMenu);

        choiceMenu=new JMenu("控制");
        bar.add(choiceMenu);

        aboutMenu=new JMenu("帮助");
        bar.add(aboutMenu);

        openItem    =new JMenuItem("打开文件");
        openDirItem =new JMenuItem("打开目录");
        closeItem   =new JMenuItem("退出程序");
        openItem.addActionListener(this);
        openDirItem.addActionListener(this);
        closeItem.addActionListener(this);
        fileMenu.add(openItem);
        fileMenu.add(openDirItem);
        fileMenu.add(closeItem);

        onTop=new JCheckBoxMenuItem("播放时位于最前面",top);
        choiceMenu.add(onTop);
        onTop.addItemListener(new ItemListener()
                              {
                                  public void itemStateChanged(ItemEvent e)
                                  {
                                      if(onTop.isSelected())
                                          top=true;
                                      else top=false;
                                      setAlwaysOnTop(top);
                                  }
                              }
        );
        choiceMenu.addSeparator();//加分割符号

        buttonGroup=new ButtonGroup();
        buttonValues=new JRadioButtonMenuItem[3];
        for(int bt=0;bt<3;bt++)
        {
            buttonValues[bt]=new JRadioButtonMenuItem(content[bt]);
            buttonGroup.add(buttonValues[bt]);
            choiceMenu.add(buttonValues[bt]);
        }
        buttonValues[0].setSelected(true);
        choiceMenu.addSeparator();

  /*loopItem=new JCheckBoxMenuItem("是否循环");
  choiceMenu.add(loopItem);
  loopItem.addItemListener(new ItemListener()
  {
   public void itemStateChanged(ItemEvent e)
   {
    loop=!loop;
   }
  }
  );*/
        infor=new JMenuItem("软件简介");
        aboutMenu.add(infor);
        infor.addActionListener(this);

        about=new JMenuItem("关于作者");
        about.addActionListener(this);
        aboutMenu.add(about);
        //菜单栏设置完毕

        panel=new JPanel();
        panel.setLayout(new BorderLayout());
        c.add(panel,BorderLayout.CENTER);

        panelSouth=new JPanel();
        panelSouth.setLayout(new BorderLayout());
        c.add(panelSouth,BorderLayout.SOUTH);

        icon=new  ImageIcon("icon\\Player.jpg");
        label=new JLabel(icon);
        panel.add(label);

        popupMenu=new JPopupMenu();
        del      =new JMenuItem("删除");//鼠标右键弹出菜单对象实例化
        popupMenu.add(del);
        del.addActionListener(this);

        delAll   =new JMenuItem("全部删除");
        popupMenu.add(delAll);
        delAll.addActionListener(this);
        reName   =new JMenuItem("重命名");
        popupMenu.add(reName);
        reName.addActionListener(this);



        scroll=new JScrollPane(list);//用于存放播放列表
        listB=new JLabel(new ImageIcon("icon\\qingdan.gif"),SwingConstants.CENTER);

        panelSouth.add(listB, BorderLayout.NORTH);
        panelSouth.add(scroll,BorderLayout.CENTER);


        dialog1=new DialogDemo(MusicPlayer.this,"软件说明");

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//设定窗口关闭方式
        //this.setTitle("d");编译通过，说明可以再次设定标题
        this.setLocation(400,250);//设定窗口出现的位置
        //this.setSize(350,320);//窗口大小
        setSize(350, 330);
        this.setResizable(false);//设置播放器不能随便调大小
    }

    private void createPlayer2() {

    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private void checkMenu(MouseEvent e) {

    }
    @Override
    public void run() {

    }
}
