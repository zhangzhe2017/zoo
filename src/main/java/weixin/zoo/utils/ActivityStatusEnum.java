package weixin.zoo.utils;

/**
 * Created by viczhang.zhangz on 2017/5/26.
 *
 * 活动状态枚举
 */
public enum ActivityStatusEnum {

    ATTEND("attend",1), PAY("pay",2), CANCEL("cancel",3), REFUND("refund",4);

    private String name;
    private int index;

    private ActivityStatusEnum(String name, int index){
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
