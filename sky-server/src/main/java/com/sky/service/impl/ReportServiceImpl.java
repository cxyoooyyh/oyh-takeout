package com.sky.service.impl;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.User;
import com.sky.mapper.OrderMapper;
import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.service.UserService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author sharkCode
 * @date 2025/5/20 11:18
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderMapper orderMapper;
    /**
     * 用户统计接口
     * @DataOverViewQueryDTO date
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> timeList = getLocalDateList(begin, end);
        // 查询此时用户总数量
        DataOverViewQueryDTO queryDate = new DataOverViewQueryDTO();
        queryDate.setEnd(LocalDateTime.of(timeList.get(0), LocalTime.MIN));
        int curUserCount = userService.queryUserCount(queryDate);
        List<Integer> userCount = new ArrayList<>();
        List<Integer> dayUserCount = new ArrayList<>();
        // 通过日期查询新增用户
        for (LocalDate time : timeList) {
            queryDate.setBegin(LocalDateTime.of(time, LocalTime.MIN));
            queryDate.setEnd(LocalDateTime.of(time, LocalTime.MAX));
            int i = userService.queryUserCount(queryDate);
            dayUserCount.add(i);
            curUserCount += i;
            userCount.add(curUserCount);
        }
        return UserReportVO.builder()
                .dateList(Arrays.toString(timeList.toArray()))
                .newUserList(Arrays.toString(dayUserCount.toArray()))
                .totalUserList(Arrays.toString(userCount.toArray()))
                .build();
    }

    /**
     * 统计 top10 销量
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {

       LocalDateTime beginDateTime =  LocalDateTime.of(begin, LocalTime.MIN);
       LocalDateTime endDateTime = LocalDateTime.of(end, LocalTime.MIN);
       List<GoodsSalesDTO> list = orderMapper.queryTop10(beginDateTime, endDateTime);
       List<String> names = new ArrayList<>();
       List<Integer> numbers = new ArrayList<>();
       for (GoodsSalesDTO salesDTO: list) {
           names.add(salesDTO.getName());
           numbers.add(salesDTO.getNumber());
       }
        return SalesTop10ReportVO.builder()
                .nameList(Arrays.toString(names.toArray()))
                .numberList(Arrays.toString(numbers.toArray()))
                .build();
    }

    @Override
    public TurnoverReportVO turnover(LocalDate begin, LocalDate end) {
        return null;
    }

    @Override
    public OrderStatisticsVO orderStatistics(LocalDate begin, LocalDate end) {
        return null;
    }

    @Override
    public void exportExcel(HttpServletResponse response) {
        // 查询数据库，获取营业数据
        LocalDate beginDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(1);


        // 通过 POI 将数据写入到 Excel 文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("static/运营数据报表模板.xlsx");

        try {
            XSSFWorkbook excel = new XSSFWorkbook(in);
            XSSFSheet sheet = excel.getSheet("Sheet1");
            sheet.getRow(1).getCell(1)
                    .setCellValue("时间" + beginDate + "-" + endDate);
            // 返回输出流到用户
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * get dateList
     * @param begin
     * @param end
     * @return
     */
    private List<LocalDate> getLocalDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> timeList = new ArrayList<>();

        timeList.add(begin);

        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            timeList.add(begin);
        }

        return timeList;
    }
}
