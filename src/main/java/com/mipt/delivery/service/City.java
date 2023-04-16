package com.mipt.delivery.service;

import lombok.Value;

/*
 * City Enum. This interface made to realize forward compatibility.
 * If you want to add new city, write it name in CityEnum.
 *
 * Usage:
 * City city = City.of(cityName);
 * if (city.equals(City.CityEnum.MOSCOW)) { doSomething(); }
 */
public interface City {

    String value();

    @Value
    class CitySimple implements City {

        String value;

        @Override
        public String value() {
            return value;
        }

    }

    enum CityEnum implements City {

        MOSCOW, SAINT_PETERSBURG;

        public static CityEnum parse(String rawValue) {
            if (rawValue == null) {
                throw new IllegalArgumentException("Raw value cannot be null!");
            }
            for (CityEnum enumValue : values()) {
                if (enumValue.name().equalsIgnoreCase(rawValue)) {
                    return enumValue;
                }
            }
            throw new IllegalArgumentException("Illegal CityEnum type!");
        }

        @Override
        public String value() {
            return name();
        }

    }

    static City of(String value) {
        try {
            return CityEnum.parse(value);
        }
        catch (IllegalArgumentException e) {
            return new CitySimple(value.toUpperCase());
        }
    }

}
