package weixin.zoo.utils;

/**
 * Created by viczhang.zhangz on 2017/5/26.
 */
public enum ActivityTypeEnum {

    ACTIVITY("activity",1),VOTE("vote",2),ACTIVITY_PAY("activity_pay",3),QUESTION("question",4);

    private String name;
    private int index;

    ActivityTypeEnum(String name, int index){
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
