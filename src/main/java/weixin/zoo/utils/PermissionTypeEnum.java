package weixin.zoo.utils;

/**
 * Created by viczhang.zhangz on 2017/7/31.
 */
public enum PermissionTypeEnum {

    FIRSTCLASS("firstclass",1),SECONDCLASS("secondclass",2);

    private String name;
    private int index;

    PermissionTypeEnum(String name, int index){
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
