package com.wsoteam.diet.POJOProfile;

import java.io.Serializable;

public class TrackInfo implements Serializable {
    private String tt;
    private String tn;
    private String net;
    private String cam;
    private String adg;
    private String cre;
    private String cl;
    private String adid;

    public TrackInfo() {
    }

    public TrackInfo(String tt, String tn, String net, String cam, String adg, String cre, String cl, String adid) {
        this.tt = tt;
        this.tn = tn;
        this.net = net;
        this.cam = cam;
        this.adg = adg;
        this.cre = cre;
        this.cl = cl;
        this.adid = adid;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getCam() {
        return cam;
    }

    public void setCam(String cam) {
        this.cam = cam;
    }

    public String getAdg() {
        return adg;
    }

    public void setAdg(String adg) {
        this.adg = adg;
    }

    public String getCre() {
        return cre;
    }

    public void setCre(String cre) {
        this.cre = cre;
    }

    public String getCl() {
        return cl;
    }

    public void setCl(String cl) {
        this.cl = cl;
    }

    public String getAdid() {
        return adid;
    }

    public void setAdid(String adid) {
        this.adid = adid;
    }
}
