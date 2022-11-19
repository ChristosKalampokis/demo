package com.example;

import java.util.List;

public class FlightObject {
    

    public List<String> airlines;
    public String originDepartureTime;
    public String originArrivalTime;
    public String destinationDepartureTime;
    public String destinationArrivalTime;
    public String originDuration;
    public String destinationDuration;
    public String originDepartureLocation;
    public String originArrivalLocation;
    public String destinationDepartureLocation;
    public String destinationArrivalLocation;
    public String priceStandard;
    public String priceFlexible;
    public int maxStops;

    public List<String> getAirlines() {
        return this.airlines;
    }
    public String getAirlinesStr() {


        String str = "";
        for(String airline:getAirlines())
        {
            str += airline+",";
        }
        return str;

    }
    public void setAirlines(List<String> airlines) {
        this.airlines = airlines;
    }

    public String getOriginDepartureTime() {
        return this.originDepartureTime;
    }

    public void setOriginDepartureTime(String originDepartureTime) {
        this.originDepartureTime = originDepartureTime;
    }

    public String getOriginArrivalTime() {
        return this.originArrivalTime;
    }

    public void setOriginArrivalTime(String originArrivalTime) {
        this.originArrivalTime = originArrivalTime;
    }

    public String getDestinationDepartureTime() {
        return this.destinationDepartureTime;
    }

    public void setDestinationDepartureTime(String destinationDepartureTime) {
        this.destinationDepartureTime = destinationDepartureTime;
    }

    public String getDestinationArrivalTime() {
        return this.destinationArrivalTime;
    }

    public void setDestinationArrivalTime(String destinationArrivalTime) {
        this.destinationArrivalTime = destinationArrivalTime;
    }

    public String getOriginDuration() {
        return this.originDuration;
    }

    public void setOriginDuration(String originDuration) {
        this.originDuration = originDuration;
    }

    public String getDestinationDuration() {
        return this.destinationDuration;
    }

    public void setDestinationDuration(String destinationDuration) {
        this.destinationDuration = destinationDuration;
    }

    public String getOriginDepartureLocation() {
        return this.originDepartureLocation;
    }

    public void setOriginDepartureLocation(String originDepartureLocation) {
        this.originDepartureLocation = originDepartureLocation;
    }

    public String getOriginArrivalLocation() {
        return this.originArrivalLocation;
    }

    public void setOriginArrivalLocation(String originArrivalLocation) {
        this.originArrivalLocation = originArrivalLocation;
    }

    public String getDestinationDepartureLocation() {
        return this.destinationDepartureLocation;
    }

    public void setDestinationDepartureLocation(String destinationDepartureLocation) {
        this.destinationDepartureLocation = destinationDepartureLocation;
    }

    public String getDestinationArrivalLocation() {
        return this.destinationArrivalLocation;
    }

    public void setDestinationArrivalLocation(String destinationArrivalLocation) {
        this.destinationArrivalLocation = destinationArrivalLocation;
    }

    public String getPriceStandard() {
        return this.priceStandard;
    }

    public void setPriceStandard(String priceStandard) {
        this.priceStandard = priceStandard;
    }

    public String getPriceFlexible() {
        return this.priceFlexible;
    }

    public void setPriceFlexible(String priceFlexible) {
        this.priceFlexible = priceFlexible;
    }

    public int getMaxStops() {
        return this.maxStops;
    }

    public void setMaxStops(int maxStops) {
        this.maxStops = maxStops;
    }


    public FlightObject(List<String> airlines, String originDepartureTime, String originArrivalTime, 
                            String destinationDepartureTime, String destinationArrivalTime, 
                            String originDuration, String destinationDuration, String originDepartureLocation, 
                            String originArrivalLocation, String destinationDepartureLocation, 
                            String destinationArrivalLocation, String priceStandard, String priceFlexible, 
                            int maxStops) {

        this.airlines = airlines;
        this.originDepartureTime = originDepartureTime;
        this.originArrivalTime = originArrivalTime;
        this.destinationDepartureTime = destinationDepartureTime;
        this.destinationArrivalTime = destinationArrivalTime;
        this.originDuration = originDuration;
        this.destinationDuration = destinationDuration;
        this.originDepartureLocation = originDepartureLocation;
        this.originArrivalLocation = originArrivalLocation;
        this.destinationDepartureLocation = destinationDepartureLocation;
        this.destinationArrivalLocation = destinationArrivalLocation;
        this.priceStandard = priceStandard;
        this.priceFlexible = priceFlexible;
        this.maxStops = maxStops;
    }



    @Override
    public String toString() {
        return "FLIGHT\n"
        +"{" +
            "airlines='" + getAirlinesStr() + "'" +
            ", \noriginDepartureTime='" + getOriginDepartureTime() + "'" +
            ", \noriginArrivalTime='" + getOriginArrivalTime() + "'" +
            ", \ndestinationDepartureTime='" + getDestinationDepartureTime() + "'" +
            ", \ndestinationArrivalTime='" + getDestinationArrivalTime() + "'" +
            ", \noriginDuration='" + getOriginDuration() + "'" +
            ", \ndestinationDuration='" + getDestinationDuration() + "'" +
            ", \noriginDepartureLocation='" + getOriginDepartureLocation() + "'" +
            ", \noriginArrivalLocation='" + getOriginArrivalLocation() + "'" +
            ", \ndestinationDepartureLocation='" + getDestinationDepartureLocation() + "'" +
            ", \ndestinationArrivalLocation='" + getDestinationArrivalLocation() + "'" +
            ", \npriceStandard='" + getPriceStandard() + "'" +
            ", \npriceFlexible='" + getPriceFlexible() + "'" +
            ", \nmaxStops='" + getMaxStops() + "'" +
            "}";
    }
  
    
}
