package myutil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 叶 on 2015/5/13.
 * 该类用于写文件，写入流在完成写入后需手动关闭
 */
public class MyWriter {
    String outPath = ".//output";
    String encode = "utf-8";
    BufferedWriter writer = null;
    OutputStream outputStream = null;
    OutputStreamWriter outputStreamWriter = null;
    SimpleDateFormat inputFormat = new SimpleDateFormat("HH-mm");

    public MyWriter(){
        allocWriter();
    }

    public MyWriter(String oPath) {
        outPath = oPath;
        allocWriter();
    }

    public MyWriter(String oPath, String enCode) {
        encode = enCode;
        outPath = oPath;
        allocWriter();
    }

    public MyWriter(String oPath, boolean now){
        if(now)
            outPath = oPath +inputFormat.format((new Date()).getTime()).toString()+".txt";
        else
            outPath = oPath+".txt";
        allocWriter();
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public void allocWriter()
    {
        try {
            outputStream = new FileOutputStream(outPath,true);
            outputStreamWriter = new OutputStreamWriter(outputStream,encode);
            writer = new BufferedWriter(outputStreamWriter);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void Write(String str)
    {
        try {
            writer.append(str);
            writer.newLine();
            writer.flush();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void closeWrite() {
        try {
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ;
    }
}
