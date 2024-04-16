package com.yxq.carpark.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class IncomeDataVo implements Serializable {
    private int id;
    private double money;
    private int  method;
    private int type;
    private String carnum;
    private String cardnum;
    private int isillegal;
    private int source=1;
    private String time;
    private String duration;
}
