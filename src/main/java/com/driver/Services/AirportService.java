package com.driver.Services;

import com.driver.Repositories.AirportRepository;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class AirportService {
    @Autowired
    AirportRepository airportRepository;
    public void addAirport(Airport airport) {
        airportRepository.addAirport(airport);
    }

    public String getLargetAirportName() {
        return airportRepository.getLargetAirportName();
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        return airportRepository.getShortestDurationOfPossibleBetweenTwoCities(fromCity, toCity);
    }

    public void addPassenger(Passenger passenger) throws Exception {
        Boolean ans =  airportRepository.addPassenger(passenger);
        if(ans == false){
            throw new Exception("Passenger already exist");
        }
    }

    public Boolean bookATicket(Integer flightId, Integer passengerId) {
        try{
            airportRepository.bookATicket(flightId, passengerId);
            return true;
        }
        catch(Exception ex){
            return false;
        }

    }

    public void cancelATicket(Integer flightId, Integer passengerId) throws Exception {
        airportRepository.cancelATicket(flightId, passengerId);
    }

    public void addFlight(Flight flight) throws Exception {
        airportRepository.addFlight(flight);
    }

    public String getAirportNameFromFlightId(Integer flightId){
        return airportRepository.getAirportNameFromFlightId(flightId);
    }

    public int calculateFlightFare(Integer flightId) {
        return airportRepository.calculateFlightFare(flightId);
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        return airportRepository.getNumberOfPeopleOn(date , airportName);
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        return airportRepository.countOfBookingsDoneByPassengerAllCombined(passengerId);
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        return airportRepository.calculateRevenueOfAFlight(flightId);
    }
}
