package com.zendesk.repository;

import com.zendesk.common.Errors;
import com.zendesk.model.Station;
import com.zendesk.model.StationCode;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class DBInit {

    private static final Logger logger = LoggerFactory.getLogger(DBInit.class);

    @Value("${dataFilePath}")
    private String CSV_FILE_LOCATION;

    @Autowired
    private RouteRepository routeRepository;

    @PostConstruct
    private void populateRepo() {
        logger.info("Starting data loading");
        List<Station> stationList = readCSV();
        routeRepository.getStationList().addAll(stationList);
        logger.info("Loaded data for " + stationList.size() + " stations");
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
                StationCode stationCode = new StationCode(data[0].trim());
                String name = data[1].trim();
                LocalDateTime date = getDate(data[2].trim());
                Station station = new Station(name, stationCode, date);
                stationList.add(station);
            }
            csvReader.close();
            return stationList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDateTime getDate(String dateString) {
        try {
            String[] params = dateString.split("/");
            int day = Integer.parseInt(params[0]);
            int month = Integer.parseInt(params[1]);
            int year = Integer.parseInt(params[2]);
            return new LocalDateTime(year, month, day, 0, 0, 0);
        } catch (NumberFormatException e) {
            // TODO : Cover this in Unit test
            logger.error(e.toString());
            throw new RuntimeException(Errors.UNABLE_TO_PARSE_FILE.getMessage(), e);
        }
    }
}
