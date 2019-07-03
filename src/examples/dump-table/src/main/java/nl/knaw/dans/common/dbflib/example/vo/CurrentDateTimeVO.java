package nl.knaw.dans.common.dbflib.example.vo;

/**
 * @program: DBFfile
 * @description: 当前的时间
 * @author: liuxiongfeng
 * @create: 2019-07-03 01:54
 **/
public class CurrentDateTimeVO {
    //年月日：YYYYMMDD
    private String ymd;
    //时分秒：HHmmss
    private String hms;

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public String getHms() {
        return hms;
    }

    public void setHms(String hms) {
        this.hms = hms;
    }
}
