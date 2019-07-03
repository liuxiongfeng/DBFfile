package nl.knaw.dans.common.dbflib.example.vo;

/**
 * @program: DBFfile
 * @description: 证券账户查询入参VO
 * @author: liuxiongfeng
 * @create: 2019-07-02 19:37
 **/
public class SFXXHCDTO {
    private String ywlsh;
    private String ywlb;
    private String zjlb;
    private String zjdm;
    private String khmc;
    private String khjgdm;
    private String khwddm;
    private String sqrq;

    public SFXXHCDTO(String ywlsh, String ywlb, String zjlb, String zjdm, String khmc, String khjgdm, String khwddm, String sqrq) {
        this.ywlsh = ywlsh;
        this.ywlb = ywlb;
        this.zjlb = zjlb;
        this.zjdm = zjdm;
        this.khmc = khmc;
        this.khjgdm = khjgdm;
        this.khwddm = khwddm;
        this.sqrq = sqrq;
    }

    public String getYwlsh() {
        return ywlsh;
    }

    public void setYwlsh(String ywlsh) {
        this.ywlsh = ywlsh;
    }

    public String getYwlb() {
        return ywlb;
    }

    public void setYwlb(String ywlb) {
        this.ywlb = ywlb;
    }

    public String getZjlb() {
        return zjlb;
    }

    public void setZjlb(String zjlb) {
        this.zjlb = zjlb;
    }

    public String getZjdm() {
        return zjdm;
    }

    public void setZjdm(String zjdm) {
        this.zjdm = zjdm;
    }

    public String getKhmc() {
        return khmc;
    }

    public void setKhmc(String khmc) {
        this.khmc = khmc;
    }

    public String getKhjgdm() {
        return khjgdm;
    }

    public void setKhjgdm(String khjgdm) {
        this.khjgdm = khjgdm;
    }

    public String getKhwddm() {
        return khwddm;
    }

    public void setKhwddm(String khwddm) {
        this.khwddm = khwddm;
    }

    public String getSqrq() {
        return sqrq;
    }

    public void setSqrq(String sqrq) {
        this.sqrq = sqrq;
    }
}
