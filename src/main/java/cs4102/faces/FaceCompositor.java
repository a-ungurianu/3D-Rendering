package cs4102.faces;

import cs4102.faces.data.FaceRepository;
import cs4102.faces.data.Model;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.MatrixFactory;
import org.la4j.vector.dense.BasicVector;

import java.util.ArrayList;
import java.util.List;

public class FaceCompositor {


    private final Vector composition;
    private final FaceRepository repository;

    FaceCompositor(FaceRepository repository) {
        this.repository = repository;
        this.composition = BasicVector.constant(repository.faceCount(), 0);
        // The initial face composition is just the first face.
        composition.set(0, 1);
        composition.set(1,1);
    }

    void setWeight(int idx, double weight) {
        composition.set(idx, weight);
    }

    Model generateFace() {
        Vector normalized = composition.divide(composition.sum());

        List<Matrix> positions = new ArrayList<>();
        List<Matrix> textures = new ArrayList<>();

        for (int i = 0; i < normalized.length(); i++) {
            double weight = normalized.get(i);
            if(weight > 0) {
                Model face = repository.getFace(i);
                positions.add(face.getPositionMatrix().multiply(weight));
                textures.add(face.getTextureMatrix().multiply(weight));
            }
        }

        Matrix positionMatrix = positions.stream().reduce(Matrix::add).get();
        Matrix textureMatrix = textures.stream().reduce(Matrix::add).get();

        return new Model(repository.getMesh(), FaceRepository.verticesFromMatrices(positionMatrix, textureMatrix));
    }
}
