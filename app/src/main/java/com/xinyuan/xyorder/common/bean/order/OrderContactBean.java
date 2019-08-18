package com.xinyuan.xyorder.common.bean.order;

import java.io.Serializable;

/**
 * <p>
 * Description：订单联系人
 * </p>
 *
 * @author hq
 * @CreateDate 2017/10/17
 */
public class OrderContactBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String address;
    private String contactName;
    private String contactPhone;
    private String gender;// 性別 = ['MALE', 'FEMALE', 'UNKNOW'],
    private Double latitude;
    private Double longitude;
    private long orderId;
    private long contactId;// 联系人id ,
    private boolean isCheck;
    private String houseNumber;
    private String simpleAddress;
    private String detailAddress;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public long getContactId() {
        return contactId;
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

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
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
}
