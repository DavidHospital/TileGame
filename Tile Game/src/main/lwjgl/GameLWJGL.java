package main.lwjgl;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

public class GameLWJGL {

	private final int floatSize = 4;
	
	private long window;
	
	private int width = 300;
	private int height = 300;
	
	private int vertexShader;
	private int fragmentShader;
	private int shaderProgram;
	
	private int vao;
	private int vbo;
	private int vboi;
	
	private int posAttrib;
	private int colAttrib;
	
	private int uniModel;
	private int uniView;
	private int uniProjection;
	
	private int indicesCount;
	
	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion());
		
		initGLFW();
		initOpenGL();
		loop();
		
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		
		// Clean up OpenGL
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(vboi);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		glDeleteShader(shaderProgram);
		
	}
	
	public void initGLFW() {
		// Setup an error callback, the default implementation
		// will print the error message in System.err
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW. Most GLFW functions will not work before doing this
		if (!glfwInit()) {
			glfwTerminate();
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		// Configure GLFW
		glfwDefaultWindowHints();	 // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);	// the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);	// the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		
		// Create the window
		window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window, true);
			}
		});
		
		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);	// int*
			IntBuffer pHeight = stack.mallocInt(1);	// int*
			
			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);
			
			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			// Center the window
			glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
				);
		}	// the stack frame is popped automatically
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		
		// Enable v-sync
		glfwSwapInterval(1);
		
		// Make the window visible
		glfwShowWindow(window);
	}
	
	public void initOpenGL() {

		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		
		GL.createCapabilities();
		
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		vboi = glGenBuffers();
		
		// Compile shader program
		String vertexSource = 
				"#version 150 core\r\n" + 
				"\r\n" + 
				"in vec3 position;\r\n" + 
				"in vec3 color;\r\n" + 
				"\r\n" + 
				"out vec3 vertexColor;\r\n" + 
				"\r\n" + 
				"uniform mat4 model;\r\n" + 
				"uniform mat4 view;\r\n" + 
				"uniform mat4 projection;\r\n" + 
				"\r\n" + 
				"void main() {\r\n" + 
				"    vertexColor = color;\r\n" + 
				"    mat4 mvp = projection * view * model;\r\n" + 
				"    gl_Position = mvp * vec4(position, 1.0);\r\n" + 
				"}\r\n" + 
				"";
		
		String fragmentSource = 
				"#version 150 core\r\n" + 
				"\r\n" + 
				"in vec3 vertexColor;\r\n" + 
				"\r\n" + 
				"out vec4 fragColor;\r\n" + 
				"\r\n" + 
				"void main() {\r\n" + 
				"    fragColor = vec4(vertexColor, 1.0);\r\n" + 
				"}\r\n" + 
				"";
		
		// Compile vertex shader
		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, vertexSource);
		glCompileShader(vertexShader);
		
		int status = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
		if (status != GL_TRUE) {
			throw new RuntimeException(glGetShaderInfoLog(vertexShader));
		}
		
		// Compile Fragment shader
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, fragmentSource);
		glCompileShader(fragmentShader);
		
		status = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
		if (status != GL_TRUE) {
			throw new RuntimeException(glGetShaderInfoLog(fragmentShader));
		}
		
		// Build and link the shader program
		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glBindFragDataLocation(shaderProgram, 0, "fragColor");
		glLinkProgram(shaderProgram);
		
		status = glGetProgrami(shaderProgram, GL_LINK_STATUS);
		if (status != GL_TRUE) {
			throw new RuntimeException(glGetProgramInfoLog(shaderProgram));
		}
		
		glUseProgram(shaderProgram);


		posAttrib = glGetAttribLocation(shaderProgram, "position");
		colAttrib = glGetAttribLocation(shaderProgram, "color");
		
		// Enable all uniforms
		uniModel = glGetUniformLocation(shaderProgram, "model");
		Matrix4f model = new Matrix4f();
		glUniformMatrix4fv(uniModel, false, model.get(BufferUtils.createFloatBuffer(16)));

		uniView = glGetUniformLocation(shaderProgram, "view");
		Matrix4f view = new Matrix4f();
		glUniformMatrix4fv(uniView, false, view.get(BufferUtils.createFloatBuffer(16)));

		uniProjection = glGetUniformLocation(shaderProgram, "projection");
		float ratio = (float) width / height;
		Matrix4f projection = new Matrix4f();
		projection.setOrtho(-ratio, ratio, -1f, 1f, -1f, 1f);
		glUniformMatrix4fv(uniProjection, false, projection.get(BufferUtils.createFloatBuffer(16)));
	}
	
	public void drawQuad(float x, float y, float w, float h) {
		float[] vertices = {
				x, 		y,			0f,	1f, 0f, 0f,
				x+w, 	y, 			0f, 0f, 1f, 0f,
				x+w,	y+h,		0f,	0f, 0f, 1f,
				x,		y+h,		1f,	1f, 1f, 1f
		};
	
		byte[] indices = {
				0, 1, 2,
				2, 3, 0
		};
		
		// bind vao and vbos
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboi);
		
		// vertices
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();
		
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		
		//indices
		indicesCount = indices.length;
		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
		
		// enable shader attributes
		glEnableVertexAttribArray(posAttrib);
		glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 6 * floatSize, 0);
		
		glEnableVertexAttribArray(colAttrib);
		glVertexAttribPointer(colAttrib, 3, GL_FLOAT, false, 6 * floatSize, 3 * floatSize);
		
		// Draw elements of quad
		glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_BYTE, 0);
		
		// unbind vao and vbos
		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	int fps = 0;
	float fpsCounter = 0f;
	
	float angle = 0f;
	float radius = 1f;
	float anglePerSecond = 1f;
	
	float lastX = 0;
	float lastY = 0;
	
	public void update(float delta) {
		fpsCounter += delta;
		fps ++;
		if (fpsCounter >= 1f) {
			fpsCounter -= 1f;
			System.out.println(fps);
			fps = 0;
		}
		
		angle += anglePerSecond * delta;
		
		float x = (float) Math.cos(angle) * radius;
		float y = (float) Math.sin(angle) * radius;
		
		Matrix4f view = new Matrix4f();
		view.translate(x, y, 0f);
		glUniformMatrix4fv(uniView, false, view.get(BufferUtils.createFloatBuffer(16)));
	}
	
	public void render(float delta) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	// clear the framebuffer

		int numX = 16;
		int numY = 16;
		
		float w = 2f / numX;
		float h = 2f / numY;
		
		for (int i = 0; i < numX; i ++) {
			for (int j = 0; j < numY; j ++) {
				float x = (float) i / numX * 2f - 1f;
				float y = (float) j / numY * 2f - 1f;
				
				drawQuad(x, y, w, h);
			}
		}
	}
	
	public void loop() {
		
		// Set the clear color
		glClearColor(0.7f, 0.7f, 0.7f, 0.0f);
		
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.\
		double time = glfwGetTime();
		while (!glfwWindowShouldClose(window)) {
			
			float deltaTime = (float) (glfwGetTime() - time);
			time = glfwGetTime();
			
			update(deltaTime);
			render(deltaTime);
			
			glfwSwapBuffers(window);	// Swap the color buffers
			
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}
	
	public static void main(String[] args) {
		new GameLWJGL().run();
	}
}
