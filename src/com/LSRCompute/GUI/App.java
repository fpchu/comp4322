package com.LSRCompute.GUI;

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

public class App {
    private JButton loadFile;
    private JPanel panelMain;
    private JButton addNodeButton;
    private JButton addLinkButton;
    private JButton breakLinkButton;
    private JTextField addNodeText;
    private JTextField addLinkText;
    private JTextField breakLinkText;
    private JTextArea lsa_info_display;
    private JTextField selectSourceText;
    private JButton singleStep;
    private JButton computeAll;
    private JTextArea textArea1;

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
                    lsa_info_display.append(lsa_to_String(lsa));
                }

            }
        });
    }

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
        String s = "";
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            s += pair.getKey() + " -> " + pair.getValue() + "\n";
            it.remove(); // avoids a ConcurrentModificationException
        }
        return s;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}


