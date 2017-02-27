package com.tacops;

/**
 * Created by Erik on 2/20/2017.
 */

public class Client{
    //Class member data
    private String client_id;
    private String client_name;
    private String team;

    public Client() {}

    public Client(String client_id, String client_name) {
        this.client_id = client_id;
        this.client_name = client_name;
    }

    public String getClientId(){
      return client_id;
    }

    public String getClientName(){
        return client_name;
    }

    public String getTeam(){
        return team;
    }

    public void setClientId(String client_id){
        this.client_id = client_id;
    }

    public void setClientName(String client_name){
        this.client_name = client_name;
    }

    public void setTeam(String team){
        this.team = team;
    }

} // End Client class


