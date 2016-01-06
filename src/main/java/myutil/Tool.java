package myutil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 叶 on 2015/5/13.
 *
 */
public class Tool {

    /**
     * ReadFile
     * @param filePath The path of file
     * @param encode The encode of file
     * @return
     */
    public static List readFile(String filePath, String encode)
    {
        List list = new ArrayList<String>();
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader br;
        String line = null;
        try {
            fis = new FileInputStream(filePath);
            isr = new InputStreamReader(fis,encode);
            br = new BufferedReader(isr);
            while((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Write String to File
     * @param filePath
     * @param str   String to write
     * @param isAppend  true to append
     * @param encode
     * @return
     */
    public static boolean write(String filePath, String str ,boolean isAppend, String encode)
    {
        OutputStreamWriter osw = null;
        FileOutputStream fileOs = null;
        BufferedWriter bw = null;
        try {
            fileOs = new FileOutputStream(filePath, isAppend);
            osw = new OutputStreamWriter(fileOs,encode);
            bw = new BufferedWriter(osw);
            bw.append(str);
            bw.newLine();

            bw.close();
            osw.close();
            fileOs.close();

            return true;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  Write List<String> to File
     * @param filePath
     * @param list
     * @param isAppend
     * @param encode
     * @return
     */
    public static boolean write(String filePath, List<String> list, boolean isAppend, String encode)
    {
        OutputStreamWriter osw = null;
        FileOutputStream fileOs = null;
        BufferedWriter bw = null;
        try {
            fileOs = new FileOutputStream(filePath, isAppend);
            osw = new OutputStreamWriter(fileOs,encode);
            bw = new BufferedWriter(osw);
            for (String s : list)
            {
                if(!s.equals(""))
                {
                    bw.append(s);
                    bw.newLine();
                }
            }

            bw.close();
            osw.close();
            fileOs.close();

            return true;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * remove \n | \r from text
     * @param text
     * @return
     */
    public static String removeEol(String text)
    {
        if(text == null) {
            return text;
        }
        if(text.contains("\n")) {
            text = text.replaceAll("\n", "");
        }
        if(text.contains("\r")) {
            text = text.replaceAll("\r", "");
        }
        if(text.contains("\n\r")) {
            text = text.replaceAll("\n", "\n\r");
        }
        if(text.contains("\r\n")) {
            text = text.replaceAll("\r\n", "");
        }
        return text;
    }

    /**
     * Delete File
     * @param file
     * @return
     */
    public static boolean deleteFile(File file)
    {
        try {
            if (file.exists()) {
                file.delete();
                System.out.println("delete："+file.getName());
                return true;
            }
        }
        catch (Exception e) {
            System.out.println("delete failed："+file.getName());
            e.printStackTrace();
        }
        return false;
    }
}
