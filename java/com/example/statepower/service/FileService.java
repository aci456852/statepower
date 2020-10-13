package com.example.statepower.service;

import com.example.statepower.DataMapper;
import com.example.statepower.bean.Record;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    @Autowired
    DataMapper dataMapper;

    public List<Record> listALL(){
        return dataMapper.listALL();
    }

    public List<Record> listTime(String time1,String time2){
        return dataMapper.listTime(time1,time2);
    }

    public void Insert(int rnumber,String ritem,String rseverity,String rspecific,String rproblem, byte[] rimg){
        dataMapper.Insert(rnumber,ritem,rseverity,rspecific,rproblem,rimg);
        return;
    }

}
