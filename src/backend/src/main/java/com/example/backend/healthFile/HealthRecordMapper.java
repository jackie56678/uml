package com.example.backend.healthFile;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface HealthRecordMapper {
    @Select("SELECT * FROM HealthRecord WHERE uid = #{uid}")
    HealthRecord getHealthRecordByUid(int uid);

    @Update("UPDATE HealthRecord SET name=#{name}, gender=#{gender}, age=#{age}, height=#{height}, " +
            "weight=#{weight}, bloodType=#{bloodType}, allergies=#{allergies}, medicalHistory=#{medicalHistory}, " +
            "medications=#{medications}, smokingStatus=#{smokingStatus}, alcoholUse=#{alcoholUse}, " +
            "physicalActivity=#{physicalActivity}, diet=#{diet}, bloodPressure=#{bloodPressure}, " +
            "cholesterolLevel=#{cholesterolLevel}, glucoseLevel=#{glucoseLevel} WHERE uid=#{uid}")
    int updateHealthRecord(HealthRecord healthRecord);
}
