package com.seungjun.randomox.db;

public class LetterDBData {

    private int _id = 0;

    private String letter_req_date = "";

    private String letter_req_text = "";

    private String letter_read = "";


    public String getLetter_req_date() {
        return letter_req_date;
    }

    public void setLetter_req_date(String letter_req_date) {
        this.letter_req_date = letter_req_date;
    }

    public String getLetter_req_text() {
        return letter_req_text;
    }

    public void setLetter_req_text(String letter_req_text) {
        this.letter_req_text = letter_req_text;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }


    public String getLetter_read() {
        return letter_read;
    }

    public void setLetter_read(String letter_read) {
        this.letter_read = letter_read;
    }
}
