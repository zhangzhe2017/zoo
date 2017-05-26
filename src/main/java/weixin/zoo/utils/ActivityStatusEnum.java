package weixin.zoo.utils;

/**
 * Created by viczhang.zhangz on 2017/5/26.
 *
 * 活动状态枚举
 */
public enum ActivityStatusEnum {

    ATTEND("报名",1), PAY("付款",2), CANCEL("取消",3), REFUND("退款",4);

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
