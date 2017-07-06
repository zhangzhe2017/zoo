package weixin.zoo.infrastructure.model;

/**
 * Created by viczhang.zhangz on 2017/7/6.
 */
public class PageDto {

    private Integer limit;

    private Integer offset;

    public PageDto(Integer offset,Integer limit) {
        this.limit = limit;
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
