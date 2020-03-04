package xmlws.roombooking.xmltools;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RoomBookingSaxParser implements RoomBookingParser {

    private RoomBooking roomBooking = new RoomBooking();
    private String localNameTmp;

    /**
     * Parse an xml file provided as an input stream
     *
     * @param inputStream the input stream corresponding to the xml file
     * @return the corresponding RoomBooking object
     */
    @Override
    public RoomBooking parse(InputStream inputStream) {

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            SAXParser saxParser = spf.newSAXParser();
            saxParser.parse(inputStream, new RoomBookingSaxParser.RoomBookingSaxHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return roomBooking;
    }

    private class RoomBookingSaxHandler extends DefaultHandler {

        public void startElement(String namespaceURI,
                                 String localName,
                                 String qName,
                                 Attributes atts)
                throws SAXException {

            localNameTmp = localName;
        }

        public void characters(char ch[], int start, int length)
                throws SAXException {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String chaineTrouvee = new String(ch, start, length);

            if (chaineTrouvee.charAt(0) != '\n') {

                if (localNameTmp.equals("label")) {
                    roomBooking.setRoomLabel(chaineTrouvee);

                } else if (localNameTmp.equals("username")) {
                    roomBooking.setUsername(chaineTrouvee);

                } else if (localNameTmp.equals("startDate")) {
                    try {
                        roomBooking.setStartDate(sdf.parse(chaineTrouvee));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else if (localNameTmp.equals("endDate")) {
                    try {
                        roomBooking.setEndDate(sdf.parse(chaineTrouvee));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
