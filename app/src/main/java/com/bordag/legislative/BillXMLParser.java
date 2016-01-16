package com.bordag.legislative;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by Bordag on 1/9/2016.
 */
public class BillXMLParser {

    Document doc;


    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public BillXMLParser(InputStream inputStream)
    {
        try
        {
            File tempFile = File.createTempFile("pre", "suf");
            copyFile(inputStream, new FileOutputStream(tempFile));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(tempFile);
            doc.getDocumentElement().normalize();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean PrintValues()
    {
        boolean bSuccess = false;
        try
        {
            System.out.println("root of xml file" + doc.getDocumentElement().getNodeName());
            NodeList nodes = doc.getElementsByTagName("bill");
            System.out.println("==========================");

            for (int i = 0; i < nodes.getLength(); i++)
            {
                Node node = nodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element) node;
                    System.out.println("Title: " + getValue("title", element));
                    System.out.println("Number: " + getValue("number", element));
                    System.out.println("Introduced: " + getValue("introduced_date", element));
                    System.out.println("Committees: " + getValue("committees", element));
                }
            }
            bSuccess = true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return bSuccess;
    }

    public int getNumOfBills()
    {
        NodeList nodes = doc.getElementsByTagName("bill");
        return nodes.getLength();
    }

    public String getBillTitle(int i)
    {
        String sTitle = null;

        try
        {
            NodeList nodes = doc.getElementsByTagName("bill");

            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                sTitle = getValue("title", element);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return sTitle;
    }

    public String getBillNumber(int i)
    {
        String sNumber = null;

        try
        {
            NodeList nodes = doc.getElementsByTagName("bill");

            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                sNumber = getValue("number", element);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return sNumber;
    }

    private static String getValue(String tag, Element element)
    {
        NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodes.item(0);
        return node.getNodeValue();
    }



}
