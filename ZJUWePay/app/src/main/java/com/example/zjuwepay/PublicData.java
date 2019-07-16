package com.example.zjuwepay;

import android.content.Context;

public class PublicData {

    private static String current_user_name, feedback, current_user_description;
    private static boolean log_state;
    private static int user_face = 0, current_user_id = 0;

    //cache for furn detail
    private static String furnDetailButtonId;

    //cache for register
    private static String regNameTemp, regPwd1Tmp, regPwd2Temp, regPhoneTemp, regEmailTemp, regDescriptionTemp;

    //cache for button binding
    private static String buttonIdTemp = "未选择按钮", buttonNameTemp = "未选择按钮";
    private static String furnitureIdTemp = "未选择家具", furnitureNameTemp = "未选择家具";
    private static String furnitureTypeTemp = "0";
    private static String comId, comAmount = "", comPhone, comAddress, comSendName, comName, comPrice = "", comImg;

    //cache for item info
    private static String itemImgUrlStr, itemName, itemId, itemPrice, itemStack, itemDescription;

    //cache for order info
    private static String orderImgUrlStr, orderName, orderId, orderPrice, orderDes, orderDate, orderAmount, orderState;

    public static String getFurnDetailButtonId() {
        return furnDetailButtonId;
    }

    public static void setFurnDetailButtonId(String id) {
        furnDetailButtonId = id;
    }

    public static String getOrderAmount() {
        return orderAmount;
    }

    public static String getOrderDate() {
        return orderDate;
    }

    public static String getOrderDes() {
        return orderDes;
    }

    public static String getOrderId() {
        return orderId;
    }

    public static String getOrderImgUrlStr() {
        return orderImgUrlStr;
    }

    public static String getOrderName() {
        return orderName;
    }

    public static String getOrderPrice() {
        return orderPrice;
    }

    public static String getOrderState() {
        return orderState;
    }

    public static void setOrderAmount(String orderAmount) {
        PublicData.orderAmount = orderAmount;
    }

    public static void setOrderDate(String orderDate) {
        PublicData.orderDate = orderDate;
    }

    public static void setOrderDes(String orderDes) {
        PublicData.orderDes = orderDes;
    }

    public static void setOrderId(String orderId) {
        PublicData.orderId = orderId;
    }

    public static void setOrderImgUrlStr(String orderImgUrlStr) {
        PublicData.orderImgUrlStr = orderImgUrlStr;
    }

    public static void setOrderName(String orderName) {
        PublicData.orderName = orderName;
    }

    public static void setOrderPrice(String orderPrice) {
        PublicData.orderPrice = orderPrice;
    }

    public static void setOrderState(String orderState) {
        PublicData.orderState = orderState;
    }

    public static void cancelItem() {
        comId = comAmount = comPhone = comAddress = comSendName = comName = comPrice = comImg = "";
    }

    public static void resetItemInfo() {
        itemImgUrlStr = itemName = itemId = itemPrice = itemStack = itemDescription = "";
    }

    public static void sendItemInfo(String amount) {
        comId = itemId;
        comName = itemName;
        comPrice = itemPrice;
        comImg = itemImgUrlStr;
        comAmount = amount;
    }

    public static String getComName() {
        return comName;
    }

    public static void setComName(String name) {
        comName = name;
    }

    public static String getComPrice() {
        return comPrice;
    }

    public static void setComPrice(String price) {
        comPrice = price;
    }

    public static String getComImg() {
        return comImg;
    }

    public static void setComImg(String img) {
        comImg = img;
    }

    public static String getComId() {
        return comId;
    }

    public static void setComId(String id) {
        comId = id;
    }

    public static String getComAmount() {
        return comAmount;
    }

    public static void setComAmount(String amount) {
        comAmount = amount;
    }

    public static String getComPhone() {
        return comPhone;
    }

    public static void setComPhone(String phone) {
        comPhone = phone;
    }

    public static String getComAddress() {
        return comAddress;
    }

    public static void setComAddress(String address) {
        comAddress = address;
    }

    public static String getComSendName() {
        return comSendName;
    }

    public static void setComSendName(String name) {
        comSendName = name;
    }

    public static String getItemImgUrlStr() {
        return itemImgUrlStr;
    }

    public static void setItemImgUrlStr(String url) {
        itemImgUrlStr = url;
    }

    public static String getItemName() {
        return itemName;
    }

    public static void setItemName(String name) {
        itemName = name;
    }

    public static String getItemId() {
        return itemId;
    }

    public static void setItemId(String id) {
        itemId = id;
    }

    public static String getItemPrice() {
        return itemPrice;
    }

    public static void setItemPrice(String price) {
        itemPrice = price;
    }

    public static String getItemStack() {
        return itemStack;
    }

    public static void setItemStack(String stack) {
        itemStack = stack;
    }

    public static String getItemDescription() {
        return itemDescription;
    }
    public static void setItemDescription(String description) {
        itemDescription = description;
    }

    public static String getFurnitureIdTemp() {
        return furnitureIdTemp;
    }

    public static void setFurnitureIdTemp(String furnitureId) {
        furnitureIdTemp = furnitureId;
    }

    public static String getFurnitureNameTemp() {
        return furnitureNameTemp;
    }

    public static void setFurnitureNameTemp(String furnitureName) {
        furnitureNameTemp = furnitureName;
    }

    public static String getFurnitureTypeTemp() {
        return furnitureTypeTemp;
    }

    public static void setFurnitureTypeTemp(String furnitureType) {
        furnitureTypeTemp = furnitureType;
    }

    public static String getButtonIdTemp() {
        return buttonIdTemp;
    }

    public static void setButtonIdTemp(String buttonId) {
        buttonIdTemp = buttonId;
    }

    public static String getButtonNameTemp() {
        return buttonNameTemp;
    }

    public static void setButtonNameTemp(String buttonName) {
        buttonNameTemp = buttonName;
    }

    public static int getCurrentUserId() {
        return current_user_id;
    }

    public static void setCurrentUserId(int id) {
        current_user_id = id;
    }

    public static String getRegNameTemp() {
        return regNameTemp;
    }

    public static void setRegNameTemp(String name) {
        regNameTemp = name;
    }

    public static String getRegPwd1Tmp() {
        return regPwd1Tmp;
    }

    public static void setRegPwd1Tmp(String pwd1) {
        regPwd1Tmp = pwd1;
    }

    public static String getRegPwd2Temp() {
        return regPwd2Temp;
    }

    public static void setRegPwd2Temp(String pwd2) {
        regPwd2Temp = pwd2;
    }

    public static String getRegPhoneTemp() {
        return regPhoneTemp;
    }

    public static void setRegPhoneTemp(String phone) {
        regPhoneTemp = phone;
    }

    public static String getRegEmailTemp() {
        return regEmailTemp;
    }

    public static void setRegEmailTemp(String email) {
        regEmailTemp = email;
    }

    public static String getRegDescription() {
        return regDescriptionTemp;
    }

    public static void setRegDescriptionTemp(String des) {
        regDescriptionTemp = des;
    }

    public static String getCurrentUserName() {
        if(current_user_name == null) {
            return "未登录";
        } else {
            return current_user_name;
        }
    }

    public static void setCurrentUserName(String name) {
        current_user_name = name;
    }

    public static String getCurrentUserDescription() {
        return current_user_description;
    }

    public static void setCurrentUserDescription(String des) {
        current_user_description = des;
    }

    public static int getUserFace() {
        return user_face;
    }

    public static void setUserFace(int face_id) {
        user_face = face_id;
    }

    public static String getFeedback() {
        return feedback;
    }

    public static void setFeedback(String msg) {
        feedback = msg;
    }

    public static boolean getLogState() {
        return log_state;
    }

    public static void setLogState(boolean state) {
        log_state = state;
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
