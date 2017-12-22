//importing required packages 
import java.awt.*; 
import java.awt.event.*; //package to implement event response 
import javax.swing.*; //package to implement swing gui
import java.sql.*; //package to connect to mysql database

class OnlineTest extends JFrame implements ActionListener  
{  
    JLabel l;  //for setting question text
    JRadioButton jb[]=new JRadioButton[5]; //for options of the question  
    JButton b1,b2,b3;  //for start/next, previous and result buttons
    ButtonGroup bg;  
    int count=0,attempted=0,current=-1,x=1,y=1,now=0; 
    long StartTime, EndTime,seconds,minutes,flag=0;; 
    int a;
    OnlineTest(String s)  
    {  
        super(s);  
        l=new JLabel();  
        add(l);  
        bg=new ButtonGroup();  
        for(int i=0;i<5;i++)  
        {  
            jb[i]=new JRadioButton();     
            add(jb[i]);  
            bg.add(jb[i]);  
        }
        b1=new JButton("Start");   //Setting text of first button
        b1.addActionListener(this); //Added event listener, i.e., action to be taken on being clicked
        add(b1); //added start button
        welcome(); //displays welcome message
        b2=new JButton("Previous"); //Setting text of second button
        b3=new JButton("Result"); //Setting text of third button
        b2.addActionListener(this); 
        b3.addActionListener(this);  
        add(b2);add(b3);
        // added previous and result buttons
        l.setBounds(30,40,450,20);  //setting dimensions of question area
        if(current !=-1)
        {   
            jb[0].setBounds(50,80,100,20);  //setting dimensions and coordinates of radio button group
            jb[1].setBounds(50,110,100,20);  
            jb[2].setBounds(50,140,100,20);  
            jb[3].setBounds(50,170,100,20);  
        }
        b1.setBounds(100,240,100,30);  //setting dimensions and coordinates of start/next button
        b2.setBounds(270,240,100,30);  //setting dimensions and coordinates of previous button
        b3.setBounds(400,240,100,30);  //setting dimensions and coordinates of result button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        setLayout(null);  
        setLocation(250,100);  
        setVisible(true);  
        setSize(600,350);  
    }  
    public void actionPerformed(ActionEvent e)  
    {  
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql:///qa","root","shivhek25@mysql"); //connecting to database 'qa'
            Statement stmt = con.createStatement();
            if(e.getSource()==b1 && current ==9 ) //if all 10 questions have been displayed and user clicks on next, i.e., no more questions are available to be displayed
            {
                adduserans(); //adding user's response to the 10th question
                JOptionPane.showMessageDialog(this,"No more questions. Please go back to previous question or end test and see result.\n");   
            }
            else if(e.getSource()==b1)  //if user clicks on start/next and there are more questions available to be displayed
            {  
                if(current == -1) //if user hasn't started test yet, i.e., she/he clicks on "start" button
                {
                    StartTime = System.currentTimeMillis(); //stores time when user starts test
                    b1.setText("Next"); //setting text of b1 button to "next"
                }
                else
                    adduserans(); //adding user's response to the question
                current++; //incrementing counter of questions countered
                setnext(); //setting next question
            }  
            else if(e.getSource()==b2 && current ==0 ) //if user clicks on previous button and there are no more questions available to be displayed
            {
                adduserans(); 
                JOptionPane.showMessageDialog(this,"No more questions. Please go back to next question or end test and see result.\n");   
            }
            else if(e.getSource()==b2)  //if user clicks on previous and there are more questions available to be displayed
            {   
                current--;  //decrementing counter of questions countered
                adduserans();
                setnext();   
            }  
            else if(e.getActionCommand().equals("Result"))  //if user clicks on result button
            {  
                EndTime = System.currentTimeMillis(); //stores time when user ends test
                EndTime-=StartTime; //stores time taken by user to give test in milliseconds
                EndTime/=1000; //stores time taken by user to give test in seconds
                if(EndTime>=60) // if time can be expressed in minutes or hours
                {
                    seconds = EndTime%60; //calculating seconds
                    EndTime/=60; //calculating minutes
                    flag=1;
                    if(EndTime>=60) //if time can be expressed in hours
                    {
                        flag=2;
                        minutes=EndTime%60; //calculating minutes
                        EndTime/=60; //calculating hours
                    }
                }
                current++;   
                check(); //checks user's responses against correct responses stored in database 
                if(flag==0)
                 a = JOptionPane.showConfirmDialog(this,"Attempted questions: "+attempted+" / 10\nTime taken: "+EndTime+" seconds\nYour Score: "+count+" / 10\nPercentage: "+(count*10)+" %\nDo you wish to see the answer key ?");  
                else if(flag==1)
                 a = JOptionPane.showConfirmDialog(this,"Attempted questions: "+attempted+" / 10\nTime taken: "+EndTime+" minutes "+seconds+" seconds\nYour Score: "+count+" / 10\nPercentage: "+(count*10)+" %\nDo you wish to see the answer key ?");     
                else
                 a = JOptionPane.showConfirmDialog(this,"Attempted questions: "+attempted+" / 10\nTime taken: "+EndTime+" hours "+minutes+" minutes "+seconds+" seconds\nYour Score: "+count+" / 10\nPercentage: "+(count*10)+" %\nDo you wish to see the answer key ?");     
                //displays number of attempted questions, total score and percentage
                if(a==JOptionPane.YES_OPTION) //checks if user wants to see answer key or not
                    showAnswerKey();
                else
                {
                    stmt.executeUpdate("delete from ua");
                    stmt.executeUpdate("delete from stuua");
                    stmt.executeUpdate("delete from stuqao");
                    stmt.executeUpdate("delete from qao");
                    System.exit(0);  //closes interface and exits
                }
            } 
        }
        catch(Exception ex)
        {
            System.out.println("actionPerformed"+ex);
        } 
    }  
    void welcome() //Welcome Message 
    {
        l.setText("Welcome to the online examination. Click button to start with the test.") ;
    }
    void setnext() //function to set next/previous question 
    {  
        jb[4].setSelected(true);  
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql:///qa","root","shivhek25@mysql"); //connecting to database 'qa'
            Statement stmt = con.createStatement();
            if(current==0)  
            {    
                String sql="select * from stuqao where qno=1"; //selects all fields of table 'stuqao' with value of field qno equal to 1
                ResultSet rs = stmt.executeQuery(sql); //executing mysql query 
                rs.next(); //pointing to next row of result set 
                String s1 =rs.getString("question"); //getting value stored in result set under field "question"
                String s2 =rs.getString("option1"); //getting value stored in result set under field "option1"
                String s3 =rs.getString("option2"); //getting value stored in result set under field "option2"
                String s4 =rs.getString("option3"); //getting value stored in result set under field "option3"
                String s5 =rs.getString("option4"); //getting value stored in result set under field "option4"
                l.setText("Q.1 "+s1); //setting question
                jb[0].setText(s2);jb[1].setText(s3);jb[2].setText(s4);jb[3].setText(s5);  //setting options
            }  
            if(current==1)  
            {  
                String sql="select * from stuqao where qno=2";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                String s1 =rs.getString("question");
                String s2 =rs.getString("option1");
                String s3 =rs.getString("option2");
                String s4 =rs.getString("option3");
                String s5 =rs.getString("option4");
                l.setText("Q.2 "+s1);  
                jb[0].setText(s2);jb[1].setText(s3);jb[2].setText(s4);jb[3].setText(s5);  
            }  
            if(current==2)  
            {  
                String sql="select * from stuqao where qno=3";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                String s1 =rs.getString("question");
                String s2 =rs.getString("option1");
                String s3 =rs.getString("option2");
                String s4 =rs.getString("option3");
                String s5 =rs.getString("option4");
                l.setText("Q.3 "+s1);  
                jb[0].setText(s2);jb[1].setText(s3);jb[2].setText(s4);jb[3].setText(s5);  
            }  
            if(current==3)  
            {  
                String sql="select * from stuqao where qno=4";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                String s1 =rs.getString("question");
                String s2 =rs.getString("option1");
                String s3 =rs.getString("option2");
                String s4 =rs.getString("option3");
                String s5 =rs.getString("option4");
                l.setText("Q.4 "+s1);  
                jb[0].setText(s2);jb[1].setText(s3);jb[2].setText(s4);jb[3].setText(s5);  
            }  
            if(current==4)  
            {  
                String sql="select * from stuqao where qno=5";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                String s1 =rs.getString("question");
                String s2 =rs.getString("option1");
                String s3 =rs.getString("option2");
                String s4 =rs.getString("option3");
                String s5 =rs.getString("option4");
                l.setText("Q.5 "+s1);  
                jb[0].setText(s2);jb[1].setText(s3);jb[2].setText(s4);jb[3].setText(s5);  
            }  
            if(current==5)  
            {  
                String sql="select * from stuqao where qno=6";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                String s1 =rs.getString("question");
                String s2 =rs.getString("option1");
                String s3 =rs.getString("option2");
                String s4 =rs.getString("option3");
                String s5 =rs.getString("option4");
                l.setText("Q.6 "+s1);  
                jb[0].setText(s2);jb[1].setText(s3);jb[2].setText(s4);jb[3].setText(s5);  
            }  
            if(current==6)  
            {  
                String sql="select * from stuqao where qno=7";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                String s1 =rs.getString("question");
                String s2 =rs.getString("option1");
                String s3 =rs.getString("option2");
                String s4 =rs.getString("option3");
                String s5 =rs.getString("option4");
                l.setText("Q.7 "+s1);  
                jb[0].setText(s2);jb[1].setText(s3);jb[2].setText(s4);jb[3].setText(s5);  
            }  
            if(current==7)  
            {  
                String sql="select * from stuqao where qno=8";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                String s1 =rs.getString("question");
                String s2 =rs.getString("option1");
                String s3 =rs.getString("option2");
                String s4 =rs.getString("option3");
                String s5 =rs.getString("option4");
                l.setText("Q.8 "+s1);  
                jb[0].setText(s2);jb[1].setText(s3);jb[2].setText(s4);jb[3].setText(s5);  
            }  
            if(current==8)  
            {  
                String sql="select * from stuqao where qno=9";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                String s1 =rs.getString("question");
                String s2 =rs.getString("option1");
                String s3 =rs.getString("option2");
                String s4 =rs.getString("option3");
                String s5 =rs.getString("option4");
                l.setText("Q.9 "+s1);  
                jb[0].setText(s2);jb[1].setText(s3);jb[2].setText(s4);jb[3].setText(s5);  
            }  
            if(current==9)  
            {  
                String sql="select * from stuqao where qno=10";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                String s1 =rs.getString("question");
                String s2 =rs.getString("option1");
                String s3 =rs.getString("option2");
                String s4 =rs.getString("option3");
                String s5 =rs.getString("option4");
                l.setText("Q.10 "+s1);  
                jb[0].setText(s2);jb[1].setText(s3);jb[2].setText(s4);jb[3].setText(s5);  
            }  
            l.setBounds(30,40,450,20);  
            for(int i=0,j=0;i<=90;i+=30,j++)  
                jb[j].setBounds(50,80+i,200,20);  
        }   
        catch(Exception e)
        {
            System.out.println("setnext\n"+e);
        }
    }  
    void adduserans() //function to connect to qa database and insert user's answers into ua table
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql:///qa","root","shivhek25@mysql");
            Statement stmt = con.createStatement();
            for(int i=0;i<=3;i++)
            {
                if(jb[i].isSelected()) //if answer has been selected in radio button group
                {
                    String sql1 = "insert into stuua(qno,userans) values("+(current+1)+",'"+jb[i].getText()+"') on duplicate key update userans='"+jb[i].getText()+"'";
                    //if user opts to change answer, she/he can do so because of the above command. 
                    //This allows the table to accept overwriting of existing values
                    stmt.executeUpdate(sql1);
                    //con.close();
                    break;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("adduserans\n"+e);
        }
    }
    void check()  //function to check number of correct answers 
    {  
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql:///qa","root","shivhek25@mysql");
            Statement stmt = con.createStatement();
            for(int i=1;i<=10;i++)
            {
                String sql="select userans, correctans from stuua where qno="+i+"";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                String s1 =rs.getString("userans");
                String s2 =rs.getString("correctans");
                if(!(s1.equals(""))) //checks if the user has attempted the question or not 
                    attempted++;
                if(s1.equals(s2)) //checks if user's answer matches correct answer
                 count++;
            }
        }
        catch(Exception e)
        {
            System.out.println("check\n"+e);
        }
    }
    void showAnswerKey() //function to print answer key if requested for
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql:///qa","root","shivhek25@mysql");
            Statement stmt = con.createStatement();
            String answerkey="";
            answerkey+="Answer Key:\nQ.No.  Your answer  Correct Answer\n";
            for(int i=1;i<=10;i++)
            {
                String sql="select userans, correctans from stuua where qno="+i+"";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                String s1 =rs.getString("userans"); //stores user's answer
                if(s1.equals("")) //if user hasn't attempted this question, we assign NA to variable
                    s1="NA";
                String s2 =rs.getString("correctans"); //stores correct answer
                if(i<=9)
                 answerkey+="   "+(char)(i+48)+"         "+s1+"         "+s2+"\n";
                else //0-9 is 48-57 in ascii 
                 answerkey+="  10"+"         "+s1+"         "+s2+"\n";
            }
            JOptionPane.showMessageDialog(this,answerkey); //prints answer key
            stmt.executeUpdate("delete from ua");
            stmt.executeUpdate("delete from stuua");
            stmt.executeUpdate("delete from stuqao");
            stmt.executeUpdate("delete from qao");
            System.exit(0);
            con.close();
        }
        catch(Exception e)
        {
            System.out.println("showAnswerKey\n"+e);
        }
    }
    static void qaoDBcon() //function to connect to qa database and insert question and options into qao table
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql:///qa","root","shivhek25@mysql");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("insert into qao values(1,'What language are we using ?','C1','C1++','J1ava','P1ython')");
            stmt.executeUpdate("insert into qao values(2,'What language are we using ?','C2','C2++','J2ava','P2ython')");
            stmt.executeUpdate("insert into qao values(3,'What language are we using ?','C3','C3++','J3ava','P3ython')");
            stmt.executeUpdate("insert into qao values(4,'What language are we using ?','C4','C4++','J4ava','P4ython')");
            stmt.executeUpdate("insert into qao values(5,'What language are we using ?','C5','C5++','J5ava','P5ython')");
            stmt.executeUpdate("insert into qao values(6,'What language are we using ?','C6','C6++','J6ava','P6ython')");
            stmt.executeUpdate("insert into qao values(7,'What language are we using ?','C7','C7++','J7ava','P7ython')");
            stmt.executeUpdate("insert into qao values(8,'What language are we using ?','C8','C8++','J8ava','P8ython')");
            stmt.executeUpdate("insert into qao values(9,'What language are we using ?','C9','C9++','J9ava','P9ython')");
            stmt.executeUpdate("insert into qao values(10,'What language are we using ?','C10','C10++','J10ava','P10ython')");
            stmt.executeUpdate("insert into qao values(11,'What language are we using ?','C11','C11++','J11ava','P11ython')");
            stmt.executeUpdate("insert into qao values(12,'What language are we using ?','C12','C12++','J12ava','P12ython')");
            stmt.executeUpdate("insert into qao values(13,'What language are we using ?','C13','C13++','J13ava','P13ython')");
            stmt.executeUpdate("insert into qao values(14,'What language are we using ?','C14','C14++','J14ava','P14ython')");
            stmt.executeUpdate("insert into qao values(15,'What language are we using ?','C15','C15++','J15ava','P15ython')");
            stmt.executeUpdate("insert into qao values(16,'What language are we using ?','C16','C16++','J16ava','P16ython')");
            stmt.executeUpdate("insert into qao values(17,'What language are we using ?','C17','C17++','J17ava','P17ython')");
            stmt.executeUpdate("insert into qao values(18,'What language are we using ?','C18','C18++','J18ava','P18ython')");
            stmt.executeUpdate("insert into qao values(19,'What language are we using ?','C19','C19++','J19ava','P19ython')");
            stmt.executeUpdate("insert into qao values(20,'What language are we using ?','C20','C20++','J20ava','P20ython')");
            con.close();
        }
        catch(Exception e)
        {
            System.out.println("qaoDBcon\n"+e);
        }
    }
    static void uaDBcon() //function to connect to qa database and insert correct answers of all questions into ua table
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql:///qa","root","shivhek25@mysql");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("insert into ua values(1,'','J1ava')");
            stmt.executeUpdate("insert into ua values(2,'','J2ava')");
            stmt.executeUpdate("insert into ua values(3,'','J3ava')");
            stmt.executeUpdate("insert into ua values(4,'','J4ava')");
            stmt.executeUpdate("insert into ua values(5,'','J5ava')");
            stmt.executeUpdate("insert into ua values(6,'','J6ava')");
            stmt.executeUpdate("insert into ua values(7,'','J7ava')");
            stmt.executeUpdate("insert into ua values(8,'','J8ava')");
            stmt.executeUpdate("insert into ua values(9,'','J9ava')");
            stmt.executeUpdate("insert into ua values(10,'','J10ava')");
            stmt.executeUpdate("insert into ua values(11,'','J11ava')");
            stmt.executeUpdate("insert into ua values(12,'','J12ava')");
            stmt.executeUpdate("insert into ua values(13,'','J13ava')");
            stmt.executeUpdate("insert into ua values(14,'','J14ava')");
            stmt.executeUpdate("insert into ua values(15,'','J15ava')");
            stmt.executeUpdate("insert into ua values(16,'','J16ava')");
            stmt.executeUpdate("insert into ua values(17,'','J17ava')");
            stmt.executeUpdate("insert into ua values(18,'','J18ava')");
            stmt.executeUpdate("insert into ua values(19,'','J19ava')");
            stmt.executeUpdate("insert into ua values(20,'','J20ava')");
            con.close();
        }
        catch(Exception e)
        {
            System.out.println("uaDBcon\n"+e);
        }
    }
    static void pickrandom()
    /*function to pick 10 random questions from the qao table, store them in stuqao table and display to the user
    this function also stores the correct answers of these 10 picked questions into the table stuua
    */
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql:///qa","root","shivhek25@mysql");
            Statement stmt = con.createStatement();
            int a[]=new int[21];
            int c=0;
            int p;
            for(int i=0;i<=20;i++)
                a[i]=0;
            while(c!=10)
            {
                p=1+(int)(Math.random()*20); //generating random integers in range [1,20]
                if(a[p]==0)
                {
                    a[p]=1; //marking the 10 randomly selected integers
                    c++; //counting number of random indexes marked
                }
            }
            c=0;
            for(int i=1;i<=20;i++)
            {
                if(a[i]==1) //checks if integer 'i' has been picked by code as a random integer 
                {
                    c++;
                    String sql="select * from qao where qno="+i+"";
                    ResultSet randomrs = stmt.executeQuery(sql);
                    randomrs.next();
                    String s1 =randomrs.getString("question");
                    String s2 =randomrs.getString("option1");
                    String s3 =randomrs.getString("option2");
                    String s4 =randomrs.getString("option3");
                    String s5 =randomrs.getString("option4");
                    stmt.executeUpdate("insert into stuqao values("+c+",'"+s1+"','"+s2+"','"+s3+"','"+s4+"','"+s5+"')");
                    randomrs.close();
                    sql="select * from ua where qno="+i+"";
                    randomrs = stmt.executeQuery(sql);
                    randomrs.next();
                    s1 =randomrs.getString("correctans");
                    stmt.executeUpdate("insert into stuua values("+c+",'','"+s1+"')");
                    randomrs.close();

                }
            }
            con.close();
        }
        catch(Exception e)
        {
            System.out.println("pickrandom\n"+e);
        }
    }
    public static void main(String s[])
    {  
        qaoDBcon(); //creating question-option database
        uaDBcon(); //creating user answer-correct answer database
        pickrandom(); //creating question-option database that will be asked to student
        new OnlineTest("Online Exam System");  // creating object 
    }  
}  