import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.TextureIO;

public class TeaPot {

	public static void main(String[] args) {
		GLEventListener glel = new GLEventListener() {
			int rot = 0;
			GL2 gl2;
			GLUT glut;
			int texture_teapot;
			int texture_floor;
			float change[] = new float[360];

			@Override
			public void display(GLAutoDrawable drawable) {
				gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);// 清除原图
				gl2.glLoadIdentity();

				// 合适的视角
				gl2.glRotated(-30, 1, 0, 0);
				gl2.glRotated(-30, 0, 0, 1);
				gl2.glScaled(0.3, 0.3, 0.3);

				// 旋转等
				rot = (rot + 1) % 360;
				gl2.glRotated(rot, 0, 1, 0);
				gl2.glTranslated(change[rot], 0, 0);
				gl2.glScaled(change[rot], change[rot], change[rot]);

				// 固定光源
				gl2.glPushMatrix();
				gl2.glRotated(-rot, 0, 1, 0);
				gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE,
						new float[] { change[rot], change[rot], change[rot], change[rot] }, 0);
				gl2.glPopMatrix();

				// 画壶
				gl2.glEnable(GL2.GL_TEXTURE_2D);
				gl2.glBindTexture(GL2.GL_TEXTURE_2D, texture_teapot);
				glut.glutSolidTeapot(0.5);
				gl2.glDisable(GL2.GL_TEXTURE_2D);

				// 画地板
				gl2.glEnable(GL2.GL_TEXTURE_2D);
				gl2.glBindTexture(GL2.GL_TEXTURE_2D, texture_floor);

				gl2.glPushMatrix();
				gl2.glTranslated(0, -0.5, 0);
				gl2.glRotated(90, 1, 0, 0);

				gl2.glBegin(GL2.GL_POLYGON);
				gl2.glTexCoord2d(0, 0);
				gl2.glVertex3d(-1, -1, 0);
				gl2.glTexCoord2d(1, 0);
				gl2.glVertex3d(1, -1, 0);
				gl2.glTexCoord2d(1, 1);
				gl2.glVertex3d(1, 1, 0);
				gl2.glTexCoord2d(0, 1);
				gl2.glVertex3d(-1, 1, 0);
				gl2.glEnd();

				gl2.glPopMatrix();
				gl2.glDisable(GL2.GL_TEXTURE_2D);
			}

			@Override
			public void dispose(GLAutoDrawable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void init(GLAutoDrawable drawable) {
				gl2 = drawable.getGL().getGL2();
				glut = new GLUT();
				gl2.getGL().getGL2().glEnable(GL2.GL_DEPTH_TEST);// 设置不可透视
				gl2.glEnable(GL2.GL_LIGHTING);// 开启灯光
				gl2.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[] { 0.8f, 0.8f, 0.8f, 0.8f }, 0);// 设置环境光
				gl2.glEnable(GL2.GL_LIGHT0);// 开启0号灯
				gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] { -10, 5, 5, 1 }, 0);// 设置0号灯位置

				// 获取图片纹理
				try {
					texture_teapot = TextureIO.newTexture(new File("d.png"), true).getTextureObject(gl2);
					texture_floor = TextureIO.newTexture(new File("g.jpg"), true).getTextureObject(gl2);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// 设置变化值
				for (int i = 0; i < 180; i++) {
					change[i] = (i + 1) / 100f;
					change[180 + i] = (180 - i) / 100f;
				}

			}

			@Override
			public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
				// TODO Auto-generated method stub

			}

		};

		GLCanvas glc = new GLCanvas(new GLCapabilities(GLProfile.get(GLProfile.GL2)));
		glc.addGLEventListener(glel);
		glc.setSize(700, 700);

		JFrame jf = new JFrame();
		jf.getContentPane().add(glc);
		jf.pack();
		jf.setVisible(true);

		new FPSAnimator(glc, 1000, true).start();
	}
}
