package com.example.fsoft_shopee_nhom02.controller;


import com.example.fsoft_shopee_nhom02.dto.DayStatisticDTO;
import com.example.fsoft_shopee_nhom02.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    OrderServiceImpl orderService;
    @GetMapping("/year")
    public Map<String, Long> getStatisticByYear(){
        Map<String, Long> monthly = new HashMap<>();
        for (int i = 1; i< 13; i++)
        {
            monthly.put("Month "+i, orderService.getAllOrderByMonth(i+""));
        }
        return monthly;
    }

    @GetMapping("/reveune")
    public Long getRevenue(){
        return orderService.getTurnOver();
    }
    @GetMapping("/day")
    public Long getStatisticByDay(@RequestBody DayStatisticDTO dayStatisticDTO){
        return orderService.getAllOrderByDay(dayStatisticDTO.getYear(), dayStatisticDTO.getMonth(), dayStatisticDTO.getDay());
    }

    @GetMapping("/totalorder")
    public Long getTotalOrder(){
        return orderService.getTotalOrder();
    }


}
