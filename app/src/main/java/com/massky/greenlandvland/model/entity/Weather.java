package com.massky.greenlandvland.model.entity;

import java.util.List;

/**
 * Created by masskywcy on 2018-12-24.
 */

public class Weather {
    private String msg;
    private List<Result> result;
    private int retCode;

    public Weather(String msg, List<Result> result, int retCode) {
        this.msg = msg;
        this.result = result;
        this.retCode = retCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "msg='" + msg + '\'' +
                ", result=" + result +
                ", retCode=" + retCode +
                '}';
    }

    public static class Result{
        private String airCondition;
        private AirQuality airQuality;
        private String city;
        private String coldIndex;
        private String date;
        private String distrct;
        private String dressingIndex;
        private String exerciseIndex;
        private List<Future> future;
        private String humidity;
        private int pollutionIndex;
        private String province;
        private String sunrise;
        private String sunset;
        private String temperature;
        private String time;
        private String updateTime;
        private String washIndex;
        private String weather;
        private String week;
        private String wind;

        public Result(String airCondition, AirQuality airQuality, String city, String coldIndex, String date, String distrct, String dressingIndex, String exerciseIndex, List<Future> future, String humidity, int pollutionIndex, String province, String sunrise, String sunset, String temperature, String time, String updateTime, String washIndex, String weather, String week, String wind) {
            this.airCondition = airCondition;
            this.airQuality = airQuality;
            this.city = city;
            this.coldIndex = coldIndex;
            this.date = date;
            this.distrct = distrct;
            this.dressingIndex = dressingIndex;
            this.exerciseIndex = exerciseIndex;
            this.future = future;
            this.humidity = humidity;
            this.pollutionIndex = pollutionIndex;
            this.province = province;
            this.sunrise = sunrise;
            this.sunset = sunset;
            this.temperature = temperature;
            this.time = time;
            this.updateTime = updateTime;
            this.washIndex = washIndex;
            this.weather = weather;
            this.week = week;
            this.wind = wind;
        }

        public String getAirCondition() {
            return airCondition;
        }

        public void setAirCondition(String airCondition) {
            this.airCondition = airCondition;
        }

        public AirQuality getAirQuality() {
            return airQuality;
        }

        public void setAirQuality(AirQuality airQuality) {
            this.airQuality = airQuality;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getColdIndex() {
            return coldIndex;
        }

        public void setColdIndex(String coldIndex) {
            this.coldIndex = coldIndex;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDistrct() {
            return distrct;
        }

        public void setDistrct(String distrct) {
            this.distrct = distrct;
        }

        public String getDressingIndex() {
            return dressingIndex;
        }

        public void setDressingIndex(String dressingIndex) {
            this.dressingIndex = dressingIndex;
        }

        public String getExerciseIndex() {
            return exerciseIndex;
        }

        public void setExerciseIndex(String exerciseIndex) {
            this.exerciseIndex = exerciseIndex;
        }

        public List<Future> getFuture() {
            return future;
        }

        public void setFuture(List<Future> future) {
            this.future = future;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public int getPollutionIndex() {
            return pollutionIndex;
        }

        public void setPollutionIndex(int pollutionIndex) {
            this.pollutionIndex = pollutionIndex;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getWashIndex() {
            return washIndex;
        }

        public void setWashIndex(String washIndex) {
            this.washIndex = washIndex;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "airCondition='" + airCondition + '\'' +
                    ", airQuality=" + airQuality +
                    ", city='" + city + '\'' +
                    ", coldIndex='" + coldIndex + '\'' +
                    ", date='" + date + '\'' +
                    ", distrct='" + distrct + '\'' +
                    ", dressingIndex='" + dressingIndex + '\'' +
                    ", exerciseIndex='" + exerciseIndex + '\'' +
                    ", future=" + future +
                    ", humidity='" + humidity + '\'' +
                    ", pollutionIndex=" + pollutionIndex +
                    ", province='" + province + '\'' +
                    ", sunrise='" + sunrise + '\'' +
                    ", sunset='" + sunset + '\'' +
                    ", temperature='" + temperature + '\'' +
                    ", time='" + time + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    ", washIndex='" + washIndex + '\'' +
                    ", weather='" + weather + '\'' +
                    ", week='" + week + '\'' +
                    ", wind='" + wind + '\'' +
                    '}';
        }

        public static class AirQuality{
            private int aqi;
            private String city;
            private String district;
            private List<FetureData> fetureData;
            private List<HourData> hourData;
            private int no2;
            private int pm10;
            private int pm25;
            private String province;
            private String quality;
            private int so2;
            private String updateTime;

            public AirQuality(int aqi, String city, String district, List<FetureData> fetureData, List<HourData> hourData, int no2, int pm10, int pm25, String province, String quality, int so2, String updateTime) {
                this.aqi = aqi;
                this.city = city;
                this.district = district;
                this.fetureData = fetureData;
                this.hourData = hourData;
                this.no2 = no2;
                this.pm10 = pm10;
                this.pm25 = pm25;
                this.province = province;
                this.quality = quality;
                this.so2 = so2;
                this.updateTime = updateTime;
            }

            public int getAqi() {
                return aqi;
            }

            public void setAqi(int aqi) {
                this.aqi = aqi;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public List<FetureData> getFetureData() {
                return fetureData;
            }

            public void setFetureData(List<FetureData> fetureData) {
                this.fetureData = fetureData;
            }

            public List<HourData> getHourData() {
                return hourData;
            }

            public void setHourData(List<HourData> hourData) {
                this.hourData = hourData;
            }

            public int getNo2() {
                return no2;
            }

            public void setNo2(int no2) {
                this.no2 = no2;
            }

            public int getPm10() {
                return pm10;
            }

            public void setPm10(int pm10) {
                this.pm10 = pm10;
            }

            public int getPm25() {
                return pm25;
            }

            public void setPm25(int pm25) {
                this.pm25 = pm25;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getQuality() {
                return quality;
            }

            public void setQuality(String quality) {
                this.quality = quality;
            }

            public int getSo2() {
                return so2;
            }

            public void setSo2(int so2) {
                this.so2 = so2;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            @Override
            public String toString() {
                return "AirQuality{" +
                        "aqi=" + aqi +
                        ", city='" + city + '\'' +
                        ", district='" + district + '\'' +
                        ", fetureData=" + fetureData +
                        ", hourData=" + hourData +
                        ", no2=" + no2 +
                        ", pm10=" + pm10 +
                        ", pm25=" + pm25 +
                        ", province='" + province + '\'' +
                        ", quality='" + quality + '\'' +
                        ", so2=" + so2 +
                        ", updateTime='" + updateTime + '\'' +
                        '}';
            }
            public static class FetureData{
                private int aqi;
                private String date;
                private String quality;

                public FetureData(int aqi, String date, String quality) {
                    this.aqi = aqi;
                    this.date = date;
                    this.quality = quality;
                }

                public int getAqi() {
                    return aqi;
                }

                public void setAqi(int aqi) {
                    this.aqi = aqi;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getQuality() {
                    return quality;
                }

                public void setQuality(String quality) {
                    this.quality = quality;
                }

                @Override
                public String toString() {
                    return "FetureData{" +
                            "aqi=" + aqi +
                            ", date='" + date + '\'' +
                            ", quality='" + quality + '\'' +
                            '}';
                }
            }
            public static class HourData{
                private int aqi;
                private String dateTime;

                public HourData(int aqi, String dateTime) {
                    this.aqi = aqi;
                    this.dateTime = dateTime;
                }

                public int getAqi() {
                    return aqi;
                }

                public void setAqi(int aqi) {
                    this.aqi = aqi;
                }

                public String getDateTime() {
                    return dateTime;
                }

                public void setDateTime(String dateTime) {
                    this.dateTime = dateTime;
                }

                @Override
                public String toString() {
                    return "HourData{" +
                            "aqi=" + aqi +
                            ", dateTime='" + dateTime + '\'' +
                            '}';
                }
            }
        }

        public static class Future{
            private String date;
            private String dayTime;
            private String night;
            private String temperature;
            private String week;
            private String wing;

            public Future(String date, String dayTime, String night, String temperature, String week, String wing) {
                this.date = date;
                this.dayTime = dayTime;
                this.night = night;
                this.temperature = temperature;
                this.week = week;
                this.wing = wing;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getDayTime() {
                return dayTime;
            }

            public void setDayTime(String dayTime) {
                this.dayTime = dayTime;
            }

            public String getNight() {
                return night;
            }

            public void setNight(String night) {
                this.night = night;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getWing() {
                return wing;
            }

            public void setWing(String wing) {
                this.wing = wing;
            }

            @Override
            public String toString() {
                return "Future{" +
                        "date='" + date + '\'' +
                        ", dayTime='" + dayTime + '\'' +
                        ", night='" + night + '\'' +
                        ", temperature='" + temperature + '\'' +
                        ", week='" + week + '\'' +
                        ", wing='" + wing + '\'' +
                        '}';
            }
        }
    }
}
