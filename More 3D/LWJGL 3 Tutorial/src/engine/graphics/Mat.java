package engine.graphics;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Mat {

	private Texture texture;
	private float width, height;
	private int textureID;
	private String path;

	public Mat(String path) {
		this.path = path;
	}

	public void create() {
		try {
			texture = TextureLoader.getTexture(path.split("[.]")[1], Mat.class.getResourceAsStream(path),
					GL11.GL_NEAREST);
		} catch (IOException e) {
			System.err.println("Can't find texture at " + path);
		}
		width = texture.getWidth();
		height = texture.getHeight();
		textureID = texture.getTextureID();
	}

	public void destroy() {
		GL13.glDeleteTextures(textureID);
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public int getTextureID() {
		return textureID;
	}

}
