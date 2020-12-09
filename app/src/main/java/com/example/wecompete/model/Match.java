package com.example.wecompete.model;

import java.util.UUID;

public class Match {
    private String id = UUID.randomUUID().toString(); //default
    private String matchTime;
    private String winner;
    private String loser;

    public Match(String matchTime, String winner, String loser) {
        this.matchTime = matchTime;
        this.winner = winner;
        this.loser = loser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getLoser() {
        return loser;
    }

    public void setLoser(String loser) {
        this.loser = loser;
    }
}
