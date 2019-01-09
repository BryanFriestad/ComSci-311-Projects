import java.io.*;
import java.util.ArrayList;

public class ImageProcessor {

    private ArrayList<ArrayList<ImagePixel>> M;
    private int width;
    private int height;

    /**
     * (a) First line contains the height H of the image as a number
     * (b) Second line contains the width W of the image as a number
     * (c) All subsequent lines contains pixel values at M[i,j] (0 ≤ i < H, 0 ≤ j < W). For instance,
     *
     * 3
     * 4
     * 98 251 246 34 0 246 255 246 127 21 0 231
     * 25 186 221 43 9 127 128 174 100 88 1 143
     * 46 201 132 23 5 217 186 165 147 31 8 251
     *
     * The numbers in the same line are separated by a single space. You can decide the way you want to represent the matrix M.
     */
    public ImageProcessor(String FName) {
        M = new ArrayList<>();
        File file = new File(FName);
        FileReader reader;
        BufferedReader buff_reader;
        try {
            reader = new FileReader(file);
            buff_reader = new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        ArrayList<String> lines = new ArrayList<String>();
        try{
            while(buff_reader.ready()){
                lines.add(buff_reader.readLine());
            }
            height = Integer.parseInt(lines.get(0));
            width = Integer.parseInt(lines.get(1));
            if(height <= 1 || width <= 1){
                throw new IllegalStateException("Error: image must have height and width greater than 1");
            }
            if(lines.size() != height + 2){
                throw new IllegalStateException("Error: image data is malformed, height does not match");
            }

            ArrayList<ImagePixel> row;
            for(int i = 0; i < height; i++){
                String[] line = lines.get(i + 2).split(" ");
                if(line.length != 3*width){
                    throw new IllegalStateException("Error: image data is malformed, width does not match");
                }
                Integer[] data = new Integer[line.length];
                for(int j = 0; j < line.length; j++){
                    data[j] = Integer.parseInt(line[j]);
                }

                row = new ArrayList<>();
                for(int j = 0; j < width; j++){
                    int r = data[3*j];
                    int g = data[3*j + 1];
                    int b = data[3*j + 2];
                    row.add(new ImagePixel(r, g, b));
                }
                M.add(row);
            }
        }
        catch(IOException e){
            e.printStackTrace();
            return;
        }
    }

    // pre:
    // post: returns the 2-D matrix I as per its definition
    public ArrayList<ArrayList<Integer>> getImportance(){
        ArrayList<ArrayList<Integer>> impoMat = new ArrayList<>();
        ArrayList<Integer> row;
        for(int i = 0; i < height; i++){
            row = new ArrayList<>();
            for(int j = 0; j < width; j++){
                row.add(getImportanceOfPixel(i, j));
            }
            impoMat.add(row);
        }
        return impoMat;
    }

    // pre:  W-k > 1
    // post: Compute the new image matrix after reducing the width by k
    //       Follow the method for reduction described above
    //       Write the result in a file named FName
    //       in the same format as the input image matrix
    public void writeReduced(int k, String FName){
        if(width - k <= 1){
            throw new IllegalArgumentException("Error: image is not wide enough to reduce width by " + k + " pixels");
        }

        ArrayList<ArrayList<ImagePixel>> temp = cloneImage();
        int orig_width = width;

        for(int j = 0; j < k; j++){
            WGraph graph = new WGraph(importanceMatrixToTextGraph(getImportance()), this.height + 1, this.width); //height plus one for the trash row
            ArrayList<Integer> top_row = new ArrayList<>();
            ArrayList<Integer> trash_row = new ArrayList<>(); //the row of trash vertices that are past the bottom pixels
            for(int i = 0; i < width; i++){
                top_row.add(0);
                top_row.add(i);
                trash_row.add(height);
                trash_row.add(i);
            }
            ArrayList<Integer> cut_path = graph.S2S(top_row, trash_row);
            makeCut(cut_path);
        }
        writeImageToFile(FName);

        M = temp;
        width = orig_width;
    }

    private Integer getImportanceOfPixel(int x, int y){
        return getXImportance(x, y) + getYImportance(x, y);
    }

    /**
     * PDist(M[H−1,j], M[i+1,j])    if i=0
     * PDist(M[i−1,j], M[0,j])      if i = H −1
     * PDist(M[i−1,j], M[i+1,j])    otherwise
     */
    private Integer getYImportance(int x, int y){
        if(x == 0){
            return ImagePixel.PixelDistance(M.get(height - 1).get(y), M.get(x + 1).get(y));
        }
        else if(x == height - 1){
            return ImagePixel.PixelDistance(M.get(x - 1).get(y), M.get(0).get(y));
        }
        else{
            return ImagePixel.PixelDistance(M.get(x - 1).get(y), M.get(x + 1).get(y));
        }
    }

    /**
     * PDist(M[i,W - 1], M[i,j + 1])    if j=0
     * PDist(M[i,j - 1], M[i,0])        if j = W − 1
     * PDist(M[i,j - 1], M[i,j + 1])    otherwise
     */
    private Integer getXImportance(int x, int y){
        if(y == 0){
            return ImagePixel.PixelDistance(M.get(x).get(width - 1), M.get(x).get(y + 1));
        }
        else if(y == width - 1){
            return ImagePixel.PixelDistance(M.get(x).get(y - 1), M.get(x).get(0));
        }
        else{
            return ImagePixel.PixelDistance(M.get(x).get(y - 1), M.get(x).get(y + 1));
        }
    }

    private ArrayList<String> importanceMatrixToTextGraph(ArrayList<ArrayList<Integer>> impoMat){
        ArrayList<String> graph = new ArrayList<>();
        int num_verts = width * (height + 1);
        /*
        height + 1 because the WGraph needs to be given a row of trash
        vertices so that the importance of the last pixels are taken into account
         */
        int num_edges = (height) * (((width - 2) * 3) + 4);
        graph.add(Integer.toString(num_verts));
        graph.add(Integer.toString(num_edges));
        int actual_edges = 0;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if(j == 0){
                    String edge1 = i + " " + j + " " + (i + 1) + " " + j + " " + impoMat.get(i).get(j);
                    String edge2 = i + " " + j + " " + (i + 1) + " " + (j + 1) + " " + impoMat.get(i).get(j);
                    graph.add(edge1);
                    graph.add(edge2);
                    actual_edges += 2;
                }
                else if(j == width - 1){
                    String edge1 = i + " " + j + " " + (i + 1) + " " + (j - 1) + " " + impoMat.get(i).get(j);
                    String edge2 = i + " " + j + " " + (i + 1) + " " + j + " " + impoMat.get(i).get(j);
                    graph.add(edge1);
                    graph.add(edge2);
                    actual_edges += 2;
                }
                else{
                    String edge1 = i + " " + j + " " + (i + 1) + " " + (j - 1) + " " + impoMat.get(i).get(j);
                    String edge2 = i + " " + j + " " + (i + 1) + " " +  j      + " " + impoMat.get(i).get(j);
                    String edge3 = i + " " + j + " " + (i + 1) + " " + (j + 1) + " " + impoMat.get(i).get(j);
                    graph.add(edge1);
                    graph.add(edge2);
                    graph.add(edge3);
                    actual_edges += 3;
                }
            }

        }
        if(actual_edges != num_edges){
            throw new IllegalStateException("Error: for some reason number of edges made by importance matrix is not correct");
        }
        return graph;
    }

    private void makeCut(ArrayList<Integer> cut_path){
        for(int i = 0; i < cut_path.size() - 2; i+=2){ //size - 2 because we are ignoring the trash vertex at the end of the path
            int x = cut_path.get(i);
            int y = cut_path.get(i + 1);
            M.get(x).remove(y); //removes the pixel at row x and column y
        }
        width--;
    }

    private void writeImageToFile(String filename){
        ArrayList<String> output = new ArrayList<>();
        output.add(Integer.toString(height));
        output.add(Integer.toString(width));
        for(int i = 0; i < height; i++){
            String line = "";
            for(int j = 0; j < width; j++){
                ImagePixel p = M.get(i).get(j);
                line += p.getR() + " ";
                line += p.getG() + " ";
                line += p.getB();
                if(j != width - 1){
                    line += " "; //if this is the last pixel in the row, dont add a space after the blue data
                }
            }
            output.add(line);
        }

        try {
            Writer fileWriter = new FileWriter(filename);
            for(String s : output){
                fileWriter.write(s + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error when writing to external file: " + filename);
            e.printStackTrace();
        }
    }

    private ArrayList<ArrayList<ImagePixel>> cloneImage(){
        ArrayList<ArrayList<ImagePixel>> clone = new ArrayList<>();
        for(int i = 0; i < height; i++){
            ArrayList<ImagePixel> row = new ArrayList<>();
            for(int j = 0; j < width; j++){
                row.add(M.get(i).get(j));
            }
            clone.add(row);
        }
        return clone;
    }
}
