package engine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
	private Vertex[] vertices;
	private int[] indices;
	private Mat mat;

	private int vertexArrayObject, positionBufferObject, imageBufferObject, colorBufferObject, textureBufferObject;

	public Mesh(Vertex[] vertices, int[] indices, Mat material) {
		this.vertices = vertices;
		this.indices = indices;
		this.mat = material;
	}

	public void create() {

		mat.create();

		/**
		 * Vertex Array Object(VAO)
		 * 
		 * 0 = position data object
		 * 
		 * 1 = texture data (buffer)
		 * 
		 * 2 = normal data (buffer)
		 * 
		 * 3 = animation data (buffer)
		 * 
		 * x -> infinity = other data (buffer)
		 */

		vertexArrayObject = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vertexArrayObject);

		// position
		FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
		float[] positionData = new float[vertices.length * 3];
		for (int i = 0; i < vertices.length; i++) {
			positionData[i * 3] = vertices[i].getPosition().getX();
			positionData[i * 3 + 1] = vertices[i].getPosition().getY();
			positionData[i * 3 + 2] = vertices[i].getPosition().getZ();
		}
		positionBuffer.put(positionData).flip();
		positionBufferObject = storeData(positionBuffer, 0, 3);

		// Color
		FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
		float[] colorData = new float[vertices.length * 3];
		for (int i = 0; i < vertices.length; i++) {
			colorData[i * 3] = vertices[i].getColor().getX();
			colorData[i * 3 + 1] = vertices[i].getColor().getY();
			colorData[i * 3 + 2] = vertices[i].getColor().getZ();
		}
		colorBuffer.put(colorData).flip();
		colorBufferObject = storeData(colorBuffer, 1, 3);

		// Texture
		FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(vertices.length * 2);
		float[] textureData = new float[vertices.length * 2];
		for (int i = 0; i < vertices.length; i++) {
			textureData[i * 2] = vertices[i].getTextureCoord().getX();
			textureData[i * 2 + 1] = vertices[i].getTextureCoord().getY();
		}
		textureBuffer.put(textureData).flip();
		textureBufferObject = storeData(textureBuffer, 2, 2);

		// End
		IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
		indicesBuffer.put(indices).flip();

		imageBufferObject = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, imageBufferObject);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

	}

	private int storeData(FloatBuffer buffer, int index, int size) {

		int bufferID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		return bufferID;

	}

	public void destroy() {

		GL15.glDeleteBuffers(positionBufferObject);
		GL15.glDeleteBuffers(colorBufferObject);
		GL15.glDeleteBuffers(imageBufferObject);
		GL15.glDeleteBuffers(textureBufferObject);

		GL30.glDeleteVertexArrays(vertexArrayObject);

		mat.destroy();

	}

	public Vertex[] getVertices() {
		return vertices;
	}

	public int[] getIndices() {
		return indices;
	}

	public Mat getMaterial() {
		return mat;
	}

	public int getVAO() {
		return vertexArrayObject;
	}

	public int getPBO() {
		return positionBufferObject;
	}

	public int getCBO() {
		return colorBufferObject;
	}

	public int getTBO() {
		return textureBufferObject;
	}

	public int getIBO() {
		return imageBufferObject;
	}

}
