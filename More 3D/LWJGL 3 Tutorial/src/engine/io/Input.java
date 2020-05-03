package engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Input {

	private GLFWKeyCallback keyboard;
	private GLFWCursorPosCallback mouseMove;
	private GLFWMouseButtonCallback mouseButtons;
	private GLFWScrollCallback mouseScroll;

	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	private static double mouseX, mouseY;
	private static double scrollX, scrollY;

	public Input() {
		keyboard = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				try {
					keys[key] = (action != GLFW.GLFW_RELEASE);
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		};

		mouseMove = new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				mouseX = xpos;
				mouseY = ypos;
			}
		};

		mouseButtons = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				buttons[button] = (action != GLFW.GLFW_RELEASE);
			}
		};

		mouseScroll = new GLFWScrollCallback() {
			public void invoke(long window, double offsetX, double offsetY) {
				System.out.println(offsetX);
				System.out.println(offsetY);
				scrollX += offsetX;
				scrollY += offsetY;
			}
		};

	}

	public static boolean isKeyDown(int key) {
		return keys[key];
	}

	public static boolean isButtonDown(int button) {
		return buttons[button];
	}

	public void destroy() {
		keyboard.free();
		mouseMove.free();
		mouseButtons.free();
		mouseScroll.free();
	}

	public GLFWKeyCallback getKeyboardCallback() {
		return keyboard;
	}

	public GLFWCursorPosCallback getMouseMoveCallback() {
		return mouseMove;
	}

	public GLFWMouseButtonCallback getMouseButtonsCallback() {
		return mouseButtons;
	}

	public GLFWScrollCallback getMouseScrollCallback() {
		return mouseScroll;
	}

	public static double getMouseX() {
		return mouseX;
	}

	public static double getMouseY() {
		return mouseY;
	}

	public static double getScrollX() {
		return scrollX;
	}

	public static double getScrollY() {
		return scrollY;
	}

}