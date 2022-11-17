package com.example.waterplan.dto;

public class WaterPlanDTO {
    String waterTime; // 물을 먹은 시간
    String eatingWater; // 먹은 물의 양

    public WaterPlanDTO(String waterTime, String eatingWater) {
        this.waterTime = waterTime;
        this.eatingWater = eatingWater;
    }

    public String getWaterTime() {
        return waterTime;
    }

    public void setWaterTime(String waterTime) {
        this.waterTime = waterTime;
    }

    public String getEatingWater() {
        return eatingWater;
    }

    public void setEatingWater(String eatingWater) {
        this.eatingWater = eatingWater;
    }
}


