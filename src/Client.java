import javax.swing.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
class Client extends JFrame implements Runnable ,ActionListener{
    private JLabel label1=new JLabel("聊天记录：");
    private JLabel label2=new JLabel("请输入：");
    private JTextField jtf = new JTextField();
    private JTextArea jta = new JTextArea(1000,30);
    private JScrollPane scrollPane=new JScrollPane(jta);
    private BufferedReader br = null;
    private PrintStream ps = null;
    private String nickName = null;
    private JDialog jDialog=new JDialog(this,"在线人员",false);

    private JButton jButton=new JButton("在线人员");
    private JTextArea NinameArea=new JTextArea(300,3);
    private JScrollPane NinamePane=new JScrollPane(NinameArea);


    public Client() throws Exception  {
        this.setLayout(null);
        NinameArea.setLineWrap(true);
        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jDialog.setVisible(true);
            }
        }); // 注册监听器

        jDialog.setSize(400,600);
        //jDialog.setVisible(true);
        this.add(jButton);
        jButton.setSize(100,25);
        jButton.setLocation(430,2);
        this.add(label1);
        label1.setSize(200,25);
        label1.setLocation(25,0);
        this.add(label2);
        label2.setSize(200,25);
        label2.setLocation(25,290);
        this.add(jtf);
        jtf.setSize(500,25);
        jtf.setLocation(25,320);
        jtf.setFont(new Font("宋体",Font.BOLD,20));
        this.add(scrollPane);
        scrollPane.setSize(500,260);
        scrollPane.setLocation(28,30);
        jta.setFont(new Font("楷体",Font.BOLD,20));
        jtf.addActionListener(this);
        this.setSize(600,400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        jDialog.setResizable(false);
        jDialog.add(NinamePane,BorderLayout.CENTER);
        nickName = JOptionPane.showInputDialog("请您输入昵称");
        Socket s = new Socket("fe80::94e:2a51:1bd:5dcf%16",8888);
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        ps = new PrintStream(s.getOutputStream());
        new Thread(this).start();
    }
    public void run(){
        while(true){
            try{
                String msg = br.readLine();
                String sub=msg.substring(0,5);
                if(sub.equals("!@#$%"))//是请求信号
                {
                    String nickname=nickName;
                    while(nickname.length()<10)
                    {
                        nickname=nickname+" ";
                    }
                    String num=msg.substring(5);
                    String mesg=sub+nickname+num;
                    ps.println(mesg);
                }
                else if(sub.equals("<,./>"))
                {
                    NinameArea.setText("");
                }
                else if(sub.equals("%^&*("))
                {
                    String name1=msg.substring(5)+"\n";
                    NinameArea.append(name1);
                }
                else
                {
                    jta.append(msg + "\n");
                }

            }catch(Exception e){}

        }
    }
    public void actionPerformed(ActionEvent e){
        ps.println(nickName + ":" + jtf.getText());
        jtf.setText("");
    }
    public static void main(String[] args) throws Exception   {
        new Client();
    }

}
