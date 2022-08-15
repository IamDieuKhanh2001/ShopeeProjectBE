package com.example.fsoft_shopee_nhom02.controller;


import com.example.fsoft_shopee_nhom02.config.PaymentConfig;
import com.example.fsoft_shopee_nhom02.dto.DayStatisticDTO;
import com.example.fsoft_shopee_nhom02.dto.DayToDayDTO;
import com.example.fsoft_shopee_nhom02.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    OrderServiceImpl orderService;
    @GetMapping("/year")
    public Map<String, String> getStatisticByYear(){
        Map<String, String> monthly = new HashMap<>();
        PaymentConfig.months.stream().forEach(e -> {
            if (orderService.getAllOrderByMonth(e) == null)
                monthly.put("Month "+e, "0");
            monthly.put("Month "+e, orderService.getAllOrderByMonth(e));
        });
        return monthly;
    }

    @GetMapping("/month")
    public String getStatisticByMonth(@RequestBody String month){
        if (orderService.getAllOrderByMonth(month) == null)
        {
            return "Turnover: 0";
        }
        return "Turnover: "+ orderService.getAllOrderByMonth(month);
    }

    @GetMapping("/reveune")
    public String getRevenue(){
        return String.valueOf(orderService.getTurnOver());
    }
    @GetMapping("/day")
    public String getStatisticByDay(@RequestBody DayStatisticDTO dayStatisticDTO){
        if (orderService.getAllOrderByDay(dayStatisticDTO.getYear(), dayStatisticDTO.getMonth(), dayStatisticDTO.getDay()) == null)
        {
            return "0";
        }
        return orderService.getAllOrderByDay(dayStatisticDTO.getYear(), dayStatisticDTO.getMonth(), dayStatisticDTO.getDay());
    }

    @GetMapping("/totalorder")
    public String getTotalOrder(){
        return String.valueOf(orderService.getTotalOrder());
    }


    @GetMapping("/fromdaytoday")
    public String getStatisticFromDayToDay(@RequestBody DayToDayDTO day){
        if (orderService.getAllOrderFromDayToDay(day.getYear(), day.getMonth(), day.getDay(), day.getYearE(), day.getMonthE(), day.getDayE()) == null)
        {
            return "0";
        }
        return orderService.getAllOrderFromDayToDay(day.getYear(), day.getMonth(), day.getDay(), day.getYearE(), day.getMonthE(), day.getDayE());
    }
}
