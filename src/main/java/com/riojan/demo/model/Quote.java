package com.riojan.demo.model;



public class Quote {

    private String author;
    private String quote;
    private String cat;
    private String id;
    private String content;
    private String title;

    public static final Quote EMPTY_QUOTE= new Quote();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String toString(){

        String result = "";
        if (this.content!=null){
            result = "<br/><br/>--<br/>«" +
                    this.quote +
                    "»." +
                    " By " +
                    this.author +
                    ".<br/>";
        }
        return result;
    }
}
