package com.revature.dao;
import java.sql.*;  
import java.util.*;


import java.security.*;


import java.util.LinkedList; 

public class DataManager{  

    Connection conn;
    String username="";
    String password="";
    String id="";
    
    public void connect() {

        // auto close connection
        try  {

             conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/project1", "postgres", "none");

            if (conn != null) {
                
                
            } else {
                System.out.println("Failed to make connection!");
            }

            
            
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.getMessage();
        }finally{
            
        }


    }

    public String say(){
        return("this is imported");
    }

    public void print(){

     

        System.out.println("printing table.");
        String query = "select * from test";
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            System.out.println("META:"+rsmd);
           
            
            while (rs.next()) {
                String year = rs.getString("year");
                System.out.println(year + "\n");
            }

            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        
    }


    
    public void printTable(){
      
        if(!checkPermission(this.username,this.password)){
            System.out.println("You must be a admin or employee to do this.");
            return;
        }

        String query = "select * from user_account";
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(query);
          

            
           
            
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String id = rs.getString("user_id");
                System.out.println(id + "  "+username+"  "+password);
            }

            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        
    }

    public void reject(String[] args){
        Boolean hasPermission = checkPermission(this.username,this.password);
        if(hasPermission){
            
        }else{
            System.out.println("You must be a admin or employee to do this.");
            return;
        }

        if(args.length==1){
            return;
        }
       
        
        int beg = 1, end = args.length;
		String[] list_ids = new String[end - beg];
		System.arraycopy(args, beg, list_ids, 0, list_ids.length);
        


        //TODO for mat list_ids into (1,2,3)
        String formated_list="";
        String template= "'%s',";
        for(int i=0;i<list_ids.length;i++){
              
            formated_list+= String.format(template, list_ids[i]);

        }
        formated_list = formated_list.substring(0, formated_list.length() - 1); //delete the last comma
        
        String query= "UPDATE applications "+
        "SET status = 'rejected' "+
        "WHERE app_id IN (%s) and status='pending'; ";                

        
        query  = String.format(query, formated_list);
        
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            stmt.executeUpdate(query);
    
            stmt.close();
            

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

    }

    private void createBankAccounts(String app_id){
        
        if(!checkPermission(this.username,this.password)){
            System.out.println("You must be a admin or employee to do this.");
            return;
        }

        String query= "UPDATE applications "+
        "SET status = 'approved' "+
        "WHERE app_id IN (%s) and status='pending'; ";        

        
        query = String.format(query, app_id);
       execute(query);
        
        //
        
        String max = getLastAccountNumber(); 
        
        if(max == "null"){
            max="0";
        }
        int new_account_number=Integer.parseInt(max)+101;
        
        query= "insert into bank(account_number,balance) values(%s,0) ";        

        
        query = String.format(query, new_account_number);
        execute(query);

       

        query= "INSERT INTO bank_owners(account_number,owner_id) VALUES "+
                      "(  %s,"+
                      " (SELECT owner_id FROM applications  WHERE app_id='%s'));";    
        
        query = String.format(query, new_account_number,app_id);
        execute(query);

        String coowner_id=getJointOwner(app_id);

        if(coowner_id==null){
            
        }else{
            query= "INSERT INTO bank_owners(account_number,owner_id) VALUES "+
                      "( %s,%s);";    
        
        query = String.format(query, new_account_number,coowner_id);
        execute(query);

        
        }
    }

   public void deposit(String[] args){
        if(args.length<3){
            return; //not enough args
        }

        String account_number=(args[1]);
        String money = (args[2]);
        money = money.replace("-", ""); //assume that user want positive
        

        if(isOwner(account_number)){
            
        }else{
            System.out.println("You do not own account#: "+account_number);
            return; 
        }


        //save tranaction to history
        String query= "insert into history(name,type,amount,account_number) values('%s','%s','%s','%s') ;";

        query = String.format(query,this.username, "deposit" ,money,account_number);
        execute(query);
        //
         query= "UPDATE bank "+
            "SET balance = balance +%.2f  "+
            "WHERE account_number='%s' ";        

            float fMoney = Float.parseFloat(money);
            query = String.format(query,fMoney ,account_number);
        execute(query);

        
    }


    public void updateStatus(String id, String value,String manager){
       
        //
         String query= "UPDATE requests "+
            "SET status = '%s', manager='%s' "+
            "WHERE id=%s ";        

            
        
        query = String.format(query,value ,manager,id);
        execute(query);

        
    }


    public void updateProfile(String username, String realname,String email){
       
        //
         String query= "UPDATE user_accounts "+
            "SET realname ='%s', email='%s' "+
            "WHERE username='%s' ";        

            
        
        query = String.format(query,realname,email,username);
        
        execute(query);

        
    }

    public void transfer(String[] args){
        if(args.length<4){
            System.out.println("failed: you are missing args.");
            return; //not enough args
        }
       
        
        String to_account = args[2];
        String amount = args[3];
 
        
        args[2]=amount;
        if(withdraw(args)){

        }else{
            return; // 
        }

        args[1]=to_account;
        
        deposit(args);

    }
   
   public boolean withdraw(String[] args){
            if(args.length<3){
                return false; //not enough args
            }

            String account_number=(args[1]);
            String money = (args[2]);
            money = money.replace("-", ""); //assume that user want positive
            
            
        

            if(isOwner(account_number)){
                
            }else{
                System.out.println("You do not own account#: "+account_number);
                return false; 
            }

            String balance = getBalance(account_number);
            float fBalance = Float.parseFloat(balance);
            float fMoney = Float.parseFloat(money);
            if(fMoney >= fBalance){
                System.out.println("tansaction failed: insufficient funds");
                return false;
            }

            String query= "UPDATE bank "+
                "SET balance = balance - %.2f "+
                "WHERE account_number='%s' ";        

                
                //float fMoney = Float.parseFloat(money);
                query = String.format(query,fMoney ,account_number);
            execute(query);

             //save tranaction to history
         query= "insert into history(name,type,amount,account_number) values('%s','%s','%s','%s') ;";

        query = String.format(query,this.username, "withdraw" ,money,account_number);

            
            
        execute(query);

        //

            return true;
        }


   private boolean isOwner(String account_number){
        String query= "select owner_id from bank_owners where owner_id='%s';";    

        query = String.format(query, this.id);
        
        boolean result=false;
        Statement stmt; 
        
        
        try{
            
            stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(query);
          

            
        
            
            if (rs.next()) {
                
                
                
                
               result=true;
                
                
            }else{
                result=false;
                
            }

            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }
        
        return result;
        
   }

   private String getBalance(String account_number){
    String query= "select balance from bank where account_number='%s';";    

    query = String.format(query, account_number);
    
    String result="0";
    Statement stmt; 
    

    
    try{
        
        stmt = conn.createStatement(); 
        ResultSet rs = stmt.executeQuery(query);
       

        
    
        
        if (rs.next()) {
            
            
           result=rs.getString("balance");    
            
    
            
            
        }else{
            result="0";
            
        }

        stmt.close();
        rs.close();

    }catch(Exception  e){
        System.err.format("ERROR: \n%s\n", e.getMessage());
    }finally{
        
    }
    
    return result;
    
}


    private String getLastAccountNumber(){
        String query = "SELECT MAX(account_number) FROM bank ;";
        String max="0";
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(query);
    
            if (rs.next()) {
                
                 max = rs.getString(1);
                
            }else{
                max="0";
            }

            stmt.close();
            
            

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        return max;
        
    }


    private String getJointOwner(String app_id){
        String query = "SELECT coowner_id FROM applications where app_id='%s';";
        String owner="";
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            query = String.format(query,app_id);
            ResultSet rs = stmt.executeQuery(query);
    
            if (rs.next()) {
                
                owner = rs.getString("coowner_id");
                
            }

            stmt.close();
            
            

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        return owner;
        
    }


    private void execute(String query){
        
        
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            stmt.executeUpdate(query);
    
            stmt.close();
            

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }


    }


    

    public void approve(String[] args){
        
        if(!checkPermission(this.username,this.password)){
            System.out.println("You must be a admin or employee to do this.");
            return;
        }

        
        if(args.length==1){
            return;
        }
        
        
        int beg = 1, end = args.length;
		String[] list_ids = new String[end - beg];
		System.arraycopy(args, beg, list_ids, 0, list_ids.length);
        


        //TODO for mat list_ids into (1,2,3)
        String formated_list="";
        String template= "'%s',";
        for(int i=0;i<list_ids.length;i++){
              
            formated_list+= String.format(template, list_ids[i]);

        }
        formated_list = formated_list.substring(0, formated_list.length() - 1); //delete the last comma
   
        
        String query= "select * from applications "+
        "WHERE app_id IN (%s) and status='pending' ; ";                

        
        query  = String.format(query, formated_list);
        
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            
            ResultSet rs = stmt.executeQuery(query);
            //
            
            while (rs.next()) {
                String id = rs.getString("app_id");
                
                createBankAccounts(id);
                
            }
            //

            stmt.close();
            
            System.out.println("approved");

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        
    }

    public void printApps(){

        Boolean hasPermission = checkPermission(this.username,this.password);
        if(hasPermission){
            
        }else{
            System.out.println("You must be a admin or employee to do this.");
            return;
        }


        
        String query = "select * from applications";
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(query);
           

            
           
            System.out.println("----------------------------------");
            while (rs.next()) {
                String id = rs.getString("app_id");
                String coowner = rs.getString("coowner_id");
                String owner = rs.getString("owner_id");
                String status = rs.getString("status");
                System.out.println(id+" : "+owner+" : "+coowner +" : "+status+ "\n");
            }
            System.out.println("----------------------------------");
            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        
    }

//get requests based on USERNAME
    public  LinkedList<String>  listRequests(String username,String filter){
        if(filter.equals("pending")){
            filter="and status='pending'";
        }else if(filter.equals("resolved")){
            filter="and status!='pending'";
        }else{
            filter="";
        }
        LinkedList<String> al=new LinkedList<String>();  
        
         
        String query = "SELECT * FROM requests where username='%s'  "+filter;

                      
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            query  = String.format(query, username);
            
            ResultSet rs = stmt.executeQuery(query);
            

            
           
            System.out.println("----------------------------------");
            while (rs.next()) {
                
                
                String id = rs.getString("id");
                String comment = rs.getString("comment");
                String picture = rs.getString("image");
                String status = rs.getString("status");
                String amount = rs.getString("amount");
                
                al.push(amount);
                al.push(comment);
                al.push( picture );
                al.push(status);
                al.push(id);
            }
            System.out.println("----------------------------------");
            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        return al;
    }



    public  LinkedList<String>  getProfile(String username){
     
        LinkedList<String> al=new LinkedList<String>();  
        
         
        String query = "SELECT * FROM user_accounts where username='%s'  ";

                      
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            query  = String.format(query, username);
            
            ResultSet rs = stmt.executeQuery(query);
           

            
           
            System.out.println("----------------------------------");
            while (rs.next()) {
                
                
                
                String realname = rs.getString("realname");
                String email = rs.getString("email");
                String rank = rs.getString("rank");
                
                al.push( rank );
                
                al.push(email);
                al.push(realname);
               
            }
            System.out.println("----------------------------------");
            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        return al;
    }

//list an employees request history
    public  LinkedList<String>  listHistory(String username,String filter){
        if(filter.equals("pending")){
            filter="and status='pending'";
        }else if(filter.equals("resolved")){
            filter="and status!='pending'";
        }else{
            filter="";
        }
        LinkedList<String> al=new LinkedList<String>();  
        
         
        String query = "SELECT * FROM requests where username='%s'  "+filter;

                      
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            query  = String.format(query, username);
            
            ResultSet rs = stmt.executeQuery(query);
           

            
           
            System.out.println("----------------------------------");
            while (rs.next()) {
                
                
                String id = rs.getString("id");
                String name = rs.getString("username");
                String comment = rs.getString("comment");
                String picture = rs.getString("image");
                String status = rs.getString("status");
                String amount = rs.getString("amount");
                String manager = rs.getString("manager");
                al.push(status);
                al.push(manager);
                al.push(amount);
                
                
                al.push(comment);
                al.push( picture );
                
                al.push(name);
                al.push(id);
                
             
		
            }
            System.out.println("----------------------------------");
            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        return al;
    }

//get based on APPROVED/DENIES/PENDING no name
    public  LinkedList<String>  listStatus(String state){
        LinkedList<String> al=new LinkedList<String>();  
        
         
        String query = "SELECT * FROM requests where status='%s' ";

        if(state.equals("resolved")){
            query="SELECT * FROM requests where status!='pending' ";
        }
                      
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            query  = String.format(query, state);
            
            ResultSet rs = stmt.executeQuery(query);
         

            
           
            System.out.println("----------------------------------");
            while (rs.next()) {
                
                
                String id = rs.getString("id");
                String comment = rs.getString("comment");
                String picture = rs.getString("image");
                String status = rs.getString("status");
                String amount = rs.getString("amount");
                String username = rs.getString("username");
                String manager_reviewed = rs.getString("manager");
                
                al.push(manager_reviewed);
                al.push(amount);
                al.push(comment);
                al.push( picture );
                al.push(status);
                al.push(username);
                al.push(id);
            }
            System.out.println("----------------------------------");
            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        return al;
    }


    public  LinkedList<String>  listAllEmployees(){
        LinkedList<String> al=new LinkedList<String>();  
        
         
        String query = "SELECT * FROM user_accounts  ";

        
                      
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            query  = String.format(query);
            
            ResultSet rs = stmt.executeQuery(query);
           

            
           
            System.out.println("----------------------------------");
            while (rs.next()) {
                
                
                
                String rank = rs.getString("rank");    
                String username = rs.getString("username");
                String realname = rs.getString("realname");
                String email = rs.getString("email");

                al.push(username);
                al.push(realname);
                al.push(email);
                al.push( rank );
              
               
            }
            System.out.println("----------------------------------");
            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        return al;
    }

    public String secure(String password){
         //
         String data = password;
          
         MessageDigest messageDigest;
         try {
             messageDigest = MessageDigest.getInstance("MD5");
             messageDigest.update(data.getBytes());
             byte[] messageDigestMD5 = messageDigest.digest();
             StringBuffer stringBuffer = new StringBuffer();
             for (byte bytes : messageDigestMD5) {
                 stringBuffer.append(String.format("%02x", bytes & 0xff));
             }
  
             password=stringBuffer.toString();
         } catch (NoSuchAlgorithmException exception) {
             // TODO Auto-generated catch block
             exception.printStackTrace();
         }
        return password;
    }

    public Boolean login(String username,String password,String rank){
            Boolean result=false;
          //
          String data = password;
          
          MessageDigest messageDigest;
          try {
              messageDigest = MessageDigest.getInstance("MD5");
              messageDigest.update(data.getBytes());
              byte[] messageDigestMD5 = messageDigest.digest();
              StringBuffer stringBuffer = new StringBuffer();
              for (byte bytes : messageDigestMD5) {
                  stringBuffer.append(String.format("%02x", bytes & 0xff));
              }
   
              password=stringBuffer.toString();
          } catch (NoSuchAlgorithmException exception) {
              // TODO Auto-generated catch block
              exception.printStackTrace();
          }
          //

        String query = "select user_id from user_accounts where username='%s' and password='%s' and rank='%s'"; //TODO change to prepared statment
        query  = String.format(query, username,password,rank);
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(query);
           

            
           
            
            if (rs.next()) {
                
                
                String id = rs.getString("user_id");
                System.out.println("logged in as "+username);
                System.out.println("your id is: "+id);
                
                this.username = username;
                this.password = password;
                this.id=id;
                result=true;
            }else{
                System.out.println("username or password was incorrect.");
                result=false;
            }

            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        return result;

    }

    public void logout(){
        this.username="";
        
    }

    public void open(){
        System.out.println(".........");
        
    }

    public void apply(){
        
        
        //check if logged in first
        if(this.username.length()>0){

            
        }else{
            System.out.println("You must login before applying.");
            return;
        }

        //is this a joint account application?
        Scanner myObj = new Scanner(System.in);  
        System.out.println("Is this for a joint account?(y/n):");

        String response = myObj.nextLine(); 
        
        //if yes
        if(response.equals("y")){
            System.out.print("Enter co-owner's username:");

            String co_username = myObj.nextLine(); 

            System.out.print("Enter co-owner's password:");

            String co_password = myObj.nextLine(); 

            //TODO authenticate...
            if(authenticate(co_username, co_password)){
                //
            }else{
                System.out.println("wrong co-owner username or co-owner password.");
                return;
            }
            //
            addNewApplication(this.username,co_username);
            
        }else{
            addNewApplication(this.username);
        }
        
        System.out.println("Your application has been submitted.");
    }

    public Boolean register(String username,String password){
        
        if(usernameExsists(username)){
            System.out.println("that username already exsists.");

            return false;
        }else{
            System.out.println("new user registered.");
        }

        //
        String data = password;
        
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(data.getBytes());
            byte[] messageDigestMD5 = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte bytes : messageDigestMD5) {
                stringBuffer.append(String.format("%02x", bytes & 0xff));
            }
 
         //   System.out.println("data:" + data);
          //  System.out.println("digestedMD5(hex):" + stringBuffer.toString());
            password=stringBuffer.toString();
        } catch (NoSuchAlgorithmException exception) {
            // TODO Auto-generated catch block
            exception.printStackTrace();
        }
        //
           
        addNewUser(username,password);
        return true;
    }

    public void addNewUser(String username,String password){
        String query = " insert into user_accounts(username,password,rank) values ('%s','%s','employee');"; //TODO change to prepared statment
        query  = String.format(query, username,password);
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            stmt.executeUpdate(query);
    
            stmt.close();
            

        }catch(Exception  e){
            System.err.format("ERROR line 835: \n%s\n", e.getMessage());
        }finally{
            
        }

        
    }

    public void addNewApplication(String owner,String coowner){
        String query= "INSERT INTO applications(owner_id,coowner_id,status) VALUES "+
                      "  ((SELECT user_id FROM user_accounts WHERE username='%s'),"+
                      " (SELECT user_id FROM user_accounts  WHERE username='%s'),'pending');";                

        
        query  = String.format(query, owner,coowner);
        
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            stmt.executeUpdate(query);
    
            stmt.close();
            

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        
    }


    public void addNewRequest(String username,String amount,String comment, String image){
        String query= "INSERT INTO requests(username,amount,comment,image,status) VALUES "+
                      " ('%s','%s','%s','%s','pending');";                

                      
        query  = String.format(query, username,amount,comment,image);
        System.out.println(query);
        System.out.println(query);
        System.out.println(query);
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            stmt.executeUpdate(query);
    
            stmt.close();
            

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        
    }

    //overloaded
    public void addNewApplication(String owner){
        String query= "INSERT INTO applications(owner_id,status) VALUES "+
                      "  ((SELECT user_id FROM user_accounts WHERE username='%s'),'pending')";                

        
        query  = String.format(query, owner);
        
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            stmt.executeUpdate(query);
    
            stmt.close();
            

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        
    }

    public Boolean authenticate(String username,String password){

        //
        String data = password;
          
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(data.getBytes());
            byte[] messageDigestMD5 = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte bytes : messageDigestMD5) {
                stringBuffer.append(String.format("%02x", bytes & 0xff));
            }
 
            password=stringBuffer.toString();
        } catch (NoSuchAlgorithmException exception) {
            // TODO Auto-generated catch block
            exception.printStackTrace();
        }
        //



        String query = "select user_id from user_accounts where username='%s' and password='%s'"; //TODO change to prepared statment
        query  = String.format(query, username,password);
        Statement stmt; 
        Boolean result=false;
        try{
            
            stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(query);
           

            
           
            
            if (rs.next()) {
                
                
              
                
                result=true;
                
            }else{
                result=false;
            }

            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        return result;

    }


    public Boolean checkPermission(String username,String password){


        String query = "select status from user_accounts where username='%s' and password='%s'"; //TODO change to prepared statment
        query  = String.format(query, username,password);
        Statement stmt; 
        Boolean result=false;
        
        try{
            
            stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(query);
          

            
        
            
            if (rs.next()) {
                
                
                String status = rs.getString("status");
                
                if(status.equals("admin") || status.equals("employee")){
                    result=true;
                }

                
                
            }else{
                result=false;
                
            }

            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }
        
        return result;

    }


    public String getRank(String username,String password){
        password=secure(password);
        String rank = "";
        String query = "select rank from user_accounts where username='%s' and password='%s'"; //TODO change to prepared statment
        query  = String.format(query, username,password);
        Statement stmt; 
        
        
        try{
            
            stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(query);
          

            
            if (rs.next()) {
              
                 rank = rs.getString("rank");
   
            }else{
                rank="none";
                
            }

            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }
        
        return rank;

    }

    public Boolean usernameExsists(String username){
        boolean result=false;
        String query = "select user_id from user_accounts where username='%s' "; //TODO change to prepared statment
        query  = String.format(query, username);
        Statement stmt; 
        try{
            
            stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery(query);
         

            
           
            
            if (rs.next()) {
                
                
                
                
                result=true;
                //this.login_name=username;
            }else{
                //
            }

            stmt.close();
            rs.close();

        }catch(Exception  e){
            System.err.format("ERROR: \n%s\n", e.getMessage());
        }finally{
            
        }

        return result;

    }
    
} 


