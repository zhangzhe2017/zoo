package weixin.zoo.infrastructure.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import weixin.zoo.infrastructure.model.Register;
import weixin.zoo.infrastructure.model.RegisterExample;

public interface RegisterMapper {
    int countByExample(RegisterExample example);

    int deleteByExample(RegisterExample example);

    int insert(Register record);

    int insertSelective(Register record);

    List<Register> selectByExample(RegisterExample example);

    int updateByExampleSelective(@Param("record") Register record, @Param("example") RegisterExample example);

    int updateByExample(@Param("record") Register record, @Param("example") RegisterExample example);
}