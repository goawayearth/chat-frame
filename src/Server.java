import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Timer;

class Server extends JFrame   implements Runnable{
    private JLabel label=new JLabel();
    private ServerSocket ss = null;
    private ArrayList<ChatThread> users = new ArrayList<ChatThread>();
    private ArrayList<Integer> online=new ArrayList<Integer>();
    private ArrayList<String> NiName=new ArrayList<>();
    /*
    客户端每2秒发一次自己存在
    服务器每三秒确认谁不在了
     */
    private Timer timer=new Timer();
    TimerTask timerTask=new TimerTask()
    {
        @Override
        public void run()
        {
            for(int i=0;i<users.size();i++)
            {
                if(online.get(i)==0)
                {
                    users.remove(i);
                    online.remove(i);
                    NiName.remove(i);
                    label.setText("在线人数："+users.size());
                    i--;
                }
            }
            online.clear();
            for(int i=0;i<users.size();i++)//把和客户端相同数量的位置制成0
            {
                int one = 0;
                online.add(one);
            }
            //向所有客户端发送数据。让客户端回复,客户端把发过去的发回来
            for(int i=0;i<users.size();i++)
            {
                ChatThread ct=users.get(i);
                ct.ps.println("!@#$%"+i);
                ct.ps.println("<,./>");
                for(int i1=0;i1<NiName.size();i1++)
                {
                    ct.ps.println("%^&*("+NiName.get(i1));
                }
            }
        }
    };
    public Server() throws Exception {
        this.setSize(400,300);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("服务器");
        this.setLayout(null);
        this.add(label);
        label.setLocation(80,10);
        label.setSize(400,50);
        label.setText("在线人数是："+users.size());
        label.setFont(new Font("宋体",Font.BOLD,15));
        ss = new ServerSocket(8888);
        timer.schedule(timerTask,1000,1000);//每三秒执行一次定时器
        new Thread(this).start();
    }
    //不断接收客户端的联接
    public void run(){
        while(true){
            try{
                Socket s = ss.accept();
                ChatThread ct = new ChatThread(s);
                users.add(ct);	ct.start();
                int one=1;
                online.add(one);
                NiName.add("###");
                label.setText("在线人数："+users.size());
            }catch(Exception ex){}
        }
    }
    class ChatThread extends Thread{
        BufferedReader br = null;
        PrintStream ps = null;
        ChatThread(Socket s) throws Exception{
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ps = new PrintStream(s.getOutputStream());
        }
        //每个客户端插座都在不断的接收客户端消息
        public void run(){
            while(true){
                try{
                    String msg = br.readLine();
                    String sub=msg.substring(0,5);
                    if(sub.equals("!@#$%"))//如果前五个数字符合就是回馈
                    {
                        String num=msg.substring(15);
                        String niName=msg.substring(5,15);
                        Integer num2 = Integer.valueOf(num);
                        int one=1;
                        online.set(num2,one);
                        NiName.set(num2,niName);
                    }
                    else
                    {
                        //群发给所有在线客户端
                        for (ChatThread ct : users) {
                            ct.ps.println(msg);
                        }
                    }
                }catch(Exception e){}
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
        new Server();
    }
}