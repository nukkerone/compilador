/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filereader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author mountainlion
 */
public class OutputCode {
    private ArrayList<String> source;
    private String filePath;
    
    public OutputCode(String filePath, ArrayList source) {
        this.source = source;
        this.filePath = filePath;
    }
    
    public OutputCode(String filePath) {
        this.filePath = filePath;
    }
    
    public void addLine(String newLine) {
        this.source.add(newLine);
    }
    
    public void append(String string) {
        if (this.source.isEmpty()) {
            this.source.add(string);
            return;
        }
        int lastIndex = this.source.size()-1;
        String lastLine = this.source.get(lastIndex);
        this.source.add(lastIndex, lastLine + string);
    }
    
    public boolean output() {
        try {
        BufferedWriter output = new BufferedWriter(new FileWriter(this.filePath));
            for (int i = 0; i < this.source.size(); i++) {
                output.write("test " + "\n");
            }
            output.close();
            return false;
        } catch (IOException e) {
            return false;
        }
    }
    
}
