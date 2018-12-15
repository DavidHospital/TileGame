package main.lwjgl;

import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Draw {

	private static int indicesCount = 0;
	
	private static int vao = 0;
	private static int vbo = 0;
	private static int vboi = 0;
	
	private static float[] r;
	private static float[] g;
	private static float[] b;
	private static float[] a;
	
	protected static void init() {
		r = new float[4];
		g = new float[4];
		b = new float[4];
		a = new float[4];
	}
	
	protected static void dispose() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(vboi);
	}
	
	public static void color(float r, float g, float b, float a) {
		for (int i = 0; i < 4; i ++) {
			Draw.r[i] = r;
			Draw.g[i] = g;
			Draw.b[i] = b;
			Draw.a[i] = a;
		}
	}
	
	public static void color(float[] r, float[] g, float[] b, float[] a) {
		assert (r.length == 4 && g.length == 4 && b.length == 4 && a.length == 4);	// check that arrays are correct length
		for (int i = 0; i < 4; i ++) {
			Draw.r[i] = r[i];
			Draw.g[i] = g[i];
			Draw.b[i] = b[i];
			Draw.a[i] = a[i];
		}
	}
	
	public static void rectangle(float x, float y, float width, float height) {
//		float[] vertices = {
//			x,		y,			0f,	r[0], g[0], b[0],
//			x+width,y, 			0f, r[1], g[1], b[1],
//			x+width,y+height,	0f,	r[2], g[2], b[2],
//			x,		y+height,	0f, r[3], g[3], b[3]
//		};
		float[] vertices = {
			-0.5f,		-0.5f,			0f,	1f, 0f, 0f,
			0.5f,		-0.5f, 			0f, 0f, 1f, 0f,
			0f,		0.5f,				0f,	0f, 0f, 1f,
		};
	
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices);
		verticesBuffer.flip();
		
//		byte[] indices = {
//			0, 1, 2,
//			2, 3, 0
//		};
//		
//		indicesCount = indices.length;
//		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
//		indicesBuffer.put(indices);
//		indicesBuffer.flip();	

		// Create and bind a new vao
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		// Create and bind a new vbo for the vertices
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
		
//		vboi = glGenBuffers();
//		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboi);
//		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
//		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public static void draw() {
		glBindVertexArray(vao);
		
		//glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboi);
		
		//glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_BYTE, 0);
		
		
		glDrawArrays(GL_TRIANGLES, 0, 3);
		
	//	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
}
