package com.example.nasaapodapp.data.util;

import com.example.nasaapodapp.data.model.ApodResponse;

import java.util.ArrayList;
import java.util.List;

public class PreloadedApodData {

    public static List<ApodResponse> getPreloadedApods() {
        List<ApodResponse> apods = new ArrayList<>();

        // APOD 1: The Andromeda Galaxy - исправлены URL
        apods.add(new ApodResponse(
                "2023-10-01",
                "The Andromeda Galaxy",
                "The Andromeda Galaxy (M31) is the closest large galaxy to our Milky Way. It's a spiral galaxy about 2.5 million light-years away and is visible to the naked eye on dark nights. Andromeda contains about one trillion stars, at least twice the number in our own galaxy.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Andromeda_Galaxy_%28with_h-alpha%29.jpg/800px-Andromeda_Galaxy_%28with_h-alpha%29.jpg",
                "NASA, ESA, J. Dalcanton et al.",
                createAndromeadaQuizzes()
        ));

        // APOD 2: The Pillars of Creation
        apods.add(new ApodResponse(
                "2023-10-02",
                "The Pillars of Creation",
                "The Pillars of Creation are part of the Eagle Nebula, located about 6,500 light-years away. These towering columns of interstellar gas and dust are the site of new star formation. First captured by Hubble in 1995, they've become one of the most iconic astronomical images.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/Pillars_of_creation_2014_HST_WFC3-UVIS_full-res_denoised.jpg/800px-Pillars_of_creation_2014_HST_WFC3-UVIS_full-res_denoised.jpg",
                "NASA, ESA, CSA, STScI",
                createPillarsQuizzes()
        ));

        // APOD 3: Saturn and Its Rings
        apods.add(new ApodResponse(
                "2023-10-03",
                "Saturn and Its Rings",
                "Saturn is the sixth planet from the Sun and the second-largest in our Solar System. It's most known for its stunning ring system, consisting mostly of ice particles with a smaller amount of rocky debris and dust. These rings extend up to 175,000 miles from the planet but are incredibly thin, only about 66 feet thick.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/Saturn_during_Equinox.jpg/800px-Saturn_during_Equinox.jpg",
                "NASA, ESA, CSA, STScI, Webb Team",
                createSaturnQuizzes()
        ));

        // APOD 4: The Butterfly Nebula - FIXED URL
        apods.add(new ApodResponse(
                "2023-10-04",
                "The Butterfly Nebula",
                "The Butterfly Nebula, also known as NGC 6302, is a bipolar planetary nebula located about 3,800 light-years away. The central star, one of the hottest in the galaxy at about 250,000 degrees Celsius, has ejected its outer layers, creating this striking butterfly-like structure.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Orion_Nebula_-_Hubble_2006_mosaic_18000.jpg/800px-Orion_Nebula_-_Hubble_2006_mosaic_18000.jpg",
                "NASA, ESA, Hubble",
                createButterflyQuizzes()
        ));

        // APOD 5: The Crab Nebula
        apods.add(new ApodResponse(
                "2023-10-05",
                "The Crab Nebula",
                "The Crab Nebula (M1) is the remnant of a supernova explosion observed by Chinese astronomers in 1054 AD. Located 6,500 light-years away, it's expanding at about 1,500 kilometers per second. At its center lies a pulsar – a rapidly rotating neutron star that emits beams of radiation.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/00/Crab_Nebula.jpg/800px-Crab_Nebula.jpg",
                "NASA, ESA, Hubble Legacy Archive",
                createCrabQuizzes()
        ));

        // APOD 6: The Helix Nebula
        apods.add(new ApodResponse(
                "2023-10-06",
                "The Helix Nebula",
                "Often called 'The Eye of God', the Helix Nebula (NGC 7293) is one of the closest planetary nebulae to Earth at about 700 light-years away. It's the result of a dying star shedding its outer layers, revealing a fascinating structure of gas and dust illuminated by the hot core of the former star.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/NGC7293_%282004%29.jpg/800px-NGC7293_%282004%29.jpg",
                "NASA, ESA, CSA, STScI",
                createHelixQuizzes()
        ));

        // APOD 7: The Horsehead Nebula
        apods.add(new ApodResponse(
                "2023-10-07",
                "The Horsehead Nebula",
                "The Horsehead Nebula (Barnard 33) is a dark nebula located about 1,500 light-years away in the constellation Orion. Its distinctive equine silhouette is formed by a dense cloud of dust and gas blocking the light from the bright nebula IC 434 behind it. It's one of the most recognizable objects in the night sky.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/Barnard_33.jpg/800px-Barnard_33.jpg",
                "NASA, ESA, Hubble Heritage Team",
                createHorseheadQuizzes()
        ));

        return apods;
    }

    private static List<ApodResponse.Quiz> createAndromeadaQuizzes() {
        List<ApodResponse.Quiz> quizzes = new ArrayList<>();

        quizzes.add(new ApodResponse.Quiz(
                "How far is the Andromeda Galaxy from Earth?",
                "2.5 million light-years",
                "42,000 light-years",
                "1 billion light-years",
                "500,000 light-years"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "Approximately how many stars are in the Andromeda Galaxy?",
                "One trillion",
                "One million",
                "One billion",
                "Ten billion"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "What is the designation for the Andromeda Galaxy in the Messier catalog?",
                "M31",
                "M42",
                "M87",
                "M13"
        ));

        return quizzes;
    }

    private static List<ApodResponse.Quiz> createPillarsQuizzes() {
        List<ApodResponse.Quiz> quizzes = new ArrayList<>();

        quizzes.add(new ApodResponse.Quiz(
                "In what year did Hubble first capture the Pillars of Creation?",
                "1995",
                "1975",
                "2005",
                "2015"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "The Pillars of Creation are part of which nebula?",
                "Eagle Nebula",
                "Orion Nebula",
                "Carina Nebula",
                "Horsehead Nebula"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "How far away are the Pillars of Creation?",
                "6,500 light-years",
                "1,500 light-years",
                "25,000 light-years",
                "100,000 light-years"
        ));

        return quizzes;
    }

    private static List<ApodResponse.Quiz> createSaturnQuizzes() {
        List<ApodResponse.Quiz> quizzes = new ArrayList<>();

        quizzes.add(new ApodResponse.Quiz(
                "What position is Saturn from the Sun in our Solar System?",
                "Sixth",
                "Fifth",
                "Fourth",
                "Seventh"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "What is the approximate thickness of Saturn's rings?",
                "66 feet",
                "660 feet",
                "6,600 feet",
                "66,000 feet"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "What is Saturn's rings primarily composed of?",
                "Ice particles",
                "Rocky debris",
                "Liquid methane",
                "Iron dust"
        ));

        return quizzes;
    }

    private static List<ApodResponse.Quiz> createButterflyQuizzes() {
        List<ApodResponse.Quiz> quizzes = new ArrayList<>();

        quizzes.add(new ApodResponse.Quiz(
                "What is the catalog designation for the Butterfly Nebula?",
                "NGC 6302",
                "NGC 7293",
                "NGC 1976",
                "NGC 5139"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "What is the approximate temperature of the central star in the Butterfly Nebula?",
                "250,000 degrees Celsius",
                "10,000 degrees Celsius",
                "50,000 degrees Celsius",
                "1 million degrees Celsius"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "What type of nebula is the Butterfly Nebula?",
                "Planetary nebula",
                "Supernova remnant",
                "Dark nebula",
                "Emission nebula"
        ));

        return quizzes;
    }

    private static List<ApodResponse.Quiz> createCrabQuizzes() {
        List<ApodResponse.Quiz> quizzes = new ArrayList<>();

        quizzes.add(new ApodResponse.Quiz(
                "In what year was the supernova that created the Crab Nebula observed?",
                "1054 AD",
                "1604 AD",
                "1572 AD",
                "1987 AD"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "What is at the center of the Crab Nebula?",
                "A pulsar",
                "A black hole",
                "A white dwarf",
                "A red giant"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "At what rate is the Crab Nebula expanding?",
                "1,500 km/s",
                "100 km/s",
                "10,000 km/s",
                "300 km/s"
        ));

        return quizzes;
    }

    private static List<ApodResponse.Quiz> createHelixQuizzes() {
        List<ApodResponse.Quiz> quizzes = new ArrayList<>();

        quizzes.add(new ApodResponse.Quiz(
                "What is the distance of the Helix Nebula from Earth?",
                "700 light-years",
                "7,000 light-years",
                "70,000 light-years",
                "7 light-years"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "What is the catalog designation for the Helix Nebula?",
                "NGC 7293",
                "NGC 6302",
                "NGC 1976",
                "NGC 3372"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "What nickname is commonly given to the Helix Nebula?",
                "The Eye of God",
                "The Sleeping Giant",
                "The Cosmic Butterfly",
                "The Crystal Ball"
        ));

        return quizzes;
    }

    private static List<ApodResponse.Quiz> createHorseheadQuizzes() {
        List<ApodResponse.Quiz> quizzes = new ArrayList<>();

        quizzes.add(new ApodResponse.Quiz(
                "In which constellation is the Horsehead Nebula located?",
                "Orion",
                "Taurus",
                "Andromeda",
                "Sagittarius"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "What type of nebula is the Horsehead Nebula?",
                "Dark nebula",
                "Planetary nebula",
                "Supernova remnant",
                "Emission nebula"
        ));

        quizzes.add(new ApodResponse.Quiz(
                "What is the Horsehead Nebula's designation in Barnard's catalog?",
                "Barnard 33",
                "Barnard 5",
                "Barnard 142",
                "Barnard 72"
        ));

        return quizzes;
    }
}