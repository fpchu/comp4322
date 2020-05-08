package com.LSRCompute.GUI;

/* Data Structure */
import java.io.FileWriter;
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
import com.LSRCompute.Util.LSRCompute;


public class App {

    private JButton loadFile;
    private JPanel panelMain;
    private JButton addNodeButton;
    private JButton breakLinkButton;
    private JTextField addNodeText;
    private JTextField breakLinkText;
    private JTextArea lsa_info_display;
    private JTextField selectSourceText;
    private JButton singleStep;
    private JButton computeAll;
    private JTextArea status_line_display;
    private JButton deleteNodeButton;
    private JTextField deleteNodeText;
    private JButton nextButton;
    private JButton showAllButton;
    private JButton dumpLSAFileButton;
    private JButton clearButton;
    private JButton modifyNetworkButton;

    LSRCompute compute; // This is the Main object


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

                    lsa_info_display.setText(null);
                    lsa_info_display.append(lsa_to_String(lsa));

                    compute = new LSRCompute(lsa); /* initialize the LSRCompute object */
                }
            }
        });


        breakLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (compute == null) {
                    ;
                }
                //compute.breakLink();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status_line_display.setText(null);
            }
        });

        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status_line_display.setText(null);
                status_line_display.setText(compute.allRoutersPath());
            }
        });

        computeAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = selectSourceText.getText();
                String[] src = text.split(",");
                for (String s: src) {
                    status_line_display.setText(null);
                    status_line_display.append(compute.routerPaths(s));
                }
            }
        });

        singleStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String text = selectSourceText.getText();
                    String[] s = compute.routerPaths(text).split("\n");
                    int i = 0;
                    do {
                        status_line_display.append(s[i++]);
                    } while (i < s.length && e.getSource().equals(nextButton));

                } catch (Exception error) {
                    JOptionPane.showMessageDialog(null, "Invalid source input");
                }
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ;
            }
        });

        addNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (compute == null)
                    JOptionPane.showMessageDialog(
                            null, "Please load the lsa file beforehand.");

                String text = addNodeText.getText();

                if (text == "")
                    JOptionPane.showMessageDialog(
                            null, "Input cannot be null.");

                String[] newNodes = text.split(",");

                HashMap<String, HashMap<String, Integer>> addItem = new HashMap<String, HashMap<String, Integer>>();
                for (String s: newNodes) {
                    addItem.putAll(string2lsa(s.trim()));
                }

                for (String node: addItem.keySet()) {
                    if (compute.getNetwork().containsKey(node)) {
                        addItem.remove(node);
                        JOptionPane.showMessageDialog(null, "Node already exists");
                        continue;
                    }
                    compute.addNode(addItem);
                }
                lsa_info_display.setText(null);
                lsa_info_display.setText(lsa_to_String(compute.getLsa()));
            }
        });
        deleteNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = deleteNodeText.getText();
                String[] nodes = text.split(",");
                if (text == "")
                    JOptionPane.showMessageDialog(
                            null, "Input cannot be null.");
                for (String node: nodes) {
                    compute.deleteNode(node.trim());
                }
                lsa_info_display.setText(null);
                lsa_info_display.setText(lsa_to_String(compute.getLsa()));
            }
        });

        breakLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = breakLinkText.getText();
                String[] links = text.split(",");
                for (String link: links) {
                    compute.deleteLink(link.trim());
                }
                lsa_info_display.setText(null);
                lsa_info_display.setText(lsa_to_String(compute.getLsa()));
            }
        });
        modifyNetworkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compute.update();
                System.out.println(compute.getNetwork().get("A"));
            }
        });

        dumpLSAFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // parent component of the dialog
                JFrame parentFrame = new JFrame();

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Specify a file to save");

                int userSelection = fileChooser.showSaveDialog(parentFrame);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    String path = fileToSave.getAbsolutePath();

                    String write = lsa2String(compute.getLsa());
                    try {
                        File myObj = new File(path);
                        myObj.createNewFile();

                        FileWriter myWriter = new FileWriter(path);
                        myWriter.write(write);
                        myWriter.close();

                    } catch (IOException error) {
                        System.out.println(error.getStackTrace());
                    }
                }
            }
        });
    }

    private static String lsa2String(HashMap<String, HashMap<String, Integer>> lsa) {
        String s = "";

        for (String node: lsa.keySet()) {
            String line = "";
            line += node + ": ";
            for (String sn: lsa.get(node).keySet()) {
                line += sn + ":" + lsa.get(node).get(sn) + " ";
            }
            line += "\n";
            s += line;
        }
        return s;
    }

    private static HashMap<String, HashMap<String, Integer>> string2lsa(String s) {
        HashMap<String, HashMap<String, Integer>> m = new HashMap<String, HashMap<String, Integer>>();
        String[] line = s.split(" ");
        String key = line[0].replace(":", "");
        HashMap<String, Integer> value = new HashMap<String, Integer>();
        for (int i=1; i < line.length; i++) {
            // Get the route table (value)
            String[] t = line[i].split(":");
            String subKey = t[0];
            Integer subVal = Integer.parseInt(t[1]);
            value.put(subKey, subVal);
        }
        m.put(key, value);
        return m;
    }

    private static HashMap<String, HashMap<String, Integer>> read_lsa(File fn) throws IOException {
        HashMap<String, HashMap<String, Integer>> m = new HashMap<String, HashMap<String, Integer>>();
        Scanner sc = new Scanner(fn);
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            m.putAll(string2lsa(line));
        }
        return m;
    }

    public static String lsa_to_String(HashMap<String, HashMap<String, Integer>> lsa) {

        Iterator it = lsa.entrySet().iterator();
        String s = "";
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            s += pair.getKey() + " -> " + pair.getValue() + "\n";
            //it.remove(); // avoids a ConcurrentModificationException
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


