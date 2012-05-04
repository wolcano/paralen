
public class HelloWorldApp {
	static class HelloWorldClass {
		public static void procedure_1_int(int i1, float f, char c, double d, long l, String s, int i2, Object cl) {
			System.out.println("i1 is "+i1);
			System.out.println("f is "+f);
			System.out.println("c is "+c);
			System.out.println("d is "+d);
			System.out.println("l is "+l);
			System.out.println("s is "+s);
			System.out.println("i2 is "+i2);
			System.out.println("cl is "+(cl == null ? "NULL" : cl.getClass()));
		}
	}

	static HelloWorldClass hwc;

	void HelloWorldApp() {
		hwc = new HelloWorldClass();
		main(null);
	}

	public static void main(String[] args) {
		System.out.println("Hello World!");
		HelloWorldClass.procedure_1_int(0, 1, 'X', 2, 3, "XYZ", 4, hwc);
	}

}

