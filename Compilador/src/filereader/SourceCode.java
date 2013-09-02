/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package filereader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class SourceCode {
    private ArrayList<String> source;
    private String filePath;
    
    public SourceCode(String filePath) {
        this.source = new ArrayList();
        this.filePath = filePath;
    }
    
    /**
     * Generates the representation of the Source
     */
    public void generateSource() {
        BufferedReader br = null;
 
            try {
                String sCurrentLine;
                br = new BufferedReader(new FileReader(this.getFilePath()));
                while ((sCurrentLine = br.readLine()) != null) {
                        this.source.add(sCurrentLine);
                }
            } catch (IOException e) {
                    e.printStackTrace();
            } finally {
                try {
                        if (br != null)br.close();
                } catch (IOException ex) {
                        ex.printStackTrace();
                }
            }
    }

    /**
     * Returns the line numbers of the source code already read
     * @return int
     */
    public int lineNumbers() {
        return this.source.size();
    }
    
    public String getFilePath() {
        return this.filePath;
    }
    
    public Iterator createIterator() {
        ArrayList elements = new ArrayList<String>();
        
        for (int i = 1; i <= this.source.size(); i++) {    // Itera a traves de las lineas
            String[] words = this.source.get(i-1).split(" ");
            List<String> fixedWordsOnThisLine =  Arrays.asList(words);  // Array.asList devuelve un List estatico en tamaño
            ArrayList<String> wordsOnThisLine = new ArrayList<String>();
            wordsOnThisLine.addAll(fixedWordsOnThisLine);
            if (i < this.source.size()) {
                wordsOnThisLine.add("\n");    // Agrego el salto de linea que se perdio al crear la estructura de arreglo de lineas
            }
            elements.addAll(elements.size(), wordsOnThisLine);
        }
        
        return elements.iterator();
    }
    
}
