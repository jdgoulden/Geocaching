// john goulden
// DMtoKML
// april 1, 2015

// this program reads in a file of degree-minute coordinate pairs
// in the format displayed on geocaching.com pages and
// converts them to signed decimal degrees, then
// writes a keyhold markup language (kml) file suiteable for use with Google Maps

// the input format is three comma-delimited fields
// name, first coordinate, second coordinate
// with the compass point, degrees and minutes separated by spaces
// as shown here

// Name with or without spaces, N 35 23.333, W 97 32.333

// coordinate pairs can be in either order (N/S first or E/W first)
// and compass directions N S E W can be in upper or lower case

// trailing empty lines in the input file might generate an "Improper coordinate format" message
// but if it's at the end of the data file the .kml file will be fine

// USAGE: java DMtoKML inputfile outputfile

// input is read from inputfile and output is written to outputfile

// USAGE: java DMtoKML inputfile

// input is read from inputfile; the output file name is the same as inputfile
// with the file extention (if any) replaced with .kml

// USAGE: java DMtoKML

// the user is prompted for both the input and output file names

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DMtoKML{
  public static void main(String [] args){
    String inputFile;
    String outputFile;
    System.out.println();
    if (args.length == 1){
      inputFile = args[0];
      int l = inputFile.lastIndexOf(".");
      if (l != -1){
        outputFile = inputFile.substring(0,l) + ".kml";
      }
      else{
        outputFile = inputFile + ".kml";
      }
    } 
    else if(args.length == 2){
      inputFile = args[0];
      outputFile = args[1];
    }
    else{
      Scanner s = new Scanner(System.in);
      System.out.print("Input file: ");
      inputFile = s.nextLine();
      System.out.println("Output file: ");
      outputFile = s.nextLine();
    }

    try{
      Scanner fin = new Scanner(new File(inputFile));
      System.out.println("Reading data from " + inputFile + ", writing to " + outputFile + "...");
      System.out.println();

      BufferedWriter fout = new BufferedWriter(new FileWriter(new File(outputFile)));

      fout.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      fout.newLine();
      fout.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
      fout.newLine();
      fout.write("  <Document>");
      fout.newLine();

      String b;
      String name, coord1, coord2;
      String [] d1;
      String [] d2;
      double degree1, degree2, north = 0, east = 0;

      while(fin.hasNextLine()){

        b = fin.nextLine();
        b = b.trim();
        System.out.print(b);

        String s[] = b.split(",");

        if (s.length != 3){
          System.out.println("Incorrect coordinate format!");
          continue;
        }

        name    = s[0].trim();
        coord1  = s[1].trim();
        coord2  = s[2].trim();

        d1 = coord1.split(" ");
        d2 = coord2.split(" ");

        degree1 = Double.parseDouble(d1[1]) + ( Double.parseDouble(d1[2])/60 );
        degree2 = Double.parseDouble(d2[1]) + ( Double.parseDouble(d2[2])/60 );

        if (d1[0].toUpperCase().equals("N")){
           north = degree1;
        }
        if (d1[0].toUpperCase().equals("S")){
           north = -1 * degree1;
        }
        if (d1[0].toUpperCase().equals("E")){
           east = degree1;
        }
        if (d1[0].toUpperCase().equals("W")){
           east = -1 * degree1;
        }
        if (d2[0].toUpperCase().equals("N")){
           north = degree2;
        }
        if (d2[0].toUpperCase().equals("S")){
           north = -1 * degree2;
        }
        if (d2[0].toUpperCase().equals("E")){
           east = degree2;
        }
        if (d2[0].toUpperCase().equals("W")){
           east = -1 * degree2;
        }

        System.out.println(" -> N: " + north + " E: " + east);

        fout.write("    <Placemark>");
        fout.newLine();
        fout.write("      <name>" + name + "</name>");
        fout.newLine();
        fout.write("      <Point>");
        fout.newLine();
        fout.write("        <coordinates>" + east + "," + north + ",0</coordinates>");
        fout.newLine();
        fout.write("      </Point>");
        fout.newLine();
        fout.write("    </Placemark>");
        fout.newLine();

      }

    fout.write("  </Document>");
    fout.newLine();
    fout.write("</kml>");
    fout.close();

    System.out.println();
    System.out.println("Done!");
    }
    catch(FileNotFoundException e){
      System.out.println("File " + inputFile + " not found!");
      System.exit(0);
    }
    catch(IOException e){
      System.out.println("There was a problem with the output file!");
      System.exit(0);
    }
  }
}

