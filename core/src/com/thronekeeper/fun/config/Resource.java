package com.thronekeeper.fun.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Resource {

    private static final Logger LOGGER = Logger.getLogger(Resource.class.getName());
    public static final Map<String, String> configurationMap = new HashMap<>();

    public static final String SPACESHIP;
    public static final String SPACE_BACKGROUND;
    public static final String SAUCER;
    public static final String ASTEROID;
    public static final String ASTEROID_BIT;
    public static final String SPACESHIP_BEAM;
    public static final String EVIL_BEAM;
    public static final String PEW_SOUND;
    public static final String BZZ_SOUND;
    public static final String UFO_SOUND;
    public static final String EXPLODE_SOUND;

    // TODO EXTRACT TO XML ARRAY
    public static final String[] UFO_ANIMATION = { "ufo_move_1.png", "ufo_move_2.png" };
    public static final String[] KABOOM_ANIMATION = { "kaboom1.png", "kaboom2.png", "kaboom3.png" };


    static {
        initializeMap();
        SPACESHIP = configurationMap.get("SPACESHIP");
        SPACE_BACKGROUND = configurationMap.get("SPACE_BACKGROUND");
        SAUCER = configurationMap.get("SAUCER");
        PEW_SOUND = configurationMap.get("PEW_SOUND");
        ASTEROID = configurationMap.get("ASTEROID");
        ASTEROID_BIT = configurationMap.get("ASTEROID_BIT");
        SPACESHIP_BEAM = configurationMap.get("SPACESHIP_BEAM");
        EVIL_BEAM = configurationMap.get("EVIL_BEAM");
        EXPLODE_SOUND = configurationMap.get("EXPLODE_SOUND");
        BZZ_SOUND = configurationMap.get("BZZ_SOUND");
        UFO_SOUND = configurationMap.get("UFO_SOUND");
    }

    private Resource() {
    }

    private static void initializeMap() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("configuration/strings.xml");
            document.getDocumentElement().normalize();

            Node resource = document.getElementsByTagName("resources").item(0);
            NodeList nodeList = resource.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node currentNode = nodeList.item(i);
                if (currentNode instanceof Element element
                        && element.hasAttribute("name")) {
                    Text data = (Text) element.getFirstChild();
                    configurationMap.put(element.getAttribute("name"), data.getData());
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    public static String get(String key) {
        return configurationMap.get(key);
    }


//    private Resource() {
//        ObjectMapper xmlMapper;
//    }
//
//    public static final String SPACE_BACKGROUND = "space.jpg";
//    public static final String SPACESHIP = "spaceship.png";
//    public static final String ASTEROID = "asteroid.png";
//    public static final String ASTEROID_BIT = "smallasteroid.png";
//    public static final String SAUCER = "saucer.png";
//    public static final String SPACESHIP_BEAM = "spaceship_beam.png";
//    public static final String EVIL_BEAM = "beam2.png";
//
//    public static final String PEW = "audio/pew.mp3";
//    public static final String EXPLODE = "audio/explode.mp3";
//    public static final String UFO_SOUND = "audio/ufo.mp3";
//    public static final String BZZ = "audio/bzz.mp3";
//
//    public static final String[] KABOOM_ANIMATION = { "kaboom1.png", "kaboom2.png", "kaboom3.png" };
//    public static final String[] UFO_ANIMATION = { "ufo_move_1.png", "ufo_move_2.png" };

}
