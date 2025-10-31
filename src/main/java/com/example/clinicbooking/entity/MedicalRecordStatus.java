package com.example.clinicbooking.entity;

public enum MedicalRecordStatus {
    WAITING,            //Chờ khám
    IN_PROGRESS,        //Đang khám
    PENDING_PREPAYMENT, //Chờ thanh toán trước
    PENDING_RESULTS,    //Chờ kết quả
    PENDING_APPROVAL,   //Chờ duyệt
    PENDING_POSTPAYMENT, //Chờ thanh toán sau
    COMPLETED,          //Hoàn thành
}
