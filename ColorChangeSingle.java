package com.example.a310287808.huedisco;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 11/21/2017.
 */

public class ColorChangeSingle {

    public String IPAddress = "192.168.86.21/api";
    public String HueUserName = "uYUjGN-2Qc4p5lO1FOC4NBAQZHjSJlgaWhXN6hYi";
    public String HueBridgeParameterType = "lights/19";
    public String finalURL;
    public String lightStatusReturned;
    public String Status;
    public String Comments;
    public StringBuffer br;
    public String ActualResult;
    public String ExpectedResult;

    AndroidDriver driver;
    Dimension size;
    //In IFTTT application, An applet is already created and single light is assigned to it to test color changing functionality

    public  void ColorChangeSingle(AndroidDriver driver,String fileName, String APIVersion, String SWVersion) throws IOException, JSONException, InterruptedException, ParseException {

            HttpURLConnection connection;

            finalURL = "http://" + IPAddress + "/" + HueUserName + "/" + HueBridgeParameterType;
            URL url = new URL(finalURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            br = new StringBuffer();

            String line = " ";
            while ((line = reader.readLine()) != null) {
                br.append(line);
            }
            String output = br.toString();
            //System.out.println(output);


            ColorChangeSingleStatus SingleStatus = new ColorChangeSingleStatus();
            lightStatusReturned = SingleStatus.ColorChangeSingleStatus(output);


        String Xval=lightStatusReturned.substring(1,6);
        String Yval=lightStatusReturned.substring(8,13);
        System.out.println(Xval);
        System.out.println(Yval);
        driver.navigate().back();
        driver.navigate().back();

        //Opening Harmony App
        driver.findElement(By.xpath("//android.widget.TextView[@text='Hue Disco']")).click();
        TimeUnit.SECONDS.sleep(3);

        // click on bulbs
        driver.findElement(By.xpath("//android.widget.TextView[@text='BULBS']")).click();
        TimeUnit.SECONDS.sleep(2);

        //Select All
        driver.findElement(By.xpath("//android.widget.Button[@text='SELECT NONE']")).click();
        TimeUnit.SECONDS.sleep(2);

        //select testing Lamp
        driver.findElement(By.xpath("//android.widget.TextView[@text='TestingLamp']")).click();
        TimeUnit.SECONDS.sleep(2);

        //clicking on top left 3 dots
        driver.findElement(By.xpath("//android.widget.ImageView[@bounds='[1300,98][1440,266]']")).click();
        TimeUnit.SECONDS.sleep(2);

        //select turn all on
        driver.findElement(By.xpath("//android.widget.TextView[@text='All Lights On']")).click();
        TimeUnit.SECONDS.sleep(2);

        // click on DISCO
        driver.findElement(By.xpath("//android.widget.TextView[@text='DISCO']")).click();
        TimeUnit.SECONDS.sleep(2);

        // tap on strobe to change the colors
        driver.findElement(By.id("nl.ijsdesign.huedisco:id/fab_strobe")).click();
        TimeUnit.SECONDS.sleep(15);

        finalURL = "http://" + IPAddress + "/" + HueUserName + "/" + HueBridgeParameterType;
        URL url1 = new URL(finalURL);
        connection = (HttpURLConnection) url1.openConnection();
        connection.connect();

        InputStream stream1 = connection.getInputStream();

        BufferedReader reader1 = new BufferedReader(new InputStreamReader(stream1));

        br = new StringBuffer();

        String line1 = " ";
        while ((line1 = reader1.readLine()) != null) {
            br.append(line1);
        }
        String output1 = br.toString();
        //System.out.println(output);


        ColorChangeSingleStatus SingleStatus1 = new ColorChangeSingleStatus();
        lightStatusReturned = SingleStatus1.ColorChangeSingleStatus(output1);


        String Xgreen=lightStatusReturned.substring(1,6);
        String Ygreen=lightStatusReturned.substring(8,13);
        System.out.println(Xgreen);
        System.out.println(Ygreen);


        String output2 = br.toString();
        JSONObject jsonObject = new JSONObject(output2);

        Object ob = jsonObject.get("state");
        String newString = ob.toString();
        Object lightNameObject = jsonObject.get("name");
        String lightName = lightNameObject.toString();

        br.append(lightName);
        br.append("\n");

        if ((Xval.equals(Xgreen)) && (Yval.equals(Ygreen))) {

            Status = "0";
            ActualResult ="Color does not changed for the selected light"+"\n"+lightName;
            Comments = "FAIL:Light is not changing its colors";
            ExpectedResult= " Light: "+lightName+" should change its colors after starting hue disco feature";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);

        } else {
            Status = "1";
            ActualResult ="Color changed for the selected light"+"\n"+lightName;
            Comments = "NA";
            ExpectedResult= " Light: "+lightName+" should change its colors after starting hue disco feature";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);
        }
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
        storeResultsExcel(Status, ActualResult, Comments, fileName, ExpectedResult,APIVersion,SWVersion);

    }
    public String CurrentdateTime;
    public int nextRowNumber;
    public void storeResultsExcel(String excelStatus, String excelActualResult, String excelComments, String resultFileName, String ExcelExpectedResult
            ,String resultAPIVersion, String resultSWVersion) throws IOException {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        CurrentdateTime = sdf.format(cal.getTime());
        FileInputStream fsIP = new FileInputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        HSSFWorkbook workbook = new HSSFWorkbook(fsIP);
        nextRowNumber=workbook.getSheetAt(0).getLastRowNum();
        nextRowNumber++;
        HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFRow row2 = sheet.createRow(nextRowNumber);
        HSSFCell r2c1 = row2.createCell((short) 0);
        r2c1.setCellValue(CurrentdateTime);

        HSSFCell r2c2 = row2.createCell((short) 1);
        r2c2.setCellValue("15");

        HSSFCell r2c3 = row2.createCell((short) 2);
        r2c3.setCellValue(excelStatus);

        HSSFCell r2c4 = row2.createCell((short) 3);
        r2c4.setCellValue(excelActualResult);

        HSSFCell r2c5 = row2.createCell((short) 4);
        r2c5.setCellValue(excelComments);

        HSSFCell r2c6 = row2.createCell((short) 5);
        r2c6.setCellValue(resultAPIVersion);

        HSSFCell r2c7 = row2.createCell((short) 6);
        r2c7.setCellValue(resultSWVersion);

        fsIP.close();
        FileOutputStream out =
                new FileOutputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        workbook.write(out);
        out.close();
    }


}
