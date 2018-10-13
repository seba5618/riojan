package com.riojan.demo.model;

import java.util.List;

public class MailModel {

    private String body;
    private String subject;
    private List<String> to;
    private List<String> cc;
    private List<String> bbc;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBbc() {
        return bbc;
    }

    public void setBbc(List<String> bbc) {
        this.bbc = bbc;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("to: ");
        sb.append(this.to);
        sb.append("\nbody: ");
        sb.append(this.body);
        sb.append("\nsubject: ");
        sb.append(this.subject);
        sb.append("\ncc: ");
        sb.append(this.cc);
        return sb.toString();
    }



    public void validate (){

    }
}
