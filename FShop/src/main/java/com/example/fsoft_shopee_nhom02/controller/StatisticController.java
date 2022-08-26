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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/statistic")
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
        String res = orderService.getAllOrderByMonth(month);
        if (res == null)
        {
            return "Turnover: 0";
        }
        return res;
    }

    @GetMapping("/reveune")
    public String getRevenue(){
        String res = orderService.getTurnOver();
        if (res == null)
        {
            return "0";
        }
        return res;
    }
    @GetMapping("/day")
    public String getStatisticByDay(@RequestBody DayStatisticDTO dayStatisticDTO){
        String res = orderService.getAllOrderByDay(dayStatisticDTO.getYear(), dayStatisticDTO.getMonth(), dayStatisticDTO.getDay());
        if (res == null)
        {
            return "0";
        }
        return res;
    }

    @GetMapping("/total_order")
    public String getTotalOrder(){
        String res = orderService.getTotalOrder();
        if (res == null)
        {
            return "0";
        }
        return res;
    }

    @GetMapping("/total_product")
    public String getTotalProduct(){
        String res = orderService.getTotalProduct();
        if (res == null)
        {
            return "0";
        }
        return res;
    }

    @GetMapping("/total_user")
    public String getTotalUser(){
        String res = orderService.getTotalUsers();
        if (res == null)
        {
            return "0";
        }
        return res;
    }

    @GetMapping("/between")
    public String getStatisticFromDayToDay(@RequestBody DayToDayDTO day){
        String res = orderService.getAllOrderFromDayToDay(day.getDaystart(), day.getDayend());
        if (res == null)
        {
            return "0";
        }
        return res;
    }
}
