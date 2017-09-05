package com.prototype.dao;


import com.prototype.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface carMessageDao {
    //车辆基本信息

    public List<carMessage> queryCarMessage(String licenseId);

    public void addCarMessage(carMessage carmessage);

    public void deleteCarMessage();

    public void updateCarMapping();

    //车辆事故

    public accidentRecord queryAccident(int accidentId);
    public List<accidentRecord>  queryAllByText(String accidentText);

    //车辆年审

    public annualRecord queryAnnual(int annualId);


    //车辆保险

    public insuranceRecords queryInsurance(int insuranceId);


    //车辆车型

    public carType queryCarType(int typeId);
    public List<carType> queryAll();

    public List<carType>  queryAllByBrand(String brand);

    //车辆保养

    public maintenanceRecord queryMaintenance(int maintenanceId);

}