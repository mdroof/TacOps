package com.tacops;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Topgun on 3/10/2017.
 */

public class GameProxy {
    //Class member data
    private String description;
    private String name;
    private String game_id;
    private String password;

    private int teamQuantity;
    private int max_players;
    private int score_limit;
    private double time_limit;
    private List<Alert> alert_log;

    public GameProxy() {
    }

    public GameProxy(String description, String name, String game_id,
                int teamQuantity, int max_players, int score_limit, double time_limit) {
        this.description = description;
        this.name = name;
        this.game_id = game_id;
        this.teamQuantity = teamQuantity;
        this.max_players = max_players;
        this.score_limit = score_limit;
        this.time_limit = time_limit;
    }

    public GameProxy(String name){
        this.name = name;
        this.max_players = 16;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTeamQuantity() {
        return teamQuantity;
    }

    public void setTeamQuantity(int teamQuantity) {
        this.teamQuantity = teamQuantity;
    }

    public int getMax_players() {
        return max_players;
    }

    public void setMax_players(int max_players) {
        this.max_players = max_players;
    }

    public int getScore_limit() {
        return score_limit;
    }

    public void setScore_limit(int score_limit) {
        this.score_limit = score_limit;
    }

    public double getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(double time_limit) {
        this.time_limit = time_limit;
    }

    public List<Alert> getAlert_log() {
        return alert_log;
    }

    public void setAlert_log(List<Alert> alert_log) {
        this.alert_log = alert_log;
    }

} // End GameProxy Class
