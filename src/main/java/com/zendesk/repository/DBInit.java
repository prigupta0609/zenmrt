package com.zendesk.repository;

import com.zendesk.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DBInit {

    private static final Logger logger = LoggerFactory.getLogger(DBInit.class);
    private SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    private static final String CSV_FILE_LOCATION = "src/main/resources/StationMap.csv";

    @Autowired
    private RouteRepository routeRepository;

    @PostConstruct
    private void populateRepo() {
        List<Station> stationList = readCSV();
        routeRepository.getStationList().addAll(stationList);
    }

    public List<Station> readCSV(){
        List<Station> stationList = new ArrayList<>();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(CSV_FILE_LOCATION));
            while (csvReader.ready()) {
                String row = csvReader.readLine().trim();
                if (row.isEmpty()) {
                    break;
                }
                String[] data = row.split(",");
                String code = data[0].trim();
                String name = data[1].trim();
                Date date = getDate(data[2].trim());
                List<String> codeList = new ArrayList<String>();
                codeList.add(code);
                Station station = new Station(name, codeList, date);
                stationList.add(station);
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stationList;
    }

    private Date getDate(String dateString) {
        try {
            String[] params = dateString.split(" ");
            int day = Integer.parseInt(params[0]);
            String month = params[1].substring(0,3);
            int year = Integer.parseInt(params[2]);
            String finalDateString = day+"/"+month+"/"+year;
            return sdf.parse(finalDateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
