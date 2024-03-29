import java.io.IOException;
import java.util.Arrays;
import java.security.SecureClassLoader;
import java.lang.ClassLoader;

//import org.apache.*;
import org.apache.bcel.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.util.*;
import org.apache.bcel.verifier.*;
import org.apache.bcel.verifier.exc.*;
import org.apache.bcel.verifier.statics.*;
import org.apache.bcel.verifier.structurals.*;

import java.lang.instrument.*;
import java.io.*;
import java.security.ProtectionDomain;

public class Paralen implements ClassFileTransformer
{
	public static void premain (String argv, Instrumentation i)
		throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		System.out.println("Paralen loaded.");
		i.addTransformer(new Paralen());
	}

	public byte[] transform (ClassLoader cl, String className, Class classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
	{
		//System.out.println("loading class "+className);
		if (className.indexOf("sun") == 0 ||
					className.indexOf("java") == 0) // we target only simple classes
		{
			return null;
		}
		System.out.println("ClassLoader is "+cl);
		System.out.println("ProtectionDomain is "+protectionDomain);
		System.out.println("transforming "+className);

		ClassGen _cg = null;
		try {
			try {
				_cg = new ClassGen((new ClassParser(new ByteArrayInputStream(classfileBuffer), className+".class")).parse()); //load class into a ClassGen
			}
			catch (IOException ex) {
			}
		}
		catch (Exception ex) {
			System.out.println("transform exception "+className+" "+ex.toString());
			ex.printStackTrace();
			throw new RuntimeException("transforming " + className, ex);
		}

		ConstantPoolGen _cp = _cg.getConstantPool(); //Get the class' constantpool
		System.out.println("# Method getMethods(): "+_cg.getMethods().length);
		try {
			Method[] meths = _cg.getMethods();
			for(Method m : meths) //Repeat for all methods in the class
			{
				if (m.getName().equals("<init>")) {
					System.out.println("## Skipping method "+m.getName());
					continue;
				}
				//if (mtd == "" || m.getName().indexOf(mtd) >= 0)
				{
					System.out.println("## Processing method: "+m.toString());
					try {
						System.out.println("### Dumping argument types");

						MethodGen methodGen = new MethodGen(m, _cg.getClassName(), _cp); //Create a new MethodGen (the equivilent of createing a brand new method
						InstructionList iList = methodGen.getInstructionList(); //Create a new list of instructions for our method
						InstructionFactory _factory = new InstructionFactory(_cg, _cp);

						Type[] ts = m.getArgumentTypes();
						System.out.println("#### argument count is "+ts.length);
						int pos = 0; // zacneme od 1. argumentu, 0 je this
						for(int i = 0; i < ts.length; i++) {
							Type t = ts[i];
							// two positions for LONG and DOUBLE
							if (t == Type.LONG || t == Type.DOUBLE) {
								System.out.println("#### long argument ++");
								pos++;
							}

							// we start experiment with INTs
							System.out.println("#### argument "+i+"("+pos+") of type "+t);
							if (!t.toString().equals("int")) {
								System.out.println("##### skipped");
							}
							else {
								//System.out.println("#### processing...");
								{
									InstructionList il = new InstructionList();
									// print static info about argument
									InstructionHandle ih_0 = il.append(_factory.createFieldAccess("java.lang.System", "out", new ObjectType("java.io.PrintStream"), Constants.GETSTATIC));
									il.append(new PUSH(_cp, "Original value of input argument "+i+"("+t.toString()+"):"));
									il.append(_factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKEVIRTUAL));

									// now print runtime value
									InstructionHandle ih_8 = il.append(_factory.createFieldAccess("java.lang.System", "out", new ObjectType("java.io.PrintStream"), Constants.GETSTATIC));
									il.append(_factory.createLoad(Type.INT, pos));
									il.append(_factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[] { Type.INT }, Constants.INVOKEVIRTUAL));

									// now we replace value with something else
									il.append(new PUSH(_cp, 7));
									il.append(_factory.createStore(Type.INT, pos));

									iList.insert(il);
								}
							}

							pos++;
						}

						{
							InstructionList il = new InstructionList();
							InstructionHandle ih_x = il.append(_factory.createFieldAccess("java.lang.System", "out", new ObjectType("java.io.PrintStream"), Constants.GETSTATIC));
							il.append(new PUSH(_cp, "Hello World from "+_cg.getClassName()+"."+m.getName()));
							il.append(_factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKEVIRTUAL));
							iList.insert(il);
						}

						iList.setPositions(); //cleanup
						methodGen.setInstructionList(iList); //set the new instructionlist
						methodGen.setMaxStack(); //cleanup
						methodGen.setMaxLocals(); //cleanup
						methodGen.removeLineNumbers(); //cleanup
						_cg.replaceMethod(m, methodGen.getMethod()); //replace the old method with our new one

						System.out.println("### Method replacement ok.");
					}
					catch (Exception e) {
						System.out.println("### Method replacement failed.");
						System.err.println(Arrays.toString(e.getStackTrace()));
					}
				}
				else {
					System.out.println("## Skipping method "+m.getName()+".");
				}
			}
		}
		catch (Exception e) {
			System.out.println("## Methods traversal failed.");
			System.err.println(Arrays.toString(e.getStackTrace()));
		}


		return _cg.getJavaClass().getBytes();
	}
}

