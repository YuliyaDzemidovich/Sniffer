package controller;

import java.util.ArrayList;

public class SnifferConfig {
    private boolean onHTTPProtocol = false;
    private boolean onThe2ndProtocol = false;
    private boolean onThe3dProtocol = false;

    private static SnifferConfig snifferConfig = new SnifferConfig();
    private ArrayList<Boolean> filters = new ArrayList<Boolean>();

    private SnifferConfig(){
        filters.add(onHTTPProtocol);
        filters.add(onThe2ndProtocol);
        filters.add(onThe3dProtocol);
    }

    public static SnifferConfig getInstance(){
        return snifferConfig;
    }

    public boolean isOnHTTPProtocol(){
        return onHTTPProtocol;
    }

    public void setOnHTTPProtocol(boolean ONHTTPProtocol){
        this.onHTTPProtocol = ONHTTPProtocol;
    }

    public boolean isOnThe2ndProtocol(){
        return onThe2ndProtocol;
    }

    public void setOnThe2ndProtocol(boolean the2ndProtocol){
        this.onThe2ndProtocol = the2ndProtocol;
    }

    public boolean isOnThe3dProtocol(){
        return onThe3dProtocol;
    }

    public void setOnThe3dProtocol(boolean the3dProtocol){
        this.onThe3dProtocol = the3dProtocol;
    }

}
