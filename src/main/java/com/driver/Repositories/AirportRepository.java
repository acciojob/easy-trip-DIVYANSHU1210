package com.driver.Repositories;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.*;

public class AirportRepository {

    TreeMap<String , Airport> Airports = new TreeMap<>();

    HashMap<Integer, Flight> Flights = new HashMap<>();

    HashMap<Integer, Passenger> Passengers = new HashMap<>();  // id : passanger


    HashMap<Integer, Set<Integer> /*HashMap<Integer, Integer>*/> Tickets = new HashMap<>(); // flightid : list of passanger id

//    HashMap<Integer, ArrayList<Integer>> Tic = new HashMap<>();
//    HashMap<Integer, Integer>Kets = new HashMap<>();


    public void addAirport(Airport airport) {
        Airports.put(airport.getAirportName(), airport);
    }


    public String getLargetAirportName() {
        int maxTerminals = Integer.MIN_VALUE;
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
        if(Tickets.containsKey(flightId)){
            if(Flights.get(flightId).getMaxCapacity() >= Tickets.get(flightId).size()){
                throw new Exception("Max capacity reached");
            }

            if(!Flights.containsKey(flightId) || !Passengers.containsKey(passengerId)){
                throw new Exception("flight or passenger or both does not exist in database");
            }

            if(Tickets.get(flightId).contains(passengerId)){
                throw new Exception("Passenger already booked");
            }

            Set<Integer> oldset = Tickets.get(flightId);
            oldset.add(passengerId);
            Tickets.put(flightId, oldset);
        }
        else{
            Set<Integer> newset = new HashSet<>();
            newset.add(passengerId);
            Tickets.put(flightId, newset);
        }
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

    public String getAirportNameFromFlightId(Integer flightId) throws Exception {
        if(!Flights.containsKey(flightId)){
            throw new Exception("Flight does not exist in database");
        }
        City city = Flights.get(flightId).getFromCity();
        for(Airport airport : Airports.values()){
            if(airport.getCity() == city)return airport.getAirportName();
        }
        return null;
    }

    public int calculateFlightFare(Integer flightId) {
        int noOfPassengers = Tickets.get(flightId).size();
        return 3000 + (noOfPassengers*50);
    }

    public int getNumberOfPeopleOn(String airportName) {
        int noOfPeople = 0;
        City city = Airports.get(airportName).getCity();
        for(Flight flight : Flights.values()){
            if(flight.getFromCity() == city || flight.getToCity() == city){
                noOfPeople += Tickets.get(flight.getFlightId()).size();
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
}
