package com.example.garbagetruck;

import java.security.PublicKey;

public class RouteInformation {
    String estimeateTime,stationName,routeName,settingTime,situation,situationPosition,packingDay,recyclingDay;

    public String getEstimateTime() {return estimeateTime;}
    public void setEstimateTime(String est) {this.estimeateTime = est;}
    public String getStationName() {return stationName;}
    public void setStationName(String sta) {this.stationName = sta;}
    public String getRouteName() {return routeName;}
    public void setRouteName(String rou) {this.routeName = rou;}
    public String getSettingTime() {return settingTime;}
    public void setSettingTime(String set) {this.settingTime = set;}
    public String getSituation() {return situation;}
    public void setSituation(String sit) {this.settingTime = sit;}
    public String getSituationPosition() {return situationPosition;}
    public void setSituationPosition(String sitp) {this.situationPosition = sitp;}
    public String getPackingDay() {return packingDay;}
    public void setPackingDay(String pac) {this.packingDay = pac;}
    public String getRecyclingDay() {return recyclingDay;}
    public void setRecyclingDay(String rec) {this.recyclingDay = rec;}

}
