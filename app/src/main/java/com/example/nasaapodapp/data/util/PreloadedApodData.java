package com.example.nasaapodapp.data.util;

import com.example.nasaapodapp.data.model.ApodResponse;
import com.example.nasaapodapp.data.model.ApodResponse.Quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreloadedApodData {

    public static List<ApodResponse> getPreloadedApods() {
        List<ApodResponse> preloadedList = new ArrayList<>();

        // Image 1: Pillars of Creation
        List<Quiz> quizzes1 = Arrays.asList(
                new Quiz(
                        "What is the name of this famous nebula?",
                        "Pillars of Creation",
                        "Horsehead Nebula", "Crab Nebula", "Orion Nebula"
                ),
                new Quiz(
                        "In which larger nebula are the Pillars of Creation located?",
                        "Eagle Nebula",
                        "Carina Nebula", "Andromeda Nebula", "Omega Nebula"
                ),
                new Quiz(
                        "What telescope captured this iconic image?",
                        "Hubble Space Telescope",
                        "James Webb Space Telescope", "Spitzer Space Telescope", "Chandra X-ray Observatory"
                )
        );

        preloadedList.add(new ApodResponse(
                "2023-01-01",
                "Pillars of Creation",
                "The Pillars of Creation are elephant trunks of interstellar gas and dust in the Eagle Nebula, approximately 7,000 light-years from Earth. They are so named because the gas and dust are in the process of creating new stars.",
                "https://stsci-opo.org/STScI-01EVSQXJ8YMFK7C85XK6Q46QD0.png",
                "NASA, ESA, CSA, STScI",
                quizzes1
        ));

        // Image 2: Andromeda Galaxy
        List<Quiz> quizzes2 = Arrays.asList(
                new Quiz(
                        "What is the Messier designation of the Andromeda Galaxy?",
                        "M31",
                        "M33", "M81", "M87"
                ),
                new Quiz(
                        "Approximately how far is the Andromeda Galaxy from our Milky Way?",
                        "2.5 million light-years",
                        "100,000 light-years", "4.2 million light-years", "10 million light-years"
                ),
                new Quiz(
                        "What will eventually happen between the Andromeda Galaxy and our Milky Way?",
                        "They will collide and merge",
                        "They will never interact", "They will orbit each other indefinitely", "Andromeda will be pulled into a black hole"
                )
        );

        preloadedList.add(new ApodResponse(
                "2023-01-02",
                "Andromeda Galaxy",
                "The Andromeda Galaxy is a spiral galaxy approximately 2.5 million light-years from Earth. It is the nearest major galaxy to the Milky Way and is expected to collide with our galaxy in about 4.5 billion years.",
                "https://esahubble.org/media/archives/images/large/heic1502a.jpg",
                "NASA/ESA",
                quizzes2
        ));

        // Image 3: Saturn and its rings
        List<Quiz> quizzes3 = Arrays.asList(
                new Quiz(
                        "What are Saturn's rings primarily composed of?",
                        "Ice particles and rock debris",
                        "Liquid hydrogen", "Methane gas", "Molten iron"
                ),
                new Quiz(
                        "How many major rings does Saturn have?",
                        "7",
                        "3", "12", "5"
                ),
                new Quiz(
                        "Which NASA mission provided detailed images of Saturn's rings?",
                        "Cassini",
                        "Voyager", "New Horizons", "Juno"
                )
        );

        preloadedList.add(new ApodResponse(
                "2023-01-03",
                "Saturn and its Magnificent Rings",
                "Saturn is the sixth planet from the Sun and is famous for its spectacular ring system, which consists mainly of ice particles, rocky debris, and dust. The planet is a gas giant composed primarily of hydrogen and helium.",
                "https://solarsystem.nasa.gov/system/resources/detail_files/11453_PIA17218.jpg",
                "NASA/JPL-Caltech/SSI",
                quizzes3
        ));

        // Image 4: Black Hole M87
        List<Quiz> quizzes4 = Arrays.asList(
                new Quiz(
                        "What is the name of the project that first imaged this black hole?",
                        "Event Horizon Telescope",
                        "Black Hole Observer", "Hawking Telescope Project", "Cosmic Origin Imager"
                ),
                new Quiz(
                        "How massive is the M87 black hole compared to our Sun?",
                        "6.5 billion times more massive",
                        "1 million times more massive", "100 million times more massive", "1 billion times more massive"
                ),
                new Quiz(
                        "In which galaxy is this black hole located?",
                        "Messier 87",
                        "Milky Way", "Andromeda", "Sombrero Galaxy"
                )
        );

        preloadedList.add(new ApodResponse(
                "2023-01-04",
                "First Image of a Black Hole",
                "This is the first image ever taken of a black hole, captured by the Event Horizon Telescope. It shows the supermassive black hole at the center of galaxy M87, with its shadow surrounded by a bright ring of light where matter is falling into the black hole.",
                "https://www.nasa.gov/wp-content/uploads/2019/04/black-hole-image.jpg",
                "Event Horizon Telescope Collaboration",
                quizzes4
        ));

        // Image 5: Mars Perseverance
        List<Quiz> quizzes5 = Arrays.asList(
                new Quiz(
                        "In which Martian crater did Perseverance land?",
                        "Jezero Crater",
                        "Gale Crater", "Hellas Basin", "Olympus Mons"
                ),
                new Quiz(
                        "What small aircraft did Perseverance bring to Mars?",
                        "Ingenuity helicopter",
                        "Dragonfly drone", "Curiosity glider", "Martian flyer"
                ),
                new Quiz(
                        "What is one of the main objectives of the Perseverance mission?",
                        "Search for signs of ancient life",
                        "Establish a human colony", "Mine for precious metals", "Test Martian agriculture"
                )
        );

        preloadedList.add(new ApodResponse(
                "2023-01-05",
                "Mars Perseverance Rover",
                "NASA's Perseverance rover is exploring the Jezero Crater on Mars, searching for signs of ancient microbial life. The mission will characterize the planet's geology and past climate, and be the first mission to collect and cache Martian rock and regolith.",
                "https://mars.nasa.gov/system/downloadable_items/45507_PIA24428-1200.jpg",
                "NASA/JPL-Caltech",
                quizzes5
        ));

        // Image 6: International Space Station
        List<Quiz> quizzes6 = Arrays.asList(
                new Quiz(
                        "When did the first module of the ISS launch?",
                        "1998",
                        "1986", "2001", "2005"
                ),
                new Quiz(
                        "How long does it take the ISS to orbit Earth?",
                        "About 90 minutes",
                        "24 hours", "12 hours", "7 days"
                ),
                new Quiz(
                        "At what altitude does the ISS orbit Earth?",
                        "Approximately 400 km (250 miles)",
                        "100 km (62 miles)", "1,000 km (620 miles)", "10,000 km (6,200 miles)"
                )
        );

        preloadedList.add(new ApodResponse(
                "2023-01-06",
                "International Space Station",
                "The International Space Station is a large spacecraft in orbit around Earth. It serves as a home where crews of astronauts and cosmonauts live and conduct research in a unique microgravity laboratory.",
                "https://www.nasa.gov/wp-content/uploads/2021/07/iss065e176845.jpg",
                "NASA",
                quizzes6
        ));

        // Image 7: Earthrise
        List<Quiz> quizzes7 = Arrays.asList(
                new Quiz(
                        "During which Apollo mission was the original Earthrise photo taken?",
                        "Apollo 8",
                        "Apollo 11", "Apollo 13", "Apollo 17"
                ),
                new Quiz(
                        "Which astronaut took the original Earthrise photograph?",
                        "William Anders",
                        "Neil Armstrong", "Buzz Aldrin", "Jim Lovell"
                ),
                new Quiz(
                        "What year was the original Earthrise photo taken?",
                        "1968",
                        "1969", "1972", "1975"
                )
        );

        preloadedList.add(new ApodResponse(
                "2023-01-07",
                "Earthrise",
                "Earthrise is a photograph of Earth and some of the Moon's surface that was taken from lunar orbit by astronaut William Anders on December 24, 1968, during the Apollo 8 mission. The image is one of the most influential environmental photographs ever taken.",
                "https://www.nasa.gov/wp-content/uploads/2013/12/as08-14-2383.jpg",
                "NASA/William Anders",
                quizzes7
        ));

        return preloadedList;
    }
}