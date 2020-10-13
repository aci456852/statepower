package com.example.statepower;

import com.example.statepower.bean.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

//映射Sql，定义接口
@Mapper
@Component
public interface DataMapper {

    //显示
    List<Record> listALL();

    //按照日期查询数据
    List<Record> listTime(@Param("time1") String time1,@Param("time2") String time2);

    //插入
    void Insert(@Param("rnumber") int rnumber,@Param("ritem") String ritem,
                @Param("rseverity") String rseverity,@Param("rspecific") String rspecific,
                @Param("rproblem") String rproblem,@Param("rimg") byte[] rimg);

}
