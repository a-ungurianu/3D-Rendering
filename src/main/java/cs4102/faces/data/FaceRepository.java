package cs4102.faces.data;

import cs4102.faces.Vector4;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FaceRepository {

    private final static String MESH_FILENAME = "mesh.csv";

    private final static String POSITION_FORMAT = "sh_%03d.csv";
    private final static String POSITION_WEIGHTS = "sh_ev.csv";

    private final static String TEXTURE_FORMAT = "tx_%03d.csv";
    private final static String TEXTURE_WEIGHTS = "tx_ev.csv";
    private final Mesh mesh;

    private List<Model> faces = new ArrayList<>();


    public FaceRepository(Path dataPath, int noFaces) throws IOException {

        Path averagePositionsFile = dataPath.resolve(String.format(POSITION_FORMAT, 0));

        Matrix averagePositions = matrixFromCSVFile(averagePositionsFile);

        Path positionWeightsFile = dataPath.resolve(POSITION_WEIGHTS);

        List<Double> positionWeights = Files.lines(positionWeightsFile).map(Double::valueOf).collect(Collectors.toList());


        Path averageTexturesFile = dataPath.resolve(String.format(TEXTURE_FORMAT, 0));

        Matrix averageTextures = matrixFromCSVFile(averageTexturesFile);

        Path textureWeightsFile = dataPath.resolve(TEXTURE_WEIGHTS);

        List<Double> textureWeights = Files.lines(textureWeightsFile).map(Double::valueOf).collect(Collectors.toList());


        Path meshFile = dataPath.resolve(MESH_FILENAME);

        List<int[]> triangles = Files.lines(meshFile).map(s -> s.split(",")).map(vals -> new int[]{Integer.parseInt(vals[0]),
                Integer.parseInt(vals[1]),
                Integer.parseInt(vals[2])}).collect(Collectors.toList());

        this.mesh = new Mesh(triangles);

        List<Vertex> averageVertices = verticesFromMatrices(averagePositions, averageTextures);

        faces.add(new Model(mesh, averageVertices));

        for (int i = 1; i <= noFaces; i++) {
            Path positionFile = dataPath.resolve(String.format(POSITION_FORMAT, i));
            Matrix positionOffsets = matrixFromCSVFile(positionFile).multiply(positionWeights.get(i-1)*5);
            Matrix positions = averagePositions.add(positionOffsets);

            Path textureFile = dataPath.resolve(String.format(TEXTURE_FORMAT, i));
            Matrix textureOffsets = matrixFromCSVFile(textureFile).multiply(textureWeights.get(i-1));
            Matrix textures = averageTextures.add(textureOffsets);


            List<Vertex> vertices = verticesFromMatrices(positions, textures);

            faces.add(new Model(mesh, vertices));
        }
    }


    public Model getFace(int index) {
        return faces.get(index);
    }

    private Matrix matrixFromCSVFile(Path file) throws IOException {
        return Basic2DMatrix.fromCSV(String.join("\n", Files.readAllLines(file)));
    }

    public static  List<Vertex> verticesFromMatrices(Matrix positions, Matrix textures) {
        List<Vertex> vertices = new ArrayList<>();

        for(int j = 0; j < positions.rows(); ++j) {
            vertices.add(new Vertex(new Vector4(positions.getRow(j)),new Vector4(textures.getRow(j))));
        }

        return vertices;
    }

    public int faceCount() {
        return faces.size();
    }

    public Mesh getMesh() {
        return mesh;
    }
}
