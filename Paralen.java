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
	//private static String tgtcls = "com/threerings/projectx/client/ProjectXApp";
	//private static String mtd = "ProjectXApp";
	private static String tgtcls = "com/threerings/projectx/client/OptionsDialog";
	//private static String mtd = "actionPerformed";
	//private static String tgtcls = "com/threerings/projectx/auction/data/ListingInfo";
	//private static String tgtcls = "com/threerings/opengl/gui/N";
	//private static String mtd = "getClipboardText";
	//private static String tgtcls = "sun/misc/CharacterDecoder";
	//private static String mtd = "<init>";
	private static String mtd = "";
	

	public static void premain (String argv, Instrumentation i)
		throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		System.out.println("Paralen loaded.");
		i.addTransformer(new Paralen());
	}

	public byte[] transform (ClassLoader cl, String className, Class classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
	{
		//System.out.println("loading class "+className);
		if (!className.equals(tgtcls)) {
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
		for(Method m : _cg.getMethods()) //Repeat for all methods in the class
		{
			if(mtd == "" || m.getName().indexOf(mtd) >= 0)
			{
				System.out.println("Found "+m.getName()+" method!");
				System.out.println(m.toString());
				try {
					System.out.println("dumping argument types");
					/*
transforming com/threerings/projectx/auction/data/ListingInfo
Found <init> method!
dumping...
public void <init>(long arg1, com.threerings.projectx.auction.data.BidStatus arg3, com.threerings.projectx.auction.data.TimeLeft arg4, int arg5, int arg6, String arg7, byte[] arg8, int arg9, boolean arg10, com.threerings.projectx.auction.data.FeaturedInfo arg11)
long
com.threerings.projectx.auction.data.BidStatus
com.threerings.projectx.auction.data.TimeLeft
int
int
java.lang.String
byte[]
int
boolean
com.threerings.projectx.auction.data.FeaturedInfo
ok...
Method replaced.
					 */
					//LocalVariableTable lvt = m.getLocalVariableTable();

					MethodGen methodGen = new MethodGen(m, _cg.getClassName(), _cp); //Create a new MethodGen (the equivilent of createing a brand new method
					InstructionList iList = methodGen.getInstructionList(); //Create a new list of instructions for our method
					InstructionFactory _factory = new InstructionFactory(_cg, _cp);
					System.out.println("phase 1.");

					int pos = 1; // zacneme od 1. argumentu, 0 je this
					for(int i = 0; i < m.getArgumentTypes().length; i++) {
						Type t = m.getArgumentTypes()[i];
						System.out.println(t);

						{
							InstructionList il = new InstructionList();

							InstructionHandle ih_0 = il.append(_factory.createFieldAccess("java.lang.System", "out", new ObjectType("java.io.PrintStream"), Constants.GETSTATIC));
							il.append(new PUSH(_cp, t.toString()));
							il.append(_factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKEVIRTUAL));


							InstructionHandle ih_8 = il.append(_factory.createFieldAccess("java.lang.System", "out", new ObjectType("java.io.PrintStream"), Constants.GETSTATIC));
							il.append(_factory.createLoad(Type.OBJECT, pos));
							il.append(_factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[] { Type.OBJECT }, Constants.INVOKEVIRTUAL));

							if (t.toString().toUpperCase() == Type.LONG.toString()) {
								pos++;
								pos++;
							}
							else {
								pos++;
							}

							iList.insert(il);
						}
					}

					// print our header!
					{
						InstructionList il = new InstructionList();
						InstructionHandle ih_x = il.append(_factory.createFieldAccess("java.lang.System", "out", new ObjectType("java.io.PrintStream"), Constants.GETSTATIC));
						il.append(new PUSH(_cp, "Hello World from method "+_cg.getClassName()));
						il.append(_factory.createInvoke("java.io.PrintStream", "println", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKEVIRTUAL));
						iList.insert(il);
					}


					iList.setPositions(); //cleanup
					methodGen.setInstructionList(iList); //set the new instructionlist
					methodGen.setMaxStack(); //cleanup
					methodGen.setMaxLocals(); //cleanup
					methodGen.removeLineNumbers(); //cleanup

					_cg.replaceMethod(m, methodGen.getMethod()); //replace the old method with our new one

					System.out.println("Method replacement ok.");
				}
				catch (Exception e) {
					System.out.println("Method replacement failed.");
					System.err.println(Arrays.toString(e.getStackTrace()));
				}
				break;
			}
		}


		return _cg.getJavaClass().getBytes();
	}
}

