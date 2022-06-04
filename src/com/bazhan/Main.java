package com.bazhan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.groupingBy;

public class Main {

    public static void main(String[] args) throws IOException {
        Map<Integer, String> idToName=people().collect(Collectors.toMap(Person::getId,Person::getName));
        System.out.println("idToName: "+idToName);
        //
        Map<Integer, Person> idToPerson=people().collect(Collectors
                .toMap(Person::getId, Function.identity()));
        System.out.println("idToPerson: "+idToPerson.getClass().getName()+idToPerson);
        idToPerson=people().collect(Collectors
                .toMap(Person::getId, Function.identity(), (existingValue, newValue)
                        ->{throw new IllegalStateException();},
                        TreeMap::new));
        System.out.println("idToPerson: "+idToPerson.getClass().getName()+idToPerson);

        //создается отображение содержащее региональные настройки для каждого языка в виде ключа
        Stream<Locale> locales=Stream.of(Locale.getAvailableLocales());
        Map<String,String> languagesNames=locales.collect(Collectors.toMap(Locale::getDisplayLanguage,
                l->l.getDisplayLanguage(l),
                (existingValue, newValue)->existingValue));
        System.out.println("languageName: "+languagesNames);

        //создается одноэлементное множество
        locales=Stream.of(Locale.getAvailableLocales());
        //добавляется в множестов каждый элемент с указанием порядка объединения сущ и нов значения
        Map<String, Set<String>>  countryLanguageSet=
                locales.collect(Collectors.toMap(Locale::getDisplayCountry,
                        l->Set.of(l.getDisplayLanguage()),
                        (a,b)->{Set<String> union=new HashSet<>(a);
                     union.addAll(b);
                    return union;
                        } ));
        System.out.println("countryLanguageSet: "+countryLanguageSet);


         Stream<Locale> locales2=Stream.of(Locale.getAvailableLocales());
         //создать пустое множество и добавляются элементы
         locales2=Stream.of(Locale.getAvailableLocales());
         //объединяет значение с одинаковыми характеристиками
         Map<String, Set<Locale>> countryToLocaleSet=
                 //нисходящий коллектор, множество списков
                 locales2.collect(groupingBy(Locale::getCountry, toSet()));
        System.out.println("countryToLocaleSet: " + countryToLocaleSet);

        //создать пустое множество и добавляются элементы
        locales2=Stream.of(Locale.getAvailableLocales());
        Map<String, Long> countryToLocaleCounts = locales2.collect(groupingBy(
                //подсчет накапливаемых элементов
                Locale::getCountry, counting()));
        System.out.println("countryToLocaleCounts: " + countryToLocaleCounts);

        Stream<City> cities = readCities("D:/Programm/CollectionIntoMaps/cities.txt");
        Map<String, Integer> stateToCityPopulation = cities.collect(groupingBy(
                City::getState, summingInt(City::getPopulation)));
        System.out.println("stateToCityPopulation: " + stateToCityPopulation);

        cities = readCities("D:/Programm/CollectionIntoMaps/cities.txt");
        Map<String, IntSummaryStatistics> stateToCityPopulationSummary =
                cities.collect(groupingBy(City::getState, summarizingInt(City::getPopulation)));
        System.out.println(stateToCityPopulationSummary.get("NY"));
    }

    public  static Stream<Person> people(){
        return Stream.of(new Person(1001, "Peter"),
                new Person(1002, "Paul"),
                new Person(1003, "Mary"));
    }

    public static Stream<City> readCities (String filename) throws IOException{
        return Files.lines(Paths.get(filename)).map(l->l.split(", ")).map(a->new City(a[0], a[1],
                Integer.parseInt(a[2])));
    }
}
