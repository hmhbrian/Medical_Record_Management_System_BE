package com.example.clinicbooking.entity;

public enum MedicalRecordStatus {
    WAITING,            //Chờ khám
    IN_PROGRESS,        //Đang khám
    PENDING_RESULTS,    //Chờ kết quả
    PENDING_APPROVAL,   //Chờ duyệt
    COMPLETED,          //Hoàn thành
}
