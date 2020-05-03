package main;

import org.lwjgl.glfw.GLFW;

import engine.graphics.Mat;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import engine.io.Input;
import engine.io.Window;
import engine.maths.Vector2f;
import engine.maths.Vector3f;

public class Main implements Runnable {
	public final int WIDTH = 1280, HEIGHT = 720;
	public Thread game;
	public Window window;
	public Renderer renderer;
	public Shader shader;

	public Mesh mesh = new Mesh(new Vertex[] {
			// 3d vertices | color | image position

			// top left | | also, flip image pos variables
			new Vertex(new Vector3f(0.5f, 0.5f, 0), new Vector3f(0.0f, 0.0f, 1.0f), new Vector2f(0.0f, 1.0f)),
			// top right
			new Vertex(new Vector3f(0.5f, -0.5f, 0), new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(0.0f, 0.0f)),
			// bottom right
			new Vertex(new Vector3f(-0.5f, 0.5f, 0), new Vector3f(1.0f, 1.0f, 1.0f), new Vector2f(1.0f, 0.0f)),
			// bottom left
			new Vertex(new Vector3f(-0.5f, -0.5f, 0), new Vector3f(1.0f, 0.0f, 0.0f), new Vector2f(1.0f, 1.0f))
			// the first variables only seem to change the original box positions, and don't
			// change the image location

	}, new int[] {
			// something
			0, 1, 2,
			// another something
			0, 3, 2
			// this was originally 0, 1, 2, '0', 3, 2, which displays correctly for me.
			// replacing '0' with '1' --> changes the way the box looks, which I assume is
			// due to the way the triangles are set up.

	}, new Mat("/textures/beautiful.png"));

	public void start() {
		game = new Thread(this, "game");
		game.start();
	}

	public void init() {
		System.out.println("Initializing game...");

		window = new Window(WIDTH, HEIGHT, "Game");
		shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");
		window.setBackgroundColor(0.25f, 0, 1.0f);
		window.create();
		renderer = new Renderer(shader);

		mesh.create();
		shader.create();

		System.out.println("Initialized!");
	}

	public void run() {
		init();
		while (!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			update();
			render();
			if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) {
				window.setFullscreen(!window.isFullscreen());
			}
		}

		// when running stops, game closes
		System.out.println("Closing game...");
		window.destroy();
		System.out.println("Game closed.");

	}

	private void update() {

		window.update();
		if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			System.out.println("X: " + Input.getMouseX() + ", Y: " + Input.getMouseY());
		}

	}

	private void render() {

		renderer.renderMesh(mesh);

		window.swapBuffers();

	}

	private void close() {
		window.destroy();
		mesh.destroy();
		shader.destroy();
	}

	public static void main(String[] args) {
		new Main().start();
	}

}
