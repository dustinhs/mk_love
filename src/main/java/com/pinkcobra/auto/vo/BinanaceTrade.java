package com.pinkcobra.auto.vo;

import lombok.Data;

@Data
public class BinanaceTrade {

    String symbol;
    long   timestamp;
    String tradeYmd;
    String TradeTm;
    float  price;
    float  quantity;


}
