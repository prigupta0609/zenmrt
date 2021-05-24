### Welcome to MRT System!

#### Problem Statement
You are provided data on the stations and lines of Singapore's urban rail system, including planned additions 
over the next few years. Your task is to use this data to build a routing service, to help users find routes 
from any station to any other station on this future network.
The app should expose an API to find and display one or more routes from a specified origin to a specified 
destination, ordered by some efficiency heuristic. Routes should have one or more steps, 
like "Take [line] from [station] to [station]" or "Change to [line]". You may add other relevant information 
to the results.

#### Achievements
The API is capable of returning only one route which is the shortest part based on number of stations. This path
will remain consistent, no matter how many times the same request has been sent.

#### Considerations
1. The data is in CSV file having format : StationCode,StationName,dd/MM/yyyy. Any mismatch in the format will not start the server. For reference, check data in /src/main/java/resources/StationMap.csv file.
2. The shortest route has been calculated based on number of stations.
3. There is only 1 shortest route.
4. Metro map (graph) is getting created every time a new request comes in based on the requested date, just for the sake of easy calculations. This can be improved to have a constant map.
5. The data is getting loaded in server on application startup. If data changes, server needs to be re-started.
6. API has been developed keeping in mind only 1 request at a time but this can certainly be extended to multi-users in a concurrent environment.
7. Every station mentioned in the CSV must have at least 1 neighbor.
8. Connected and non-connected station lines has been taken into scope.
9. Every station mentioned in CSV is unique or in other terms, every line of CSV is a unique station.
10. Stations with same name but different station codes has been considered as junctions.

#### Design
1. API follows MVC design pattern having 1 Controller, 1 Service, 1 Repository.
2. There are 2 HTTP REST API end-points - GET /isActive (to check if service and up and running) and GET /route?from=XXX&to=YYY&date=dd/MM/yyyy (to fetch the shortest route).
3. Date is optional parameter in request.
4. Both the end-points return response in String format.
5. The map data is being organized in graphical format.
6. Dijkstra algorithm has been used to calculate the shortest distance between origin and destination.
7. All the external models exposed through REST API end-points goes in contract folder.
8. Internal models which are used to carry out internal operations goes in model folder.
9. Other common stuff like Errors, Constants, Validator goes in common folder.
10. All the search algorithm and graph creation method goes in routesearch folder.

#### Instructions
1. Maven version - 3.6.3 or above
2. Java version - 1.8 or above
3. Framework - spring-boot 2.4.0
3. Compile and build using **mvn clean install** on zenmrt folder.
4. Following are the ways to run the service:
````
    (a)Run using **mvn spring-boot:run** on zenmrt folder. 
    (b)Run using **java -jar target/zenmrt-1.0-SNAPSHOT.jar** on zenmrt folder.
    (c)Run bash script on zenmrt folder **bash bin/zenmrt**. This command will build and run the service together.
````
5. Execute test cases using **mvn test** on zenmrt folder.

## Sample request url:
1. http://localhost:8080/isActive -> ````MRT Router is active````
2. http://localhost:8080/route?from=Buangkok&to=Geylang%20Bahru&date=24/05/2018
````
Travel from Buangkok to Geylang Bahru
Stations travelled : 10
Route : ('NE15', 'NE14', 'NE13', 'NE12', 'CC13', 'CC12', 'CC11', 'CC10', 'DT26', 'DT25', 'DT24')

Take NE line from Buangkok to Hougang
Take NE line from Hougang to Kovan
Take NE line from Kovan to Serangoon
Change from NE line to CC line
Take CC line from Serangoon to Bartley
Take CC line from Bartley to Tai Seng
Take CC line from Tai Seng to MacPherson
Change from CC line to DT line
Take DT line from MacPherson to Mattar
Take DT line from Mattar to Geylang Bahru
````