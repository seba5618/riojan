package com.riojan.demo.model;

import com.sendgrid.Email;

import java.util.Collections;
import java.util.List;

public class MailModel {

    private String body;
    private String subject;
    private boolean enrich;
    private List<Email> to = Collections.emptyList();
    private List<Email> cc = Collections.emptyList();
    private List<Email> bbc = Collections.emptyList();

    public MailModel() {
    }

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

    public List<Email> getTo() {
        return to;
    }

    public void setTo(List<Email> to) {
        this.to = to;
    }

    public List<Email> getCc() {
        return cc;
    }

    public void setCc(List<Email> cc) {
        this.cc = cc;
    }

    public List<Email> getBbc() {
        return bbc;
    }

    public void setBbc(List<Email> bbc) {
        this.bbc = bbc;
    }

    public boolean isEnrich() {
        return enrich;
    }

    public void setEnrich(boolean enrich) {
        this.enrich = enrich;
    }

    public String toString(){
        String sb = "To: " +
                this.to +
                "\nCc: " +
                this.cc +
                "\nBbc: " +
                this.bbc +
                "\nSubject: " +
                this.subject +
                "\nBody: " +
                this.body;
        return sb;
    }
}
