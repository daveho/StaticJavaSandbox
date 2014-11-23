package org.cloudcoder.sjs;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Statically examine a Java class for suspicious features.
 * For example, find attempts to do I/O, dynamic class loading,
 * start threads, etc.  The intended application is programming
 * exercise judges, where the system will be executing untrusted
 * code.  Credit: Nick Parlante mentioned that
 * <a href="http://codingbat.com">CodingBat</a> does this,
 * so I am stealing the idea for <a href="http://cloudcoder.org">CloudCoder</a>.
 * 
 * @author David Hovemeyer
 */
public class Inspector {
	
	private class CV extends ClassVisitor {
		public CV() {
			super(Opcodes.ASM5);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc,
				String signature, String[] exceptions) {
			return new MV(access, name, desc, signature, exceptions);
		}
	}
	
	private class MV extends MethodVisitor {

		private int access;
		private String name;
		private String desc;
		private String signature;
		private String[] exceptions;

		public MV(int access, String name, String desc, String signature, String[] exceptions) {
			super(Opcodes.ASM5);
			this.access = access;
			this.name = name;
			this.desc = desc;
			this.signature = signature;
			this.exceptions = exceptions;
		}
		
	}

	public Inspector() {
	}
	
	public void examine(byte[] data) {
		ClassReader r = new ClassReader(data);
		ClassVisitor cv = new CV();
		r.accept(cv, 0);
	}
	
	
}
