package com.driver.Repositories;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.*;

public class AirportRepository {

    private TreeMap<String , Airport> Airports = new TreeMap<>();

    private HashMap<Integer, Flight> Flights = new HashMap<>();

    private HashMap<Integer, Passenger> Passengers = new HashMap<>();  // id : passanger


    private HashMap<Integer, Set<Integer> /*HashMap<Integer, Integer>*/> Tickets = new HashMap<>(); // flightid : list of passanger id

    private HashMap<Integer,Integer> revenueMap= new HashMap<>();
    private HashMap<Integer,Integer> paymentMap= new HashMap<>();
//    HashMap<Integer, ArrayList<Integer>> Tic = new HashMap<>();
//    HashMap<Integer, Integer>Kets = new HashMap<>();


    public void addAirport(Airport airport) {
        Airports.put(airport.getAirportName(), airport);
    }


    public String getLargetAirportName() {
        String answer="";
        int ans=0;
        for(String name:Airports.keySet()){
            int co=Airports.get(name).getNoOfTerminals();
            if(co>ans){
                ans=co;
                answer=name;
            }
        }
        return answer;
//        int maxTerminals = 0;
//        String largestAirport = "";
//        for(String key: Airports.keySet()){
//            if(Airports.get(key).getNoOfTerminals() > maxTerminals){
//                maxTerminals = Airports.get(key).getNoOfTerminals();
//                largestAirport = key;
//            }
//        }
//        return largestAirport;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
//        double shortestDuration = Integer.MAX_VALUE;
//        for(Flight flight: Flights.values()){
//            if(flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity)){
//                if(shortestDuration > flight.getDuration()){
//                    shortestDuration = flight.getDuration();
//                }
//            }
//        }
//        if(shortestDuration == Integer.MAX_VALUE)return -1;
//        return shortestDuration;
        double duration=Integer.MAX_VALUE;
        for (Flight flight :Flights.values()){
            if(fromCity.equals(flight.getFromCity()) && toCity.equals(flight.getToCity())){
                if(duration>flight.getDuration()){
                    duration=flight.getDuration();
                }
            }
        }
        return duration==Integer.MAX_VALUE?-1:duration;
    }

    public void addPassenger(Passenger passenger) {
        Passengers.put(passenger.getPassengerId(), passenger);
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
//         Flight flight = Flights.get(flightId);
//         int maxcapacity = flight.getMaxCapacity();
//         Set<Integer> set = new HashSet<>();
//         if(Tickets.containsKey(flightId)){
//             set = Tickets.get(flightId);
//         }
//
//         int capacity = set.size();
//         if(capacity == maxcapacity) return "FAILURE";
//         else if(set.contains(passengerId))return "FAILURE";
//         int fare=calculateFlightFare(flightId);
//         paymentMap.put(passengerId,fare);
//         fare+=revenueMap.getOrDefault(flightId,0);
//         revenueMap.put(flightId,fare);
//         set.add(passengerId);
//         set.add(passengerId);
//         Tickets.put(flightId, set);
//         return "SUCCESS";

        Flight flight=Flights.get(flightId);
        int maxcapacity=flight.getMaxCapacity();
        Set<Integer> list= new HashSet<>();
        if(Tickets.containsKey(flightId)){
            list=Tickets.get(flightId);
        }
        int capacity=list.size();
        if(capacity==maxcapacity) return "FAILURE";
        else if(list.contains(passengerId)) return "FAILURE";
        int fare=calculateFlightFare(flightId);
        paymentMap.put(passengerId,fare);
        fare+=revenueMap.getOrDefault(flightId,0);
        revenueMap.put(flightId,fare);
        list.add(passengerId);
        Tickets.put(flightId,list);
        return "SUCCESS";
    }



    public String cancelATicket(Integer flightId, Integer passengerId)  {
        Set<Integer> list= Tickets.get(flightId);
        if(list.contains(passengerId)){
            list.remove(passengerId);
            int fare=paymentMap.getOrDefault(passengerId,0);
            paymentMap.remove(passengerId);
            int revenue=revenueMap.getOrDefault(flightId,0);
            revenueMap.put(flightId,revenue-fare);
            return "SUCCESS";
        }
        return "FAILURE";



//        if(!Tickets.containsKey(flightId) || !Tickets.get(flightId).contains(passengerId)){
//            return "FAILURE";
//        }
//
//        Tickets.get(flightId).remove(passengerId);
//        int fare=paymentMap.getOrDefault(passengerId,0);
//        paymentMap.remove(passengerId);
//        int revenue=revenueMap.getOrDefault(flightId,0);
//        revenueMap.put(flightId,revenue-fare);
//        return "SUCCESS";
    }


    public void addFlight(Flight flight) {
        Flights.put(flight.getFlightId(), flight);
    }

    public String getAirportNameFromFlightId(Integer flightId)  {
        if(!Flights.containsKey(flightId)) return null;
        Flight flight= Flights.get(flightId);
        City city=flight.getFromCity();
        for (String airportname:Airports.keySet()){
            Airport airport=Airports.get(airportname);
            if(city.equals(airport.getCity())){
                return airportname;
            }
        }
        return null;
//        if(Flights.containsKey(flightId)){
//            City city = Flights.get(flightId).getFromCity();
//            for(Airport airport : Airports.values()){
//                if(airport.getCity().equals(city))
//                    return airport.getAirportName();
//            }
//        }
//        return null;
    }

    public int calculateFlightFare(Integer flightId) {
//        int fare = 3000;
//        int alreadyBooked = 0;
//        if(Tickets.containsKey(flightId))
//            alreadyBooked = Tickets.get(flightId).size();
//        return (fare + (alreadyBooked*50));
        int fare=3000;
        int alreadyBooked=0;
        if(Tickets.containsKey(flightId))
            alreadyBooked=Tickets.get(flightId).size();
        return (fare+(alreadyBooked*50));
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
//        int noOfPeople = 0;
//        Airport airport = Airports.get(airportName);
//        if(airport!=null){
//            City city = Airports.get(airportName).getCity();
//            for(Flight flight : Flights.values()){
//                if(date.equals(flight.getFlightDate())){
//                    if(flight.getFromCity().equals(city) || flight.getToCity().equals(city)){
//                        noOfPeople += Tickets.get(flight.getFlightId()).size();
//                    }
//                }
//
//            }
//        }
//        return noOfPeople;
        Airport airport=Airports.get(airportName);
        int count=0;
        if(airport!=null){
            City city=airport.getCity();
            for(Flight flight : Flights.values()){
                if(date.equals(flight.getFlightDate())){
                    if(city.equals(flight.getToCity()) || city.equals(flight.getFromCity())){
                        Integer flightId=flight.getFlightId();
                        Set<Integer> list=Tickets.get(flightId);
                        if(list!=null){
                            count+= list.size();
                        }
                    }
                }
            }}
        return count;
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
//        int cnt = 0;
//        for(Set<Integer>passengers  : Tickets.values()){
//            if(passengers.contains(passengerId))cnt++;
//        }
//        return cnt;

        int count=0;
        for(Integer flightId:Tickets.keySet()){
            Set<Integer> list=Tickets.get(flightId);
            if(list.contains(passengerId)){
                count++;
            }
        }
        return count;
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        Integer revenue = revenueMap.getOrDefault(flightId, 0);
        return revenue;
    }
}
