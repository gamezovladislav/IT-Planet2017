package org.worlditplanet.java;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sun.invoke.empty.Empty;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Реализация расчёта и высталения счетов абонентам оператора.
 *
 * @author Gamezo Vladislav, Sergey Morgunov {@literal <smorgunov@at-consulting.ru>}
 */
public class BillingEngineImpl implements BillingEngine {

    private Map<Long, Tariff> tariffs = new HashMap<Long, Tariff>();
    private Map<Long, Long> subscribers = new HashMap<Long, Long>();

    private void getTariffs(Path tariffsPath) {
        try {
            Document document =
                    DocumentBuilderFactory.
                            newInstance().newDocumentBuilder().parse(new FileInputStream(tariffsPath.toFile()));
            Node root = document.getDocumentElement();
//            System.out.println(root.getNodeName());
            NodeList tars = root.getChildNodes();
//            System.out.println(tars.getLength());
            for (int i = 0; i < tars.getLength(); i++) {
                long id = -1L;
                String name = "";
                double abonentFee = -1.0D;
                double smsPrice = -1.0D;
                double callPrice = -1.0D;
                double intPrice = -1;
                String uom = "";
                int smsPack = -1;
                int callPack = -1;
                long intPack = -1L;
                Node tar = tars.item(i);
                if (tar.getNodeType() != Node.TEXT_NODE) {
                    NamedNodeMap rootFields = tar.getAttributes();
                    id = new Long(rootFields.getNamedItem("id").getNodeValue());
                    name = rootFields.getNamedItem("name").getNodeValue();
                    NodeList pricesAndPackets = tar.getChildNodes();
                    for (int k = 0; k < pricesAndPackets.getLength(); k++) {
                        Node node = pricesAndPackets.item(k); //prices, packets
                        if (node.getNodeType() != Node.TEXT_NODE) {
                            if (node.getNodeName().equals("prices")) {
                                NodeList childrenOfPrice = node.getChildNodes(); //abonentFee, sms, call, internet
                                for (int j = 0; j < childrenOfPrice.getLength(); j++) {
                                    Node child = childrenOfPrice.item(j);
                                    if (child.getNodeType() != Node.TEXT_NODE) {
//                                        System.out.println(j + " " + child.getNodeName());
                                        if (child.getNodeName().equals("abonentFee")) {
//                                            System.out.println();
                                            abonentFee = Double.parseDouble(child.getAttributes().getNamedItem("value").getNodeValue());
                                        } else if (child.getNodeName().equals("sms")) {
                                            smsPrice = Double.parseDouble(child.getAttributes().getNamedItem("value").getNodeValue());
                                        } else if (child.getNodeName().equals("call")) {
                                            callPrice = Double.parseDouble(child.getAttributes().getNamedItem("value").getNodeValue());
                                        } else if (child.getNodeName().equals("internet")) {
                                            intPrice = Double.parseDouble(child.getAttributes().getNamedItem("value").getNodeValue());
                                            uom = child.getAttributes().getNamedItem("uom").getNodeValue();
                                            intPrice *= convertTo(uom);
                                        } else throw new RuntimeException("unknown field");
                                    }
                                }
                            } else if (node.getNodeName().equals("packets")) {
                                NodeList childrenOfPackets = node.getChildNodes(); //abonentFee, sms, call, internet
                                for (int j = 0; j < childrenOfPackets.getLength(); j++) {
                                    Node child = childrenOfPackets.item(j);
                                    if (child.getNodeType() != Node.TEXT_NODE) {
                                        if (child.getNodeName().equals("sms")) {
                                            smsPack = Integer.parseInt(child.getAttributes().getNamedItem("value").getNodeValue());
                                        } else if (child.getNodeName().equals("call")) {
                                            callPack = Integer.parseInt(child.getAttributes().getNamedItem("value").getNodeValue());
                                        } else if (child.getNodeName().equals("internet")) {
                                            intPack = Long.parseLong(child.getAttributes().getNamedItem("value").getNodeValue());
                                            uom = child.getAttributes().getNamedItem("uom").getNodeValue();
                                            intPack *= convertTo(uom);
                                        } else throw new RuntimeException("unknown field");
                                    }
                                }
                            } else throw new RuntimeException("IncorrectFormat tariffs.xml");
                        }
                    }
                    if (id >= 0 && abonentFee >= 0 && smsPrice >= 0 && callPrice >= 0 && intPrice >= 0 && smsPack >= 0 && callPack >= 0 && name.length() > 0) {
                        tariffs.put(id, new Tariff(id, name, abonentFee, smsPrice, callPrice, intPrice, smsPack, callPack, intPack));
                    } else throw new RuntimeException("Tariff with id "+id+" incorrect");
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long convertTo(String uom) {
        return uom.equals("kb") ? 1L :
                uom.equals("mb") ? 1024L :
                        1048576L;
    }

    private void getSubscribers(Path subscribersPath) {
        try {
            Document document =
                    DocumentBuilderFactory.
                            newInstance().newDocumentBuilder().parse(new FileInputStream(subscribersPath.toFile()));
            Node root = document.getDocumentElement();
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() != Node.TEXT_NODE) {
                    String sId = child.getAttributes().getNamedItem("msisdn").getNodeValue();
                    if (!isNumber(sId)) throw new RuntimeException("Subscriber with msisdn "+sId+" incorrect");
                    Long id = Long.valueOf(sId);
                    String sTariff = child.getAttributes().getNamedItem("msisdn").getNodeValue();
                    if (!isNumber(sTariff)) throw new RuntimeException("Subscriber with msisdn "+sId+" incorrect");
                    if (!tariffs.containsKey(Long.valueOf(sTariff))) throw new RuntimeException("Subscriber with msisdn "+sId+" incorrect");
                    Long tariff = Long.valueOf(sTariff);
                    subscribers.put(id, tariff);
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private boolean isNumber(String s) {
        if (s.length() != 11) return false;
        for (int i = 0; i < 11; i++) {
            if (!(s.charAt(i) >= '0' && s.charAt(i) <= '9')) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void billing(Path tariffsPath, Path subscribersPath, Path actionsPath, Path invoicesPath) {
        getTariffs(tariffsPath);
        getSubscribers(subscribersPath);
        try {
            ZipFile zf = new ZipFile(String.valueOf(actionsPath));
            ZipEntry ze = zf.getEntry("name");
            InputStream is = zf.getInputStream(ze);
            Files.copy(is, Paths.get("actions.xml"));
            File action = new File("actions.xml");
            Document document =
                    DocumentBuilderFactory.
                            newInstance().newDocumentBuilder().parse(new FileInputStream(subscribersPath.toFile()));
            Node root = document.getDocumentElement();
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeType() != Node.TEXT_NODE) {
                    Node child = children.item(i);
                    String id = child.getAttributes().getNamedItem("msisdm").getNodeValue();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
