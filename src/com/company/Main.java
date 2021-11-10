package com.company;

import java.io.*;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main implements java.io.Serializable {

    private static int antalVare = 5;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        VareData[] shoppingList = new VareData[5];

        shoppingList =  textTilArray();

        printArray(shoppingList);

        lavDataFil(shoppingList);

        File file = new File("varer.set");

        shoppingList =  dataFilTilArray(file);

        printArray(shoppingList);

        double[] totalPrice =  totalprice(shoppingList);

        System.out.println("prisen uden rabet er: " + totalPrice[0] + "kr. og prisen med rabatter er: " + totalPrice[1] + "kr." );

        printReceipt(shoppingList);



    }

    public static VareData[] textTilArray () throws FileNotFoundException {

        File bestilling = new File("src/com/company/bestilling.txt");
        Scanner input = new Scanner(bestilling);
        VareData[] shoppingList = new VareData[5];

        for (int i = 0; i < antalVare; i++) {
        shoppingList[i] = new VareData(input.nextInt() ,input.next() , input.nextDouble()  );
        }
        input.close();
        return shoppingList;

    }

    public static void printArray (VareData[] array) {

        for ( VareData data : array) {
            System.out.println( data.getAntal() + " stk. " + data.getVareNavn() + " til en stykpris på " + data.getStykPris() + "kr." );
        }

    }


    public static void lavDataFil( VareData[] shoppingList ) throws IOException {

        File varer = new File("varer.set");

        if (varer.createNewFile()) {
            System.out.println("File created " + varer.getName());
        } else {
            System.out.println("File already exist");
        }
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(varer));

        for (VareData list : shoppingList) {
            output.writeObject(list);
        }

        output.close();

    }

    public static VareData[] dataFilTilArray(File data) throws IOException, ClassNotFoundException {

        ObjectInputStream input = new ObjectInputStream(new FileInputStream(data));
        List <VareData> list = new ArrayList<>();
        try {
            for (; ;) {
                list.add((VareData) input.readObject());
            }
        } catch (EOFException exc)  {
            // end of stream
        }

        input.close();

        VareData[] vareData = new VareData[list.size()];
        for (int i = 0; i < list.size(); i++) {
             vareData[i] =  list.get(i);
        }

        return vareData;

    }

    public static double[] totalprice(VareData[] array) {

        double totalPriceNoDiscount = 0;
        double totalPriceWithDiscount = 0;

        for (VareData items : array) {


            totalPriceNoDiscount += totalItemPrice(items)[0];
            totalPriceWithDiscount += totalItemPrice(items)[1];
        }

        return  new double[] {totalPriceNoDiscount , totalPriceWithDiscount};
    }


    public static double[] totalItemPrice(VareData item) {

        double priceNoDiscount = item.getAntal() * item.getStykPris();

        if (item.getAntal() > 10) {
            double priceWithDiscount = (item.getAntal() * item.getStykPris()) * 0.85;

            return new double[] { priceNoDiscount , priceWithDiscount  };
        } else {
            return new double[] {priceNoDiscount , priceNoDiscount};
        }

    }


    public static  void printReceipt ( VareData[] items) throws IOException {

        File receipt = new File ("faktura.txt");

        if (receipt.createNewFile()) {
            System.out.println("File created " + receipt.getName());
        } else {
            System.out.println("File already exist");
        }

        FileWriter fileWriter = new FileWriter(receipt);

        fileWriter.write("Fakta\n"); // Ja

        fileWriter.write("You have bought the following items\n\n");

        DecimalFormat df = new DecimalFormat("###.##");

        for (VareData item : items) {



            if (totalItemPrice(item)[1] != totalItemPrice(item)[0] ) {
                fileWriter.write(item.getAntal() + "\t" +  item.getVareNavn() +"\t\t " + "(spar "
                        + df.format( (totalItemPrice(item)[0] - totalItemPrice(item)[1])) +",-) \t\t kr." + df.format( totalItemPrice(item)[1]) + "\n") ;
            } else {
                fileWriter.write(item.getAntal() + "\t" +  item.getVareNavn() +"\t\t\t\t\t\t\t kr. " + df.format(totalItemPrice(item)[1]) + "\n");
            }

        }

        fileWriter.write("\nYour total price is: " + df.format( totalprice(items)[0] ) + "\n") ;
        fileWriter.write("Your total discount is: " + df.format(  totalprice(items)[0] - totalprice(items)[1]) + "\n"    );
        fileWriter.write("Your total price with discount is: " + df.format(totalprice(items)[1]));

        fileWriter.close();


        BufferedReader in = new BufferedReader(new FileReader("faktura.txt"));

        String line = in.readLine();
        while(line != null)  {
            System.out.println(line);
            line = in.readLine();
        }

        in.close();

    }


}
