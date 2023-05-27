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
        int maxTerminals = 0;
        String largestAirport = "";
        for(String key: Airports.keySet()){
            if(Airports.get(key).getNoOfTerminals() > maxTerminals){
                maxTerminals = Airports.get(key).getNoOfTerminals();
                largestAirport = key;
            }
        }
        return largestAirport;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        double shortestDuration = Integer.MAX_VALUE;
        for(Flight flight: Flights.values()){
            if(flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity)){
                if(shortestDuration > flight.getDuration()){
                    shortestDuration = flight.getDuration();
                }
            }
        }
        if(shortestDuration == Integer.MAX_VALUE)return -1;
        return shortestDuration;
    }

    public boolean addPassenger(Passenger passenger) {
        if(Passengers.containsKey(passenger.getPassengerId())) return false;
        Passengers.put(passenger.getPassengerId(), passenger);
        return true;
    }

    public void bookATicket(Integer flightId, Integer passengerId) throws Exception {
         Flight flight = Flights.get(flightId);
         int maxcapacity = flight.getMaxCapacity();
         Set<Integer> set = new HashSet<>();
         if(Tickets.containsKey(flightId)){
             set = Tickets.get(flightId);
         }

         int capacity = set.size();
         if(capacity == maxcapacity) throw new Exception("Max capacity reached");
         else if(set.contains(passengerId))throw new Exception("passanger already exist");
         int fare=calculateFlightFare(flightId);
         paymentMap.put(passengerId,fare);
         fare+=revenueMap.getOrDefault(flightId,0);
         revenueMap.put(flightId,fare);
         set.add(passengerId);
         set.add(passengerId);
         Tickets.put(flightId, set);

//        if(Tickets.containsKey(flightId)){
//            if(Flights.get(flightId).getMaxCapacity() == Tickets.get(flightId).size()){
//                throw new Exception("Max capacity reached");
//            }
//
//            if(!Flights.containsKey(flightId) || !Passengers.containsKey(passengerId)){
//                throw new Exception("flight or passenger or both does not exist in database");
//            }
//
//            if(Tickets.get(flightId).contains(passengerId)){
//                throw new Exception("Passenger already booked");
//            }
//
//            Set<Integer> oldset = Tickets.get(flightId);
//            oldset.add(passengerId);
//            Tickets.put(flightId, oldset);
//        }
//        else{
//            Set<Integer> newset = new HashSet<>();
//            newset.add(passengerId);
//            Tickets.put(flightId, newset);
//        }
    }


//    public void bookATicket(Integer flightId, Integer passengerId) throws Exception {
//        if(!Flights.containsKey(flightId) || !Passengers.containsKey(passengerId)){
//            throw new Exception("flight or passenger or both does not exist in database");
//        }
//
//        if(Kets.containsKey(passengerId)){
//            throw new Exception("Passenger already booked");
//        }
//
//        if(Tic.containsKey(flightId)){
//            if(Tic.get(flightId).size() >= Flights.get(flightId).getMaxCapacity()){
//                throw new Exception("Max capacity reached");
//            }
//
//            ArrayList<Integer> oldList = Tic.get(flightId);
//            oldList.add(passengerId);
//            Tic.put(flightId, oldList);
//            Kets.put(passengerId, flightId);
//        }
//        else{
//            ArrayList<Integer> newList = new ArrayList<>();
//            newList.add(passengerId);
//            Tic.put(flightId, newList);
//            Kets.put(passengerId, flightId);
//        }
//    }

    public void cancelATicket(Integer flightId, Integer passengerId) throws Exception {

        if(!Tickets.containsKey(flightId) || !Tickets.get(flightId).contains(passengerId)){
            throw new Exception("flight or passenger or both does not exist in {Tickets} database");
        }

        Tickets.get(flightId).remove(passengerId);
        int fare=paymentMap.getOrDefault(passengerId,0);
        paymentMap.remove(passengerId);
        int revenue=revenueMap.getOrDefault(flightId,0);
        revenueMap.put(flightId,revenue-fare);

//        if(!Tic.containsKey(flightId) || !Kets.containsKey(passengerId)){
//            throw new Exception("flight or passenger or both does not exist in {Tickets} database");
//        }
//        if(Kets.containsKey(passengerId) && Kets.get(passengerId) != flightId){
//            throw new Exception("This particular passenger has not booked this flight");
//        }
//
//        for(int i : Tic.get(flightId)){
//            if(Tic.get(flightId).get(i) == passengerId)Tic.get(flightId).remove(i);
//        }
//
//        Kets.remove(passengerId);
    }

    public void addFlight(Flight flight) throws Exception {
        if(Flights.containsKey(flight.getFlightId())){
            throw new Exception("flight already exist");
        }
        Flights.put(flight.getFlightId(), flight);
    }

    public String getAirportNameFromFlightId(Integer flightId)  {
        if(Flights.containsKey(flightId)){
            City city = Flights.get(flightId).getFromCity();
            for(Airport airport : Airports.values()){
                if(airport.getCity().equals(city))
                    return airport.getAirportName();
            }
        }
        return null;
    }

    public int calculateFlightFare(Integer flightId) {
        int fare = 3000;
        int alreadyBooked = 0;
        if(Tickets.containsKey(flightId))
            alreadyBooked = Tickets.get(flightId).size();
        return (fare + (alreadyBooked*50));
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        int noOfPeople = 0;
        Airport airport = Airports.get(airportName);
        if(airport!=null){
            City city = Airports.get(airportName).getCity();
            for(Flight flight : Flights.values()){
                if(date.equals(flight.getFlightDate())){
                    if(flight.getFromCity().equals(city) || flight.getToCity().equals(city)){
                        noOfPeople += Tickets.get(flight.getFlightId()).size();
                    }
                }

            }
        }
        return noOfPeople;
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        int cnt = 0;
        for(Set<Integer>passengers  : Tickets.values()){
            if(passengers.contains(passengerId))cnt++;
        }
        return cnt;
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        Integer revenue = revenueMap.getOrDefault(flightId, 0);
        return revenue;
    }
}
