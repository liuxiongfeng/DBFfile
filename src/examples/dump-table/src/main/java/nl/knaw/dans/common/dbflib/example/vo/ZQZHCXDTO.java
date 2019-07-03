package nl.knaw.dans.common.dbflib.example.vo;

/**
 * @program: DBFfile
 * @description: 证券账户查询入参VO
 * @author: liuxiongfeng
 * @create: 2019-07-02 19:37
 **/
public class ZQZHCXDTO {
    private String ywlsh;
    private String khmc;
    private String zjlb;
    private String zjdm;
    private String ymth;
    private String zhlb;
    private String zqzh;
    private String khjgdm;
    private String khwddm;
    private String sqrq;

    public ZQZHCXDTO(String ywlsh, String khmc, String zjlb, String zjdm, String ymth, String zhlb, String zqzh, String khjgdm, String khwddm, String sqrq) {
        this.ywlsh = ywlsh;
        this.khmc = khmc;
        this.zjlb = zjlb;
        this.zjdm = zjdm;
        this.ymth = ymth;
        this.zhlb = zhlb;
        this.zqzh = zqzh;
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

    public String getKhmc() {
        return khmc;
    }

    public void setKhmc(String khmc) {
        this.khmc = khmc;
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

    public String getYmth() {
        return ymth;
    }

    public void setYmth(String ymth) {
        this.ymth = ymth;
    }

    public String getZhlb() {
        return zhlb;
    }

    public void setZhlb(String zhlb) {
        this.zhlb = zhlb;
    }

    public String getZqzh() {
        return zqzh;
    }

    public void setZqzh(String zqzh) {
        this.zqzh = zqzh;
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
