package com.backend.project_management.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthlyTaskCountDTO {
//    private long total;
//    private long completed;
//    private long inProgress;
//    private long delay;
//    private long onHold;

    private int day;
    private String label;

    public MonthlyTaskCountDTO(int day, long count) {
        this.day = day;
        this.label = "Day " + day + ": " + count + (count == 1 ? " task" : " tasks");
    }

    public int getDay() { return day; }
    public String getLabel() { return label; }
}

