package com.xinyuan.xyorder.common.bean.mine;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * Description：
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/14
 */
public class AddressInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String address;
    private String contactName;
    private String contactPhone;
    private String gender;
    private Double latitude;
    private Double longitude;
    private long contactId;
    private String houseNumber;
    private String simpleAddress;
    private String detailAddress;

    public AddressInfo(){

    }
    public AddressInfo(String address, Double latitude, Double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AddressInfo(String address, Double latitude, Double longitude,String detailAddress, String simpleAddress) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detailAddress = detailAddress;
        this.simpleAddress = simpleAddress;
    }

    public AddressInfo(String address, String contactName, String contactPhone, String gender, Double latitude, Double longitude, long contactId, String houseNumber,String detailAddress,String simpleAddress) {
        this.address = address;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.gender = gender;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contactId = contactId;
        this.houseNumber = houseNumber;
        this.detailAddress = detailAddress;
        this.simpleAddress = simpleAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getSimpleAddress() {
        return simpleAddress;
    }

    public void setSimpleAddress(String simpleAddress) {
        this.simpleAddress = simpleAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    @Override
    public String toString() {
        return "AddressInfo{" +
                "address='" + address + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", gender='" + gender + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", contactId=" + contactId +
                ", houseNumber='" + houseNumber + '\'' +
                ", simpleAddress='" + simpleAddress + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                '}';
    }
}
