package com.LSRCompute;

/* Data Structure */
import java.util.HashMap;
import java.util.Iterator;

/* File handling */
import java.io.File;
import java.util.Scanner;
import java.io.IOException;

/* javax swing UI */
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* LSRCompute class */
import com.LSRCompute.LSRCompute;

public class App {
    private JButton loadFile;
    private JPanel panelMain;
    private JTextArea lsa_info;


    private static HashMap<String, HashMap<String, Integer>> read_lsa(File fn) throws IOException {
        HashMap<String, HashMap<String, Integer>> m = new HashMap<String, HashMap<String, Integer>>();
        Scanner sc = new Scanner(fn);
        while(sc.hasNextLine()) {
            String[] line = sc.nextLine().split(" ");
            String key = line[0].replace(":", ""); // Node identifier (key)
            HashMap<String, Integer> value = new HashMap<String, Integer>();
            for (int i=1; i < line.length; i++) {
                // Get the route table (value)
                String[] t = line[i].split(":");
                String subKey = t[0];
                Integer subVal = Integer.parseInt(t[1]);
                value.put(subKey, subVal);
            }
            m.put(key, value);
        }
        return m;
    }

    public static String lsa_to_String(HashMap<String, HashMap<String, Integer>> lsa) {
        Iterator it = lsa.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return "";
    }

    public App() {
        loadFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a FileChooser
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = jfc.showOpenDialog(null);
                // int returnValue = jfc.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    HashMap<String, HashMap<String, Integer>> lsa = null;
                    try {
                         lsa = read_lsa(selectedFile);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    System.out.println(lsa);
                    lsa_info.
                }

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}


