package be.thevaultraiders.service;

/**
 * Created by Arthur on 11/02/2016.
 */

import be.thevaultraiders.models.CustomerOrder;
import be.thevaultraiders.models.Product;
import be.thevaultraiders.models.Warehouse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextFileReader
{
    private BufferedReader reader;
    private String sCurrentLine;
    private int line;

    private int nRows;

    public int getnRows() {
        return nRows;
    }

    public int getnCols() {
        return nCols;
    }

    public int getnDrones() {
        return nDrones;
    }

    public int gettDeadline() {
        return tDeadline;
    }

    public int getMaxLoad() {
        return maxLoad;
    }

    public int getnWarehouses() {
        return nWarehouses;
    }

    public Warehouse[] getWarehouses() {
        return warehouses;
    }

    public int getnOrders() {
        return nOrders;
    }

    public CustomerOrder[] getOrders() {
        return orders;
    }

    private int nCols;
    private int nDrones;
    private int tDeadline;
    private int maxLoad;
    private int nWarehouses;
    private Warehouse[] warehouses;
    private int nOrders;
    private CustomerOrder[] orders;

    public void parseFile(String fileName)
    {
        line = 1;

        try {
            reader = new BufferedReader(new FileReader(fileName));
            sCurrentLine = reader.readLine();
            line++;
            //Read First Line
            String sCurrLineSplit[] = sCurrentLine.split(" ");

            nRows = Integer.parseInt(sCurrLineSplit[0]);
            nCols = Integer.parseInt(sCurrLineSplit[1]);
            nDrones = Integer.parseInt(sCurrLineSplit[2]);
            tDeadline = Integer.parseInt(sCurrLineSplit[3]);
            maxLoad = Integer.parseInt(sCurrLineSplit[4]);

            int[] weights = parseWeights();

            sCurrentLine = reader.readLine();
            line++;
            nWarehouses = Integer.parseInt(sCurrentLine);
            Warehouse[] warehouses = parseWarehouses(nWarehouses, weights);

            sCurrentLine = reader.readLine();
            line++;
            nOrders = Integer.parseInt(sCurrentLine);
            CustomerOrder[] orders = parseOrders(nOrders, weights);
            System.out.println("End Parse");
            /*
            while ((sCurrentLine != null && !sCurrentLine.contains("]"))) {

                switch(sCurrentLine){
                    case "OBJECTS[":{
                        System.out.println("\"OBJECTS\" detected, parsing...");
                        break;
                    }
                }
                sCurrentLine = reader.readLine();
                line++;
            }
            */

        }catch(StringIndexOutOfBoundsException e){
            System.err.println("Error occured at line " + line);
        }
        catch (IOException e) {
            System.err.println("Error occured at line " + line);
        } catch (NumberFormatException e) {
            System.err.println("Number parsing error occured at line " + line);
        } finally {
            try {
                if (reader != null)reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private int[] parseWeights() {
        String[] sCurrLineSplit;
        try {
            sCurrentLine = reader.readLine();
            line++;
            //Read First Line
            int nProducts = Integer.parseInt(sCurrentLine);
            sCurrentLine = reader.readLine();
            line++;
            sCurrLineSplit = sCurrentLine.split(" ");
        }catch(Exception e){
            System.err.println("Parse Exception");
            sCurrLineSplit = new String[0];
        }

        int[] weights = new int[sCurrLineSplit.length];
        for(int i=0; i<sCurrLineSplit.length; i++){
            weights[i]=(Integer.parseInt(sCurrLineSplit[i]));
        }
        return weights;
    }

    private Warehouse[] parseWarehouses(int nWarehouses, int[] weights){
        //public Warehouse(int locX, int locY, int numProductTypes)
        Warehouse[] warehouses = new Warehouse[nWarehouses];
        for(int i=0; i<nWarehouses; i++){
            try {
                sCurrentLine = reader.readLine();
                line++;
                //Read First Line
                String sCurrLineSplit[] = sCurrentLine.split(" ");
                warehouses[i] = new Warehouse(Integer.parseInt(sCurrLineSplit[0]), Integer.parseInt(sCurrLineSplit[1]), weights.length);

                sCurrentLine = reader.readLine();
                line++;
                String sCurrLineSplit2[] = sCurrentLine.split(" ");
                List<Product> list = new ArrayList<Product>();
                for(int j=0; j<sCurrLineSplit2.length; j++){
                    for(int k=0; k<Integer.parseInt(sCurrLineSplit2[j]); k++){
                        list.add(new Product(j, weights[j]));
                    }
                }
                warehouses[i].addStockProducts(list);

            }catch(Exception e){
                warehouses = new Warehouse[0];
            }
        }
        return warehouses;
    }

    private CustomerOrder[] parseOrders(int nOrders, int[] weights){
        CustomerOrder[] orders = new CustomerOrder[1250];
        for(int i=0; i<nOrders; i++){
            try {
                sCurrentLine = reader.readLine();
                line++;
                //Read First Line
                String sCurrLineSplit[] = sCurrentLine.split(" ");
                orders[i] = new CustomerOrder(Integer.parseInt(sCurrLineSplit[0]), Integer.parseInt(sCurrLineSplit[1]));
                sCurrentLine = reader.readLine();
                line++;
                int nOrdersLocal = Integer.parseInt(sCurrentLine);
                sCurrentLine = reader.readLine();
                line++;
                String sCurrLineSplit2[] = sCurrentLine.split(" ");
                for(int j=0; j<nOrdersLocal; j++){
                    orders[i].addProduct(new Product(Integer.parseInt(sCurrLineSplit2[j]), weights[Integer.parseInt(sCurrLineSplit2[j])]));
                }
            }catch(Exception e){
                orders = new CustomerOrder[0];
            }
        }
        return orders;
    }
}
