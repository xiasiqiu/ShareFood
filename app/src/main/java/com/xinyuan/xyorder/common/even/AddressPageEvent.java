package com.xinyuan.xyorder.common.even;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/14
 */
public class AddressPageEvent {
    public static final String EDITE = "EDITE";
    public static final String NOEDITE = "NOEDITE";
    public static final String FALSH = "FALSH";
    public static final String CHOOES = "CHOOES";
    public static final String CHOOSELOCATION = "CHOOSELOCATION";
    private String flag;
    private Object obj;

    public AddressPageEvent(String flag) {
        this.flag = flag;
    }

    public AddressPageEvent(String flag, Object obj) {
        this.flag = flag;
        this.obj = obj;
    }

    public String getFlag() {
        return flag;
    }

    public Object getObj() {
        return obj;
    }
}
