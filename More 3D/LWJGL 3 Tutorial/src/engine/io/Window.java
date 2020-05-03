package engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import engine.maths.Vector3f;

public class Window {

	public Input input;

	private int width;
	private int height;
	private String title;
	private long window;
	public int frames;
	public long time;
	private Vector3f background = new Vector3f(0, 0, 0);
	private GLFWFramebufferSizeCallback sizeCallback;
	private boolean isResized;
	private boolean isFullscreen;
	private int[] windowPosX = new int[1], windowPosY = new int[1];

	public Window(int w, int h, String t) {
		this.width = w;
		this.height = h;
		this.title = t;
	}

	public void create() {

		System.out.println(GLFW.glfwGetPrimaryMonitor());

		// error handling
		if (!GLFW.glfwInit()) {
			System.err.println("Error: GLFW wasn't initialized.");
			return;
		}

		input = new Input();
		// 0 values involve fullscreen and screen sharing, I think. More research is
		// required
		window = GLFW.glfwCreateWindow(width, height, title, isFullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);

		if (window == 0) {
			System.err.println("Error: window wasn't created.");
			return;
		}

		// set window position
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		windowPosX[0] = (videoMode.width() - width) / 2;
		windowPosY[0] = (videoMode.height() - height) / 2;
		GLFW.glfwSetWindowPos(window, windowPosX[0], windowPosY[0]);

		// used to make sure we're swapping interval of correct window
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		createCallbacks();

		// show window
		GLFW.glfwShowWindow(window);

		GLFW.glfwSwapInterval(1);

		time = System.currentTimeMillis();

	}

	public void update() {
		// check for resizing of window
		if (isResized) {
			GL11.glViewport(0, 0, width, height);
			isResized = false;
		}
		// set window size
		GL11.glViewport(0, 0, width, height);
		// setting color
		GL11.glClearColor(background.getX(), background.getY(), background.getZ(), 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		// normal updating thing
		GLFW.glfwPollEvents();
		frames++;
		if (System.currentTimeMillis() > time + 1000) {
			GLFW.glfwSetWindowTitle(window, title + " | FPS: " + frames);
			time = System.currentTimeMillis();
			frames = 0;
		}
	}

	private void createCallbacks() {
		sizeCallback = new GLFWFramebufferSizeCallback() {
			public void invoke(long window, int w, int h) {
				width = w;
				height = h;
				isResized = true;
			}
		};

		// setting up to get input from the following
		GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
		GLFW.glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
		GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
		GLFW.glfwSetScrollCallback(window, input.getMouseScrollCallback());
		GLFW.glfwSetFramebufferSizeCallback(window, sizeCallback);
	}

	public void swapBuffers() {
		GLFW.glfwSwapBuffers(window);
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}

	public void setBackgroundColor(float r, float g, float b) {
		background.setVectorValues(r, g, b);
	}

	public void destroy() {
		input.destroy();
		sizeCallback.free();
		GLFW.glfwWindowShouldClose(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}

	public boolean isFullscreen() {
		return isFullscreen;
	}

	public void setFullscreen(boolean isFullscreen) {
		this.isFullscreen = isFullscreen;
		isResized = true;

		if (isFullscreen) {
			GLFW.glfwGetWindowPos(window, windowPosX, windowPosY);
			GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
		} else {
			GLFW.glfwSetWindowMonitor(window, 0, windowPosX[0], windowPosY[0], width, height, 0);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}

	public long getWindow() {
		return window;
	}

}
