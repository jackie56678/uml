package com.example.backend.healthFile;

import org.springframework.stereotype.Service;

@Service
public class HealthRecordService {

    private final HealthRecordMapper healthRecordMapper;

    public HealthRecordService(HealthRecordMapper healthRecordMapper) {
        this.healthRecordMapper = healthRecordMapper;
    }

    public HealthRecord getHealthRecordByUid(int uid) {
        return healthRecordMapper.getHealthRecordByUid(uid);
    }

    public boolean updateHealthRecord(HealthRecord healthRecord) {
        return healthRecordMapper.updateHealthRecord(healthRecord) > 0;
    }
}
