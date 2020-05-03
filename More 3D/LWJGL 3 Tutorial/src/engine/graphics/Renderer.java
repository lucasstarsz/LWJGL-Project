package engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class Renderer {

	private Shader shader;

	public Renderer(Shader shader) {
		this.shader = shader;
	}

	public void renderMesh(Mesh mesh) {
		// bind array
		GL30.glBindVertexArray(mesh.getVAO());
		// enable GPU access to variables (position, color, texture coordinates)
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		// bind "something"
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIBO());
		// activate image texture/material
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// bind material
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, mesh.getMaterial().getTextureID());
		// bind shader
		shader.bind();
		// GPU rendering
		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
		// unbind shader
		shader.unbind();
		// unbind "something"
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		// disable GPU access to variables
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		// unbind array
		GL30.glBindVertexArray(0);
	}
}
