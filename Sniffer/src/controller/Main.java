package controller;

import view.SelectView;

public class Main {

    public static void main(String[] args){
        SelectView selectView = SelectView.getInstance();
        selectView.draw();
    }

}
