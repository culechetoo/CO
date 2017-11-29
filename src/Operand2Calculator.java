import java.math.BigInteger;

public class Operand2Calculator {
	String st;
	String I;
	public Operand2Calculator(String a, String i) {
		st = a;
		I = i;
	}
	public int calcoperand() {
		if(I.equals("1")) {
			return rotate(st.substring(0, 4), st.substring(4,12));
		}
		else {
			return Integer.parseInt(st.substring(8, 12), 2);
			//return shift(st.substring(0, 8), Integer.parseInt(st.substring(8, 12), 2));
		}
	}
	private int rotate(String r, String i) {
		String value = new BigInteger(r, 2).toString(2);
		int rotate = Integer.parseInt(value);
		String newstring = "";
		if(rotate==0) {
			return Integer.parseInt(i, 2);
		}
		if(rotate<4) {
			String a = i.substring(0, 2*rotate);
			String b = i.substring(rotate*2, 8);
			newstring = b+"000000000000000000000000"+a;
		}
		else {
			for(int j = 0; j<rotate-4; j++) {
				newstring = newstring + "00";
			}
			newstring = newstring + i;
			for(int j = 0; j<(12-(rotate-4)); j++) {
				newstring = newstring + "00";
			}
		}
		return Integer.parseInt(newstring, 2);
	}
	private int shift(String r, int a) {
		int shift = Integer.parseInt(r.substring(0,5), 2);
		if(r.substring(7, 8).equals("0")) {
			String type = r.substring(5,7);
			if(type.equals("00")) {	
				String b = Integer.toBinaryString(a);
				int f = b.length();
				while(f<12) {
					b = "0"+b;
					f = b.length();
				}
				String c = b.substring(shift, 12);
				for(int i = 0; i<shift; i++) {
					c = c+"0";
				}
				return Integer.parseInt(c, 2);
			}
			else if(type.equals("01")) {
				return a>>>shift;
			}
			else if(type.equals("10")) {
				return a<<shift;
			}
			else {
				return Integer.rotateRight(a, shift);
			}
		}
		return 0;
	}
}